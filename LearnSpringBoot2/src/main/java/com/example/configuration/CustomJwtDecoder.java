package com.example.configuration;

import java.text.ParseException;
import java.util.Objects;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.example.dto.request.IntrospectRequest;
import com.example.service.AuthenticationServce;
import com.nimbusds.jose.JOSEException;


@Component
public class CustomJwtDecoder implements JwtDecoder {

	@Value("${app.security.signer-key}")
	private String signerKey;

	@Autowired
	private AuthenticationServce authenticationServce;

	private NimbusJwtDecoder nimbusJwtDecoder = null;

	@Override
	public Jwt decode(String token) throws JwtException {

		try {

			var respone= authenticationServce.introspectRespone(IntrospectRequest.builder().token(token).build());
		
			if (!respone.isValid()) {
				throw new JwtException("Token invalid");
			}
			
		} catch (JOSEException | ParseException e) {
			throw new JwtException(e.getMessage());
		}

		if (Objects.isNull(nimbusJwtDecoder)) {
			SecretKey secretKeySpec= new SecretKeySpec(signerKey.getBytes(), "HS512");
			nimbusJwtDecoder=NimbusJwtDecoder
					.withSecretKey(secretKeySpec)
					.macAlgorithm(MacAlgorithm.HS512)
					.build();
		
		}
		
		return nimbusJwtDecoder.decode(token);
	}

}
