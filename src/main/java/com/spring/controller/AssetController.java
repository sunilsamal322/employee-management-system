package com.spring.controller;

import com.spring.dto.ApiResponse;
import com.spring.dto.AssetDto;
import com.spring.service.AssetServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;

@RequestMapping("/assets")
@RestController
public class AssetController {

    @Autowired
    private AssetServices assetServices;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/organization/{organizationId}")
    public ResponseEntity<AssetDto> saveAsset(@Valid @RequestBody AssetDto assetDto, @PathVariable Integer organizationId)
    {
        return new ResponseEntity<>(assetServices.saveAsset(assetDto,organizationId), HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<AssetDto> getAssetById(@PathVariable Integer id)
    {
        return new ResponseEntity<>(assetServices.getAssetById(id),HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<AssetDto>> getAllAssets()
    {
        return new ResponseEntity<>(assetServices.getAllAssets(),HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<AssetDto> updateAsset(@PathVariable Integer id,@Valid @RequestBody AssetDto assetDto)
    {
        return new ResponseEntity<>(assetServices.updateAsset(id,assetDto),HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteAsset(@PathVariable Integer id)
    {
        assetServices.deleteAsset(id);
        return new ResponseEntity<>(new ApiResponse("Asset deleted successfully",String.valueOf(HttpStatus.OK), Instant.now()),HttpStatus.OK);
    }
}
