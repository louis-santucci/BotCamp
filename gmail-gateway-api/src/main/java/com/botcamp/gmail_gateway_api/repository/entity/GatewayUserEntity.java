package com.botcamp.gmail_gateway_api.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "botcamp_user")
@Table(name = "botcamp_user")
@Data
public class GatewayUserEntity extends AEntity {

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Column(name = "gmail_email")
    private String gmailEmail;
}
