package com.example.brokagefrim.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Table(name = "assets", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"assetName","currency","customerId"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Asset extends AbstractBaseModel {
    private String assetName;
    private Long size;
    private Long usableSize;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    public Long customerId;
}