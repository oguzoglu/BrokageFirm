package com.example.brokagefrim.dto.mapper.impl;

import com.example.brokagefrim.dto.mapper.AssetMapper;
import com.example.brokagefrim.dto.request.AssetRequestDto;
import com.example.brokagefrim.dto.response.AssetResponseDto;
import com.example.brokagefrim.model.Asset;
import org.springframework.stereotype.Component;

@Component
public class AssetMapperImpl implements AssetMapper {

    @Override
    public Asset toEntity(AssetRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Asset asset = new Asset();

        asset.setAssetName(dto.getAssetName());
        asset.setSize(dto.getSize());
        asset.setUsableSize(dto.getUsableSize());
        asset.setCustomerId(dto.getCustomerId());

        return asset;
    }

    @Override
    public AssetResponseDto toDto(Asset entity) {
        if (entity == null) {
            return null;
        }

        AssetResponseDto responseDto = new AssetResponseDto();

        responseDto.setId(entity.getId());
        responseDto.setAssetName(entity.getAssetName());
        responseDto.setSize(entity.getSize());
        responseDto.setUsableSize(entity.getUsableSize());
        responseDto.setCustomerId(entity.getCustomerId());
        responseDto.setCreateDate(entity.getCreateDate());
        responseDto.setUpdateDate(entity.getUpdateDate());

        return responseDto;
    }

    @Override
    public void updateEntityFromDto(AssetRequestDto dto, Asset entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getAssetName() != null) {
            entity.setAssetName(dto.getAssetName());
        }
        if (dto.getSize() != null) {
            entity.setSize(dto.getSize());
        }
        if (dto.getUsableSize() != null) {
            entity.setUsableSize(dto.getUsableSize());
        }
        if (dto.getCustomerId() != null) {
            entity.setCustomerId(dto.getCustomerId());
        }
    }
}