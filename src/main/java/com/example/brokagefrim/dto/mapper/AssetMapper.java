package com.example.brokagefrim.dto.mapper;

import com.example.brokagefrim.dto.request.AssetRequestDto;
import com.example.brokagefrim.dto.response.AssetResponseDto;
import com.example.brokagefrim.model.Asset;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AssetMapper {
    Asset toEntity(AssetRequestDto dto);

    AssetResponseDto toDto(Asset entity);

    void updateEntityFromDto(AssetRequestDto dto, @MappingTarget Asset entity);
}