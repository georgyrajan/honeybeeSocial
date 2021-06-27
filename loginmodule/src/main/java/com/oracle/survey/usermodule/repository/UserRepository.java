package com.oracle.survey.usermodule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oracle.survey.usermodule.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	
	public User findByUserid(String id);

	public User findByUseridAndPassword(String userid, String password);

	public User findByUseridAndToken(String userid, String token);

}
