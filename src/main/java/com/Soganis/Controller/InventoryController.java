package com.Soganis.Controller;

import com.Soganis.Entity.*;
import com.Soganis.Model.*;
import com.Soganis.Repository.ItemFormatListRepo;
import com.Soganis.Service.InventoryService;
import com.Soganis.Service.ItemService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;


@CrossOrigin(origins = "https://www.soganiuniforms.shop")
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    
      @Autowired
      private InventoryService inventoryService;

      @Autowired
      private ItemService itemService;

      @Autowired
      private ItemFormatListRepo itemFormatListRepo;

    @GetMapping("/getAllItems")
    public ResponseEntity<List<Items>> getAllItems(@RequestParam(required = false) String searchTerm,
                                                   @RequestParam(defaultValue = "20") int maxResults,
                                                   @RequestParam("storeId") String storeId) {
        List<Items> items = itemService.getAllItems(searchTerm, maxResults,storeId);
        if (!items.isEmpty()) {
            return ResponseEntity.ok(items);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
     @GetMapping("/getAllItemCode")
    public ResponseEntity<List<BarcodeModel>> getAllItemsCode(@RequestParam(required = false) String searchTerm,
                                                   @RequestParam("storeId") String storeId) {
        List<BarcodeModel> barcodeModel = itemService.getAllItemCode(searchTerm, 20, storeId);
        if (!barcodeModel.isEmpty()) {
            return ResponseEntity.ok(barcodeModel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
      @PostMapping("/stock_update")
    public ResponseEntity<String> stockUpdate(@RequestBody StockUpdateModel stockUpdateModel) {
      
      String status=inventoryService.updateStock(stockUpdateModel.getItemCode(), stockUpdateModel.getQty(), stockUpdateModel.getStoreId());
        if(status.equals("Inventory Updated"))
        {
            return new ResponseEntity<>(status, HttpStatus.OK);
        }
        else{
              return new ResponseEntity<>(status, HttpStatus.BAD_REQUEST);
        }
    }
    
    
    
      @PostMapping("/generate_order")
    public ResponseEntity<InputStreamResource> generateOrder(@RequestBody PurchaseOrderModel orders) {
        try {
            String status = inventoryService.updateOrder(orders.getPurchaseOrderBookList(),orders.getUser().getStoreId());
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Orders");

            // Create a bold font for header
            Font boldFont = workbook.createFont();
            boldFont.setBold(true);

            // Create a cell style for headers
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(boldFont);

            // Create the header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Sno", "Description", "Color", "Size", "Quantity", "Item Type", "School"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle); // Apply bold style to header
            }

            // Fill data
            int rowNum = 1;
            int count = 1;
            for (PurchaseOrderBook order : orders.getPurchaseOrderBookList()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(count);
                row.createCell(1).setCellValue(order.getDescription());
                row.createCell(2).setCellValue(order.getColor());
                row.createCell(3).setCellValue(order.getSize().toString());
                row.createCell(4).setCellValue(order.getQuantity());
                row.createCell(5).setCellValue(order.getItemType());
                row.createCell(6).setCellValue(order.getSchool());
                count++;
            }

            // Adjust column widths
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write the workbook to a ByteArrayOutputStream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            workbook.close();

            // Convert ByteArrayOutputStream to InputStreamResource
            ByteArrayInputStream bis = new ByteArrayInputStream(baos.toByteArray());
            InputStreamResource resource = new InputStreamResource(bis);

            // Prepare the response with the Excel file
            String filename = "order" + new Random().nextInt(100) + ".xlsx";
            HttpHeaders headers1 = new HttpHeaders();
            headers1.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
            headers1.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
            headers1.setContentLength(baos.size());

            return new ResponseEntity<>(resource, headers1, HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    
    @PostMapping("/order/delete_order")
    public String deletePurchaseOrder(@RequestParam("orderId")int orderId)
    {
        String status=inventoryService.deletePurchaseOrder(orderId);
        return "Success";
    
    }
    
    @PostMapping("/update_inventory")
    public String updateInventory(@RequestBody InventoryUpdateModel inventoryUpdateModel )
    {
    String status=inventoryService.addItemsInventory(inventoryUpdateModel.getItemAddModel(),inventoryUpdateModel.getUser().getStoreId());
    return status;
        
    }

    @PostMapping("/edit")
    public ResponseEntity<byte[]>  updateInventoryList(@RequestBody List<Items> itemsList )
    {
        String status=inventoryService.editInventoryItems(itemsList);

        byte[] content = status.getBytes(StandardCharsets.UTF_8);

        // Set headers to indicate it's a file download
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", "inventory_edit_status.txt");

        // Return the response entity with the content and headers
        return new ResponseEntity<>(content, headers, HttpStatus.OK);

    }



    @PostMapping ("/format")
    public String inventoryFormat(@RequestBody User user) throws IOException
    {
    String status=inventoryService.generateInventoryExcel(user);
    return status;
    }


    @GetMapping("/groupList")
    public List<ItemFormatList> getItemFormatGroupList()
    {
        return inventoryService.itemFormatGroupList();
    }

    @GetMapping("/groupDataList")
    public List<String> groupDataList()
    {
        return inventoryService.groupDataList();
    }




    @PostMapping("/format/groupData")
    public ResponseEntity<byte[]> inventoryFormatGroup(@RequestBody User user, @RequestParam("groupId") int groupId) throws IOException {
        return inventoryService.generateInventoryExcelGroupWise(user, groupId);
    }

    @PostMapping("/format/group/groupData")
    public ResponseEntity<byte[]> inventoryFormatGroupData(@RequestBody User user, @RequestParam("itemType") String itemType) throws IOException {
        return inventoryService.generateInventoryExcelGroupData(user, itemType);
    }




    @GetMapping("/download/stock_add_list")
    public ResponseEntity<byte[]> inventoryFormat() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Add New Item");

        Row headerRow = sheet.createRow(0);
        String[] headers = {"Item Code", "Item Name", "Item Type", "Item Size", "Item Color", "Item Category", "Price", "Wholesale Price", "Quantity", "Barcode Description", "Group Id"};

        // Create header cells and set the header names
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Auto-size the columns based on the header content
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        workbook.close();

        // Create HTTP response
        HttpHeaders headersDownload = new HttpHeaders();
        headersDownload.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headersDownload.setContentDispositionFormData("attachment", "New Item List.xlsx");

        return ResponseEntity.ok()
                .headers(headersDownload)
                .body(bos.toByteArray());
    }
    @PostMapping("/export")
    public ResponseEntity<byte[]> inventoryExportList(@RequestBody List<Items> items) throws IOException {
        // Create a workbook and a spreadsheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Inventory");

        // Header row with bold style
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Barcode Id", "Item Code", "Item Name", "Description", "Item Type", "Item Color",
                "Item Size", "Item Category",  "Price", "Wholesale Price", "Quantity", "Store ID", };

        CellStyle headerCellStyle = workbook.createCellStyle();
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        headerCellStyle.setFont(boldFont);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerCellStyle);  // Apply bold style only to header
        }

        // Populate data rows without specific style
        int rowIndex = 1;
        for (Items item : items) {
            Row row = sheet.createRow(rowIndex++);

            row.createCell(0).setCellValue(item.getItemBarcodeID());
            row.createCell(1).setCellValue(item.getItemCode());
            row.createCell(2).setCellValue(item.getItemName());
            row.createCell(3).setCellValue(item.getDescription());
            row.createCell(4).setCellValue(item.getItemType());
            row.createCell(5).setCellValue(item.getItemColor());
            row.createCell(6).setCellValue(item.getItemSize());
            row.createCell(7).setCellValue(item.getItemCategory());
            row.createCell(8).setCellValue(item.getPrice());
            row.createCell(9).setCellValue(item.getWholeSalePrice());
            row.createCell(10).setCellValue(item.getQuantity());
            row.createCell(11).setCellValue(item.getStoreId());

        }

        // Resize columns to fit content
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write workbook to byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        // Set HTTP response headers
        HttpHeaders headersResponse = new HttpHeaders();
        headersResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headersResponse.setContentDispositionFormData("attachment", "inventory.xlsx");

        return new ResponseEntity<>(outputStream.toByteArray(), headersResponse, HttpStatus.OK);
    }





    @PostMapping("/add")
    public ResponseEntity<List<ItemModel>> uploadExcelFile(@RequestParam("file") MultipartFile file, @RequestParam("storeId") String storeId) {
        if (file.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {

            // Call service to read the Excel file and return list of ItemModel
            List<ItemModel> items = inventoryService.inventory_quantity_update(file,storeId );

            return new ResponseEntity<>(items, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<byte[]> updateInventory(@RequestBody List<ItemModel> itemModelList) {
        String status = inventoryService.inventoryUpdate(itemModelList);

        // Convert status to byte array with UTF-8 encoding
        byte[] content = status.getBytes(StandardCharsets.UTF_8);

        // Set headers to indicate it's a file download
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", "inventory_update_status.txt");

        // Return the response entity with the content and headers
        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }

    @PostMapping("/edit/upload")
    public ResponseEntity<byte[]> inventoryEditExcel(@RequestParam("file") MultipartFile file, @RequestParam("storeId") String storeId) {

        ItemEditModelStatus itemEditModelList=inventoryService.updateInventory(file,storeId);
        String status=inventoryService.inventoryEditModel(itemEditModelList.getItemEditModelList(),itemEditModelList.getStatus(),storeId);


        byte[] content = status.getBytes(StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", "inventory_edit_status.txt");
        return new ResponseEntity<>(content, headers, HttpStatus.OK);

    }


    @PostMapping("/stock/add")
    public String addItemStock(@RequestBody List<ItemAddStockModel> itemModel,@RequestParam("storeId") String storeId)
    {
      String status=inventoryService.addItemStock(itemModel,storeId);
      return status;
    }
    
    
    @GetMapping("/search/school_list")
    public List<String> school_list(@RequestParam("storeId") String storeId)
    {
      List<String> schoolList=inventoryService.school_list(storeId);
      return schoolList;
    }
    
    @GetMapping("/search/item_list")
    public List<String> item_list(@RequestParam("storeId") String storeId)
    {
      List<String> itemList=inventoryService.item_list(storeId);
      return itemList;
    }
    
    @GetMapping("/check/item_code")
    public String check_item_code(@RequestParam("itemCode")String itemCode,@RequestParam("storeId") String storeId)
    {
        String status=inventoryService.checkItemCode(itemCode,storeId);
        return status;
    }


}

