package com.nt.rookies.assets.services;

import com.nt.rookies.assets.dtos.ReportResourceDto;
import com.nt.rookies.assets.dtos.ReportResponseDto;
import com.nt.rookies.assets.exceptions.BadRequestException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class ReportExcelService {

    private ReportService reportService;

    public ReportExcelService(ReportService reportService) {
        this.reportService = Objects.requireNonNull(reportService);
    }

    public ReportResourceDto exportReport(Integer locationId) {
        List<ReportResponseDto> report = reportService.reportAsset(locationId);
        Resource resource = prepareExcel(report);
        return ReportResourceDto.builder().resource(resource).
                mediaType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).build();
    }

    private Resource prepareExcel(List<ReportResponseDto> report) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Asset Report");

        prepareHeaders(workbook, sheet, "Category", "Total", "Assigned",
                "Available", "Not Available", "Waiting For Recycling", "Recycled");
        populateData(workbook, sheet, report);

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            workbook.write(byteArrayOutputStream);
            return new ByteArrayResource(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            throw new BadRequestException("Error while generating excel");
        }
    }

    private void prepareHeaders(Workbook workbook, Sheet sheet, String... headers) {

        Row headerRow = sheet.createRow(0);
        Font font = workbook.createFont();
        font.setBold(true);

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);

        int columnNo = 0;
        for (String header : headers) {
            Cell headerCell = headerRow.createCell(columnNo++);
            headerCell.setCellValue(header);
            headerCell.setCellStyle(cellStyle);
        }
    }

    private void populateCell(Sheet sheet, Row row, int columnNo, String value, CellStyle cellStyle) {

        Cell cell = row.createCell(columnNo);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(value);
        sheet.autoSizeColumn(columnNo);
    }

    private void populateData(Workbook workbook, Sheet sheet, List<ReportResponseDto> reportResponseList) {

        int rowNo = 1;
        Font font = workbook.createFont();

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);

        for (ReportResponseDto response : reportResponseList) {
            int columnNo = 0;
            Row row = sheet.createRow(rowNo);
            populateCell(sheet, row, columnNo++,
                    String.valueOf(response.getCategoryDto().getName()), cellStyle);
            populateCell(sheet, row, columnNo++,
                    String.valueOf(response.getTotal()), cellStyle);
            populateCell(sheet, row, columnNo++,
                    String.valueOf(response.getAssigned()), cellStyle);
            populateCell(sheet, row, columnNo++,
                    String.valueOf(response.getAvailable()), cellStyle);
            populateCell(sheet, row, columnNo++,
                    String.valueOf(response.getNotAvailable()), cellStyle);
            populateCell(sheet, row, columnNo++,
                    String.valueOf(response.getWaitingForRecycling()), cellStyle);
            populateCell(sheet, row, columnNo++,
                    String.valueOf(response.getRecycled()), cellStyle);

            rowNo++;
        }
    }

}
