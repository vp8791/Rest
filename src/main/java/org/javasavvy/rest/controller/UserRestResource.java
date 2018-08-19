package org.javasavvy.rest.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import org.javasavvy.rest.modal.ResourceStatuses;
import org.javasavvy.rest.modal.UserModel;
import org.javasavvy.rest.modal.UserStatusModel;
import org.javasavvy.tutorial.entity.User;
import org.javasavvy.tutorial.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;

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
	public UserModel getUser(@Context HttpServletRequest requestContext, @Context HttpHeaders headers) {
		boolean isHeadersPresent = isHeadersPresent(headers);
		if (!isHeadersPresent) {
			UserModel noheadersmodel = new UserModel();
			noheadersmodel.setEmail("NO HEADERS");
			noheadersmodel.setFirstName("NO HEADERS");
			noheadersmodel.setLastName("NO HEADERS");
			noheadersmodel.setPassword("NO HEADERS");
			noheadersmodel.setSex("NO HEADERS");
			noheadersmodel.setId(101);
			return noheadersmodel;
		}

		boolean isUserAuthorized = isUserAuthorized(headers);
		if (!isUserAuthorized) {
			UserModel userModal = new UserModel();
			userModal.setEmail("UNAUTHORIZED");
			userModal.setFirstName("UNAUTHORIZED");
			userModal.setLastName("UNAUTHORIZED");
			userModal.setPassword("UNAUTHORIZED");
			userModal.setSex("UNAUTHORIZED");
			userModal.setId(101);
			return userModal;
		}

		UserModel userModal = new UserModel();
		userModal.setEmail("jajfaddf");
		userModal.setFirstName("jayaram");
		userModal.setLastName("poks");
		userModal.setPassword("passwd");
		userModal.setSex("male");
		userModal.setId(101);
		return userModal;
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/user-info/{id}")
	public UserStatusModel getUser(@Context HttpServletRequest requestContext, @Context HttpHeaders headers,
			@PathParam("id") long id) {
		System.out.println("user-id" + id);
		boolean isHeadersPresent = isHeadersPresent(headers);
		if (!isHeadersPresent) {
			UserStatusModel noHeadersModel = new UserStatusModel();
			noHeadersModel.setStatus(ResourceStatuses.RESOURCE_NO_HEADERS);
			noHeadersModel.setMessage("Please add valid headers and submit");
			return noHeadersModel;
		}

		boolean isUserAuthorized = isUserAuthorized(headers);
		if (!isUserAuthorized) {
			UserStatusModel unauthorizedModel = new UserStatusModel();
			unauthorizedModel.setStatus(ResourceStatuses.RESOURCE_UNAUTHORIZED_USER);
			unauthorizedModel.setMessage("User is unauthorized to access resource");
			return unauthorizedModel;
		}
		return getUserDetails(id);
	}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/delete/{id}")
	public UserStatusModel deleteUser(@Context HttpServletRequest requestContext, @Context HttpHeaders headers,
			@PathParam("id") long id) {
		boolean isHeadersPresent = isHeadersPresent(headers);
		if (!isHeadersPresent) {
			UserStatusModel noHeadersModel = new UserStatusModel();
			noHeadersModel.setStatus(ResourceStatuses.RESOURCE_NO_HEADERS);
			noHeadersModel.setMessage("Please add valid headers and submit");
			return noHeadersModel;
		}

		boolean isUserAuthorized = isUserAuthorized(headers);
		if (!isUserAuthorized) {
			UserStatusModel unauthorizedModel = new UserStatusModel();
			unauthorizedModel.setStatus(ResourceStatuses.RESOURCE_UNAUTHORIZED_USER);
			unauthorizedModel.setMessage("User is unauthorized to access resource");
			return unauthorizedModel;
		}

		UserStatusModel status = new UserStatusModel();
		try {
			User deleteUser = userService.deleteUser(id);
			UserModel userModal = new UserModel();
			userModal.setEmail(deleteUser.getEmail());
			userModal.setFirstName(deleteUser.getFirstName());
			userModal.setLastName(deleteUser.getLastName());
			userModal.setPassword(deleteUser.getPassword());
			userModal.setSex(deleteUser.getSex());
			userModal.setId(id);
			status.setUser(userModal);
			status.setStatus(ResourceStatuses.RESOURCE_DELETE_SUCCESS);
			status.setMessage("User Deleted successfully(" + id + ")");
			return status;
		} catch (Exception e) {
			e.printStackTrace();
			status.setStatus(ResourceStatuses.RESOURCE_DELETE_FAILED);
			status.setMessage("Unable to delete user with Id(" + id + "):" + e.getMessage());
		}
		return status;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/create-user")
	public UserStatusModel createUser(@Context HttpServletRequest requestContext, @Context HttpHeaders headers,
			UserModel userModel) {
		boolean isHeadersPresent = isHeadersPresent(headers);
		if (!isHeadersPresent) {
			UserStatusModel noHeadersModel = new UserStatusModel();
			noHeadersModel.setStatus(ResourceStatuses.RESOURCE_NO_HEADERS);
			noHeadersModel.setMessage("Please add valid headers and submit");
			return noHeadersModel;
		}

		boolean isUserAuthorized = isUserAuthorized(headers);
		if (!isUserAuthorized) {
			UserStatusModel unauthorizedModel = new UserStatusModel();
			unauthorizedModel.setStatus(ResourceStatuses.RESOURCE_UNAUTHORIZED_USER);
			unauthorizedModel.setMessage("User is unauthorized to access resource");
			return unauthorizedModel;
		}

		return insertUser(userModel);
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/update-user")
	public UserStatusModel createOrUpdate(@Context HttpServletRequest requestContext, @Context HttpHeaders headers,
			UserModel userModel) {
		boolean isHeadersPresent = isHeadersPresent(headers);
		if (!isHeadersPresent) {
			UserStatusModel noHeadersModel = new UserStatusModel();
			noHeadersModel.setStatus(ResourceStatuses.RESOURCE_NO_HEADERS);
			noHeadersModel.setMessage("Please add valid headers and submit");
			return noHeadersModel;
		}

		boolean isUserAuthorized = isUserAuthorized(headers);
		if (!isUserAuthorized) {
			UserStatusModel unauthorizedModel = new UserStatusModel();
			unauthorizedModel.setStatus(ResourceStatuses.RESOURCE_UNAUTHORIZED_USER);
			unauthorizedModel.setMessage("User is unauthorized to access resource");
			return unauthorizedModel;
		}

		UserStatusModel status = new UserStatusModel();

		try {
			boolean isUserExists = isUserPresent(userModel.getId());
			if (!isUserExists) {
				UserStatusModel insertUser = insertUser(userModel);
				return insertUser;
			} else if (isUserExists) {
				status = getUserDetails(userModel.getId());
				UserStatusModel updateUser = updateUser(userModel);
				return updateUser;
			} else {
				UserStatusModel updateUser = new UserStatusModel();
				status.setStatus(ResourceStatuses.RESOURCE_UNKNOWN_ERROR);
				status.setMessage("Unknow Error while Put operation of (" + userModel + ")");
			}
		} catch (Exception e) {
			e.printStackTrace();
			status.setStatus(ResourceStatuses.RESOURCE_DB_FAILED);
			status.setMessage("Put Operation failed(" + e.getMessage() + ")");
		}

		return status;
	}

	private UserStatusModel insertUser(UserModel userModel) {
		UserStatusModel status = new UserStatusModel();
		User user = null;
		try {
			user = userService.addUser(userModel.getFirstName(), userModel.getLastName(), userModel.getEmail(),
					userModel.getSex(), userModel.getPassword());
			userModel.setEmail(user.getEmail());
			userModel.setFirstName(user.getFirstName());
			userModel.setLastName(user.getLastName());
			userModel.setSex(user.getSex());
			userModel.setId(user.getId());
			status.setUser(userModel);
			status.setStatus(ResourceStatuses.RESOURCE_ADD_SUCCESS);
			status.setMessage("User Created Successfully:(" + user.getId() + ")");

		} catch (Exception e) {
			e.printStackTrace();
			status.setStatus(ResourceStatuses.RESOURCE_ADD_FAILED);
			status.setMessage("Error in Creating user:(" + userModel + ")" + e.getMessage());
		}
		return status;
	}

	private UserStatusModel updateUser(UserModel userModel) {

		UserStatusModel status = new UserStatusModel();
		User user = null;
		try {
			user = userService.updateUser(userModel.getFirstName(), userModel.getLastName(), userModel.getEmail(),
					userModel.getSex(), userModel.getPassword(), userModel.getId());
			userModel.setEmail(user.getEmail());
			userModel.setFirstName(user.getFirstName());
			userModel.setLastName(user.getLastName());
			userModel.setSex(user.getSex());
			userModel.setId(user.getId());
			status.setUser(userModel);
			status.setStatus(ResourceStatuses.RESOURCE_UPDATE_SUCCESS);
			status.setMessage("User Updated Successfully:" + userModel);
			return status;
		} catch (Exception e) {
			e.printStackTrace();
			status.setStatus(ResourceStatuses.RESOURCE_UPDATE_FAILED);
			status.setMessage("Error in Updating user(" + userModel.getId() + ")" + e.getMessage());
		}
		return status;
	}

	private boolean isUserPresent(long id) {
		return userService.isUserPresent(id);
	}

	private UserStatusModel getUserDetails(long id) {
		System.out.println("user-info:id" + id);
		UserStatusModel userStatus = new UserStatusModel();
		try {
			User user = userService.getUser(id);
			UserModel userModal = new UserModel();
			if (user != null) {
				userStatus.setStatus(200);
				userStatus.setMessage("User info");
				userModal.setEmail(user.getEmail());
				userModal.setFirstName(user.getFirstName());
				userModal.setLastName(user.getLastName());
				userModal.setSex(user.getSex());
				userModal.setId(id);
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

	private List<String> printHeaders(HttpHeaders headers) {
		List<String> headersList = new ArrayList<String>();
		headersList.add("===============Printing headers for your Understanding::==============");
		for (String header : headers.getRequestHeaders().keySet()) {
			headersList.add(header + "=" + headers.getRequestHeader(header));
			System.out.println(header + "=" + headers.getRequestHeader(header));
		}
		return headersList;
	}

	private boolean isHeadersPresent(HttpHeaders headers) {
		List<String> requestHeaderUser = headers.getRequestHeader(Constants.HEADER_USER);
		List<String> requestHeaderPassword = headers.getRequestHeader(Constants.HEADER_PASSWORD);

		if(requestHeaderUser== null  || requestHeaderPassword == null) {
			return false;
		}
		
		if(!requestHeaderUser.isEmpty()) {
			if(requestHeaderUser.size() != 1) {
				return false;
			}
		} else {
			return false;
		}
		
		if(!requestHeaderPassword.isEmpty()) {
			if( requestHeaderPassword.size() != 1) {
				return false;
			} 
		} else {
			return false;
		}
		

		String user = headers.getRequestHeader(Constants.HEADER_USER).get(0);
		String password = headers.getRequestHeader(Constants.HEADER_PASSWORD).get(0);
		if (user == null || password == null) {
			return false;
		}

		return true;

	}

	private boolean isUserAuthorized(HttpHeaders headers) {
		List<String> headersList = printHeaders(headers);
		String user = headers.getRequestHeader(Constants.HEADER_USER).get(0);
		String password = headers.getRequestHeader(Constants.HEADER_PASSWORD).get(0);
		boolean isAuthorized = userService.isUserAuthorized(user, password);
		return isAuthorized;

	}

}
