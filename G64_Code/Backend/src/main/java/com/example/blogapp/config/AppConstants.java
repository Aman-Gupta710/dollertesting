package com.example.blogapp.config;

public class AppConstants {

	public static final String PAGE_NUMBER = "0";
	public static final String PAGE_SIZE = "10";
	public static final String SORT_BY = "postId";
	public static final String SORT_DIR = "asc";
	public static final Integer ROLE_ADMIN=501;
	public static final Integer ROLE_USER = 502;

	// token expiration time
	public static final int EXPIRATION_TIME=15;

	// Post download link
	public static final String imageDownloadLink ="http://localhost:8080/api/v1/post/image/";


	public static final String frontendUrl = "http://localhost:3000";
}
