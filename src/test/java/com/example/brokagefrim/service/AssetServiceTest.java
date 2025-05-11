package com.example.brokagefrim.service;

import com.example.brokagefrim.model.Asset;
import com.example.brokagefrim.model.Currency;
import com.example.brokagefrim.repository.AssetRepository;
import com.example.brokagefrim.service.impl.AssetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetServiceTest {

    @Mock
    private AssetRepository assetRepository;

    private AssetService assetService;

    @BeforeEach
    void setUp() {
        assetService = new AssetServiceImpl(assetRepository);
    }

    @Test
    void testUpdateAssetForOrderIncrease() {
        Asset asset = new Asset();
        asset.setUsableSize(10L);

        when(assetRepository.findFirstByCustomerIdAndAssetNameAndCurrency(1L, "ONR", Currency.TRY))
                .thenReturn(Optional.of(asset));
        assetService.updateAssetForOrder(1L, "ONR", 1L, true);

        verify(assetRepository, times(1)).save(asset);
        assertEquals(11L, asset.getUsableSize());
    }

    @Test
    void testUpdateAssetForOrderDecrease() {
        Asset asset = new Asset();
        asset.setUsableSize(10L);

        when(assetRepository.findFirstByCustomerIdAndAssetNameAndCurrency(1L, "TSL", Currency.TRY))
                .thenReturn(Optional.of(asset));
        assetService.updateAssetForOrder(1L, "TSL", 1L, false);

        verify(assetRepository, times(1)).save(asset);
        assertEquals(9L, asset.getUsableSize());
    }
}