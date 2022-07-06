package com.spring.serviceImpl;

import com.spring.dto.AssetDto;
import com.spring.exception.ResourceNotFoundException;
import com.spring.model.Asset;
import com.spring.model.Organization;
import com.spring.repository.AssetRepository;
import com.spring.repository.OrganizationRepository;
import com.spring.service.AssetServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssetServicesImpl implements AssetServices {

    @Autowired
    private AssetRepository assetRepository;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AssetDto saveAsset(AssetDto assetDto, Integer organizationId) {
        Organization organization =organizationRepository.findById(organizationId).orElseThrow(()->new ResourceNotFoundException("organization","id",organizationId));
        Asset asset=modelMapper.map(assetDto,Asset.class);
        asset.setOrganization(organization);
        asset.setAssetAddedTime(new Date());
        return modelMapper.map(assetRepository.save(asset),AssetDto.class);
    }

    @Override
    public AssetDto getAssetById(Integer id) {
        Asset asset=assetRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("asset","id",id));
        return modelMapper.map(asset,AssetDto.class);
    }

    @Override
    public List<AssetDto> getAllAssets() {

        List<Asset> assets=assetRepository.findAll();
        List<AssetDto> assetDtos=assets.stream().map(asset->modelMapper.map(asset,AssetDto.class)).collect(Collectors.toList());
        return assetDtos;
    }

    @Override
    public AssetDto updateAsset(Integer id, AssetDto assetDto) {
        Asset existingAsset=assetRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("asset","id",id));
        existingAsset.setName(assetDto.getName());
        return modelMapper.map(assetRepository.save(existingAsset),AssetDto.class);
    }

    @Override
    public void deleteAsset(Integer id) {
        Asset asset=assetRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("asset","id",id));
        assetRepository.delete(asset);
    }
}
