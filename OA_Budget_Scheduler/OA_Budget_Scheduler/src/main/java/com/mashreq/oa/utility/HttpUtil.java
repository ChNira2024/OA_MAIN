package com.mashreq.oa.utility;

import java.net.URI;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.mashreq.oa.entity.ResponseEntityBO;
import com.mashreq.oa.entity.TokenResponse;
import com.mashreq.oa.exceptions.BudgetSchedulerException;

@Component
public class HttpUtil {
	@Autowired
	private RestTemplate restTemplate;

	@Value("${file.mail.restUrl}")
	private String restURL;

	@Value("${file.mailSecurity.headerValue}")
	private String headerValue;

	@Value("${mail.from.address}")
	private String fromAddress;

	@Value("${mail.body.content1}")
	private String line1;

	@Value("${mail.body.content2}")
	private String line2;

	@Value("${token.url}")
	private String tokenURL;

	@Value("${client_id}")
	private String client_id;

	@Value("${client_secret}")
	private String client_secret;

	@Value("${grant_type}")
	private String grant_type;

	@Value("${scope}")
	private String scope;

	private long tokenTimeInSec;

	private TokenResponse tokenResponse;

	public final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

	public String sendMail(String subject, String values, String mail) throws Exception {

		logger.info("mailid in utility ::" + mail);

		String content = line1 + "<p>" + line2 + "<p>" + values + "" + "<b>" + "*Note" + "</b>"
				+ ": This is system generated email. Please do not replay.<br>" + "<p>" + "Thanks & Regards,<br>"
				+ "<p>" + "BPM System";
		logger.info("content::" + content);
		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("x-auth-shared-key", headerValue);

		ResponseEntityBO request = new ResponseEntityBO();
		request.setAttachments("");
		request.setCcAddress("");
		request.setBccAddress("");

		request.setEmailBody(content);
		request.setFromAddress(fromAddress);
		request.setToAddress(mail);
		request.setSubject(subject);

		URI expanded = new URI(restURL);
		ResponseEntity<String> response = null;
		try {
			HttpEntity<ResponseEntityBO> entity = new HttpEntity<>(request, headers);
			response = restTemplate.postForEntity(expanded, entity, String.class);

			logger.info("Mail Response from RestTemplate:" + response);

		} catch (Exception ex) {
			logger.error("Exception ::" + ex);

			return ex.getMessage();
		}
		logger.info(response.toString());
		logger.info(response + ", Mail Call - Is response status code is 200? :"
				+ StringUtils.contains(response.toString(), "200"));

		if (!StringUtils.contains(response.toString(), "200")) {
			throw new BudgetSchedulerException("Exception occured while making Mail REST call ");

		}
		return response.toString();

	}

	public TokenResponse generateToken() {
		return generateToken(scope);
	}

	public TokenResponse generateToken(String scope1) {
		// logger.info("start testToken");
		long timeInSec = System.currentTimeMillis() / 1000;

		if (this.tokenResponse != null && this.tokenTimeInSec > 0
				&& (timeInSec - this.tokenTimeInSec) < (this.tokenResponse.getExpires_in() - 100)) {
			// logger.info("Returning exiting token");
			return this.tokenResponse;
		}
		// HttpHeaders headerResponse=new HttpHeaders();
		String status = "Success";
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			// TODO: need to set headerValue once we get the token
			// headers.set("x-auth-shared-key", headerValue);
			// application/x-www-form-urlencoded

			MultiValueMap<String, String> urlVariables = new LinkedMultiValueMap<String, String>();
			urlVariables.add("client_id", client_id);
			urlVariables.add("client_secret", client_secret);
			urlVariables.add("grant_type", grant_type);
			urlVariables.add("scope", scope1);
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(
					urlVariables, headers);
			logger.info("request for the URL::" + request.toString());
			ResponseEntity<TokenResponse> response = restTemplate.postForEntity(tokenURL, request, TokenResponse.class);
			logger.info("Response from the request:::" + response.toString());

			if (response != null) {
				this.tokenResponse = response.getBody(); 
				logger.info("tokenResponse:::"+tokenResponse);
				this.tokenTimeInSec = System.currentTimeMillis() / 1000; 
				return tokenResponse;
			} else {
				logger.error("Failed to get Response from API Connect URL");
				return null;
			}

			// logger.info("Response"+response.toString());

			/*
			 * JSONObject jsonObject = new JSONObject(
			 * jsonResponse.substring(jsonResponse.indexOf("{"),
			 * jsonResponse.lastIndexOf("}")));
			 * 
			 * logger.info("Token::"+jsonObject.getString("access_token"));
			 * logger.info("token_type::"+jsonObject.getString("token_type"));
			 * logger.info("scope::"+jsonObject.getString("scope"));
			 * logger.info("expires_in::"+jsonObject.getLong("expires_in"));
			 * logger.info("consented_on::"+jsonObject.getLong("consented_on"));
			 * 
			 * TokenResponse tokenResponse = new TokenResponse();
			 * tokenResponse.setToken_type(jsonObject.getString("token_type"));
			 * tokenResponse.setAccess_token(jsonObject.getString("access_token"));
			 * tokenResponse.setScope(jsonObject.getString("scope"));
			 * tokenResponse.setExpires_in(jsonObject.getLong("expires_in"));
			 * tokenResponse.setConsented_on(jsonObject.getLong("consented_on"));
			 * this.tokenResponse = tokenResponse; this.tokenTimeInSec =
			 * System.currentTimeMillis() / 1000;
			 */

		} catch (Exception ex) {
			status = ex.getMessage();
			logger.error("testToken+status:" + status);
			logger.error("Get Cause:::" + ex.getCause());
			ex.printStackTrace();
			return null;
		}

	}

//	public static void main(String[] args) {
//
//		String jsonResponse = "<200,{\"token_type\":\"Bearer\",\"access_token\":\"AAIgMzY3YjNjNThhOGNiMjM5N2MyNjg1OThkNWExOTBkZmX7iqC-FUIuYErA-aQd3DRAc_cjaclRSRSKZnOu2y-bviYeKX7VU1HST_WfURZUlnn15YwYh0RRWtyDnW1R65CCBlGB-Eqt5VDgqWMwgrpbxkPoPIihiEFUXbUHcK9PUws\",\"scope\":\"EXT\",\"expires_in\":86490,\"consented_on\":1664272416},[Connection:\"Keep-Alive\", Transfer-Encoding:\"chunked\", X-RateLimit-Limit:\"name=default,100;\", X-RateLimit-Remaining:\"name=default,0;\", Accept:\"text/plain, application/json, application/*+json, */*\", User-Agent:\"Apache-HttpClient/4.5.12 (Java/1.8.0_302)\", Accept-Encoding:\"gzip,deflate\", X-Client-IP:\"10.216.48.78\", X-Global-Transaction-ID:\"ad497f146332c8200065c722\", Content-Type:\"application/json\", Cache-Control:\"no-store\", Pragma:\"no-cache\", Strict-Transport-Security:\"max-age=31536000; includeSubDomains\", Date:\"Tue, 27 Sep 2022 09:53:36 GMT\"]>";
//		JSONObject jsonObject = new JSONObject(
//				jsonResponse.substring(jsonResponse.indexOf("{"), jsonResponse.lastIndexOf("}")));
//
//		System.out.println("Token::" + jsonObject.getString("access_token"));
//		System.out.println("token_type::" + jsonObject.getString("token_type"));
//		System.out.println("scope::" + jsonObject.getString("scope"));
//		System.out.println("expires_in::" + jsonObject.getLong("expires_in"));
//		System.out.println("consented_on::" + jsonObject.getLong("consented_on"));
//
//	}
}
