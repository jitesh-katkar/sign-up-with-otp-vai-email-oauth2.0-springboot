package com.sign_up.config;

import static com.google.api.services.gmail.GmailScopes.GMAIL_SEND;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.sign_up.util.GMailerService;

@Configuration
public class MailConfig {
	
	@Value("${server.port}")
	public static int SERVER_PORT;
	
	@Bean
	 public Gmail getGmailservice() throws GeneralSecurityException, IOException {
		  NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
	        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
	        return new Gmail.Builder(httpTransport, jsonFactory, getCredentials(httpTransport, jsonFactory))
	                .setApplicationName("Test Mailer")
	                .build();
	 }
	
	  private static Credential getCredentials(final NetHttpTransport httpTransport, GsonFactory jsonFactory)
	            throws IOException {
	        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(GMailerService.class.getResourceAsStream("/credentials.json")));

	        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
	                httpTransport, jsonFactory, clientSecrets, Set.of(GMAIL_SEND))
	                .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile()))
	                .setAccessType("offline")
	                .build();

	        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(SERVER_PORT).build();
	        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	    }
	
}
