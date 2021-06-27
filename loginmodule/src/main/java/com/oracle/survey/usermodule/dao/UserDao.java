package com.oracle.survey.usermodule.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.survey.usermodule.dto.UserDTO;
import com.oracle.survey.usermodule.entity.User;
import com.oracle.survey.usermodule.repository.UserRepository;
/**
 * Dao class for login flow
 *
 * @author Georgy Rajan
 * @version 1.0
 * @since 2021-06-22
 */
@Service
public class UserDao {

	ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	@Autowired
	UserRepository userRepository;

	/**
	 * @return
	 */
	public List<UserDTO> getUsersList() {
		List<User> usersList = userRepository.findAll();

		List<UserDTO> response = new ArrayList<>();
		usersList.forEach(entity -> {
			UserDTO userResponse = mapper.convertValue(entity, UserDTO.class);
			response.add(userResponse);
		});
		return response;
	}

	/**
	 * @param id
	 * @return
	 */
	public User getUser(String id) {
		return userRepository.findByUserid(id);
	}

	/**
	 * @param user
	 * @return
	 */
	public UserDTO convertToUserDTO(User user) {
		return mapper.convertValue(user, UserDTO.class);
	}

	/**
	 * @param user
	 * @return
	 */
	public User convertToUserEntity(UserDTO user) {
		User userResponse = mapper.convertValue(user, User.class);
		if (ObjectUtils.isEmpty(userResponse.getUserid())) {
			userResponse.setUserid(UUID.randomUUID().toString());
		}
		if (ObjectUtils.isEmpty(userResponse.getRoles().get(0).getId())) {
			userResponse.getRoles().get(0).setId(UUID.randomUUID().toString());
		}

		userResponse.getRoles().get(0).setUser(userResponse);

		return userResponse;
	}

	/**
	 * @param user
	 * @return
	 */
	public UserDTO createUser(UserDTO user) {
		User userCreated = userRepository.save(convertToUserEntity(user));
		return convertToUserDTO(userCreated);
	}

	/**
	 * @param user
	 * @return
	 */
	public UserDTO updateUser(User user) {
		User userUpdated = userRepository.save(user);
		return convertToUserDTO(userUpdated);
	}

	/**
	 * @param userid
	 * @param password
	 * @return
	 */
	public User findByUseridAndPassword(String userid, String password) {
		return userRepository.findByUseridAndPassword(userid, password);
	}

	/**
	 * @param userid
	 * @param token
	 * @return
	 */
	public UserDTO findByUseridAndToken(String userid, String token) {
		User userUpdated = userRepository.findByUseridAndToken(userid, token);
		return convertToUserDTO(userUpdated);
	}

	/**
	 * @param user
	 * @param userFound
	 */
	public void copyUserDetails(UserDTO user, User userFound) {
		userFound.setEmail(user.getEmail());
		userFound.setFirstname(user.getFirstname());
		userFound.setLastname(user.getLastname());
		userFound.setMobile(user.getMobile());
	}

}
