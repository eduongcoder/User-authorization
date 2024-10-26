package com.example.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example.dto.request.AuthenticationRequest;
import com.example.dto.request.IntrospectRequest;
import com.example.dto.request.LogoutRequest;
import com.example.dto.request.RefreshRequest;
import com.example.dto.respone.AuthenticationRespone;
import com.example.dto.respone.IntrospectRespone;
import com.example.entity.InvalidatedToken;
import com.example.entity.User;
import com.example.exception.AppException;
import com.example.exception.ErrorCode;
import com.example.repository.InvalidatedTokenRepository;
import com.example.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationServce {

	UserRepository userRepository;

	InvalidatedTokenRepository invalidatedTokenRepository;

	@NonFinal
	@Value("${app.security.signer-key}")
	private String signerKey;

	@NonFinal
	@Value("${app.security.valid-duration}")
	private long VALID_DURATION;

	@NonFinal
	@Value("${app.security.refreshable-duration}")
	private long REFRESHABLE_DURATION;

	public IntrospectRespone introspectRespone(IntrospectRequest request) throws JOSEException, ParseException {
		var token = request.getToken();

		boolean isValid = true;
		try {
			verifyToken(token, false);

		} catch (AppException e) {
			isValid = false;
		}

		return IntrospectRespone.builder().valid(isValid).build();

	}

	public AuthenticationRespone authenticated(AuthenticationRequest request) {
		var user = userRepository.findByUsername(request.getUsername())
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
		boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

		if (!authenticated)
			throw new AppException(ErrorCode.UNAUTHENTICATED);

		var token = generateToken(user);

		return AuthenticationRespone.builder().token(token).authenticated(true).build();
	}

	public String generateToken(User user) {

		JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

		JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder().subject(user.getUsername()).issuer("Duong")
				.issueTime(new Date())
				.expirationTime(new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
				.jwtID(UUID.randomUUID().toString()).claim("scope", buildScope(user)).build();

		Payload payload = new Payload(jwtClaimsSet.toJSONObject());

		JWSObject jwsObject = new JWSObject(header, payload);

		try {
			jwsObject.sign(new MACSigner(signerKey.getBytes()));
			return jwsObject.serialize();
		} catch (JOSEException e) {

			log.info("Cannot create token " + e.toString());
			throw new RuntimeException(e);
		}
	}

	public void logout(LogoutRequest request) throws JOSEException, ParseException {
		try {
			var signToken = verifyToken(request.getToken(), false);

			String jit = signToken.getJWTClaimsSet().getJWTID();
			Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

			InvalidatedToken invalidatedToken = InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

			invalidatedTokenRepository.save(invalidatedToken);
		} catch (AppException e) {
			log.info("Token already expired");
		}

	}

	private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {

		JWSVerifier verifier = new MACVerifier(signerKey.getBytes());

		SignedJWT signedJWT = SignedJWT.parse(token);

		Date expiryTime = (isRefresh)
				? new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant()
						.plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
				: signedJWT.getJWTClaimsSet().getExpirationTime();

		var verified = signedJWT.verify(verifier);

		if (!verified && expiryTime.after(new Date())) {
			throw new AppException(ErrorCode.UNAUTHENTICATED);
		}

		if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
			throw new AppException(ErrorCode.UNAUTHENTICATED);
		}

		return signedJWT;
	}

	public AuthenticationRespone refreshToken(RefreshRequest request) throws JOSEException, ParseException {
		var signedJWT = verifyToken(request.getToken(), true);

		var jit = signedJWT.getJWTClaimsSet().getJWTID();

		var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

		InvalidatedToken invalidatedToken = InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

		invalidatedTokenRepository.save(invalidatedToken);

		var username = signedJWT.getJWTClaimsSet().getSubject();

		var user = userRepository.findByUsername(username)
				.orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

		var token = generateToken(user);

		return AuthenticationRespone.builder().token(token).authenticated(true).build();

	}

	private String buildScope(User user) {
		StringJoiner stringJoiner = new StringJoiner(" ");
		if (!CollectionUtils.isEmpty(user.getRoles())) {
			user.getRoles().forEach(roles -> {
				stringJoiner.add("ROLE_" + roles.getName());
				if (!CollectionUtils.isEmpty(roles.getPermissions()))
					roles.getPermissions().forEach(permissions -> stringJoiner.add(permissions.getName()));
			});
		}
		return stringJoiner.toString();
	}
}
