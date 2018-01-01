package com.novent.foodordering.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.novent.foodordering.constatnt.ResponseCode;
import com.novent.foodordering.constatnt.ResponseMessage;
import com.novent.foodordering.constatnt.ResponseStatus;

public class CommonsValidator {
	
	public static boolean isExpire(String userName, HttpServletRequest request) {
		System.out.println("------------------------------------");
		Date time = null;
		Date time2 = null;
		
		try {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = new Date();
		
		String dd = dateFormat.format(date);
		System.out.println("current Date :: "+dd);		
		System.out.println("User Name :: " + userName);
		String token = request.getSession().getAttribute(userName).toString();
		String [] tokenArray = token.split("\\.");
		String accessTime = tokenArray[0];
		String remmberMe= tokenArray[1];
		System.out.println("accessTime :: "+accessTime);
		System.out.println("remmberMe  :: "+remmberMe);

		
			time = dateFormat.parse(accessTime);
			time2 = dateFormat.parse(dd);
			long currentTime = TimeUnit.MILLISECONDS.toMinutes(time2.getTime());
			long LastAccesse = TimeUnit.MILLISECONDS.toMinutes(time.getTime());
			System.out.println("currentTime - LastAccesse :: "+(currentTime - LastAccesse));
			if(remmberMe.equals("true")){
				System.out.println("allways loggedIn");
				return false;
			} else if (currentTime - LastAccesse > 1) {
				System.out.println("if");
				return true;
			} else {
				System.out.println("else");
				return false;
			}
		} catch (Exception e) {
			return true;
		}
	}
	
	public static ResponseObject validEmail(String email){
		ResponseObject response = null;
		String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"; 
		if (email == null || email.equals("")){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_ADDRESS_REQUIRED_ERROR);				
		} else if (!email.matches(regex)) {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_FORMAT_ERROR);
		}
		return response;
	}
	public static ResponseObject validPhoneNumber(String phoneNumber){
		ResponseObject response = null;
		PhoneNumber  phone = null;
		boolean isValidNumber = false;
		boolean isJONumber = false;
		
		PhoneNumberUtil pnUtil = PhoneNumberUtil.getInstance();
		try{
			phone = pnUtil.parse(phoneNumber,"");
			isValidNumber = pnUtil.isValidNumber(phone);
		    isJONumber = pnUtil.getRegionCodeForNumber(phone).equals("JO");
		} catch (Exception e) {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_ERROR);
		}
		
		if (phoneNumber == null || phoneNumber.equals("")){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PHONENUMBER_REQUIRED_ERROR);				
		}  else if(phoneNumber != null && phoneNumber != "" && !isValidNumber && phoneNumber.substring(0, 2).equals("00")){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PREFIX_FORMAT_ERROR);			
		} else if (!isJONumber || !isValidNumber){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PHONENUMBER_FORMAT_ERROR);
		}
		return response;
	}

	public static ResponseObject validUserName(String userName){
		ResponseObject response = null;
		if (userName == null || userName.equals("")){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_REQUIRED_ERROR);				
		}  else if(userName.length() > 20 || userName.length() < 6){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_LENGTH_ERROR);
		}
		return response;
	}
	
	public static ResponseObject validFullName(String fullName){
		ResponseObject response = null;
		if (fullName == null || fullName.equals("")){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_FULLNAME_REQUIRED_ERROR);				
		} else if (fullName.length() > 40 || fullName.length() < 10){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_FULLNAME_LENGTH_ERROR);
		}
		return response;
	}
	
	public static ResponseObject validPassword(String password){
		ResponseObject response = null;
		if (password == null || password.equals("")){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PASSWORD_REQUIRED_ERROR);				
		} else if(password.length() < 6 || password.length() > 10){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PASSWORD_LENGTH_ERROR);
		}
		return response;
	}
	
	public static ResponseObject validARName(String nameAR){
		ResponseObject response = null;
		String regex = "^[\u0621-\u064A0-9 ]+$";
		if (nameAR == null || nameAR.equals("")){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_AR_REQUIRED_ERROR);				
		} else if(!nameAR.matches(regex)){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_ARABICNAME_ERROR);
		} 
		return response;
	}
	
	public static String getToken() {
	    String CharSet = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ234567890!@#$";
	    String Token = "";
	    for (int a = 1; a <= 30; a++) {
	        Token += CharSet.charAt(new Random().nextInt(CharSet.length()));
	    }
	    return Token;
	}
}
