package com.sign_up.util;

import static com.google.api.services.gmail.GmailScopes.GMAIL_SEND;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;

@Service
public class GMailerService {

	private static final String TEST_EMAIL = "JAK.Unicorn25@gmail.com";
	private final Gmail service;

	public GMailerService() throws Exception {
		NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
		GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
		service = new Gmail.Builder(httpTransport, jsonFactory, getCredentials(httpTransport, jsonFactory))
				.setApplicationName("Test Mailer").build();
	}

	private static Credential getCredentials(final NetHttpTransport httpTransport, GsonFactory jsonFactory)
			throws IOException {
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory,
				new InputStreamReader(GMailerService.class.getResourceAsStream("/credentials.json")));

		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory,
				clientSecrets, Set.of(GMAIL_SEND))
				.setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile())).setAccessType("offline")
				.build();

		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8889).build();
		return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	}

	public void sendMail(String toEmailId) throws Exception {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		MimeMessage email = new MimeMessage(session);
		email.setFrom(new InternetAddress(TEST_EMAIL));
		jakarta.mail.Message.RecipientType to = RecipientType.TO;
		email.addRecipient(to, new InternetAddress(toEmailId));
		//email.setRecipient(to, new InternetAddress(toEmailId));

		
		email.setSubject("One time Password to login Unicorn app...!!!");
		email.setText(" The secret one-time password to  login is : " + OtpUtil.generateOtp()
				+ "\nDo not share it with anyone.");
		System.out.println("OTP : " + OtpUtil.generateOtp());

		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		email.writeTo(buffer);
		byte[] rawMessageBytes = buffer.toByteArray();
		String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
		Message msg = new Message();
		msg.setRaw(encodedEmail);

		try {
			msg = service.users().messages().send("me", msg).execute();
			System.out.println("Message id: " + msg.getId());
			System.out.println(msg.toPrettyString());
		} catch (GoogleJsonResponseException e) {
			GoogleJsonError error = e.getDetails();
			if (error.getCode() == 403) {
				System.err.println("Unable to send message: " + e.getDetails());
			} else {
				throw e;
			}
		}

	}

	/*
	 * public static void main(String[] args) throws Exception { new
	 * GMailerService().sendMail(""); }
	 */
}
