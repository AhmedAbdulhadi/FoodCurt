package com.novent.foodordering.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.novent.foodordering.entity.ModelEntity;
import com.novent.foodordering.service.ModelEntityServices;
import com.novent.foodordering.util.ResponseObject;


@RestController
@RequestMapping("api/v1/modelEntity")
@CrossOrigin(origins = "*")
public class ModelEntityController {
	@Autowired
	private ModelEntityServices modelEntityService;
	
	
	@RequestMapping( method = RequestMethod.GET )
	public ResponseObject getAllModelEntity() {
		return  modelEntityService.getAllModelEntity();
	}
	
	@RequestMapping( method = RequestMethod.GET , value = "/{id}")
	public ResponseObject getModelEntityId(@PathVariable long id) {
		return  modelEntityService.getModelEntityById(id);
	}
	
	@RequestMapping( method = RequestMethod.POST )
	public ResponseObject createModelEntity (@RequestBody ModelEntity modelEntity){
		return modelEntityService.createModelEntity(modelEntity);		
	}
	 
	@RequestMapping( method = RequestMethod.PUT , value = "/{id}")
	public ResponseObject updateModelEntity (@PathVariable long id, @RequestBody ModelEntity modelEntity) {	
		return modelEntityService.updateModelEntity( id , modelEntity );			
	}
	
	@RequestMapping( method = RequestMethod.DELETE , value = "/{id}")
	public ResponseObject deleteModelEntity(@PathVariable long id) {
		return modelEntityService.deleteModelEntity(id);			
	}
	
    @RequestMapping( method = RequestMethod.POST , value = "/login")
	public ResponseObject loginModelEntity(@RequestBody ModelEntity modelEntity){
			return modelEntityService.loginModelEntity(modelEntity.getUserName(),modelEntity.getPassword());
	}
    
    @RequestMapping( method = RequestMethod.POST , value = "/logout/{id}")
	public ResponseObject logOutModelEntity(@PathVariable long id ){
			return modelEntityService.logOutModelEntity(id);
	}
    
}


