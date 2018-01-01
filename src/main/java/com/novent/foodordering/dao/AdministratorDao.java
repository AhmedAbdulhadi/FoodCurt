package com.novent.foodordering.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.novent.foodordering.entity.Administrator;

@Repository
//public interface AdministratorDao extends CrudRepository<Administrator, Long>{
public interface AdministratorDao extends PagingAndSortingRepository<Administrator, Long> {
	
	public List<Administrator> findAllByOrderByIdAsc();
	
	public List<Administrator> findAllByOrderByIdDesc();
 
	public Page<Administrator> findByStatusOrderByIdAsc(boolean status, Pageable pageable);

	public Page<Administrator> findByStatusOrderByIdDesc(boolean status, Pageable pageable);
	
	public List<Administrator> findAll();
	
	public Administrator findById(long id);
	
	public Administrator findByUserName(String userName);
	
	public Administrator findByPhoneNumber(String phoneNumber);
	
	public Administrator findByEmail(String email);
	
}
