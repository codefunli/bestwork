package com.nineplus.bestwork.utils;

import java.util.Collections;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class RestTemplateUtils {

	/**
	 * buildHttpHeaders
	 * 
	 * @param MediaType mediaType
	 * @param String    token
	 * @return HttpHeaders
	 */
	public HttpHeaders buildHttpHeaders(MediaType mediaType, String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.ALL));
		headers.setContentType(mediaType);
		headers.setBearerAuth(token);
		return headers;
	}

	/**
	 * buildHttpHeadersJsonWithToken
	 * 
	 * @param String token
	 * @return HttpHeaders
	 */
	public HttpHeaders buildHttpHeadersJsonWithToken(String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "Bearer " + token);
		return headers;
	}

	/**
	 * Create header with contentType is application/x-www-form-urlencoded
	 * 
	 * @return HttpHeaders
	 */
	public HttpHeaders buildHeadersWithAppFormUrlEnCoded() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		return headers;
	}

}
