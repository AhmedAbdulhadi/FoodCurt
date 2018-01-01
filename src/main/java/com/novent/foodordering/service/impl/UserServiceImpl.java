package com.novent.foodordering.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.novent.foodordering.constatnt.ResponseCode;
import com.novent.foodordering.constatnt.ResponseMessage;
import com.novent.foodordering.constatnt.ResponseStatus;
import com.novent.foodordering.constatnt.Type;
import com.novent.foodordering.dao.LoginDao;
import com.novent.foodordering.dao.UserDao;
import com.novent.foodordering.entity.Logins;
import com.novent.foodordering.entity.Users;
import com.novent.foodordering.service.UserService;
import com.novent.foodordering.util.CommonsValidator;
import com.novent.foodordering.util.ResponseObject;
import com.novent.foodordering.util.ResponseObjectAll;
import com.novent.foodordering.util.ResponseObjectCrud;
import com.novent.foodordering.util.ResponseObjectData;
import com.novent.foodordering.util.ResponseObjectPage;

@Service
@Component
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private LoginDao loginDao;
	

	@Override
	public ResponseObject getAllUsers(HttpServletRequest request, String sort) {
		ResponseObject response = null;
		String token = request.getHeader("token");
		HttpSession session = request.getSession();
		boolean valid = token != null;
		boolean isExpire = true;
		
		
		if (valid) {
			Logins login = loginDao.findByToken(token);
			Users user = null;
			if (login != null) {
				user = userDao.findByUserName(login.getUserName());
				isExpire = CommonsValidator.isExpire(login.getUserName(), request);
			}
			if (user == null) {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_INCORRECT_TOKEN_ERROR);
			} else if (user.getPassword().equals(login.getPassword())) {
				if (user.getIsLoggedIn()) {
					if (!isExpire) {
						if (Type.ADMINISTRATOR == user.getRole()) {
						
								 List<Users> allUsers;
								
								if (sort.equals("Asc")) {
									allUsers = userDao.findAllByOrderByIdAsc();
								} else {
									sort = "Desc";
									allUsers = userDao.findAllByOrderByIdDesc();
								}
								
								if(!allUsers.isEmpty()){
									response = new ResponseObjectAll<Users>(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, allUsers);
								} else {
									response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
								}
						} else
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTHORIZATION_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
					} else {
						session.removeAttribute(login.getUserName());
						user.setIsLoggedIn(false);
						userDao.save(user);
						login.setToken("123");
						loginDao.save(login);
						response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_SESSION_TIMEOUT_ERROR);
					}
				} else {
					response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
				}
			} else {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
			}
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
		}
		return response;
	}

	@Override
	public ResponseObject getUserByStatus(boolean status, HttpServletRequest request, String sort, Pageable pageable) {
		ResponseObject response = null;
		String token = request.getHeader("token");
		HttpSession session = request.getSession();
		boolean valid = token != null;
		boolean isExpire = true;

		if (valid) {
			Logins login = loginDao.findByToken(token);
			Users user = null;
			if (login != null) {
				user = userDao.findByUserName(login.getUserName());
				isExpire = CommonsValidator.isExpire(login.getUserName(), request);
			}

			if (user == null) {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_INCORRECT_TOKEN_ERROR);
			} else if (user.getPassword().equals(login.getPassword())) {
				if (user.getIsLoggedIn()) {
					if (!isExpire) {
						if (Type.ADMINISTRATOR == user.getRole()) {
							Page<Users> allUsers;
							if (sort.equals("Asc")) {
								allUsers = userDao.findByStatusOrderByIdAsc(status, pageable);
							} else {
								sort = "Desc";
								allUsers = userDao.findByStatusOrderByIdDesc(status, pageable);
							}
							List<Users> jsonUserList = new ArrayList<Users>();
							if (!allUsers.getContent().isEmpty()) {

								for (Iterator<Users> iterator = allUsers.iterator(); iterator.hasNext();) {
									Users users = iterator.next();
									jsonUserList.add(users);
								}

								response = new ResponseObjectPage<Users>(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE,
										allUsers.getNumberOfElements(), allUsers.isLast(), allUsers.getTotalPages(),
										allUsers.getTotalElements(), allUsers.getNumber(), sort,
										allUsers.isFirst(), jsonUserList);

							} else
								response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
						} else
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTHORIZATION_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
					} else {
						session.removeAttribute(login.getUserName());
						user.setIsLoggedIn(false);
						userDao.save(user);
						login.setToken("123");
						loginDao.save(login);
						response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_SESSION_TIMEOUT_ERROR);
					}
				} else {
					response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
				}
			} else {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
			}
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
		}
		return response;
	}

	@Override
	public ResponseObject getUserById(long userId, HttpServletRequest request) {
		ResponseObject response = null;
		// Get the token
		String token = request.getHeader("token");
		HttpSession session = request.getSession();
		boolean valid = token != null;
		boolean isExpire = true;
		// if the token exist
		if (valid) {
			Logins login = loginDao.findByToken(token);
			Users user = null;
			if (login != null) {
				user = userDao.findByUserName(login.getUserName());
				isExpire = CommonsValidator.isExpire(login.getUserName(), request);
			}

			if (user == null) {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_INCORRECT_TOKEN_ERROR);
			} else if (user.getPassword().equals(login.getPassword())) {
				if (user.getIsLoggedIn()) {
					if (!isExpire) {
						if (Type.ADMINISTRATOR == user.getRole()) {
							Users foundUser = userDao.findById(userId);
							if (foundUser != null)
								response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, foundUser);
							else
								response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
						} else {
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTHORIZATION_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
						}
					} else {
						session.removeAttribute(login.getUserName());
						user.setIsLoggedIn(false);
						userDao.save(user);
						login.setToken("123");
						loginDao.save(login);
						response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_SESSION_TIMEOUT_ERROR);
					}
				} else {
					response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
				}
			} else {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
			}
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
		}
		return response;
	}

	@Override
	public ResponseObject createUser(Users user) {
		ResponseObject response = null;
		ResponseObject validationResponse = null;
		long id = 0;

		String phoneNumber = user.getPhoneNumber();
		String userName = user.getUserName();
		String fullName = user.getFullName();
		String password = user.getPassword();
		String email = user.getEmail();
		boolean validValidation = true;

		validationResponse = CommonsValidator.validPhoneNumber(phoneNumber);
		if (validationResponse != null)
			response = validationResponse;

		validationResponse = CommonsValidator.validEmail(email);
		if (validationResponse != null)
			response = validationResponse;

		validationResponse = CommonsValidator.validUserName(userName);
		if (validationResponse != null)
			response = validationResponse;

		validationResponse = CommonsValidator.validFullName(fullName);
		if (validationResponse != null)
			response = validationResponse;

		validationResponse = CommonsValidator.validPassword(password);
		if (validationResponse != null)
			response = validationResponse;

		if (response != null)
			validValidation = false;

		if (phoneNumber != null && phoneNumber != "" && phoneNumber.charAt(0) == '+') {
			phoneNumber = phoneNumber.replace("+", "");
		}

		if (phoneNumber != null && phoneNumber != "" && phoneNumber.charAt(3) == '0') {
			phoneNumber = phoneNumber.replace("0", "");
		}

		Users phoneNumberUser = userDao.findByPhoneNumber(phoneNumber);
		Users userNameUser = userDao.findByUserName(user.getUserName());
		Users emailUser = userDao.findByEmail(user.getEmail());

		boolean valid = ((phoneNumberUser == null) && (userNameUser == null) && (emailUser == null));

		if (phoneNumberUser != null) {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PHONENUMBER_ALREADY_EXIST_ERROR);
		} else if (userNameUser != null) {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_ALREADY_EXIST_ERROR);
		} else if (emailUser != null) {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_ALREADY_EXIST_ERROR);
		} else if (valid && validValidation) {
			user.setPhoneNumber(phoneNumber);
			user.setIsLoggedIn(false);
			user.setRole(Type.USER);
			userDao.save(user);
			id = user.getId();
			response = new ResponseObjectCrud(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_CREATE_CODE, ResponseMessage.SUCCESS_CREATING_MESSAGE, id);
		}
		return response;
	}

	@Override
	public ResponseObject updateUser(long userId, Users user, HttpServletRequest request) {
		ResponseObject response = null;
		ResponseObject validationResponse = null;
		boolean valid = true;
		boolean isExpire = true;
		boolean validValidation = true;

		// Get the token
		String token = request.getHeader("token");
		HttpSession session = request.getSession();
		boolean isToken = token != null;

		if (isToken) {
			Logins login = loginDao.findByToken(token);
			Users loginUser = null;
			Users userById = null;
			if (login != null && userId != 0) {
				userById = userDao.findById(userId);
				loginUser = userDao.findByUserName(login.getUserName());
				isExpire = CommonsValidator.isExpire(login.getUserName(), request);
			} else {
				valid = false;
			}
			
			if (loginUser == null) {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_INCORRECT_TOKEN_ERROR);
			} else if (userById == null) {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_PATCHING_MESSAGE);
			} else if (loginUser.getRole() == Type.USER && (!loginUser.equals(userById))) {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
			} else if (!(loginUser.getRole() == Type.ADMINISTRATOR || loginUser.getRole() == Type.USER)) {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
			} else if (userById != null && loginUser != null && valid && loginUser.getPassword().equals(login.getPassword())) {
				if (loginUser.getIsLoggedIn()) {
					if (!isExpire) {
						if ((Type.USER == userById.getRole())) {
							
							String phoneNumber = user.getPhoneNumber();
							String userName = user.getUserName();
							String fullName = user.getFullName();
							String password = user.getPassword();
							String email = user.getEmail();

							if (!userById.isStatus()) {
								valid = false;
								response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_UPDATE_USER_ERROR);
							}

							if (phoneNumber != null && phoneNumber != "" && valid) {

								validationResponse = CommonsValidator.validPhoneNumber(phoneNumber);
								if (validationResponse != null)
									response = validationResponse;
 
								if (phoneNumber != null && phoneNumber != "" && phoneNumber.charAt(0) == '+') {
									phoneNumber = phoneNumber.replace("+", "");
								}

								if (phoneNumber != null && phoneNumber != "" && phoneNumber.charAt(3) == '0') {
									phoneNumber = phoneNumber.replace("0", "");
								}

								Users phoneNumberUser = userDao.findByPhoneNumber(user.getPhoneNumber());
								if (phoneNumberUser != null && !userById.equals(phoneNumberUser)) {
									valid = false;
									response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PHONENUMBER_ALREADY_EXIST_ERROR);
								}
								
								if (response != null)
									validValidation = false;

								 if (valid && validValidation) {
									userById.setPhoneNumber(phoneNumber);
									userById.setUpdatedAt(new Date());
									userDao.save(userById);
									response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, userById);
								}
							}

							if (userName != null && !userName.equals("") && valid) {
								Users userNameUser = userDao.findByUserName(userName);
								if (userNameUser != null && !userById.equals(userNameUser)) {
									valid = false;
									response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_USERNAME_ALREADY_EXIST_ERROR);
								}
								
								validationResponse = CommonsValidator.validUserName(userName);
								if (validationResponse != null)
									response = validationResponse;

								if (response != null)
									validValidation = false;

								if (valid && validValidation) {
									userById.setUserName(userName);
									userById.setUpdatedAt(new Date());
									userDao.save(userById);
									response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, userById);
								}
							}

							if (fullName != null && !fullName.equals("") && valid) {
								validationResponse = CommonsValidator.validFullName(fullName);
								if (validationResponse != null)
									response = validationResponse;

								if (response != null)
									validValidation = false;

								if (valid && validValidation) {
									userById.setFullName(fullName);
									userById.setUpdatedAt(new Date());
									userDao.save(userById);
									response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, userById);
								}
							}

							if (password != null && !password.equals("") && valid) {
								validationResponse = CommonsValidator.validPassword(password);
								if (validationResponse != null)
									response = validationResponse;

								if (response != null)
									validValidation = false;

								if (valid && validValidation) {
									userById.setPassword(password);
									userById.setUpdatedAt(new Date());
									userDao.save(userById);
									response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, userById);
								}
							}

							if (email != null && !email.equals("") && valid) {
								Users emailUser = userDao.findByEmail(user.getEmail());
								validationResponse = CommonsValidator.validEmail(email);
								if (validationResponse != null)
									response = validationResponse;

								if (emailUser != null && !userById.equals(emailUser)) {
									valid = false;
									response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_EMAIL_ALREADY_EXIST_ERROR);
								}

								if (response != null)
									validValidation = false;

								if (valid && validValidation) {
									userById.setEmail(email);
									userById.setUpdatedAt(new Date());
									userDao.save(userById);
									response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, userById);
								}
							}

						} else {
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTHORIZATION_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
						}
					} else {
						session.removeAttribute(login.getUserName());
						loginUser.setIsLoggedIn(false);
						userDao.save(loginUser);
						login.setToken("123");
						loginDao.save(login);
						response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_SESSION_TIMEOUT_ERROR);
					}
				} else {
					response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
				}
			} else {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
			}
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
		}
		return response;
	}

	@Override
	public ResponseObject deleteUser(long userId, HttpServletRequest request) {
		ResponseObject response = null;
		// Get the token
		String token = request.getHeader("token");
		HttpSession session = request.getSession();
		boolean valid = token != null;
		boolean isExpire = true;

		// if the token exist
		if (valid) {
			Logins login = loginDao.findByToken(token);
			Users loginUser = null;
			Users user = null;
			if (login != null && userId != 0) {
				loginUser = userDao.findByUserName(login.getUserName());
				user = userDao.findById(userId);
				isExpire = CommonsValidator.isExpire(login.getUserName(), request);
			} else {
				valid = false;
			}
			if (user == null) {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_PATCHING_MESSAGE);
			} else if (loginUser == null) {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_INCORRECT_TOKEN_ERROR);
			} else if (loginUser.getRole() == Type.USER && (!loginUser.equals(user))) {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
			} else if (!(loginUser.getRole() == Type.ADMINISTRATOR || loginUser.getRole() == Type.USER)) {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
			} else if (user != null && loginUser != null && loginUser.getPassword().equals(login.getPassword())) {
				if (loginUser.getIsLoggedIn()) {
					if (!isExpire) {
						if (Type.USER == user.getRole()) {
							if (user.isStatus()) {
								user.setStatus(false);
								user.setIsLoggedIn(false);
								user.setDeletedAt(new Date());
								userDao.save(user);
								response = new ResponseObjectCrud(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_DELETTING_MESSAGE, user.getId());
							} else
								response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_DELETTING_MESSAGE);
						} else {
							response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTHORIZATION_CODE, ResponseMessage.FAILED_AUTHORIZATION_MESSAGE);
						}
					} else {
						session.removeAttribute(login.getUserName());
						user.setIsLoggedIn(false);
						userDao.save(user);
						login.setToken("123");
						loginDao.save(login);
						response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_SESSION_TIMEOUT_ERROR);
					}
				} else {
					response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
				}
			} else {
				response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
			}
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_AUTH_CODE, ResponseMessage.FAILED_AUTHENTICATION_MESSAGE);
		}
		return response;
	}
}