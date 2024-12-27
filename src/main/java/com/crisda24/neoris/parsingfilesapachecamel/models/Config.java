package com.crisda24.neoris.parsingfilesapachecamel.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Table("config_table")
public class Config implements Serializable {

    @Id

    private Long id;
    private String name;
}
