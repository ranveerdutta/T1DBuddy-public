package com.rd.t1d.helper.googledrive;

import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListener;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class FileUploader {

    private File _createGoogleFile(String googleFolderIdParent, String contentType,
                                          String customFileName, AbstractInputStreamContent uploadStreamContent) throws IOException, GeneralSecurityException {

        File fileMetadata = new File();
        fileMetadata.setName(customFileName);
        fileMetadata.setMimeType(contentType);

        List<String> parents = Arrays.asList(googleFolderIdParent);
        fileMetadata.setParents(parents);

        Drive driveService = GoogleDriveUtils.getDriveService();

        File file = driveService.files().create(fileMetadata, uploadStreamContent)
                .setFields("id").execute();

        return file;
    }

    public File createGoogleFile(String googleFolderIdParent, String contentType,
                                        String customFileName, byte[] uploadData) throws IOException, GeneralSecurityException {
        AbstractInputStreamContent uploadStreamContent = new ByteArrayContent(contentType, uploadData);
        return _createGoogleFile(googleFolderIdParent, contentType, customFileName, uploadStreamContent);
    }

    public File createGoogleFile(String googleFolderIdParent, String contentType,
                                        String customFileName, java.io.File uploadFile) throws IOException, GeneralSecurityException {

        AbstractInputStreamContent uploadStreamContent = new FileContent(contentType, uploadFile);
        return _createGoogleFile(googleFolderIdParent, contentType, customFileName, uploadStreamContent);
    }

    public File createGoogleFile(String googleFolderIdParent, String contentType,
                                        String customFileName, InputStream inputStream) throws IOException, GeneralSecurityException {

        AbstractInputStreamContent uploadStreamContent = new InputStreamContent(contentType, inputStream);
        return _createGoogleFile(googleFolderIdParent, contentType, customFileName, uploadStreamContent);
    }

    public byte[] downloadGoogleFile(String fileId) throws IOException, GeneralSecurityException {
        Drive driveService = GoogleDriveUtils.getDriveService();
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
            //driveService.files().get(fileId).setFields("mimeType")
              //      .executeMediaAndDownloadTo(outputStream);

            Drive.Files.Get request = driveService.files().get(fileId);
            request.getMediaHttpDownloader().setProgressListener(new CustomProgressListener());
            //request.getMediaHttpDownloader().setDirectDownloadEnabled(true);
            request.executeMediaAndDownloadTo(outputStream);
            return outputStream.toByteArray();

        }

    }

}

@Slf4j
class CustomProgressListener implements MediaHttpDownloaderProgressListener {
    public void progressChanged(MediaHttpDownloader downloader) {
        switch (downloader.getDownloadState()) {
            case MEDIA_IN_PROGRESS:
                log.info("progress: " + downloader.getProgress());
                break;
            case MEDIA_COMPLETE:
                log.info("Download is complete!");
        }
    }
}
