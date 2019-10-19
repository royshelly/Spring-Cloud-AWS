package com.royshelly.springCloudAWS.service;

import java.util.List;
import com.amazonaws.services.s3.model.Bucket;

public interface S3Service {
	public void downloadFile(String keyName);

	public void uploadFile(String keyName, String uploadFilePath);

	public Bucket createBucket(String bucketName);

	public Bucket getBucket(String bucketName);
	
	public void listBuckets() ;

}
