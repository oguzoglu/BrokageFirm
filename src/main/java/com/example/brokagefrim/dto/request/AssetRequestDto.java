package com.example.brokagefrim.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetRequestDto {
    private String assetName;
    private Long size;
    private Long usableSize;
    private Long customerId;
}