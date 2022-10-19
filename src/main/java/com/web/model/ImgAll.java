package com.web.model;

import lombok.Data;

@Data
public class ImgAll {
    private int id;
    private int aid;
    private String fileName;
    private String originalName;
    private String mimeType;
    private String fileUrl;
    private String type;
}
