package com.rd.t1d.helper.googledrive;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.rd.t1d.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
public class GoogleDriveUtils {

    private static final String APPLICATION_NAME = "T1D-Buddy";

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    // Directory to store user credentials for this application.
    private static final java.io.File CREDENTIALS_FOLDER //
            = new java.io.File(System.getProperty("credential.dir"));

    private static final String USER_SECRET_FILE_NAME = "user-account-secret.json";

    private static final String SERVICE_SECRET_FILE_NAME = "service-account-secret.json";

    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);

    //private static final int TOKEN_EXPIRY_IN_MINUTES = 55;

    // Global instance of the {@link FileDataStoreFactory}.
    //private static FileDataStoreFactory DATA_STORE_FACTORY;

    // Global instance of the HTTP transport.
    //private static HttpTransport HTTP_TRANSPORT;

    private static GoogleCredential credential;

    //private static HttpRequestInitializer httpRequestInitializer;

    //private static Date tokenDate;

    //private static Drive _driveService;

    static {
        try {
            //HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            //DATA_STORE_FACTORY = new FileDataStoreFactory(CREDENTIALS_FOLDER);
            credential = getServiceAccountCredentials();
            //tokenDate = new Date();
            /*httpRequestInitializer = new HttpRequestInitializer() {
                @Override
                public void initialize(HttpRequest httpRequest) throws IOException {

                    credential.initialize(httpRequest);
                    httpRequest.setConnectTimeout(90 * 1000);
                    httpRequest.setReadTimeout(90 * 1000);
                }
            };*/
        } catch (Throwable t) {
            t.printStackTrace();
            //System.exit(1);
        }
    }

    /*public static Credential getUserAccountCredentials() throws IOException {

        java.io.File clientSecretFilePath = new java.io.File(CREDENTIALS_FOLDER, USER_SECRET_FILE_NAME);

        if (!clientSecretFilePath.exists()) {
            throw new FileNotFoundException("Please copy " + USER_SECRET_FILE_NAME //
                    + " to folder: " + CREDENTIALS_FOLDER.getAbsolutePath());
        }

        try(InputStream in = new FileInputStream(clientSecretFilePath);
             Reader reader = new InputStreamReader(in)){
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, reader);

            // Build flow and trigger user authorization request.
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                    clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
            LocalServerReceiver receiver =
                    new LocalServerReceiver.Builder().setPort(8085).build();
            Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

            return credential;
        }


    }*/

    public static GoogleCredential getServiceAccountCredentials() throws IOException {

        java.io.File clientSecretFilePath = new java.io.File(CREDENTIALS_FOLDER, SERVICE_SECRET_FILE_NAME);

        if (!clientSecretFilePath.exists()) {
            throw new FileNotFoundException("Please copy " + SERVICE_SECRET_FILE_NAME //
                    + " to folder: " + CREDENTIALS_FOLDER.getAbsolutePath());
        }

        try(InputStream in = new FileInputStream(clientSecretFilePath);
                ){
            GoogleCredential credential = GoogleCredential.fromStream(in)
                    .createScoped(SCOPES).setExpiresInSeconds(15552000L);
            credential.refreshToken();
            return credential;
        }


    }

    public static Drive getDriveService() throws IOException, GeneralSecurityException {
        /*if (_driveService != null) {
            return _driveService;
        }*/
        //Credential credential = getServiceAccountCredentials();

        /*HttpRequestInitializer httpRequestInitializer = new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest) throws IOException {

                credential.initialize(httpRequest);
                httpRequest.setConnectTimeout(180 * 1000);
                httpRequest.setReadTimeout(180 * 1000);
            }
        };*/

        /*if(tokenDate != null && DateUtils.minDiffFromToday(tokenDate) > TOKEN_EXPIRY_IN_MINUTES){
            log.info("Refreshing google token");
            credential.refreshToken();
            tokenDate = new Date();
        }*/

        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        if(null == credential){
            credential = getServiceAccountCredentials();
        }

        Drive _driveService = new Drive.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
                //.setHttpRequestInitializer(httpRequestInitializer).setApplicationName(APPLICATION_NAME).build();


        return _driveService;
    }

}
