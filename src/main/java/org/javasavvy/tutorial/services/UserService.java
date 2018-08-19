package org.javasavvy.tutorial.services;

import org.javasavvy.tutorial.entity.User;
import org.springframework.stereotype.Service;


public interface UserService {
	
	
	public User getUser(long id);
	public boolean isUserPresent(long id);
	public User getUser(String email);
	public User addUser(String firstName,String lastName,String email,String sex,String password);
	public User updateUser(String firstName,String lastName,String email,String sex,String password, long id);
	public User deleteUser(long id);
	public boolean isUserAuthorized(String user, String password);

}
