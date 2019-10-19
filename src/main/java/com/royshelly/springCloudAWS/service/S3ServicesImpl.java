package com.royshelly.springCloudAWS.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import com.royshelly.springCloudAWS.util.S3ServiceUtil;

@Service
public class S3ServicesImpl implements S3Service {

	private Logger logger = LoggerFactory.getLogger(S3ServicesImpl.class);

	@Autowired
	private AmazonS3 s3client;

	@Value("${rs.s3.bucket}")
	private String bucketName;

	@Value("${rs.s3.region}")
	private String region;

	@Override
	public void downloadFile(String keyName) {
		try {

			System.out.println("Downloading an object");
			S3Object s3object = s3client.getObject(new GetObjectRequest(bucketName, keyName));
			System.out.println("Content-Type: " + s3object.getObjectMetadata().getContentType());
			S3ServiceUtil.displayText(s3object.getObjectContent());
			logger.info("=== Import File - Done! ====");

		} catch (AmazonServiceException ase) {
			logger.error(ase.getMessage());
		} catch (AmazonClientException ace) {
			logger.error("Error Message: " + ace.getMessage());
		} catch (IOException ioe) {
			logger.error("IOE Error Message: " + ioe.getMessage());
		}
	}

	@Override
	public void uploadFile(String keyName, String uploadFilePath) {
		try {

			File file = new File(uploadFilePath);
			s3client.putObject(new PutObjectRequest(bucketName, keyName, file));
			logger.info("=== Upload File - Done! ====");

		} catch (AmazonServiceException ase) {
			logger.error(ase.getMessage());
		} catch (AmazonClientException ace) {
			logger.error(ace.getMessage());
		}

	}

	
	@Override
	public Bucket createBucket(String bucketName) {
		
		Bucket b = null;

		if (s3client.doesBucketExist(bucketName)) {
			logger.info("==Bucket already exists==");
			b = getBucket(bucketName);
		} else {
			try {
				b = s3client.createBucket(bucketName);
				logger.info("====Bucket Created Successfully===");
			} catch (AmazonS3Exception e) {
				logger.error(e.getMessage());
			}
		}
		return b;
	}

	public Bucket getBucket(String bucketName) {
		
		Bucket named_bucket = null;
		List<Bucket> buckets = s3client.listBuckets();
		logger.info("===listing buckets===");
		for (Bucket b : buckets) {
			if (b.getName().equals(bucketName)) {
				named_bucket = b;
			}
		}
		return named_bucket;
	}

	@Override
	public void listBuckets() {
		List<Bucket> buckets = s3client.listBuckets();
        for (Bucket b : buckets) {
            logger.info(b.getName());
        }
	}

}
