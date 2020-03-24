package com.jordanproject.service;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jordanproject.model.User;
import com.jordanproject.repository.UserRepository;




@Service
public class UserService {
	
	@Autowired
	private UserRepository ur;
	
//	Want to check the statement
	Object target;
	
	org.slf4j.Logger logger = LoggerFactory.getLogger(UserService.class);

	@Async
	public CompletableFuture<List<User>> saveUser(MultipartFile file) throws Exception {
		long startTime = System.currentTimeMillis();
		List<User> files = parseCSVFile(file);
		logger.info("saving list of users of size {}", files.size()," ", Thread.currentThread().getName());
		files = ur.saveAll(files);
		long endTime = System.currentTimeMillis();
		logger.info("total time {}", (endTime - startTime));
		return CompletableFuture.completedFuture(files);
	}
	
	@Async
	public CompletableFuture<List<User>> findAllUsers() {
		logger.info("finding list of users " + Thread.currentThread().getName());
		List<User> users = ur.findAll();
		logger.info("Found all users: # of Users ", users.size());
		return CompletableFuture.completedFuture(users);
	}
	
	private List<User> parseCSVFile(final MultipartFile file) throws Exception {
		  final List<User> users = new ArrayList<>();
	        try {
	            try (final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
	                String line;
	                while ((line = br.readLine()) != null) {
	                    final String[] data = line.split(",");
	                    final User user = new User();
	                    user.setName(data[0]);
	                    user.setEmail(data[1]);
	                    user.setGender(data[2]);
	                    users.add(user);
	                }
	                return users;
	            }
	        } catch (final IOException e) {
	            logger.error("Failed to parse CSV file {}", e);
	            throw new Exception("Failed to parse CSV file {}", e);
	        }
	}
	
	
	
}
