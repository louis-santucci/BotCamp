package com.botcamp.gmail_gateway_api.repository.entity;

import com.botcamp.common.entity.AEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.propertyeditors.StringArrayPropertyEditor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static com.botcamp.common.entity.EntityNamingAttributes.*;

@EqualsAndHashCode(callSuper = true)
@Entity(name = GATEWAY_USER)
@Table(name = GATEWAY_USER)
@Data
@Builder
@AllArgsConstructor
public class GatewayUserEntity extends AEntity {

    protected GatewayUserEntity() {
        super();
    }

    @Column(name = GATEWAY_USER_USERNAME, unique = true)
    private String username;

    @Column(name = GATEWAY_USER_PASSWORD)
    @JsonIgnore
    private String password;

    @Column(name = GATEWAY_USER_GMAIL_EMAIL)
    private String gmailEmail;
}
