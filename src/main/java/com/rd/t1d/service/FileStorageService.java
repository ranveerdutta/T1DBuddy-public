package com.rd.t1d.service;

import com.google.common.collect.ImmutableList;
import com.rd.t1d.data.entity.node.StoredFile;
import com.rd.t1d.data.repository.StoredFileRepository;
import com.rd.t1d.dto.FileObject;
import com.rd.t1d.exception.ErrorCode;
import com.rd.t1d.exception.T1DBuddyException;
import com.rd.t1d.helper.googledrive.FileEncryptor;
import com.rd.t1d.helper.googledrive.FileUploader;
import com.rd.t1d.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class FileStorageService {

    private static final List<String> REQUIRED_SCOPES =
            ImmutableList.of("https://www.googleapis.com/auth/photoslibrary.readonly",
                    "https://www.googleapis.com/auth/photoslibrary.appendonly");

    private static final List<String> ALLOWED_IMAGE_FILE_TYPES =
            ImmutableList.of("jpeg", "jpg", "png");

    private static final List<String> ALLOWED_RECORD_FILE_TYPES =
            ImmutableList.of("jpeg", "jpg", "png", "pdf");

    public static final String IMAGE_FILE_TYPE = "image";

    public static final String RECORD_FILE_TYPE = "record";

    @Autowired
    private StoredFileRepository storedFileRepository;

    @Autowired
    private FileEncryptor fileEncryptor;

    @Value("${google.drive.parent.folder}")
    private String googleFolderIdParent;

    @Value("${google.drive.record.folder}")
    private String googleRecordFolderId;



    @Autowired
    private FileUploader fileUploader;

    @Transactional
    public StoredFile uploadFile(File filesToUpload, String email, String contentType, String fileType) throws IOException, GeneralSecurityException, ExecutionException, InterruptedException {
        log.info("file is being uploaded for the user: " + email + " of size(KB): " + filesToUpload.length()/1024);
        log.info("file name: " + filesToUpload.getName());
        String fileExt = com.google.common.io.Files.getFileExtension(filesToUpload.getName());
        if(StringUtils.isBlank(fileExt) ||
                (IMAGE_FILE_TYPE.equals(fileType) && !ALLOWED_IMAGE_FILE_TYPES.contains(fileExt.toLowerCase())) ||
                (RECORD_FILE_TYPE.equals(fileType) && !ALLOWED_RECORD_FILE_TYPES.contains(fileExt.toLowerCase()))) {
            throw new T1DBuddyException(ErrorCode.WRONG_FILE_TYPE);
        }
        String googleFolder;
        if(RECORD_FILE_TYPE.equals(fileType)){
            googleFolder = googleRecordFolderId;
        }else{
            googleFolder = googleFolderIdParent;
        }

        File newFile = encryptIfRequired(filesToUpload, fileType);
        com.google.api.services.drive.model.File uploadedFile = fileUploader.createGoogleFile(googleFolder, contentType, newFile.getName(), newFile);

        //update database with file details
        StoredFile storedFile = new StoredFile();
        storedFile.setContentType(contentType);
        storedFile.setFileName(newFile.getName());
        storedFile.setFileId(uploadedFile.getId());
        ZonedDateTime current = DateUtils.getCurrentZonedDateTime();
        storedFile.setCreatedAt(current);
        storedFile.setUpdatedAt(current);
        storedFile.setCreatedByEmail(email);
        storedFile.setParentFolderId(googleFolder);
        storedFileRepository.save(storedFile);

        log.info("Uploaded file with id: "+ uploadedFile.getId() + " by user: " + email);

        return storedFile;

    }

    private File encryptIfRequired(File file, String fileType){
        if(RECORD_FILE_TYPE.equals(fileType)){
            return fileEncryptor.encryptFile(file);
        }else{
            return file;
        }
    }

    @Cacheable(value = "imageCache", key = "#fileId")
    public byte[] downloadFile(String fileId) throws IOException, GeneralSecurityException {
        log.info("downloading image with file id: " + fileId);
        List<StoredFile> fileList = storedFileRepository.findByFileId(fileId);

        if(null == fileList || fileList.isEmpty()){
            throw new T1DBuddyException(ErrorCode.FILE_NOT_FOUND);
        }

        //StoredFile storedFile = fileList.get(0);

        return fileUploader.downloadGoogleFile(fileId);
    }

    public FileObject downloadRecordFileObject(String fileId) throws IOException, GeneralSecurityException {
        log.info("downloading record with file id: " + fileId);
        List<StoredFile> fileList = storedFileRepository.findByFileId(fileId);

        if(null == fileList || fileList.isEmpty()){
            throw new T1DBuddyException(ErrorCode.FILE_NOT_FOUND);
        }

        byte[] bytes = fileUploader.downloadGoogleFile(fileId);

        return new FileObject(fileEncryptor.decryptFile(bytes), fileEncryptor.getDecryptedFileName(fileList.get(0).getFileName()));
    }

}
