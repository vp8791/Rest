package org.javasavvy.rest.modal;

public interface ResourceStatuses {
	
	public static int RESOURCE_FOUND = 200;
	public static int RESOURCE_NOT_FOUND = 201;
	
	public static int RESOURCE_ADD_SUCCESS = 202;
	public static int RESOURCE_ADD_FAILED = 203;
	
	public static int RESOURCE_UPDATE_SUCCESS = 204;
	public static int RESOURCE_UPDATE_FAILED = 205;
	
	public static int RESOURCE_DELETE_SUCCESS = 206;
	public static int RESOURCE_DELETE_FAILED = 207;
	
	public static int RESOURCE_DB_FAILED= 208;

}
