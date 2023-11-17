package com.nt.rookies.assets.mappers;

import com.nt.rookies.assets.dtos.AssetDto;
import com.nt.rookies.assets.dtos.AssetRequestDto;
import com.nt.rookies.assets.dtos.CategoryDto;
import com.nt.rookies.assets.dtos.LocationDto;
import com.nt.rookies.assets.entities.Asset;
import com.nt.rookies.assets.entities.Category;
import com.nt.rookies.assets.entities.Location;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AssetMapper {

    public static AssetDto toDto(Asset asset){
        AssetDto assetDto = new AssetDto();
        assetDto.setAssetId(asset.getAssetId());
        assetDto.setName(asset.getName());
        assetDto.setInstalledDate(asset.getInstalledDate());
        assetDto.setSpecification(asset.getSpecification());
        assetDto.setState(asset.getState());
        assetDto.setCategory(CategoryMapper.toDto(asset.getCategory()));
        assetDto.setLocationId(LocationMapper.toDto(asset.getLocationId()));
        assetDto.setUpdatedAt(asset.getUpdatedAt());
        return  assetDto;
    }

    public static Asset toEntity(AssetDto assetDto){
        Asset entity = new Asset();
        entity.setAssetId(assetDto.getAssetId());
        entity.setName(assetDto.getName());
        entity.setInstalledDate(assetDto.getInstalledDate());
        entity.setSpecification(assetDto.getSpecification());
        entity.setState(assetDto.getState());

        if(Objects.nonNull(assetDto.getCategory())){
            Category categoryEntity = new Category();
            categoryEntity.setCategoryId(Objects.requireNonNull(assetDto.getCategory().getCategoryId(), "Category ID is required!"));
            entity.setCategory(categoryEntity);
        }

        if(Objects.nonNull(assetDto.getLocationId())){
            Location locationEntity = new Location();
            locationEntity.setLocationId(Objects.requireNonNull(assetDto.getLocationId().getLocationId(), "Location ID is required!"));
            entity.setLocationId(locationEntity);
        }
        entity.setUpdatedAt(assetDto.getUpdatedAt());

        return entity;
    }

    /** 
     * @param assetRequestDto AssetRequestDto object to be parsed
     * @param locationDto LocationDto object to be parsed
     * @param categoryDto CategoryDto object to be parsed
     * @param updatedAt LocalDateTime value to add to updatedAt field
     * @return Asset Entity
     */
    public static Asset toEntity(AssetRequestDto assetRequestDto, LocationDto locationDto, CategoryDto categoryDto, LocalDateTime updatedAt){
        Asset entity = new Asset();
        entity.setAssetId(assetRequestDto.getAssetId());
        entity.setName(assetRequestDto.getName());
        entity.setInstalledDate(assetRequestDto.getInstalledDate().atStartOfDay());
        entity.setSpecification(assetRequestDto.getSpecification());
        entity.setState(assetRequestDto.getState());
        entity.setLocationId(LocationMapper.toEntity(locationDto));
        entity.setCategory(CategoryMapper.toEntity(categoryDto));
        entity.setUpdatedAt(updatedAt);
        return entity;
    }

    public static List<AssetDto> toDtoList(List<Asset> entities){
        return entities.stream().map(AssetMapper::toDto).collect(Collectors.toList());
    }

    public static List<AssetDto> toDtoList(Iterable<Asset> entities){
        List<AssetDto> assetDtos = new LinkedList<>();
        entities.forEach(e -> assetDtos.add(toDto(e)));
        return assetDtos;
    }
}
