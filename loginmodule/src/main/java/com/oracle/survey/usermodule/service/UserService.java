package com.oracle.survey.usermodule.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.oracle.survey.usermodule.config.Loggable;
import com.oracle.survey.usermodule.dao.UserDao;
import com.oracle.survey.usermodule.dto.UserDTO;
import com.oracle.survey.usermodule.entity.User;
import com.oracle.survey.usermodule.exception.UserException;

/**
 * Service class for user flow
 *
 * @author Georgy Rajan
 * @version 1.0
 * @since 2021-06-22
 */
@Service
public class UserService {

	private static final Logger LOGGER = LogManager.getLogger(UserService.class);

	@Autowired
	UserDao userDao;

	/**
	 * @return
	 */
	@Loggable
	public List<UserDTO> getUsersList() {
		LOGGER.debug("get all user list call");
		return userDao.getUsersList();
	}

	/**
	 * @param id
	 * @return
	 */
	@Loggable
	public User getUserWithId(String id) {
		LOGGER.debug("get all user call with id %s ", id);
		return userDao.getUser(id);
	}

	/**
	 * @param user
	 * @return
	 */
	@Loggable
	public UserDTO createUser(UserDTO user) {
		User userFound = userDao.getUser(user.getUserid());
		if (!ObjectUtils.isEmpty(userFound)) {
			LOGGER.debug("user with id found %s", user.getUserid());
			throw new UserException("Username already exist");
		}
		return userDao.createUser(user);
	}

	/**
	 * @param user
	 * @return
	 */
	@Loggable
	public UserDTO updateUser(UserDTO user) {
		User userFound = userDao.getUser(user.getUserid());
		if (!ObjectUtils.isEmpty(userFound)) {
			LOGGER.debug("user with id not found %s", user.getUserid());
			throw new UserException("Username not found");
		}
		userDao.copyUserDetails(user, userFound);
		return userDao.updateUser(userFound);
	}

	/**
	 * @param user
	 * @return
	 */
	@Loggable
	public UserDTO updateUser(User user) {
		return userDao.updateUser(user);
	}

	/**
	 * @param userName
	 * @param password
	 * @return
	 */
	@Loggable
	public User findByUseridAndPassword(String userName, String password) {
		return userDao.findByUseridAndPassword(userName, password);
	}

	/**
	 * @param userId
	 * @param token
	 * @return
	 */
	@Loggable
	public UserDTO getUserWithIdAndToken(String userId, String token) {
		return userDao.findByUseridAndToken(userId, token);
	}

}
