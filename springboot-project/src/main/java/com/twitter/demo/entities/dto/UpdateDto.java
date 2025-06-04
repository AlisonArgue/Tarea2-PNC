package com.twitter.demo.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Data
public class UpdateDto {

    //private UUID id;
    private String newName;
    private String newEmail;
    private String newPwr;
}
