package com.sign_up.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class CustomerLoginRequestBody {

	@NotBlank(message = "Username or email is required")
	@Email(message = "Invalid email format")
	private String usernameOrEmail;

	@NotBlank(message = "Password is required")
	private String password;

	// Getters and Setters
	public String getUsernameOrEmail() {
		return usernameOrEmail;
	}

	public void setUsernameOrEmail(String usernameOrEmail) {
		this.usernameOrEmail = usernameOrEmail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
