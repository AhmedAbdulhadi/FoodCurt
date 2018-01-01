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
	public final static String FAILED_AUTHENTICATION_MESSAGE = "You are not Authenticate";
	public final static String FAILED_AUTHORIZATION_MESSAGE = "You are not Authorize";

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
	public final static String FAILED_AREANAMEAR_ALREADY_EXIST_ERROR = "Arabic Area Name already exist";
	public final static String FAILED_EMAIL_ALREADY_EXIST_ERROR = "Email already exist";
	public final static String FAILED_USERNAME_LENGTH_ERROR = "user Name length must be between 6 and 20";
	public final static String FAILED_USERNAME_LENGTH_GREATER_ERROR = "user Name length must be greater than 6";
	public final static String FAILED_USERNAME_LENGTH_LESS_ERROR = "user Name length must be less than 20";
	public final static String FAILED_FULLNAME_LENGTH_ERROR = "full Name length must be between 10 and 40";
	public final static String FAILED_FULLNAME_LENGTH_GREATER_ERROR = "full Name length must be greater than 10";
	public final static String FAILED_FULLNAME_LENGTH_LESS_ERROR = "full Name length must be less than 40";
	public final static String FAILED_PASSWORD_LENGTH_ERROR = "password length must be between 6 and 10";
	public final static String FAILED_PASSWORD_LENGTH_GREATER_ERROR = "password length must be greater than 6 ";
	public final static String FAILED_PASSWORD_LENGTH_LESS_ERROR = "password length must be less than 10";
	public final static String FAILED_ADMINISTRATOR_NUMBER_ERROR = "administrator not found";
	public final static String FAILED_ERROR = "ERROR";
	public final static String FAILED_PREFIX_FORMAT_ERROR = "phone number must start with +962";
	public final static String FAILED_PHONENUMBER_FORMAT_ERROR = "invalid phone Number format";
	public final static String FAILED_EMAIL_FORMAT_ERROR = "invalid email address";
	public final static String FAILED_RESTAURANT_NAME_ERROR = "restaurant Name length must be between 3 and 15";
	public final static String FAILED_RESTAURANT_NAME_GREATER_ERROR = "restaurant Name length must greater than 3 ";
	public final static String FAILED_RESTAURANT_NAME_LESS_ERROR = "restaurant Name length must be less than 15";
	public final static String FAILED_BRANCH_NAME_ERROR = "Branch Name length must be between 5 and 15";
	public final static String FAILED_RESTAURANT_ALREADY_EXIST_ERROR = "Restaurant Name already exist";
	public final static String FAILED_RESTAURANTAR_ALREADY_EXIST_ERROR = "Arabic Restaurant Name already exist";
	public final static String FAILED_PHONENUMBER_REQUIRED_ERROR = "Phone number is required field";
	public final static String FAILED_USERNAME_REQUIRED_ERROR = "UserName is required field";
	public final static String FAILED_USERID_REQUIRED_ERROR = "UserId is required field";
	public final static String FAILED_FULLNAME_REQUIRED_ERROR = "FullName is required field";
	public final static String FAILED_PASSWORD_REQUIRED_ERROR = "Password is required field";
	public final static String FAILED_AR_REQUIRED_ERROR = "Arabic Name is required field";
	public final static String FAILED_EMAIL_ADDRESS_REQUIRED_ERROR = "Email Address is required field";
	public final static String FAILED_PRIVILAGE_REQUIRED_ERROR = "Privilage is required field";
	public final static String FAILED_ADMINISTRATORID_REQUIRED_ERROR = "Administrator Id is required field";
	public final static String FAILED_ADMIN_REQUIRED_ERROR = "Admin Id is required field";
	public final static String FAILED_AREANAME_REQUIRED_ERROR = "Area Name is required field";
	public final static String FAILED_AREANAMEAR_REQUIRED_ERROR = "Arabic Area Name is required field";
	public final static String FAILED_ITEMNAME_REQUIRED_ERROR = "Item Name is required field";
	public final static String FAILED_PRICE_REQUIRED_ERROR = "Price is required field";
	public final static String FAILED_RESTAURANTNAME_REQUIRED_ERROR = "Restaurant Name is required field";
	public final static String FAILED_RESTAURANTNAMEAR_REQUIRED_ERROR = "Arabic Restaurant Name is required field";
	public final static String FAILED_BRANCHNAME_REQUIRED_ERROR = "Branch Name is required field";
	public final static String FAILED_BRANCHNAMEAR_REQUIRED_ERROR = "Arabic Branch Name is required field";
	public final static String FAILED_BRANCHID_REQUIRED_ERROR = "Branch Id is required field";
	public final static String FAILED_AREAID_REQUIRED_ERROR = "areaId is required field";
	public final static String FAILED_RESTAURANTID_REQUIRED_ERROR = "restaurantId is required field";
	public final static String FAILED_QUANTITY_REQUIRED_ERROR = "quantity is required field";
	public final static String FAILED_UPDATE_USER_ERROR = "User account is suspended";
	public final static String FAILED_UPDATE_ADMINISTRATOR_ERROR = "Administrator account is suspended";
	public final static String FAILED_UPDATE_ADMIN_ERROR = "Admin account is suspended";
	public final static String FAILED_UPDATE_AREA_ERROR = "Area is suspended";
	public final static String FAILED_UPDATE_RESTAURANT_ERROR = "Restaurant is suspended";
	public final static String FAILED_UPDATE_ITEM_ERROR = "Item is suspended";
	public final static String FAILED_UPDATE_BRANCH_ERROR = "Branch is suspended";
	public final static String FAILED_NOORDER_ERROR = "Items is required field ";
	public final static String FAILED_ARABICNAME_ERROR = "invalid Arabic Name";
	public final static String FAILED_PASSWORD_ERROR = "Incorrect password";
	public final static String FAILED_USER_ALREADY_LOGGEDIN_ERROR = "User already logged In";
	public final static String SUCCESS_LOGGEDOUT = "success logged out";
	public final static String FAILED_NOTLOGGEDIN = "user already logged out";
	public final static String FAILED_INCORRECT_CREDENTIALS_ERROR = "incorrect credentials";
	public final static String FAILED_INCORRECT_TOKEN_ERROR = "incorrect token";
	public final static String FAILED_SESSION_TIMEOUT_ERROR = "Session TimeOut";

}
