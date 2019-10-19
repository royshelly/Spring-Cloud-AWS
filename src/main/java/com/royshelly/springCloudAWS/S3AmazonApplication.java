package com.royshelly.springCloudAWS;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.amazonaws.services.s3.model.Bucket;
import com.royshelly.springCloudAWS.service.S3Service;

@SpringBootApplication
public class S3AmazonApplication implements CommandLineRunner {

	@Autowired
	S3Service s3Services;

	@Value("${rs.s3.uploadfile}")
	private String uploadFilePath;

	@Value("${rs.s3.key}")
	private String downloadKey;

	public static void main(String[] args) {
		SpringApplication.run(S3AmazonApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {

		System.out.println("-----CREATE A S3 BUCKET----");
		Bucket b = s3Services.createBucket("test-s3-bucket");
		if (b == null) {
			System.out.println("Error creating bucket!\n");
		} else {
			System.out.println("Bucket Successfully Created");
		}

		System.out.println("------LIST S3 BUCKETS --------");
		s3Services.listBuckets();

		System.out.println("---------------- START UPLOAD FILE ----------------");
		s3Services.uploadFile("rs-s3-upload-file.txt", uploadFilePath);
		System.out.println("---------------- START DOWNLOAD FILE ----------------");
		s3Services.downloadFile(downloadKey);

	}

}
