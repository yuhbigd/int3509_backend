package com.project.nhatrotot.service.file_service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.Executors;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.project.nhatrotot.rest.advice.CustomException.FileMaximumExceedException;
import com.project.nhatrotot.rest.advice.CustomException.GeneralException;
import com.project.nhatrotot.rest.advice.CustomException.InvalidFileExtensionException;
import com.project.nhatrotot.util.MultipartFileConverter;
import com.project.nhatrotot.util.S3Listener;
import com.project.nhatrotot.util.constant.UserConstant;

enum FILE_TYPE {
    VIDEO, IMAGE
};

@Service
public class FileUploadService {
    @Autowired
    private TransferManager transferManager;
    @Autowired
    private AmazonS3 s3Client;
    @Value("${app.properties.aws.bucket}")
    private String bucket;
    @Value("${app.properties.aws.s3_url}")
    private String s3Url;
    private long MAXIMUM_VIDEO_SIZE_ALLOWED = 100; // in MB
    private long MAXIMUM_IMAGE_SIZE_ALLOWED = 5; // in MB
    private String imageExtensions = "jpg,jpeg,bmp,gif,png";
    private String videoExtensions = "mp4,mkv,mp3";

    public void deleteFile(final String keyName, String userId, String userRole) {
        if (userRole != UserConstant.ROLE_CLIENT || keyName.substring(0, 36).equals(userId)) {
            final DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, keyName);
            s3Client.deleteObject(deleteObjectRequest);
        } else {
            throw new GeneralException("not permitted", HttpStatus.FORBIDDEN);
        }
    }

    public void deleteFiles(String[] keys, String userId, boolean isSubAdmin) throws Exception {
        boolean isBelongToUser = Arrays.stream(keys).allMatch((key) -> !key.substring(0, 36).equals(userId));
        if (isBelongToUser || isSubAdmin) {
            DeleteObjectsRequest delObjReq = new DeleteObjectsRequest(bucket)
                    .withKeys(keys)
                    .withQuiet(false);
            s3Client.deleteObjects(delObjReq);
        } else {
            throw new GeneralException("not permitted", HttpStatus.FORBIDDEN);
        }

    }

    public String upload(MultipartFile file, String type, SseEmitter sseEmitter, String userId)
            throws IOException, AmazonServiceException, AmazonClientException, InterruptedException {
        String filename = file.getOriginalFilename();
        switch (type) {
            case "video": {
                validateUploadedFile(file, FILE_TYPE.VIDEO);
                if (FilenameUtils.getExtension(file.getOriginalFilename()) != "mp4") {
                    filename = file.getOriginalFilename().substring(0, filename.length() - 3) + "mp4";
                }
                break;
            }
            case "image": {
                validateUploadedFile(file, FILE_TYPE.IMAGE);
                break;
            }
            default:
                throw new InvalidFileExtensionException("File not valid");
        }

        return uploadFile(file, filename, sseEmitter, userId);
    }

    private void validateUploadedFile(MultipartFile file, FILE_TYPE TYPE) {
        switch (TYPE) {
            case VIDEO:
                validateExtension(file, videoExtensions);
                validateFileSize(file, MAXIMUM_VIDEO_SIZE_ALLOWED);
                break;

            case IMAGE:
                validateExtension(file, imageExtensions);
                validateFileSize(file, MAXIMUM_IMAGE_SIZE_ALLOWED);
                break;
        }

    }

    private void validateExtension(MultipartFile file, String validFileExtensions) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (extension != null && !validFileExtensions.contains(extension)) {
            throw new InvalidFileExtensionException("File not valid");
        }
    }

    private void validateFileSize(MultipartFile file, long sizeAllowed) {
        if (convertBytesToMb(file.getSize()) >= sizeAllowed) {
            String message = "File size cannot be greater than " + Long.toString(sizeAllowed) + "MB";
            throw new FileMaximumExceedException(message);
        }
    }

    private String uploadFile(MultipartFile multipartFile, String fileName, SseEmitter sseEmitter, String userId)
            throws IOException, AmazonServiceException, AmazonClientException, InterruptedException {
        String dateTime = String.valueOf(LocalDateTime.now());
        String encodedDate = Base64.getEncoder().encodeToString(dateTime.getBytes());
        String awsFileName = userId + "_" + encodedDate + "_" + fileName;
        final File file = MultipartFileConverter.convert(multipartFile, awsFileName);
        PutObjectRequest putObjectRequest = putRequest(file, awsFileName, sseEmitter);
        Upload uploadFile = transferManager.upload(putObjectRequest);
        uploadFile.waitForCompletion();
        sseEmitter.complete();
        sseEmitter.send(sseEmitter.event().name("complete").data("complete"));
        file.delete();
        return s3Url + awsFileName;
    }

    private PutObjectRequest putRequest(File file, String fileName, SseEmitter sseEmitter) {
        PutObjectRequest request = new PutObjectRequest(bucket, fileName, file);
        request.withCannedAcl(CannedAccessControlList.PublicRead);
        long totalLength = file.length();
        request.setGeneralProgressListener(S3Listener.create(sseEmitter, totalLength));
        return request;
    }

    private long convertBytesToMb(long bytes) {
        return bytes / 1024 / 1000;
    }
}
