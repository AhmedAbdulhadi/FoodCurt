package com.novent.foodordering.constatnt;
/*
 * created by novent Group backend team (shakalns) 
 */
public class ResponseCode {
	
	public final static String SUCCESS_RESPONSE_CODE = "200" ; // for all except create
	public final static String SUCCESS_CREATE_CODE = "201"; // for create
	public final static String FAILED_RESPONSE_CODE = "400" ; // for all except get
	public final static String FAILED_GET_CODE = "404" ; // for get
	public final static String FAILED_AUTH_CODE = "401"; // for login
	public final static String FAILED_AUTHORIZATION_CODE = "403";

}
