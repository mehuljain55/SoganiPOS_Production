package com.Soganis.Service;

import com.Soganis.Entity.*;
import com.Soganis.Model.*;
import com.Soganis.Repository.ItemListRepository;
import com.Soganis.Repository.ItemsRepository;
import com.Soganis.Repository.PurchaseOrderBookRepo;
import com.Soganis.Repository.SchoolRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;

@Service
public class InventoryService {

    @Autowired
    private ItemsRepository itemRepo;

    @Autowired
    private PurchaseOrderBookRepo purchaseOrderRepo;

    @Autowired
    private SchoolRepository schoolRepo;

    @Autowired
    private ItemListRepository itemListRepo;

    public String updateInventory(BillingModel billing, String storeId) {

        if (billing.getItemBarcodeID().equals("SG9999999")) {
            return "Success";
        }

        try {

            Items item = itemRepo.getItemByItemBarcodeID(billing.getItemBarcodeID(), storeId);

            if (item.getGroup_id().equals("NA")) {
                int item_sold = billing.getQuantity();
                int updatedInventoryQuantity = item.getQuantity() - item_sold;

                if (updatedInventoryQuantity <= 0) {
                    updatedInventoryQuantity = 0;
                }
                item.setQuantity(updatedInventoryQuantity);
                itemRepo.save(item);
                return "Success";
            } else {

                List<Items> groupList = itemRepo.findItemsByGroupId(item.getGroup_id());
                for (Items sale : groupList) {
                    int item_sold = billing.getQuantity();
                    int updatedInventoryQuantity = item.getQuantity() - item_sold;

                    if (updatedInventoryQuantity <= 0) {
                        updatedInventoryQuantity = 0;
                    }
                    sale.setQuantity(updatedInventoryQuantity);
                    itemRepo.save(sale);

                }
                return "Success";

            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    public String updateStock(String itemCode, int qty, String storeId) {
        try {
            Items item = itemRepo.findItemsByItemCode(itemCode, storeId);
            int quantity = item.getQuantity() + qty;
            item.setQuantity(quantity);
            itemRepo.save(item);
            return "Inventory Updated";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to update inventory duplicate entery or item not found";
        }
    }

    public String updateInterCompanyInventory(BillingModel billing, String storeId) {

        if (billing.getItemBarcodeID().equals("SG9999999")) {
            return "Success";
        }

        try {

            Items item = itemRepo.getItemByItemBarcodeID(billing.getItemBarcodeID(), storeId);

            if (item.getGroup_id().equals("NA")) {
                int item_sold = billing.getQuantity();
                int updatedInventoryQuantity = item.getQuantity() - item_sold;

                if (updatedInventoryQuantity <= 0) {
                    updatedInventoryQuantity = 0;
                }
                item.setQuantity(updatedInventoryQuantity);
                itemRepo.save(item);
                return "Success";
            } else {

                List<Items> groupList = itemRepo.findItemsByGroupId(item.getGroup_id());
                for (Items sale : groupList) {
                    int item_sold = billing.getQuantity();
                    int updatedInventoryQuantity = item.getQuantity() - item_sold;

                    if (updatedInventoryQuantity <= 0) {
                        updatedInventoryQuantity = 0;
                    }
                    sale.setQuantity(updatedInventoryQuantity);
                    itemRepo.save(sale);

                }
                return "Success";

            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    public String generate_order(String barcodedId, String storeId) {
        try {
            Items item = itemRepo.getItemByItemBarcodeID(barcodedId, storeId);
            PurchaseOrderBook order = new PurchaseOrderBook();
            String description = item.getItemCategory() + " " + item.getItemType();
            int currentStock = item.getQuantity();
            order.setDescription(description);
            order.setBarcodedId(barcodedId);
            order.setItemType(item.getItemType());
            order.setSchool(item.getItemCategory());
            order.setDate(new Date());
            order.setSize(item.getItemSize());
            order.setColor(item.getItemColor());
            order.setStoreId(storeId);
            order.setStatus("NOT GENERATED");
            order.setCurrentStock(currentStock);
            purchaseOrderRepo.save(order);
            return "Success";

        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    public String inventoryEditModel(List<ItemEditModel> itemEditModelList,String storeId)
    {
        String status="";
        List<SchoolList> schoolList=schoolRepo.findSchoolNameCode(storeId);
        List<ItemList> itemList=itemListRepo.findItemListByStoreId(storeId);

        try{
            for(ItemEditModel itemEditModel:itemEditModelList) {
                try {
                    Items item = itemRepo.getItemByItemBarcodeID(itemEditModel.getItemBarcodeID(), storeId);
                    if (item == null) {
                        status = status + "Item not found " + itemEditModel.getItemCode() + "\n";
                        continue;
                    }
                    item.setItemCode(itemEditModel.getItemCode());
                    item.setItemName(itemEditModel.getItemName());
                    item.setDescription(itemEditModel.getDescription());
                    item.setItemType(item.getItemType());
                    item.setItemColor(item.getItemColor());
                    item.setItemSize(itemEditModel.getItemSize());
                    item.setItemCategory(itemEditModel.getItemCategory());
                    String price = item.getPrice() + "";
                    String wholeSalePrice = itemEditModel.getWholeSalePrice() + "";
                    item.setPrice(price);
                    item.setWholeSalePrice(wholeSalePrice);
                    item.setQuantity(itemEditModel.getQuantity());
                    item.setStoreId(storeId);
                    String schoolCode = findMatchingSchoolCode(schoolList, itemEditModel.getItemCategory());
                    item.setSchoolCode(schoolCode);
                    String itemTypeCode = findMatchingItemTypeCode(itemList, itemEditModel.getItemType());
                    item.setItemTypeCode(itemTypeCode);
                    itemRepo.save(item);
                }catch (Exception e)
                {
                    e.printStackTrace();
                    status=status+"Unable to update item(dupliate entry or invalid price format):"+itemEditModel.getItemCode()+ "\n";
                }
            }
            status=status+"All item successfully updates"+"\n";
            return  status;

        }catch (Exception e)
        {
            e.printStackTrace();
            return status=status+" Exception "+"\n";
        }

    }

    public String findMatchingSchoolCode(List<SchoolList> schoolList, String search) {
        search = search.toLowerCase();

        // First check for exact match
        for (SchoolList school : schoolList) {
            if (school.getSchoolName().equalsIgnoreCase(search)) {
                return school.getSchoolCode();
            }
        }

        // Check for partial matches
        SchoolList bestMatch = null;
        int bestMatchPosition = Integer.MAX_VALUE;

        for (SchoolList school : schoolList) {
            String schoolName = school.getSchoolName().toLowerCase();
            int index = schoolName.indexOf(search);

            // If the search string is part of schoolName, prioritize by position of match
            if (index != -1 && index < bestMatchPosition) {
                bestMatch = school;
                bestMatchPosition = index;
            }
        }

        // Return the schoolCode of the best partial match
        return (bestMatch != null) ? bestMatch.getSchoolCode() : null;
    }

    public List<ItemEditModel> updateInventory(MultipartFile file, String storeId) {
        List<ItemEditModel> items = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                // Skip the header row
                if (row.getRowNum() == 0) {
                    continue;
                }

                String barcodeId = getCellValue(row, 0);
                String itemCode = getCellValue(row, 1);

                // Skip rows with missing barcodeId or itemCode
                if (barcodeId == null || itemCode == null) {
                    continue;
                }

                ItemEditModel item = new ItemEditModel();
                item.setItemBarcodeID(barcodeId);
                item.setItemCode(itemCode);
                item.setItemName(getCellValue(row, 2));
                item.setDescription(getCellValue(row, 3));
                item.setItemType(getCellValue(row, 4));
                item.setItemColor(getCellValue(row, 5));
                item.setItemSize(getCellValue(row, 6));
                item.setItemCategory(getCellValue(row, 7));

                // Handle potential number parsing exceptions
                try {
                    item.setPrice(Integer.parseInt(getCellValue(row, 8)));
                    item.setWholeSalePrice(Integer.parseInt(getCellValue(row, 9)));
                    item.setQuantity(Integer.parseInt(getCellValue(row, 10)));
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing numeric values on row " + row.getRowNum() + ": " + e.getMessage());
                    continue; // Skip this row if there's a parsing error
                }

                item.setStoreId(storeId);
                items.add(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return items;
    }

    private String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }

    public String purchase_order(PurchaseOrderBook purchaseOrderBook, String storeId) {
        try {
            int currentStock = 0;
            purchaseOrderBook.setCurrentStock(currentStock);
            purchaseOrderBook.setStatus("NOT GENERATED");
            purchaseOrderBook.setDate(new Date());
            purchaseOrderBook.setStoreId(storeId);
            purchaseOrderRepo.save(purchaseOrderBook);
            return "Success";

        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }



    public List<PurchaseOrderBook> view_order(String storeId) {
        List<PurchaseOrderBook> lst = purchaseOrderRepo.findItemsWithStatusNotGenerated(storeId);
        return lst;
    }

    public String deletePurchaseOrder(int orderId) {
        try {
            Optional<PurchaseOrderBook> opt = purchaseOrderRepo.findById(orderId);
            if (opt.isPresent()) {
                PurchaseOrderBook order = opt.get();
                purchaseOrderRepo.deleteById(orderId);

            }
            return "Success";

        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    public String updateOrder(List<PurchaseOrderBook> orders, String storeId) {
        for (PurchaseOrderBook order : orders) {
            order.setStatus("GENERATED");
            order.setStoreId(storeId);
            purchaseOrderRepo.save(order);
        }
        return "Success";
    }

    public String addItemsInventory(List<ItemAddModel> itemList, String storeId) {
        for (ItemAddModel itemAddModel : itemList) {
            Items item = itemRepo.getItemByItemBarcodeID(itemAddModel.getBarcodedId(), storeId);
            int total_quantity = item.getQuantity() + itemAddModel.getQuantity();
            item.setQuantity(total_quantity);
            itemRepo.save(item);
        }
        return "Success";
    }

    public String addItemStock(List<ItemAddStockModel> itemList, String storeId) {
        System.out.println("Store Id:" + storeId);
        try {
            for (ItemAddStockModel itemModel : itemList) {
                Items itemAddModel = new Items();
                Items item = itemRepo.save(itemAddModel);
                String barcodeId = generateBarcode(item.getSno());
                item.setItemBarcodeID(barcodeId);
                item.setItemCode(itemModel.getItemCode());
                item.setItemName(itemModel.getItemName());
                item.setItemCategory(itemModel.getItemCategory());
                item.setItemColor(itemModel.getItemColor());
                item.setItemSize(itemModel.getItemSize());
                item.setItemType(itemModel.getItemType());
                item.setPrice(itemModel.getPrice());
                item.setWholeSalePrice(itemModel.getWholeSalePrice());
                String schoolCode = schoolRepo.findSchoolCodeBySchoolName(item.getItemCategory(), storeId);
                String itemTypeCode = itemListRepo.findItemTypeCodeByDescription(item.getItemType(), storeId);
                item.setQuantity(itemModel.getQuantity());
                item.setDescription(itemModel.getDescription());
                item.setSchoolCode(schoolCode);
                item.setItemTypeCode(itemTypeCode);
                if (itemModel.getGroupId() == null) {
                    item.setGroup_id("NA");
                } else if (itemModel.getGroupId().equals("")) {
                    System.out.println("Group Id");
                    item.setGroup_id("NA");
                } else {
                    String groupId = itemModel.getGroupId() + item.getItemSize();
                    item.setGroup_id(groupId);
                }

                item.setStoreId(storeId);
                System.out.println(item);
                itemRepo.save(item);
            }

            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }

    public List<String> school_list(String storeId) {
        List<String> school_list = schoolRepo.findSchoolList(storeId);
        return school_list;
    }

    public List<String> item_list(String storeId) {
        List<String> item_list = itemListRepo.findItemType(storeId);
        return item_list;
    }

    public String generateBarcode(int id) {

        String prefix = "SG";
        String idStr = String.format("%09d", id);
        String barcodeId = prefix + idStr;

        return barcodeId;
    }

    public String checkItemCode(String itemCode, String storeId) {
        List<Items> items = itemRepo.checkItemCodeForNewItem(itemCode, storeId);

        if (items == null) {
            return "New";
        } else if (items.size() == 0) {
            return "New";
        } else {
            return "Exists";
        }

    }

    public String generateInventoryExcel(User user) throws IOException {

        List<String> itemList = itemRepo.findDistinctItemTypes(user.getStoreId());

        for (String item : itemList) {

            if (item.equals("CUSTOM")) {
                continue;
            }

            String item_code = itemRepo.findDistinctItemTypeCode(item, user.getStoreId());

            List<String> itemSizes = getSortedItemSizes(itemRepo.findDistinctItemSizeByItemType(item, user.getStoreId()));
            List<String> itemCategory = getSortedItemCategory(itemRepo.findDistinctSchoolByType(item, user.getStoreId()));
            List<ItemAddInventoryModel> itemAddList = new ArrayList<>();

            for (String school : itemCategory) {
                List<String> itemColorList = itemRepo.findDistinctItemColor(school, item);
                for (String itemColor : itemColorList) {
                    String schoolCode = itemRepo.findDistinctSchoolCode(school);
                    ItemAddInventoryModel itemModel = new ItemAddInventoryModel(schoolCode, item, itemColor);
                    itemAddList.add(itemModel);
                }

            }

            exportExcelInventoryFormat(item, itemAddList, itemSizes);

        }
        return "Success";
    }

    public String exportExcelInventoryFormat(String itemType, List<ItemAddInventoryModel> itemList, List<String> itemSize) {
        String filePath = "C:\\Users\\mehul\\Desktop\\New folder\\" + itemType + ".xlsx";

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Inventory");
        Row schoolRow = sheet.createRow(0);
        Row itemCodeRow = sheet.createRow(1);
        Row colorRow = sheet.createRow(2);

        schoolRow.createCell(0).setCellValue("School");
        itemCodeRow.createCell(0).setCellValue("Item Code");
        colorRow.createCell(0).setCellValue("Color");

        for (int i = 0; i < itemList.size(); i++) {
            ItemAddInventoryModel item = itemList.get(i);
            schoolRow.createCell(i + 1).setCellValue(item.getSchoolCode());
            itemCodeRow.createCell(i + 1).setCellValue(item.getItemCode());
            colorRow.createCell(i + 1).setCellValue(item.getItemColor());
        }
        for (int i = 0; i < itemSize.size(); i++) {
            Row sizeRow = sheet.createRow(i + 3);  // Start from row 4 (0-indexed, so i+3)
            sizeRow.createCell(0).setCellValue(itemSize.get(i)); // Sizes in A4, A5, A6...
        }

        for (int i = 0; i <= itemList.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        File directory = new File(filePath).getParentFile();
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                return "Failed to create directory: " + directory.getAbsolutePath();
            }
        }

        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error during export: " + e.getMessage();
        }

        return "Excel exported successfully to " + filePath;
    }

    public List<ItemModel> inventory_quantity_update(MultipartFile file, String storeId) throws IOException {
        List<ItemModel> itemList = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0); // Assuming the first sheet

            // Find the total number of rows and columns
            Row firstRow = sheet.getRow(0);
            int totalColumns = firstRow.getPhysicalNumberOfCells();
            int totalRows = sheet.getPhysicalNumberOfRows();

            // Row 1 contains school codes, row 2 contains item codes, row 3 contains colors
            Row schoolCodeRow = sheet.getRow(0); // School codes (B1, C1, D1...)
            Row itemCodeRow = sheet.getRow(1);   // Item codes (B2, C2, D2...)
            Row colorRow = sheet.getRow(2);      // Colors (B3, C3, D3...)

            // Loop through columns starting from the second column (B, C, D...)
            for (int col = 1; col < totalColumns; col++) {
                String schoolCode = schoolCodeRow.getCell(col).getStringCellValue();
                String itemCode = itemCodeRow.getCell(col).getStringCellValue();
                String color = colorRow.getCell(col).getStringCellValue();

                // Loop through each row from Row 4 onwards (A4...An for sizes, B4...Bn for quantities)
                for (int rowIdx = 3; rowIdx < totalRows; rowIdx++) {
                    Row currentRow = sheet.getRow(rowIdx);
                    if (currentRow == null) {
                        continue;
                    }

                    // Column A contains sizes
                    Cell sizeCell = currentRow.getCell(0);
                    if (sizeCell == null || sizeCell.getCellType() != CellType.STRING) {
                        continue;
                    }
                    String size = sizeCell.getStringCellValue();

                    // Column col (B, C, D...) contains quantities
                    Cell quantityCell = currentRow.getCell(col);
                    if (quantityCell != null && quantityCell.getCellType() == CellType.NUMERIC) {
                        int quantity = (int) quantityCell.getNumericCellValue();

                        // Create ItemModel and map the data
                        ItemModel item = new ItemModel(schoolCode, itemCode, size, color);
                        item.setQuantity(quantity);
                        itemList.add(item);
                    }
                }
            }
        }

        return itemList;
    }

    public String findMatchingItemTypeCode(List<ItemList> itemList, String descriptionSearch) {
        descriptionSearch = descriptionSearch.toLowerCase();

        // First check for exact match
        for (ItemList item : itemList) {
            if (item.getDescription().equalsIgnoreCase(descriptionSearch)) {
                return item.getItemTypeCode();
            }
        }

        // Check for partial matches
        ItemList bestMatch = null;
        int bestMatchPosition = Integer.MAX_VALUE;

        for (ItemList item : itemList) {
            String description = item.getDescription().toLowerCase();
            int index = description.indexOf(descriptionSearch);

            // If description contains search string, prioritize by position of match
            if (index != -1 && index < bestMatchPosition) {
                bestMatch = item;
                bestMatchPosition = index;
            }
        }

        // Return the itemTypeCode of the best partial match
        return (bestMatch != null) ? bestMatch.getItemTypeCode() : null;
    }


    public List<String> getSortedItemSizes(List<String> itemSizes) {
        return itemSizes.stream()
                .sorted(Comparator.comparing((String s) -> isNumeric(s) ? 0 : 1)
                        .thenComparing(s -> isNumeric(s) ? Integer.parseInt(s) : 0)
                        .thenComparing(s -> s))
                .collect(Collectors.toList());
    }

    public List<String> getSortedItemCategory(List<String> itemCategory) {
        // Sort the list alphabetically
        Collections.sort(itemCategory);
        return itemCategory;
    }

// Helper method to check if a string is numeric
    private boolean isNumeric(String str) {
        return str != null && str.matches("\\d+");
    }

    public String inventoryUpdate(List<ItemModel> itemModelList) {
        String status = "";


        for (ItemModel itemModel : itemModelList) {
            System.out.println(itemModel);
            try {
                Items item = itemRepo.findItemInventoryUpdate(
                        itemModel.getSchoolCode(),
                        itemModel.getItemCode(),
                        itemModel.getSize(),
                        itemModel.getItemColor(),
                        itemModel.getStoreId()
                );

                if (item != null) {
                    int qty = item.getQuantity();
                    int stock = qty + itemModel.getQuantity();
                    item.setQuantity(stock);
                    itemRepo.save(item);
                } else {
                    status = status + itemModel.getItemCode() + " " + itemModel.getSchoolCode() + " " + itemModel.getSize() + " " + itemModel.getItemColor() + " not found" + "\n";
                }
            } catch (Exception e) {
                status = status + itemModel.getItemCode() + " " + itemModel.getSchoolCode() + " " + itemModel.getSize() + " " + itemModel.getItemColor() + "exception duplicate entry or item not present" + "\n";
            }

        }
        System.out.println(status);
        return status;
    }
}
