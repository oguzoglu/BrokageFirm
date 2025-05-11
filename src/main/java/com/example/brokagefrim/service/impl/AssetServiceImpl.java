package com.example.brokagefrim.service.impl;

import com.example.brokagefrim.model.Asset;
import com.example.brokagefrim.model.Currency;
import com.example.brokagefrim.repository.AssetRepository;
import com.example.brokagefrim.service.AssetService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssetServiceImpl implements AssetService {

    private static final Currency DEFAULT_CURRENCY = Currency.TRY;

    private final AssetRepository assetRepository;

    @Autowired
    public AssetServiceImpl(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    @Override
    public List<Asset> findAll() {
        return assetRepository.findAll();
    }


    @Override
    public Asset save(Asset asset) {
        return assetRepository.save(asset);
    }



    @Override
    public List<Asset> findByAssetName(String assetName) {
        return assetRepository.findByAssetNameAndCurrency(assetName, DEFAULT_CURRENCY);
    }

    @Override
    public List<Asset> findByCustomerId(Long customerId) {
        return assetRepository.findByCustomerIdAndCurrency(customerId, DEFAULT_CURRENCY);
    }

    @Override
    public List<Asset> findByCustomerIdAndAssetName(Long customerId, String assetName) {
        return assetRepository.findByCustomerIdAndAssetNameAndCurrency(customerId, assetName, DEFAULT_CURRENCY);
    }

    @Override
    public Optional<Asset> findByCustomerIdAndAssetNameOp(Long customerId, String assetName) {
        return assetRepository.findFirstByCustomerIdAndAssetNameAndCurrency(customerId, assetName, DEFAULT_CURRENCY);
    }

    @Override
    @Transactional
    public void updateUsableSize(Long id, Long newUsableSize) {
        assetRepository.updateUsableSizeById(id, newUsableSize);
    }

    @Override
    @Transactional
    public void updateSize(Long id, Long newSize) {
        assetRepository.updateSizeById(id, newSize);
    }

    @Override
    @Transactional
    public void updateAssetForOrder(Long customerId, String assetName, Long amount, boolean increase) {
        Optional<Asset> assetOpt = assetRepository.findFirstByCustomerIdAndAssetNameAndCurrency(customerId, assetName, DEFAULT_CURRENCY);

        if (assetOpt.isPresent()) {
            Asset asset = assetOpt.get();
            var currentUsableSize = asset.getUsableSize();

            if (increase) {
                asset.setUsableSize(currentUsableSize + amount);
            } else {
                asset.setUsableSize(currentUsableSize - amount);
            }

            assetRepository.save(asset);
        } else {
            if (increase) {
                Asset newAsset = new Asset();
                newAsset.setCustomerId(customerId);
                newAsset.setAssetName(assetName);
                newAsset.setSize(amount);
                newAsset.setUsableSize(amount);
                assetRepository.save(newAsset);
            }
        }
    }
}