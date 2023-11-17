package com.nt.rookies.assets.services;

import com.nt.rookies.assets.dtos.AssetDto;
import com.nt.rookies.assets.dtos.AssetListRequestDto;
import com.nt.rookies.assets.dtos.AssetResponse;
import com.nt.rookies.assets.entities.Asset;
import com.nt.rookies.assets.entities.AssetState;
import com.nt.rookies.assets.exceptions.ApiException;
import com.nt.rookies.assets.exceptions.NotFoundException;
import com.nt.rookies.assets.mappers.AssetMapper;
import com.nt.rookies.assets.repositories.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
// import java.util.stream.Collectors;
// import java.util.stream.IntStream;

@Service
public class AssetService {
    @Autowired
    private AssetRepository repository;

    public AssetService(AssetRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    public List<AssetDto> getAll() {
        return AssetMapper.toDtoList(this.repository.findAll());
    }

    public AssetResponse viewAsset(Integer locationId, AssetListRequestDto assetListRequestDto) {

        String searchTerm = assetListRequestDto.getSearchTerm();
        String sortBy = assetListRequestDto.getSortBy();
        String sortDir = assetListRequestDto.getSortDir();
        Integer cateFill = assetListRequestDto.getCateFill();
        AssetState stateFill = assetListRequestDto.getStateFill();
        Integer pageNo = assetListRequestDto.getPageNo();
        Integer pageSize = assetListRequestDto.getPageSize();

        AssetResponse assetResponse = new AssetResponse();

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        Page<Asset> assets;

        if (!searchTerm.trim().isEmpty() && cateFill == 0 && stateFill == null) {
            assets = repository.findBySearch(locationId, searchTerm, pageable);
        } else if (!searchTerm.trim().isEmpty() && cateFill != 0 && stateFill == null) {
            assets = repository.findByCategoryAndSearch(locationId, searchTerm, cateFill, pageable);
        } else if (!searchTerm.trim().isEmpty() && cateFill == 0 && stateFill != null) {
            assets = repository.findByStateAndSearch(locationId, searchTerm, stateFill.toString(), pageable);
        } else if (searchTerm.trim().isEmpty() && cateFill != 0 && stateFill == null) {
            assets = repository.findByCategory(locationId, cateFill, pageable);
        } else if (searchTerm.trim().isEmpty() && cateFill == 0 && stateFill != null) {
            assets = repository.findByState(locationId, stateFill.toString(), pageable);
        } else if (searchTerm.trim().isEmpty() && cateFill != 0 && stateFill != null) {
            assets = repository.findByCategoryAndState(locationId, cateFill, stateFill.toString(), pageable);
        } else if (searchTerm.trim().isEmpty() && cateFill == 0 && stateFill == null) {
            assets = repository.findAll(locationId, pageable);
        } else {
            assets = this.repository.findAll(locationId, searchTerm, cateFill, stateFill.toString(), pageable);
        }

        List<Asset> listAsset = assets.getContent();
        List<AssetDto> content = AssetMapper.toDtoList(listAsset);

        assetResponse.setContent(content);
        assetResponse.setPageNo(assets.getNumber() + 1);
        assetResponse.setPageSize(assets.getSize());
        assetResponse.setTotalPages(assets.getTotalPages());
        assetResponse.setTotalElements(assets.getTotalElements());
        assetResponse.setLast(assets.isLast());

        return assetResponse;

    }

    public AssetDto getByCode(String id) {
        return AssetMapper.toDto(repository.findByAssetId(id).orElseThrow(() ->new NotFoundException("Asset with assetId: " + id+ " Not Found")));
    }

    public AssetDto declineAssignment(String assetId) {

        Asset asset = repository.findByAssetId(assetId).orElseThrow(() ->new NotFoundException("Asset with assetId: " + assetId + " Not Found"));
        asset.setState(AssetState.AVAILABLE);

        return AssetMapper.toDto(repository.save(asset));

    }

    public AssetDto editAsset(String assetId, AssetDto assetDto) {
        AssetDto editAsset = AssetMapper.toDto(repository.findByAssetId(assetId).orElseThrow(() ->new NotFoundException("Asset with assetId: " + assetId + " Not Found")));
        if(!editAsset.getState().equals(AssetState.ASSIGNED)){
            editAsset.setName(assetDto.getName());
            editAsset.setSpecification(assetDto.getSpecification());
            editAsset.setInstalledDate(assetDto.getInstalledDate());
            editAsset.setState(assetDto.getState());
            editAsset.setUpdatedAt(LocalDateTime.now());
        } else throw new ApiException(HttpStatus.BAD_REQUEST, "Can not edit asset that is being assigned!");
        return AssetMapper.toDto(repository.save(AssetMapper.toEntity(editAsset)));
    }

    public AssetDto deleteAsset(String assetId) {
        AssetDto editAsset = AssetMapper.toDto(repository.findByAssetId(assetId).orElseThrow(() ->new NotFoundException("Asset with assetId: " + assetId + " Not Found")));
        Integer history = repository.getHistoryAsset(assetId).orElse(0);
        if (!editAsset.getState().equals(AssetState.ASSIGNED) && history == 0 ) {
            editAsset.setState(AssetState.RECYCLED);
            AssetMapper.toDto(repository.save(AssetMapper.toEntity(editAsset)));
        }else throw new ApiException(HttpStatus.BAD_REQUEST, "Can not delete asset that are have been assigned!");
        return editAsset;
    }

    public Integer checkHistory(String assetId){
        AssetDto editAsset = AssetMapper.toDto(repository.findByAssetId(assetId).orElseThrow(() ->new NotFoundException("Asset with assetId: " + assetId + " Not Found")));
        Integer history = repository.getHistoryAsset(assetId).orElse(0);
        if(history == 0){
            return 1;
        }
        return  0;
    }


}
