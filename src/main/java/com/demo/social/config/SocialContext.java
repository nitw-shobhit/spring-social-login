package com.demo.social.config;

import java.util.Calendar;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.google.api.Google;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.web.context.request.NativeWebRequest;

public class SocialContext implements ConnectionSignUp, SignInAdapter {

	private static Random rand;

	private final UserCookieGenerator userCookieGenerator;

	private static final ThreadLocal<String> currentUser = new ThreadLocal<String>();

	private final UsersConnectionRepository connectionRepository;

	private final Facebook facebook;

	private final Twitter twitter;
	
	private final Google google;
	
	private final LinkedIn linkedIn;
	
	public SocialContext(UsersConnectionRepository connectionRepository, UserCookieGenerator userCookieGenerator,
			Facebook facebook, Twitter twitter, Google google, LinkedIn linkedIn) {
		this.connectionRepository = connectionRepository;
		this.userCookieGenerator = userCookieGenerator;
		this.facebook = facebook;
		this.twitter = twitter;
		this.google = google;
		this.linkedIn = linkedIn;

		rand = new Random(Calendar.getInstance().getTimeInMillis());
	}

	@Override
	public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
		userCookieGenerator.addCookie(userId, request.getNativeResponse(HttpServletResponse.class));
		return null;
	}

	@Override
	public String execute(Connection<?> connection) {
		return Long.toString(rand.nextLong());
	}

	public Type isSignedIn(HttpServletRequest request, HttpServletResponse response) {
		String userId = userCookieGenerator.readCookieValue(request);
		if (isValidId(userId)) {

			if (isConnectedFacebookUser(userId)) {
				return Type.FB;
			} else if (isConnectedGoogleUser(userId)) {
				return Type.GOOGLE;
			} else if (isConnectedLinkedinUser(userId)) {
				return Type.LINKEDIN;
			} else if (isConnectedTwitterUser(userId)) {
				return Type.TWITTER;
			}
		}

		currentUser.set(userId);
		return null;
	}

	private boolean isValidId(String id) {
		return isNotNull(id) && (id.length() > 0);
	}

	private boolean isNotNull(Object obj) {
		return obj != null;
	}

	private boolean isConnectedFacebookUser(String userId) {

		ConnectionRepository connectionRepo = connectionRepository.createConnectionRepository(userId);
		Connection<Facebook> facebookConnection = connectionRepo.findPrimaryConnection(Facebook.class);
		return facebookConnection != null;
	}

	private boolean isConnectedGoogleUser(String userId) {
		ConnectionRepository connectionRepo = connectionRepository.createConnectionRepository(userId);
		Connection<Google> googleConnection = connectionRepo.findPrimaryConnection(Google.class);
		return googleConnection != null;
	}
	
	private boolean isConnectedTwitterUser(String userId) {
		ConnectionRepository connectionRepo = connectionRepository.createConnectionRepository(userId);
		Connection<Twitter> twitterConnection = connectionRepo.findPrimaryConnection(Twitter.class);
		return twitterConnection != null;
	}

	private boolean isConnectedLinkedinUser(String userId) {
		ConnectionRepository connectionRepo = connectionRepository.createConnectionRepository(userId);
		Connection<LinkedIn> linkedInConnection = connectionRepo.findPrimaryConnection(LinkedIn.class);
		return linkedInConnection != null;
	}

	public String getUserId() {

		return currentUser.get();
	}

	public Facebook getFacebook() {
		return facebook;
	}

	public Twitter getTwitter() {
		return twitter;
	}

	public Google getGoogle() {
		return google;
	}

	public LinkedIn getLinkedIn() {
		return linkedIn;
	}

}
