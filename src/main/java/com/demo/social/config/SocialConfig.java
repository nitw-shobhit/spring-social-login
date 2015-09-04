package com.demo.social.config;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.impl.GoogleTemplate;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.impl.LinkedInTemplate;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

@Configuration
public class SocialConfig implements InitializingBean {

	private static final String FB_KEY = "PUT YOUR KEY HERE";
	private static final String FB_SECRET = "INSERT SECRET VALUE";
	
	private static final String TW_KEY = "PUT YOUR KEY HERE";
	private static final String TW_SECRET = "INSERT SECRET VALUE";
	
	private static final String GG_KEY = "PUT YOUR KEY HERE";
	private static final String GG_SECRET="INSERT SECRET VALUE";
	
	private static final String LK_KEY = "PUT YOUR KEY HERE";
	private static final String LK_SECRET="INSERT SECRET VALUE";

	private SocialContext socialContext;

	private UsersConnectionRepository usersConnectionRepositiory;

	@Inject
	private DataSource dataSource;

	@Bean
	public SocialContext socialContext() {

		return socialContext;
	}

	@Bean
	public ConnectionFactoryLocator connectionFactoryLocator() {

		ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
		registry.addConnectionFactory(new FacebookConnectionFactory(FB_KEY, FB_SECRET));
		registry.addConnectionFactory(new TwitterConnectionFactory(TW_KEY, TW_SECRET));
		registry.addConnectionFactory(new GoogleConnectionFactory(GG_KEY, GG_SECRET));
		registry.addConnectionFactory(new LinkedInConnectionFactory(LK_KEY, LK_SECRET));
		return registry;
	}

	@Bean
	public UsersConnectionRepository usersConnectionRepository() {
		return usersConnectionRepositiory;
	}

	@Bean
	@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
	public ConnectionRepository connectionRepository() {
		String userId = socialContext.getUserId();
		return usersConnectionRepository().createConnectionRepository(userId);
	}

	@Bean
	@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
	public Facebook facebook() {
		String accessToken = connectionRepository().getPrimaryConnection(Facebook.class).createData().getAccessToken();
		return new FacebookTemplate(accessToken);
	}
	
	@Bean
	@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
	public Twitter twitter() {
		String accessToken = connectionRepository().getPrimaryConnection(Twitter.class).createData().getAccessToken();
		return new TwitterTemplate(accessToken);
	}
	
	@Bean
	@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
	public Google google() {
		String accessToken = connectionRepository().getPrimaryConnection(Google.class).createData().getAccessToken();
		return new GoogleTemplate(accessToken);
	}
	
	@Bean
	@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
	public LinkedIn linkedin() {
		String accessToken = connectionRepository().getPrimaryConnection(LinkedIn.class).createData().getAccessToken();
		return new LinkedInTemplate(accessToken);
	}

	@Bean
	public ProviderSignInController providerSignInController() {
		ProviderSignInController providerSigninController = new ProviderSignInController(connectionFactoryLocator(),
				usersConnectionRepository(), socialContext);
		providerSigninController.setPostSignInUrl("/access");
		return providerSigninController;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		JdbcUsersConnectionRepository usersConnectionRepositiory = new JdbcUsersConnectionRepository(dataSource,
				connectionFactoryLocator(), Encryptors.noOpText());
		socialContext = new SocialContext(usersConnectionRepositiory, new UserCookieGenerator(), facebook(), twitter(), google(), linkedin());
		usersConnectionRepositiory.setConnectionSignUp(socialContext);
		this.usersConnectionRepositiory = usersConnectionRepositiory;
	}
}
