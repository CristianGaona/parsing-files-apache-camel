package com.crisda24.neoris.parsingfilesapachecamel.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Table("config")
public class Config implements Serializable {

    @Id

    private Long id;
    private String name;
    private String key;
    private String fileInput;
    private String fileOutput;
    private String fileTransform;
}
