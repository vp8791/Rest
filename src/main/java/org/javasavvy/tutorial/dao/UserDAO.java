package org.javasavvy.tutorial.dao;

import org.javasavvy.tutorial.entity.User;
import org.springframework.stereotype.Repository;

@Repository("userDao")
public interface UserDAO {

	
	public User addUser(User user);
	public User getUser(long id);
	public boolean isUserPresent(long id);
	public User getUser(String email);
	public User updateUser(User user);
	public User deleteUser(long id);
}
