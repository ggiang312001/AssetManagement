package com.nt.rookies.assets.services;

import com.nt.rookies.assets.dtos.CategoryDto;
import com.nt.rookies.assets.dtos.ReportRequestDto;
import com.nt.rookies.assets.dtos.ReportResponse;
import com.nt.rookies.assets.dtos.ReportResponseDto;
import com.nt.rookies.assets.entities.AssetState;
import com.nt.rookies.assets.mappers.CategoryMapper;
import com.nt.rookies.assets.repositories.AssetRepository;
import com.nt.rookies.assets.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public ReportService(AssetRepository assetRepository, CategoryRepository categoryRepository) {
        this.assetRepository = assetRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<ReportResponseDto> reportAsset(Integer locationId){
        List<ReportResponseDto> listReportResponseDtos = new ArrayList<>();
        List<CategoryDto> categoryDtoList = CategoryMapper.toDtoList(categoryRepository.findAll());

        for (int i = 0; i < categoryDtoList.size(); i++) {
            ReportResponseDto reportResponseDto = new ReportResponseDto();
            CategoryDto categoryDto = CategoryMapper.toDto(categoryRepository.findById(categoryDtoList.get(i).getCategoryId()));
            reportResponseDto.setCategoryDto(categoryDto);

            Integer assigned = assetRepository.getQuantity(categoryDtoList.get(i).getCategoryId(), AssetState.ASSIGNED.toString(), locationId).orElse(0);
            Integer available = assetRepository.getQuantity(categoryDtoList.get(i).getCategoryId(), AssetState.AVAILABLE.toString(), locationId).orElse(0);
            Integer notAvailable = assetRepository.getQuantity(categoryDtoList.get(i).getCategoryId(), AssetState.NOT_AVAILABLE.toString(), locationId).orElse(0);
            Integer waitingForRecycling = assetRepository.getQuantity(categoryDtoList.get(i).getCategoryId(), AssetState.WAITING_FOR_RECYCLING.toString(), locationId).orElse(0);
            Integer recycled = assetRepository.getQuantity(categoryDtoList.get(i).getCategoryId(), AssetState.RECYCLED.toString(), locationId).orElse(0);
            Integer total = assigned + available + notAvailable + waitingForRecycling + recycled;

            reportResponseDto.setTotal(total);
            reportResponseDto.setAssigned(assigned);
            reportResponseDto.setAvailable(available);
            reportResponseDto.setNotAvailable(notAvailable);
            reportResponseDto.setWaitingForRecycling(waitingForRecycling);
            reportResponseDto.setRecycled(recycled);

            listReportResponseDtos.add(reportResponseDto);
        }
        return listReportResponseDtos;
    }

    public ReportResponse viewReportAsset(ReportRequestDto reportRequestDto, Integer locationId){

        String sortBy = reportRequestDto.getSortBy();
        String sortDir = reportRequestDto.getSortDir();
        Integer pageNo = reportRequestDto.getPageNo();
        Integer pageSize = reportRequestDto.getPageSize();

        List<ReportResponseDto> listAll = reportAsset(locationId);

        List<ReportResponseDto> listSort = new ArrayList<>();
        if(sortBy.equalsIgnoreCase("category")){
            if(sortDir.equalsIgnoreCase("desc")){
                listSort = listAll.stream().sorted((o1,o2)->o2.getCategoryDto().getName().compareTo(o1.getCategoryDto().getName())).collect(Collectors.toList());
            }
            if(sortDir.equalsIgnoreCase("asc")){
                listSort = listAll.stream().sorted(Comparator.comparing(o -> o.getCategoryDto().getName())).collect(Collectors.toList());
            }
        }
        if(sortBy.equalsIgnoreCase("total")){
            if(sortDir.equalsIgnoreCase("desc")){
                listSort = listAll.stream().sorted(Comparator.comparingInt(ReportResponseDto::getTotal).reversed()).collect(Collectors.toList());
            }
            if(sortDir.equalsIgnoreCase("asc")){
                listSort = listAll.stream().sorted(Comparator.comparingInt(ReportResponseDto::getTotal)).collect(Collectors.toList());
            }
        }
        if(sortBy.equalsIgnoreCase("assigned")){
            if(sortDir.equalsIgnoreCase("desc")){
                listSort = listAll.stream().sorted(Comparator.comparingInt(ReportResponseDto::getAssigned).reversed()).collect(Collectors.toList());
            }
            if(sortDir.equalsIgnoreCase("asc")){
                listSort = listAll.stream().sorted(Comparator.comparingInt(ReportResponseDto::getAssigned)).collect(Collectors.toList());
            }
        }
        if(sortBy.equalsIgnoreCase("available")){
            if(sortDir.equalsIgnoreCase("desc")){
                listSort = listAll.stream().sorted(Comparator.comparingInt(ReportResponseDto::getAvailable).reversed()).collect(Collectors.toList());
            }
            if(sortDir.equalsIgnoreCase("asc")){
                listSort = listAll.stream().sorted(Comparator.comparingInt(ReportResponseDto::getAvailable)).collect(Collectors.toList());
            }
        }
        if(sortBy.equalsIgnoreCase("notAvailable")){
            if(sortDir.equalsIgnoreCase("desc")){
                listSort = listAll.stream().sorted(Comparator.comparingInt(ReportResponseDto::getNotAvailable).reversed()).collect(Collectors.toList());
            }
            if(sortDir.equalsIgnoreCase("asc")){
                listSort = listAll.stream().sorted(Comparator.comparingInt(ReportResponseDto::getNotAvailable)).collect(Collectors.toList());
            }
        }
        if(sortBy.equalsIgnoreCase("waitingForRecycling")){
            if(sortDir.equalsIgnoreCase("desc")){
                listSort = listAll.stream().sorted(Comparator.comparingInt(ReportResponseDto::getWaitingForRecycling).reversed()).collect(Collectors.toList());
            }
            if(sortDir.equalsIgnoreCase("asc")){
                listSort = listAll.stream().sorted(Comparator.comparingInt(ReportResponseDto::getWaitingForRecycling)).collect(Collectors.toList());
            }
        }
        if(sortBy.equalsIgnoreCase("recycled")){
            if(sortDir.equalsIgnoreCase("asc")){
                listSort = listAll.stream().sorted(Comparator.comparingInt(ReportResponseDto::getRecycled).reversed()).collect(Collectors.toList());
            }
            if(sortDir.equalsIgnoreCase("desc")){
                listSort = listAll.stream().sorted(Comparator.comparingInt(ReportResponseDto::getRecycled)).collect(Collectors.toList());
            }
        }
        List<ReportResponseDto> listPaging = listSort.subList((pageNo-1)*pageSize, Math.min(listSort.size(), (pageNo-1)*pageSize+pageSize) );
        ReportResponse result = new ReportResponse();
        result.setContent(listPaging);
        result.setPageNo(pageNo);
        result.setPageSize(pageSize);
        result.setTotalElements(listAll.size());
        return  result;
    }

}
