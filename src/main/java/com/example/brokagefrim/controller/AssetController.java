package com.example.brokagefrim.controller;

import com.example.brokagefrim.dto.mapper.AssetMapper;
import com.example.brokagefrim.dto.response.AssetResponseDto;
import com.example.brokagefrim.model.Asset;
import com.example.brokagefrim.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService assetService;
    private final AssetMapper assetMapper;

    @Autowired
    public AssetController(AssetService assetService, AssetMapper assetMapper) {
        this.assetService = assetService;
        this.assetMapper = assetMapper;
    }

    @GetMapping
    public ResponseEntity<List<AssetResponseDto>> getAssets(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) String assetName) {

        List<Asset> assets;

        if (customerId != null && assetName != null) {
            assets = assetService.findByCustomerIdAndAssetName(customerId, assetName);
        } else if (customerId != null) {
            assets = assetService.findByCustomerId(customerId);
        } else if (assetName != null) {
            assets = assetService.findByAssetName(assetName);
        } else {
            assets = assetService.findAll();
        }

        List<AssetResponseDto> assetDtos = assets.stream()
                .map(assetMapper::toDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(assetDtos, HttpStatus.OK);
    }

    @GetMapping("/{customerId}/{assetName}")
    public ResponseEntity<?> getAssetById(@PathVariable Long customerId, @PathVariable String assetName){
        Asset asset = assetService.findByCustomerIdAndAssetNameOp(customerId, assetName).orElse(null);
        return new ResponseEntity<>(asset, HttpStatus.OK);
    }
}