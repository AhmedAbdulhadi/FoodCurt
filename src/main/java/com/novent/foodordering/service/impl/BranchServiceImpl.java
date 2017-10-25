package com.novent.foodordering.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.novent.foodordering.constatnt.ResponseCode;
import com.novent.foodordering.constatnt.ResponseMessage;
import com.novent.foodordering.constatnt.ResponseStatus;
import com.novent.foodordering.dao.AreaDao;
import com.novent.foodordering.dao.BranchDao;
import com.novent.foodordering.dao.RestaurantDao;
import com.novent.foodordering.entity.Area;
import com.novent.foodordering.entity.Branch;
import com.novent.foodordering.entity.Restaurant;
import com.novent.foodordering.service.BranchService;
import com.novent.foodordering.util.ResponseObject;
import com.novent.foodordering.util.ResponseObjectAll;
import com.novent.foodordering.util.ResponseObjectCrud;
import com.novent.foodordering.util.ResponseObjectData;

@Service
@Component
public class BranchServiceImpl implements BranchService{
	
	@Autowired
	private BranchDao branchDao;
	@Autowired
	private RestaurantDao restaurantDao;
	@Autowired
	private AreaDao areaDao;

	@Override
	public ResponseObject getAllBranch() {
		ResponseObject response = null;
		List<Branch> allBranchs = branchDao.findAll();
		if(!allBranchs.isEmpty()){
			response = new ResponseObjectAll<Branch>(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, allBranchs);
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
		}
		return response;
	}

	@Override
	public ResponseObject getBranchById(long branchId) {
		ResponseObject response = null;
		Branch branch = branchDao.findByBranchId(branchId);
		if (branch != null){
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_GETTING_MESSAGE, branch);
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_GETTING_MESSAGE);
		}
		return response;
	}

	@Override
	public ResponseObject createBranch(Branch branch) {
		ResponseObject response = null;
		PhoneNumber  phone = null;
		boolean isValidNumber = false;
		boolean isJONumber = false;
		long id = 0;
		
		Area area = areaDao.findByAreaId(branch.getAreaId());
		Restaurant restaurant = restaurantDao.findByRestaurantId(branch.getRestaurantId());
		Branch phoneNumberBranch = branchDao.findByPhoneNumber(branch.getPhoneNumber());
		
		boolean valid = (restaurant != null && area != null && branch != null && phoneNumberBranch == null);
		
		PhoneNumberUtil pnUtil = PhoneNumberUtil.getInstance();
		
		String branchName = branch.getBranchName();
		String phoneNumber = branch.getPhoneNumber();
//		String rate = branch.getRate();
//		String workingHours = branch.getWorkingHours();
//		boolean isOpen = branch.getIsOpen();
//		long restaurantId = branch.getRestaurantId();
//		long areaId = branch.getAreaId();
		
		try{
			phone = pnUtil.parse(phoneNumber,"");
			isValidNumber = pnUtil.isValidNumber(phone);
		    isJONumber = pnUtil.getRegionCodeForNumber(phone).equals("JO");
		} catch (Exception e) {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_ERROR);
		}
		
		if (restaurant == null){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_NO_RESTAURANT_ERROR);
		} else if(area == null){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_NO_AREA_ERROR);
		} else if(branchName.length() < 5 || branchName.length() > 15){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_BRANCH_NAME_ERROR);			
		} else if(!isValidNumber && phoneNumber.substring(0, 2).equals("00")){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PREFIX_FORMAT_ERROR);			
		} else if (!isJONumber || !isValidNumber){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PHONENUMBER_FORMAT_ERROR);
		} else if (phoneNumberBranch != null){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_PHONENUMBER_ALREADY_EXIST_ERROR);
		} else if(valid){
			int numOfBranches = restaurant.getNumberOfBranches();
			restaurant.setNumberOfBranches(++numOfBranches);
			List<Branch> branches = restaurant.getBranches();
			branches.add(branch);
			branchDao.save(branch);
			restaurant.setBranches(branches);
			restaurantDao.save(restaurant);
			id = branch.getBranchId();
			List<Branch> branchList = area.getBranches();
			branchList.add(branch);
			area.setBranches(branchList);
			areaDao.save(area);
			response = new ResponseObjectCrud(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_CREATE_CODE, ResponseMessage.SUCCESS_CREATING_MESSAGE, id);
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_CREATING_MESSAGE);
	}
		return response;
	}

	@Override
	public ResponseObject updateBranch(long branchId, Branch branch) {
		ResponseObject response = null;
		PhoneNumber  phone = null;
		boolean isValidNumber = false;
		boolean isJONumber = false;
		
		Branch branchToUpdate = branchDao.findByBranchId(branchId);
		Branch branchPhoneNumber = branchDao.findByPhoneNumber(branch.getPhoneNumber());
		Area area = areaDao.findByAreaId(branch.getAreaId());
//		Restaurant restaurant = restaurantDao.findByRestaurantId(branch.getRestaurantId());
		
		boolean valid = (branchToUpdate != null && (branchPhoneNumber != null && branchToUpdate.equals(branchPhoneNumber) || branchPhoneNumber == null) && area != null);
		
		PhoneNumberUtil pnUtil = PhoneNumberUtil.getInstance();

		String branchName = branch.getBranchName();
		String phoneNumber = branch.getPhoneNumber();
		String rate = branch.getRate();
		String workingHours = branch.getWorkingHours();
		boolean isOpen = branch.getIsOpen();
		long areaId = branch.getAreaId();
		
		try{
			phone = pnUtil.parse(phoneNumber,"");
			isValidNumber = pnUtil.isValidNumber(phone);
		    isJONumber = pnUtil.getRegionCodeForNumber(phone).equals("JO");
		} catch (Exception e) {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_ERROR);
		}
		
		if( branchToUpdate == null){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_NO_BRANCH_ERROR);
		} else if(area == null){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_NO_AREA_ERROR);
		} else if(branchName.length() < 5 || branchName.length() > 15){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_BRANCH_NAME_ERROR);			
		} else if(!isValidNumber && phoneNumber.substring(0, 2).equals("00")){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PREFIX_FORMAT_ERROR);			
		} else if (!isJONumber || !isValidNumber){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_PHONENUMBER_FORMAT_ERROR);
		} else if (branchPhoneNumber != null && !branchToUpdate.equals(branchPhoneNumber)){
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_GET_CODE, ResponseMessage.FAILED_PHONENUMBER_ALREADY_EXIST_ERROR);
		} else if(valid){
			branchToUpdate.setBranchName(branchName);
			branchToUpdate.setPhoneNumber(phoneNumber);
			branchToUpdate.setRate(rate);
			branchToUpdate.setIsOpen(isOpen);
			branchToUpdate.setAreaId(areaId);
			branchToUpdate.setWorkingHours(workingHours);
			branchToUpdate.setUpdatedAt(new Date());
			branchDao.save(branchToUpdate);
			response = new ResponseObjectData(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_UPDATING_MESSAGE, branchToUpdate);
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_UPDATING_MESSAGE);
		}
		return response;
	}

	@Override
	public ResponseObject deleteBranch(long branchId) {
		ResponseObject response = null;
		Branch branch = branchDao.findByBranchId(branchId);
		if(branch != null && branch.isStatus()){
			branch.setStatus(false);
			branch.setDeletedAt(new Date());
			branchDao.save(branch);
			response = new ResponseObjectCrud(ResponseStatus.SUCCESS_RESPONSE_STATUS, ResponseCode.SUCCESS_RESPONSE_CODE, ResponseMessage.SUCCESS_DELETTING_MESSAGE, branchId);
		} else {
			response = new ResponseObject(ResponseStatus.FAILED_RESPONSE_STATUS, ResponseCode.FAILED_RESPONSE_CODE, ResponseMessage.FAILED_DELETTING_MESSAGE);
		}
		return response;
	}

}
