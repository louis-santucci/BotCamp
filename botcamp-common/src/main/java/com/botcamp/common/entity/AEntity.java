package com.botcamp.common.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Data
public abstract class AEntity {
    private static final String UUID_HIBERNATE_GENERATOR = "uuid-hibernate-generator";
    private static final String UUID_GENERATOR_STRATEGY = "org.hibernate.id.UUIDGenerator";
    protected AEntity() {}

    @Id
    @GeneratedValue(generator = UUID_HIBERNATE_GENERATOR)
    @GenericGenerator(name = UUID_HIBERNATE_GENERATOR, strategy = UUID_GENERATOR_STRATEGY)
    private String id;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Date updatedAt;
}