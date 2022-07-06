package com.spring.service;

import com.spring.dto.AssetDto;
import com.spring.model.Asset;

import java.util.List;

public interface AssetServices {

    AssetDto saveAsset(AssetDto assetDto, Integer organizationId);

    AssetDto getAssetById(Integer id);

    List<AssetDto> getAllAssets();

    AssetDto updateAsset(Integer id,AssetDto asset);

    void deleteAsset(Integer id);
}
