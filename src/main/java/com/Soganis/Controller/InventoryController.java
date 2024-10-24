package com.Soganis.Controller;

import com.Soganis.Entity.Items;
import com.Soganis.Entity.PurchaseOrderBook;
import com.Soganis.Entity.User;
import com.Soganis.Model.*;
import com.Soganis.Service.InventoryService;
import com.Soganis.Service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @GetMapping("/getUser")
    public User getUser()
    {
        User user=new User();
        user.setUserId("mridul");
        user.setPassword("abc");
        user.setStoreId("Nx");
        return  user;
    }
    
    @GetMapping("/format")
    public String inventoryFormat(@RequestBody User user) throws IOException
    {
    String status=inventoryService.generateInventoryExcel(user);
    return status;
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


    @PostMapping("/stock/add")
    public String addItemStock(@RequestBody List<ItemAddStockModel> itemModel)
    {
      String status=inventoryService.addItemStock(itemModel);
      return "Status";
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
