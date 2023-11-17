package com.nt.rookies.assets.services;

import com.nt.rookies.assets.dtos.*;
import com.nt.rookies.assets.entities.Asset;

import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThat;

import com.nt.rookies.assets.entities.AssetState;
import com.nt.rookies.assets.entities.Category;
import com.nt.rookies.assets.entities.Location;
import com.nt.rookies.assets.exceptions.NotFoundException;
import com.nt.rookies.assets.mappers.AssetMapper;
import com.nt.rookies.assets.repositories.AssetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;

import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AssetServiceTest {

    @Mock
    private AssetRepository assetRepository;
    @InjectMocks
    private AssetService assetService;

    @Mock
    private AssetMapper assetMapper;

    private static AssetDto assetDto;
    //private static Asset asset;
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private static String assetId = "LA001";
    private static String name = "laptop Dell HP Probook 450 G4";
    private static String specification = "RAM 4GB, HDD 500GB, Intel Core i5 gen 5";
    private static CategoryDto category = CategoryDto.builder().categoryId(1).name("sampleName").build();
    private static LocalDateTime installedDate = LocalDateTime.of(2022, 02, 02, 02, 02);
    private static LocationDto location = LocationDto.builder().locationId(1).city("sampleName").build();
    private static AssetState State = AssetState.AVAILABLE;

//    @Test
//    public void getByCodeTest() {
//
//        Asset asset = new Asset(assetId, name, specification, new Category(), installedDate, new Location(), AssetState.AVAILABLE, LocalDateTime.now());
//
//        Mockito.when(assetRepository.findByAssetId(assetId).orElseThrow(() ->new NotFoundException("Asset with assetId: " + assetId + " Not Found"))).thenReturn(asset);
//        AssetDto result = assetService.getByCode(assetId);
//
//        Mockito.verify(assetRepository, Mockito.times(1)).findByAssetId(assetId);
//        assertThat(result.getAssetId()).isEqualTo(assetId);
//
//    }

//    @Test
//    public void testAssignmentDeclined(){
//
//        Asset asset = new Asset(assetId, name, specification, new Category(), installedDate, new Location(), AssetState.AVAILABLE, LocalDateTime.now());
//
//        Mockito.when(assetRepository.findByAssetId(assetId).orElseThrow(() ->new NotFoundException("Asset with assetId: " + assetId + " Not Found"))).thenReturn(asset);
//        Mockito.when(assetRepository.save(asset)).thenReturn(asset);
//
//        AssetDto result = assetService.declineAssignment(assetId);
//        System.out.println(result.getState());
//
//        Mockito.verify(assetRepository, Mockito.times(1)).save(asset);
//        assertTrue(result.getState().equals(AssetState.AVAILABLE));
//
//    }

}
