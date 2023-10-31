package com.botcamp.gmail_gateway_api.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user")
@Data
public class BandcampUserEntity extends AEntity {

    @Column
    private String username;

    @Column
    @JsonIgnore
    private String password;

    @Column
    private String gmailEmail;
}
