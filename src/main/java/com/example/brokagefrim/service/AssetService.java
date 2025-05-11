package com.example.brokagefrim.service;

import com.example.brokagefrim.model.Asset;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import java.util.Optional;

public interface AssetService {

    @PreAuthorize("hasAuthority('ADMIN')")
    List<Asset> findAll();
    Asset save(Asset asset);

    @PreAuthorize("hasAuthority('ADMIN')")
    List<Asset> findByAssetName(String assetName);

    @PreAuthorize("hasAuthority('ADMIN') or @authorization.isCustomerOwner(#customerId)")
    List<Asset> findByCustomerId(Long customerId);

    @PreAuthorize("hasAuthority('ADMIN') or @authorization.isCustomerOwner(#customerId)")
    List<Asset> findByCustomerIdAndAssetName(Long customerId, String assetName);
    void updateUsableSize(Long id, Long newUsableSize);
    void updateSize(Long id, Long newSize);
    Optional<Asset> findByCustomerIdAndAssetNameOp(Long customerId, String assetName);
    void updateAssetForOrder(Long customerId, String assetName, Long amount, boolean increase);
}