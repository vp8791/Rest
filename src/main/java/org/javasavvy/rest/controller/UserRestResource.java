package org.javasavvy.rest.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.javasavvy.rest.modal.ResourceStatuses;
import org.javasavvy.rest.modal.UserModel;
import org.javasavvy.rest.modal.UserStatusModel;
import org.javasavvy.tutorial.entity.User;
import org.javasavvy.tutorial.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Path("/user")
public class UserRestResource {

	@Autowired(required = true)
	@Qualifier("userService")
	private UserService userService;

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/user-json-meta")
	public UserModel getUser() {
		UserModel userModal = new UserModel();
		userModal.setEmail("jajfaddf");
		userModal.setFirstName("jayaram");
		userModal.setLastName("poks");
		userModal.setPassword("passwd");
		userModal.setSex("male");
		userModal.setUserId(101);
		return userModal;
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/user-info/{userId}")
	public UserStatusModel getUser(@PathParam("userId") long userId) {
		System.out.println("user-info:userId" + userId);
		return getUserDetails(userId);
	}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/delete/{userId}")
	public UserStatusModel deleteUser(@PathParam("userId") long userId) {
		UserStatusModel status = new UserStatusModel();
		try {
			User deleteUser = userService.deleteUser(userId);
			UserModel userModal = new UserModel();
			userModal.setEmail(deleteUser.getEmail());
			userModal.setFirstName(deleteUser.getFirstName());
			userModal.setLastName(deleteUser.getLastName());
			userModal.setPassword(deleteUser.getPassword());
			userModal.setSex(deleteUser.getSex());
			userModal.setUserId(userId);
			status.setUser(userModal);
			status.setStatus(ResourceStatuses.RESOURCE_DELETE_SUCCESS);
			status.setMessage("User Deleted successfully(" + userId +")");
			return status;
		} catch (Exception e) {
			e.printStackTrace();
			status.setStatus(ResourceStatuses.RESOURCE_DELETE_FAILED);
			status.setMessage("Unable to delete user with Id(" + userId + "):" + e.getMessage());
		}
		return status;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/create-user")
	public UserStatusModel createUser(UserModel userModel) {
		return updateOrInsert(true, userModel);
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/update-user")
	public UserStatusModel createOrUpdate(UserModel userModel) {
		UserStatusModel status = new UserStatusModel();

		try {
			status = getUserDetails(userModel.getUserId());
			if (status.getStatus() == ResourceStatuses.RESOURCE_NOT_FOUND) {
				UserStatusModel insertUser = updateOrInsert(true, userModel);
				return insertUser;
			} else if (status.getStatus() == ResourceStatuses.RESOURCE_FOUND) {
				UserStatusModel updateUser = updateOrInsert(false, userModel);
				return updateUser;
			}
		} catch (Exception e) {
			e.printStackTrace();
			status.setStatus(ResourceStatuses.RESOURCE_DB_FAILED);
			status.setMessage("Put Operation failed(" + e.getMessage() + ")");
		}

		return status;
	}

	private UserStatusModel updateOrInsert(boolean isCreate, UserModel userModal) {
		UserStatusModel status = new UserStatusModel();
		User user = null;
		if (isCreate) {
			try {
				user = userService.addUser(userModal.getFirstName(), userModal.getLastName(), userModal.getEmail(),
						userModal.getSex(), userModal.getPassword());
				userModal.setEmail(user.getEmail());
				userModal.setFirstName(user.getFirstName());
				userModal.setLastName(user.getLastName());
				userModal.setSex(user.getSex());
				userModal.setUserId(user.getUserId());
				status.setUser(userModal);
				status.setStatus(ResourceStatuses.RESOURCE_ADD_SUCCESS);
				status.setMessage("User Created Successfully:" + userModal);

			} catch (Exception e) {
				e.printStackTrace();
				status.setStatus(ResourceStatuses.RESOURCE_ADD_FAILED);
				status.setMessage("Error in Creating user:" + e.getMessage());
			}
		} else { // Update Resource
			try {
				user = userService.updateUser(userModal.getFirstName(), userModal.getLastName(), userModal.getEmail(),
						userModal.getSex(), userModal.getPassword(), userModal.getUserId());
				userModal.setEmail(user.getEmail());
				userModal.setFirstName(user.getFirstName());
				userModal.setLastName(user.getLastName());
				userModal.setSex(user.getSex());
				userModal.setUserId(user.getUserId());
				status.setUser(userModal);
				status.setStatus(ResourceStatuses.RESOURCE_UPDATE_SUCCESS);
				status.setMessage("User Updated Successfully:" + userModal);
				return status;
			} catch (Exception e) {
				e.printStackTrace();
				status.setStatus(ResourceStatuses.RESOURCE_UPDATE_FAILED);
				status.setMessage("Error in Updating user " + e.getMessage());
			}
		}
		return status;
	}

	private UserStatusModel getUserDetails(long userId) {
		System.out.println("user-info:userId" + userId);
		UserStatusModel userStatus = new UserStatusModel();
		try {
			User user = userService.getUser(userId);
			UserModel userModal = new UserModel();
			if (user != null) {
				userStatus.setStatus(200);
				userStatus.setMessage("User info");
				userModal.setEmail(user.getEmail());
				userModal.setFirstName(user.getFirstName());
				userModal.setLastName(user.getLastName());
				userModal.setSex(user.getSex());
				userModal.setUserId(userId);
				userStatus.setUser(userModal);
				userStatus.setStatus(ResourceStatuses.RESOURCE_FOUND);
				userStatus.setMessage("User Found");
			} else {
				userStatus.setStatus(ResourceStatuses.RESOURCE_NOT_FOUND);
				userStatus.setMessage("User info");
			}
		} catch (Exception e) {
			e.printStackTrace();
			userStatus.setStatus(ResourceStatuses.RESOURCE_DB_FAILED);
			userStatus.setMessage(e.getMessage());
		}
		return userStatus;
	}

}
