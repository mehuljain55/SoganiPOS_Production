package com.Soganis.Controller;

import com.Soganis.Model.SalesReportModel;
import com.Soganis.Service.SalesReportService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/sales")
public class SalesController {

    @Autowired
    private SalesReportService service;

    @GetMapping("/report/school_code_and_date")
    public ResponseEntity<List<SalesReportModel>> saleReportBySchoolAndDate(
            @RequestParam String schoolCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam("storeId") String storeId) {
        {
            System.out.println("School and Date");
            List<SalesReportModel> billing = service.getSaleBySchoolAndDate(schoolCode, startDate, endDate,storeId);

            if (billing.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(billing, HttpStatus.OK);
            }
        }
    }

    @GetMapping("/report/school_item_type_date")
    public ResponseEntity<List<SalesReportModel>> saleReportBySchoolAndItemTypeDate(
            @RequestParam("itemCode") String itemCode,
            @RequestParam("schoolCode") String schoolCode,
            @RequestParam("storeId") String storeId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        {

            System.out.println("All Filter");

            List<SalesReportModel> billing = service.getSaleBySchoolAndItemTypeDate(itemCode, schoolCode, startDate, endDate,storeId);

            if (billing.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {

                return new ResponseEntity<>(billing, HttpStatus.OK);
            }
        }
    }

    @GetMapping("/report/school_code")
    public ResponseEntity<List<SalesReportModel>> saleReportBySchool(
            @RequestParam("schoolCode") String schoolCode,
            @RequestParam("storeId") String storeId) {
        {

            System.out.println("School");

            List<SalesReportModel> billing = service.getSaleBySchool(schoolCode,storeId);

            if (billing.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {

                return new ResponseEntity<>(billing, HttpStatus.OK);
            }
        }
    }

    @GetMapping("/report/item_code")
    public ResponseEntity<List<SalesReportModel>> saleReportByItemType(@RequestParam("itemCode") String itemCode,@RequestParam("storeId") String storeId) {
        {

            System.out.println("Item");

            List<SalesReportModel> billing = service.getSaleByItemType(itemCode,storeId);

            if (billing.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(billing, HttpStatus.OK);
            }
        }

    }

    @GetMapping("/report/sales_date")
    public ResponseEntity<List<SalesReportModel>> saleReportByCustomDate(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam("storeId") String storeId) {
        {

            System.out.println("Custom Date");
            List<SalesReportModel> billing = service.getSaleByDateRange(startDate, endDate,storeId);

            if (billing.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(billing, HttpStatus.OK);
            }
        }
    }

    @GetMapping("/report/school_and_item_type")
    public ResponseEntity<List<SalesReportModel>> saleReportBySchoolAndItemType(
            @RequestParam("schoolCode") String schoolCode,
            @RequestParam("itemCode") String itemCode,
            @RequestParam("storeId") String storeId) {
        {

            System.out.println("School and Item");
            List<SalesReportModel> billing = service.getSaleBySchoolAndItemType(itemCode, schoolCode,storeId);
            if (billing.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {

                return new ResponseEntity<>(billing, HttpStatus.OK);
            }
        }
    }

    @GetMapping("/report/item_code_and_date")
    public ResponseEntity<List<SalesReportModel>> saleReportByItemTypeDate(
            @RequestParam("itemCode") String itemCode,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam("storeId") String storeId) {
        {

            System.out.println("Item and Date");
            List<SalesReportModel> billing = service.getSaleByItemTypeAndDate(itemCode, startDate, endDate,storeId);

            if (billing.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(billing, HttpStatus.OK);
            }
        }
    }
    


    @PostMapping("/export")
    public ResponseEntity<byte[]> exportSalesReport(@RequestBody List<SalesReportModel> salesData) throws IOException {

        int totalSale = 0;

        // Calculate the total amount sum
        for (SalesReportModel sales : salesData) {
            totalSale += sales.getTotalAmount();
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sales Report");

        // Create a bold font for the header and total amount cells
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);

        // Create a font for the title
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 25); // Set font size to 25

        // Create cell styles
        CellStyle boldStyle = workbook.createCellStyle();
        boldStyle.setFont(boldFont);

        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFont(titleFont);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        CellStyle rightAlignedBoldStyle = workbook.createCellStyle();
        rightAlignedBoldStyle.setFont(boldFont);
        rightAlignedBoldStyle.setAlignment(HorizontalAlignment.RIGHT);

        CellStyle borderStyle = workbook.createCellStyle();
        BorderStyle border = BorderStyle.THIN;
        borderStyle.setBorderTop(border);
        borderStyle.setBorderBottom(border);
        borderStyle.setBorderLeft(border);
        borderStyle.setBorderRight(border);

        // Create a title row
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Sales Report");
        titleCell.setCellStyle(titleStyle);

        // Merge all columns for the title
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6)); // Adjust the end column based on the number of columns

        // Create header row
        Row headerRow = sheet.createRow(1);
        String[] headers = {"Item Barcode ID", "Description", "Item Type", "Item Color", "Size", "Sell Price", "Quantity", "Amount"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(boldStyle);
            cell.setCellStyle(borderStyle); // Apply border style to header cells
        }

        // Populate data rows
        int rowNum = 2;
        for (SalesReportModel item : salesData) {
            Row row = sheet.createRow(rowNum++);
            Cell[] cells = new Cell[headers.length];
            for (int i = 0; i < headers.length; i++) {
                cells[i] = row.createCell(i);
                // Set cell values
                switch (i) {
                    case 0:
                        cells[i].setCellValue(item.getItemBarcodeID());
                        break;
                    case 1:
                        cells[i].setCellValue(item.getDescription());
                        break;
                    case 2:
                        cells[i].setCellValue(item.getItemType());
                        break;
                    case 3:
                        cells[i].setCellValue(item.getItemColor());
                        break;
                    case 4:
                        cells[i].setCellValue(item.getItemSize());
                        break;
                    case 5:
                        cells[i].setCellValue(item.getSellPrice());
                        break;
                    case 6:
                        cells[i].setCellValue(item.getTotalQuantity());
                        break;
                    case 7:
                        cells[i].setCellValue(item.getTotalAmount());
                        break;
                }
                cells[i].setCellStyle(borderStyle); // Apply border style to data cells
            }
        }

        // Create the total amount row at the end
        Row totalRow = sheet.createRow(rowNum);

        // Merge all cells from the first column to the second-to-last column
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, headers.length - 2));

        // Create the "Total Amount" label in the merged cell with right alignment
        Cell totalLabelCell = totalRow.createCell(0);
        totalLabelCell.setCellValue("Total Amount");
        totalLabelCell.setCellStyle(rightAlignedBoldStyle);

        // Set the total amount value in the last column
        Cell totalAmountCell = totalRow.createCell(headers.length - 1);
        totalAmountCell.setCellValue(totalSale);
        totalAmountCell.setCellStyle(boldStyle);

        // Apply border style to the total row
        for (int i = 0; i < headers.length; i++) {
            Cell cell = totalRow.getCell(i);
            if (cell == null) {
                cell = totalRow.createCell(i);
            }
            cell.setCellStyle(borderStyle);
        }

        // Auto-size columns for better readability
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the Excel data to a ByteArrayOutputStream
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();

        // Create HTTP response
        HttpHeaders headersDownload = new HttpHeaders();
        headersDownload.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headersDownload.setContentDispositionFormData("attachment", "SalesReport.xlsx");

        return ResponseEntity.ok()
                .headers(headersDownload)
                .body(bos.toByteArray());
    }
    
    

}
