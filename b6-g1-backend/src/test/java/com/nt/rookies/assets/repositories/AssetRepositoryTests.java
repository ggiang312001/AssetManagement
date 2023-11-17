package com.nt.rookies.assets.repositories;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.nt.rookies.assets.entities.*;
import com.nt.rookies.assets.exceptions.NotFoundException;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.nt.rookies.assets.dtos.AssetDto;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 */
@DataJpaTest
@AutoConfigureTestDatabase
public class AssetRepositoryTests {

    private AssetRepository assetRepository;
    private TestEntityManager entityManager;
    @Autowired
    public AssetRepositoryTests(AssetRepository assetRepository, TestEntityManager enbEntityManager){
        this.assetRepository = assetRepository;
        this.entityManager = entityManager;
    }

    @Test
    @Order(1)
    public void testGetByLocation(){
        int locationId = 1;
        Pageable pageable = PageRequest.of(1,3);
        List<Asset> actual = assetRepository.findAll(locationId, pageable).getContent();
        assertNotNull(actual);
    }

    @Test
    @Order(2)
    public void testSaveAsset(){
        Category category = new Category();
        category.setCategoryId(0);
        category.setName("Laptop");

        Location location = new Location(0, "Ha Noi");

        Asset asset = new Asset(); 
        asset.setAssetId("LA004");
        asset.setName("Laptop 04");
        asset.setCategory(category);
        asset.setLocationId(location);
        asset.setState(AssetState.AVAILABLE);
        asset.setInstalledDate(LocalDateTime.now());
        Asset saved = assetRepository.save(asset);

        assertTrue(saved.getAssetId().equals(asset.getAssetId()));
    }
    public void testGetByLocationAndSearch(){
        int locationId = 1;
        String searchTerm = "laptop";
        Pageable pageable = PageRequest.of(1,10);
        List<Asset> actual = assetRepository.findBySearch(locationId,searchTerm,pageable).getContent();
        assertNotNull(actual);
    }

    @Test
    public void testGetByLocationAndCategory(){
        int locationId = 1;
        int category = 1;
        Pageable pageable = PageRequest.of(1,10);
        List<Asset> actual = assetRepository.findByCategory(locationId, category, pageable).getContent();
        assertNotNull(actual);
    }

    @Test
    public void testGetByLocationAndState(){
        int locationId = 1;
        AssetState assetState = AssetState.AVAILABLE;
        Pageable pageable = PageRequest.of(1,10);
        List<Asset> actual = assetRepository.findByState(locationId, assetState.toString(), pageable).getContent();
        assertNotNull(actual);
    }

    @Test
    public void testGetByCategoryAndState(){
        int locationId = 1;
        AssetState assetState = AssetState.AVAILABLE;
        int category = 1;
        Pageable pageable = PageRequest.of(1,10);
        List<Asset> actual = assetRepository.findByCategoryAndState(locationId,category,assetState.toString(),pageable).getContent();
        assertNotNull(actual);
    }

    @Test
    public void testGetBySearchAndCategory(){
        int locationId = 1;
        String searchTerm = "laptop";
        int category = 2;
        Pageable pageable = PageRequest.of(1,10);
        List<Asset> actual = assetRepository.findByCategoryAndSearch(locationId,searchTerm,category,pageable).getContent();
        assertNotNull(actual);
    }

    @Test
    public void testGetBySearchAndState(){
        int locationId = 1;
        String searchTerm = "laptop";
        AssetState assetState = AssetState.AVAILABLE;
        Pageable pageable = PageRequest.of(1,10);
        List<Asset> actual = assetRepository.findByStateAndSearch(locationId,searchTerm,assetState.toString(),pageable).getContent();
        assertNotNull(actual);
    }

    @Test
    public void testGetBySearchAndCategoryAndState(){
        int locationId = 1;
        String searchTerm = "laptop";
        int category = 2;
        AssetState assetState = AssetState.AVAILABLE;
        Pageable pageable = PageRequest.of(1,10);
        List<Asset> actual = assetRepository.findAll(locationId, searchTerm,category,assetState.toString(),pageable).getContent();
        assertNotNull(actual);
    }

    @Test
    public void testGetById(){
        String id = "LA001";
        Asset asset = assetRepository.findByAssetId(id).orElseThrow(() ->new NotFoundException("Asset with assetId: " + id + " Not Found"));
        assertTrue(id.equalsIgnoreCase(asset.getAssetId()));
    }

    @Test
    public void testGetQuantity(){
        Integer categoryId = 1;
        String state = "AVAILABLE";
        Integer locationId = 1;
        Integer quantity = assetRepository.getQuantity(categoryId,state,locationId).orElse(0);
        assertTrue(quantity >= 0);
    }

    @Test
    public void testHistoryAsset(){
        String assetId = "LA008";
        Integer history =assetRepository.getHistoryAsset(assetId).orElse(0);
        assertTrue(history >= 0);
    }

}
