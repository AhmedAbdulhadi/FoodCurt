package com.novent.foodordering.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.novent.foodordering.entity.Users;


@Repository
//public interface UserDao extends CrudRepository<Users, Long> {
	public interface UserDao extends PagingAndSortingRepository<Users, Long> {
	
	public List<Users> findAllByOrderByIdAsc();
	
	public List<Users> findAllByOrderByIdDesc();
		
	public Page<Users> findByStatusOrderByIdAsc(boolean status, Pageable pageable);

	public Page<Users> findByStatusOrderByIdDesc(boolean status, Pageable pageable);
	
	public Users findById(long id);
	
	public Users findByUserName(String userName);
	
	public Users findByPhoneNumber(String phoneNumber);
	
	public Users findByEmail(String email);
}
