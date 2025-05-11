package com.example.brokagefrim.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetResponseDto {
    private Long id;
    private String assetName;
    private Long size;
    private Long usableSize;
    private Long customerId;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}