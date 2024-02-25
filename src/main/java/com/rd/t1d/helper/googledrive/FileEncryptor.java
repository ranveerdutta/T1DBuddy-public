package com.rd.t1d.helper.googledrive;


import com.rd.t1d.exception.ErrorCode;
import com.rd.t1d.exception.T1DBuddyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

@Slf4j
@Component
public class FileEncryptor {

    private static final String ENCRYPTED_FILE_SUFFIX = "_enc";

    @Value("${record.file.encryption.key}")
    private Integer encryptionKey;

    public File encryptFile(File file){
        File encFile = new File(file.getAbsoluteFile() + ENCRYPTED_FILE_SUFFIX);
        encFile.deleteOnExit();
        try(FileInputStream fl = new FileInputStream(file);  FileOutputStream fos = new FileOutputStream(encFile)) {
            byte[] data = new byte[(int)file.length()];
            fl.read(data);
            int i = 0;
            for (byte b : data) {
                data[i] = (byte)(b ^ encryptionKey);
                i++;
            }

            fos.write(data);

        } catch (Exception e) {
            log.info("error while encrypting the file: " + file.getName() , e);
            throw new T1DBuddyException(ErrorCode.INVALID_FILE, e);
        }

        return encFile;
    }

    public byte[] decryptFile(byte[] byteArr){
        int i = 0;
        for (byte b : byteArr) {
            byteArr[i] = (byte)(b ^ encryptionKey);
            i++;
        }

        return byteArr;
    }

    public String getDecryptedFileName(String fileName){
        if(fileName.endsWith(ENCRYPTED_FILE_SUFFIX)){
            return fileName.substring(0, fileName.length()-4);
        }else{
            return fileName;
        }
    }
}
