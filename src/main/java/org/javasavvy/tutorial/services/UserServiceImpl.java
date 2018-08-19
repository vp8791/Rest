package org.javasavvy.tutorial.services;


import java.util.Date;

import org.javasavvy.tutorial.dao.UserDAO;
import org.javasavvy.tutorial.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
@Transactional(propagation=Propagation.REQUIRED)
public class UserServiceImpl implements UserService {

	
	@Autowired(required=true)
	private UserDAO userDAO;
	
	
	public User getUser(long id) {
		return userDAO.getUser(id);
	}

	public User getUser(String email) {
		return userDAO.getUser(email);
	}

	public User addUser(String firstName,String lastName,String email,String sex,String password) {		
		User user = new User();
		user.setCreateDate(new Date());
		user.setUpdatedDate(new Date());
		user.setEmail(email);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setSex(sex);
		user.setPassword(password);
		
		return userDAO.addUser(user);
	}

	@Override
	public User updateUser(String firstName, String lastName, String email, String sex, String password,
			long id) {
		User user = new User();
		user.setEmail(email);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setSex(sex);
		user.setPassword(password);
		user.setId(id);
		return userDAO.updateUser(user);
	}

	@Override
	public User deleteUser(long id) {
		return userDAO.deleteUser(id);
	}

	@Override
	public boolean isUserPresent(long id) {
		return userDAO.isUserPresent(id);
	}


}
