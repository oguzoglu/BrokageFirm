package com.example.brokagefrim.repository;

import com.example.brokagefrim.model.Asset;
import com.example.brokagefrim.model.Currency;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByAssetNameAndCurrency(String assetName, Currency currency);
    List<Asset> findByCustomerIdAndCurrency(Long customerId, Currency currency);
    List<Asset> findByCustomerIdAndAssetNameAndCurrency(Long customerId, String assetName, Currency currency);
    Optional<Asset> findFirstByCustomerIdAndAssetNameAndCurrency(Long customerId, String assetName, Currency currency);

    @Modifying
    @Transactional
    @Query("update Asset a set a.usableSize = :usableSize where a.id = :id and a.deleted = false")
    void updateUsableSizeById(@Param("id") Long id, @Param("usableSize") Long usableSize);

    @Modifying
    @Transactional
    @Query("update Asset a set a.size = :size where a.id = :id and a.deleted = false")
    void updateSizeById(@Param("id") Long id, @Param("size") Long size);
}
