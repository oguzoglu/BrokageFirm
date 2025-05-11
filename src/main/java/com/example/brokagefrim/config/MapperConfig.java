package com.example.brokagefrim.config;

import com.example.brokagefrim.dto.mapper.AssetMapper;
import com.example.brokagefrim.dto.mapper.CustomerMapper;
import com.example.brokagefrim.dto.mapper.OrderMapper;
import com.example.brokagefrim.dto.mapper.impl.AssetMapperImpl;
import com.example.brokagefrim.dto.mapper.impl.CustomerMapperImpl;
import com.example.brokagefrim.dto.mapper.impl.OrderMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public AssetMapper assetMapper() {
        return new AssetMapperImpl();
    }

    @Bean
    public CustomerMapper customerMapper() {
        return new CustomerMapperImpl();
    }

    @Bean
    public OrderMapper orderMapper() {
        return new OrderMapperImpl();
    }
}