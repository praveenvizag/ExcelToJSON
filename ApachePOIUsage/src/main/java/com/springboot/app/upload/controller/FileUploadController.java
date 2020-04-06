package com.springboot.app.upload.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.app.upload.service.UploadService;
@RestController
public class FileUploadController {
	
	private UploadService uploadService;

	public FileUploadController(UploadService uploadService) {
		this.uploadService = uploadService;
	}

	@PostMapping(value = "/upload")
	public List<Map<String, String>> upload(@RequestParam("file") MultipartFile file) throws Exception {
		return uploadService.upload(file);

	}

}
