package com.jordanproject.controller;

import java.awt.PageAttributes.MediaType;
import java.util.concurrent.CompletableFuture;

import javax.print.attribute.standard.Media;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jordanproject.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {
	
	@Autowired
	private UserService us;
	org.slf4j.Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@PostMapping(value = "/users", consumes = {org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
	public ResponseEntity saveUsers(@RequestParam(value = "file") MultipartFile[] files) throws Exception {
		for(MultipartFile file: files) {
			logger.info("File being sent through for loop");
			us.saveUser(file);
		}
		return ResponseEntity.status(HttpStatus.CREATED).build();
		
	}
	
	@GetMapping(value = "/users", produces = "application/json")
	public CompletableFuture<ResponseEntity> findAllUsers() {
		return us.findAllUsers().thenApply(ResponseEntity::ok);
	}

}
