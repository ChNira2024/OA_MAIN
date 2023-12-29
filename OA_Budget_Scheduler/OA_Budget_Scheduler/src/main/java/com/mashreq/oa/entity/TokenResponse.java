package com.mashreq.oa.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenResponse {

	private String token_type;
	private String access_token;
	private String scope;
	private Long expires_in;
	private Long consented_on;

	public TokenResponse() {

	}

	public TokenResponse(String token_type, String access_token, String scope, Long expires_in, Long consented_on) {
		super();
		this.token_type = token_type;
		this.access_token = access_token;
		this.scope = scope;
		this.expires_in = expires_in;
		this.consented_on = consented_on;
	}

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public Long getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(Long expires_in) {
		this.expires_in = expires_in;
	}

	public Long getConsented_on() {
		return consented_on;
	}

	public void setConsented_on(Long consented_on) {
		this.consented_on = consented_on;
	}

	@Override
	public String toString() {
		return "TokenResponse [token_type=" + token_type + ", access_token=" + access_token + ", scope=" + scope
				+ ", expires_in=" + expires_in + ", consented_on=" + consented_on + "]";
	}

}
