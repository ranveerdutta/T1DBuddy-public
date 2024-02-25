package com.rd.t1d.controller;

import com.rd.t1d.data.entity.node.StoredFile;
import com.rd.t1d.dto.FileObject;
import com.rd.t1d.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.ExecutionException;

@RestController
@Slf4j
public class FileStorageController {

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping(value = "/image/{imageId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageFile(@PathVariable("imageId") String imageId) throws GeneralSecurityException, IOException {
        return fileStorageService.downloadFile(imageId);
    }

    @PostMapping(value = "/user/{email}/image")
    public @ResponseBody String uploadImageFile(@PathVariable("email") String email, @RequestParam("file") MultipartFile multipartFile) throws GeneralSecurityException, IOException, ExecutionException, InterruptedException {
        File convFile = new File(System.getProperty("java.io.tmpdir")+"/" + multipartFile.getOriginalFilename());
        convFile.deleteOnExit();
        multipartFile.transferTo(convFile);
        StoredFile storedFile = fileStorageService.uploadFile(convFile,email, multipartFile.getContentType(), FileStorageService.IMAGE_FILE_TYPE);
        return storedFile.getFileId();
    }

    @PostMapping(value = "/user/{email}/record-file")
    public @ResponseBody String uploadRecordFile(@PathVariable("email") String email, @RequestParam("file") MultipartFile multipartFile) throws GeneralSecurityException, IOException, ExecutionException, InterruptedException {
        File convFile = new File(System.getProperty("java.io.tmpdir")+"/" + multipartFile.getOriginalFilename());
        convFile.deleteOnExit();
        multipartFile.transferTo(convFile);
        StoredFile storedFile = fileStorageService.uploadFile(convFile,email, multipartFile.getContentType(), FileStorageService.RECORD_FILE_TYPE);
        return storedFile.getFileId();
    }

    @GetMapping(value = "/file/{fileId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> getRecordFile(@PathVariable("fileId") String fileId) throws GeneralSecurityException, IOException {
        FileObject fileObject = fileStorageService.downloadRecordFileObject(fileId);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Disposition",
                "attachment; filename=\"" + "Report_" + fileObject.getFileName() + "\"");
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(fileObject.getBytes());
    }

}
