package com.sign_up.util;

import java.io.FileInputStream;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.UserCredentials;

public class OtpUtil {
	
	public static String generateOtp() {
		return String.valueOf((int) (Math.random() * 900000) + 100000);
	}
	
	public static UserCredentials getUserCredentials() throws IOException {
        // Path to your downloaded credentials JSON file
        FileInputStream credentialsStream = new FileInputStream("src/main/resources/credentials.json");

        // Load user credentials
        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream)
                .createScoped("https://mail.google.com/");
        credentials.refreshIfExpired();

        return (UserCredentials) credentials;
    }
}
