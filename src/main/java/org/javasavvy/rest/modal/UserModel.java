package org.javasavvy.rest.modal;

public class UserModel {
	
	private String firstName;
	private String lastName;
	private String email;
	private String sex;
	private String password;
	private long id;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "UserModel [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", sex=" + sex
				+ ", password=" + password + ", id=" + id + "]";
	}
	
	
	
	

}
