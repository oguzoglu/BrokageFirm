package com.example.brokagefrim.model;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public abstract class AbstractBaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Boolean deleted = false;

    @PrePersist
    public void prePersist() {
        if (createDate == null) {
            createDate = LocalDateTime.now();
        }
        updateDate = LocalDateTime.now();
    }

    @PreRemove
    public void markAsDeleted() {
        this.deleted = true;
        updateDate = LocalDateTime.now();
    }
}
