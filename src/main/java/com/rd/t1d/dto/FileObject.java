package com.rd.t1d.dto;

import lombok.Data;

@Data
public class FileObject {

    private byte[] bytes;

    private String fileName;

    public FileObject(byte[] bytes, String fileName) {
        this.bytes = bytes;
        this.fileName = fileName;
    }
}
