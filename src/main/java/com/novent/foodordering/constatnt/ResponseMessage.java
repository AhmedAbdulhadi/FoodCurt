package com.novent.foodordering.constatnt;
/*
 * created by novent Group backend team (shakalns) 
 */
public class ResponseMessage {
	// fail message for all 
	public final static String FAILED_GETTING_MESSAGE = "not found"; 
	public final static String FAILED_PATCHING_MESSAGE = "Invalid id ";
	public final static String FAILED_DELETTING_MESSAGE = "Invalid id or already deleted";
	public final static String FAILED_LOGIN_MESSAGE = "Invalid phone number or password ";
    public final static String FAILED_STRING_MESSAGE = "wrong date format";
    public final static String FAILED_CREATING_MESSAGE = "duplicate params";
    public final static String FAILED_UPDATING_MESSAGE = "duplicate params";
	// success messages for all 
	public final static String SUCCESS_GETTING_MESSAGE = "Found";
	public final static String SUCCESS_CREATING_MESSAGE = "Created";
	public final static String SUCCESS_UPDATING_MESSAGE = "Updated";
	public final static String SUCCESS_PATCHING_MESSAGE = "Changed";
	public final static String SUCCESS_DELETTING_MESSAGE = "Deleted";
	public final static String SUCCESS_LOGIN_MESSAGE = "logged on successfully";
	//new added
	public final static String FAILED_PHONENUMBER_ALREADY_EXIST_ERROR = "phone number already exist";
	public final static String FAILED_USERNAME_ALREADY_EXIST_ERROR = "userName already exist";
	public final static String FAILED_NO_ADMIN_ERROR = "no admin found or deleted ";
	public final static String FAILED_NO_USER_ERROR = "no user found";
	public final static String FAILED_NO_AREA_ERROR = "no area found";
	public final static String FAILED_NO_ITEM_ERROR = "no item found";
	public final static String FAILED_NO_BRANCH_ERROR = "no branch found";
	public final static String FAILED_NO_RESTAURANT_ERROR = "no Restaurant found";
	public final static String FAILED_AREANAME_ALREADY_EXIST_ERROR = "Area Name already exist";
	public final static String FAILED_EMAIL_ALREADY_EXIST_ERROR = "Email already exist";
	public final static String FAILED_USERNAME_LENGTH_ERROR = "user Name length must be between 6 and 20";
	public final static String FAILED_FULLNAME_LENGTH_ERROR = "full Name length must be between 10 and 40";
	public final static String FAILED_PASSWORD_LENGTH_ERROR = "password length must be between 6 and 10";
	public final static String FAILED_ADMINISTRATOR_NUMBER_ERROR = "administrator not found";
	public final static String FAILED_ERROR = "ERROR";
	public final static String FAILED_PREFIX_FORMAT_ERROR = "phone number not start with 00";
	public final static String FAILED_PHONENUMBER_FORMAT_ERROR = "invalid phone Number format";
	public final static String FAILED_EMAIL_FORMAT_ERROR = "invalid email address";
	public final static String FAILED_RESTAURANT_NAME_ERROR = "restaurant Name length must be between 3 and 15";
	public final static String FAILED_BRANCH_NAME_ERROR = "Branch Name length must be between 5 and 15";
	public final static String FAILED_RESTAURANT_ALREADY_EXIST_ERROR = "RESTAURANT Name already exist";



	
	


	

}
