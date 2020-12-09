package com.poorvika.controller;

import java.util.Map;

import javax.inject.Inject;

import com.poorvika.client.HashAlgorithm;
import com.poorvika.client.PayUHTTTPClient;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.exceptions.HttpClientException;
import io.micronaut.http.hateoas.JsonError;
import io.reactivex.Maybe;

@Controller("/home")
public class PayUController {
	@Value("${demo.key}")
	private String key;
	@Value("${demo.Salt}")
	private String Salt;

	@Inject
	private HashAlgorithm hashAlgorithm;
	@Inject
	private PayUHTTTPClient payUHTTTPClient;

	@Post
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Maybe<String> payUHome(@Body Map<String, String> requestBody) {
		String Hashsequence = key + "|" + requestBody.get("txnid") + "|" + requestBody.get("amount") + "|"
				+ requestBody.get("productinfo") + "|" + requestBody.get("firstname") + "|" + requestBody.get("email")
				+ "|" + requestBody.get("phone") + "|" + requestBody.get("surl") + "|" +requestBody.get("furl") +"|" + "|" + "|" + "|" + "|" + "|" + "|" + "|" + Salt;

		
		System.out.println(Hashsequence);
		String Hash = hashAlgorithm.hashCal2(Hashsequence);
		return this.payUHTTTPClient.getHash(requestBody.get("txnid"), requestBody.get("amount"),requestBody.get("productinfo"), requestBody.get("firstname"), requestBody.get("email"),requestBody.get("phone"),requestBody.get("surl"),requestBody.get("furl"), Hash);

		// Hash calculation
		// Hash =
		// sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5||||||beneficiarydetail|SALT)

		// return HttpResponse.ok(Hashsequence);
	}

	@Error(exception = HttpClientException.class)
	public HttpResponse<?> onHTTPClientFailed(HttpRequest request, HttpClientException ex) {
		JsonError error = new JsonError(ex.getMessage());
		return HttpResponse.<JsonError>serverError().body(error);
	}

	@Error(status = HttpStatus.NOT_FOUND)
	public HttpResponse<JsonError> notFound(HttpRequest request) {
		JsonError error = new JsonError("Resource Not Found");
		return HttpResponse.<JsonError>notFound().body(error);
	}
	/*
	 * @Post(value = "/value") public String home(@PathVariable("key")String
	 * key,@PathVariable("txnid")String txnid,
	 * 
	 * @PathVariable("amount")String amount,@PathVariable("productinfo")String
	 * productinfo ,
	 * 
	 * @PathVariable("firstname")String firstname) { System.out.println(key+""+txnid
	 * +""+amount+""+productinfo); String Store = key+""+txnid
	 * +""+amount+""+productinfo; return Store; }
	 * 
	 * @Get() public String message(){ return "Welcome to Micronaut Framework 6"; }
	 */
}