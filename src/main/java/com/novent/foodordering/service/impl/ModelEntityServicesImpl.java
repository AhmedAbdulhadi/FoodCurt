package com.novent.foodordering.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.novent.foodordering.constatnt.ResponseCode;
import com.novent.foodordering.constatnt.ResponseMessage;
import com.novent.foodordering.constatnt.ResponseStatus;
import com.novent.foodordering.dao.ModelEntityDao;
import com.novent.foodordering.entity.ModelEntity;
import com.novent.foodordering.service.ModelEntityServices;
import com.novent.foodordering.util.ResponseObject;
import com.novent.foodordering.util.ResponseObjectAll;
import com.novent.foodordering.util.ResponseObjectCrud;
import com.novent.foodordering.util.ResponseObjectData;

@Service
@Component
public class ModelEntityServicesImpl implements ModelEntityServices {
	@Autowired
	private ModelEntityDao modelEntityDao;
	
	@Override
	public ResponseObject getAllModelEntity() {
		List<ModelEntity> allModelEntitys = modelEntityDao.findAll();
		ResponseObject response = null;
		
		
		if (allModelEntitys.isEmpty()){
		response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS ,ResponseCode.FAILED_GET_CODE , ResponseMessage.FAILED_GETTING_MESSAGE );  	
		}else{
		response = new ResponseObjectAll<ModelEntity>(ResponseStatus.SUCCESS_RESPONSE_STATUS , ResponseCode.SUCCESS_RESPONSE_CODE , ResponseMessage.SUCCESS_GETTING_MESSAGE , allModelEntitys);
		}
		return response;
	
	}

	
	@Override
	public ResponseObject createModelEntity(ModelEntity modelEntity) {
		
		boolean valid  = (((modelEntityDao.findByPhoneNumber(modelEntity.getPhoneNumber()) == null) && (modelEntityDao.findByEmail(modelEntity.getEmail()) == null)&& (modelEntityDao.findByUserName(modelEntity.getUserName()) == null))? true : false) ;
		
		ResponseObject response = null;
		long id  =-1;
		if (valid) {
		 modelEntity.setCreatedAt(new Date());
		 modelEntityDao.save(modelEntity);
		 id = modelEntity.getModelEntityId();
		 response = new ResponseObjectCrud(ResponseStatus.SUCCESS_RESPONSE_STATUS , ResponseCode.SUCCESS_CREATE_CODE , ResponseMessage.SUCCESS_CREATING_MESSAGE ,id );  
		}
		else 
	    response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS , ResponseCode.FAILED_RESPONSE_CODE , "phone number , email or user name is already exist");  

		return response;
		
	}
	@Override
	public ResponseObject  updateModelEntity(long id, ModelEntity modelEntity ) {
		
		ModelEntity modelEntityId    = modelEntityDao.findByModelEntityId(id);
		ModelEntity modelEntityPhone = modelEntityDao.findByPhoneNumber(modelEntity.getPhoneNumber());
		ModelEntity modelEntityEmail = modelEntityDao.findByEmail(modelEntity.getEmail());
		ModelEntity modelEntityName = modelEntityDao.findByUserName(modelEntity.getUserName());
		
		boolean valid = false;
		ResponseObject response = null;

		
		if (modelEntityId != null)
	    valid = (modelEntityPhone == null && modelEntityEmail == null) || 
	    (modelEntityEmail != null && modelEntityPhone == null && modelEntityId.equals(modelEntityEmail)) || 
	    (modelEntityPhone != null && modelEntityEmail == null && modelEntityId.equals(modelEntityPhone)) || 
	    ((modelEntityPhone!= null && modelEntityEmail != null) && (modelEntityId .equals(modelEntityEmail)) && modelEntityId.equals(modelEntityPhone));
		
		if (modelEntityName != null && !(modelEntityName.equals(modelEntityId)))
			valid = false;
		
		
		if (valid && modelEntityId.isStatus()){
		modelEntity.setUpdatedAt(new Date());
		modelEntity.setModelEntityId(id);
		modelEntity.setPassword(modelEntityId.getPassword());
		modelEntityDao.save(modelEntity);
		response = new ResponseObjectCrud(ResponseStatus.SUCCESS_RESPONSE_STATUS , ResponseCode.SUCCESS_RESPONSE_CODE , ResponseMessage.SUCCESS_UPDATING_MESSAGE , id );
		}else if (!valid) {
		response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS , ResponseCode.FAILED_RESPONSE_CODE , "phone number , email or user name is already exist" );
		}else if (!modelEntity.isStatus()){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS , ResponseCode.FAILED_RESPONSE_CODE , "Deactivated ModelEntity " );

		}
		
		return response;
	}
	


	@Override
	public ResponseObject deleteModelEntity(long id ) {
		ModelEntity modelEntity = modelEntityDao.findByModelEntityId(id);
		ResponseObject response = null; 
		
		if (modelEntity != null && modelEntity.isStatus()){
		modelEntity.setStatus(false);
		modelEntity.setDeletedAt(new Date());
		modelEntityDao.save(modelEntity);
		response = new ResponseObjectCrud(ResponseStatus.SUCCESS_RESPONSE_STATUS ,ResponseCode.SUCCESS_RESPONSE_CODE ,ResponseMessage.SUCCESS_DELETTING_MESSAGE , id);
		}else{
		response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS ,ResponseCode.FAILED_RESPONSE_CODE ,ResponseMessage.FAILED_DELETTING_MESSAGE );	
		}
		
		return response;
		
	}
	


	@Override
	public ResponseObject loginModelEntity(String userName, String password) {
		ModelEntity modelEntity = modelEntityDao.findByUserName(userName);
		ResponseObject response = null;
		
		if(modelEntity==null){response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS ,ResponseCode.FAILED_RESPONSE_CODE ,ResponseMessage.FAILED_LOGIN_MESSAGE );	
		
		}		
		else if (!modelEntity.isStatus()) {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS ,ResponseCode.FAILED_AUTH_CODE ,"Deavctivated modelEntity");	
			
		}
		else if((modelEntity!=null)&&(password.equals(modelEntity.getPassword())) && modelEntity.isStatus())
			{
			 String token = generateToken();
			 modelEntity.setToken(token);
			 modelEntityDao.save(modelEntity);
		   response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS , ResponseCode.SUCCESS_RESPONSE_CODE , ResponseMessage.SUCCESS_LOGIN_MESSAGE ,modelEntity);
			}
		else{
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS ,ResponseCode.FAILED_RESPONSE_CODE ,ResponseMessage.FAILED_LOGIN_MESSAGE );	
         }
	
		return response;	
	}
	
	
	
	
	//generate login token
	private String generateToken() {
		String token = "";
		char[]charSet = "ABCDEFGHIJKLMNOPQRSTUZWXYZabcdefghijklmnopqrstuvwxyz123456789".toCharArray();
		while(token.length() < 20){
			token += charSet[(int)(Math.random()*60)];
		}
		return token;
	}



	@Override
	public ResponseObject getModelEntityById(long modelEntityId) {
       ResponseObject response = null;
       ModelEntity modelEntity = modelEntityDao.findByModelEntityId(modelEntityId);
		
		if (modelEntity == null)
		response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS ,ResponseCode.FAILED_GET_CODE , ResponseMessage.FAILED_GETTING_MESSAGE );  	
		else{
		modelEntity.setPassword(null);
		response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS , ResponseCode.SUCCESS_RESPONSE_CODE , ResponseMessage.SUCCESS_GETTING_MESSAGE , modelEntity);
		}
		return response;
			}


	@Override
	public ResponseObject logOutModelEntity(long id) {
		ModelEntity modelEntity = modelEntityDao.findByModelEntityId(id);
		ResponseObject response = null;
		if (modelEntity !=  null){
	    modelEntity.setToken("0000");
	    modelEntityDao.save(modelEntity);
		response = new ResponseObject(ResponseStatus.SUCCESS_RESPONSE_STATUS , ResponseCode.SUCCESS_RESPONSE_CODE , "logged out successfully");
		}else 
		response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS , ResponseCode.FAILED_RESPONSE_CODE , "faild logging out");	
		
		return response;
		
	
	}


	
	

	

}
