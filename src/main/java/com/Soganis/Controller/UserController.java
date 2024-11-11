package com.Soganis.Controller;

import com.Soganis.Entity.*;
import com.Soganis.Model.BillTransactionModel;
import com.Soganis.Model.BillViewModel;
import com.Soganis.Model.ItemExchangeModel;
import com.Soganis.Model.ItemReturnModel;
import com.Soganis.Service.InventoryService;
import com.Soganis.Service.ItemService;
import com.Soganis.Service.UserService;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import org.springframework.http.MediaType;


@CrossOrigin(origins = "https://www.soganiuniforms.shop")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private ItemService itemService;

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/login")
    public ResponseEntity<User> getUserInfo(@RequestBody User userRequest) {
        System.out.println("User controller accessed");

        String userid = userRequest.getUserId();
        User user = service.getUserInfo(userid);

        if (user != null && user.getPassword().equals(userRequest.getPassword())) {
            System.out.println("User validated");
            return ResponseEntity.ok(user);
        } else {
            System.out.println("Incorrect Credential");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @GetMapping("/getBill/{bill_no}/{storeId}")
    public ResponseEntity<BillViewModel> getBill(@PathVariable String bill_no, @PathVariable String storeId) {
        if(bill_no.length()==10)
        {

            BillViewModel billViewModel=itemService.getBillList(bill_no,storeId);
            return ResponseEntity.ok(billViewModel);
        }

        BillViewModel billViewModel = itemService.getBillByMobileNo(bill_no, storeId);
        if (billViewModel != null) {
            return ResponseEntity.ok(billViewModel);  // Return the bill if found
        } else {
            System.out.println("Data Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Return 404 if not found
        }
    }


    @PostMapping("/return_stock/bill")
    public ResponseEntity<String> stockReturn(@RequestBody List<ItemReturnModel> items) {

        String status = itemService.stockReturn(items);
        String updateCollectionReport=service.updateUserCashCollectionReport();
        return ResponseEntity.ok(status);

    }

    @PostMapping("/stock/defect")
    public ResponseEntity<String> exchangeItems(@RequestBody ItemReturnModel items) {

        String status = itemService.stockDefectReturn(items);

        return ResponseEntity.ok(status);

    }

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Server is running";
    }

    @PostMapping("/billRequest")
    public ResponseEntity<byte[]> generateBill(@RequestBody Billing bill) {
        try {
            String storeId=itemService.getStoreId(bill.getUserId());
            bill.setStoreId(storeId);
            Billing createBill = itemService.saveBilling(bill);
            createBill.setBill(bill.getBill());
            String status=service.updateUserCashCollectionReport();

            byte[] pdfBytes = print_bill(createBill.getBillNo(),storeId);

            if (pdfBytes != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("inline", createBill.getCustomerName() + "_" + createBill.getBillNo() + ".pdf");

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(pdfBytes);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/exchange/billRequest")
    public ResponseEntity<byte[]> generate_bill_exchange(@RequestBody ItemExchangeModel itemModel) {
        try {
            Billing bill = itemModel.getBill();
            Billing createBill = itemService.saveBillExchange(bill, itemModel.getItemModel());
            String storeId=itemService.getStoreId(itemModel.getUser().getUserId());
            createBill.setBill(bill.getBill());
            String status=service.updateUserCashCollectionReport();
            byte[] pdfBytes = print_bill(createBill.getBillNo(),storeId);

            if (pdfBytes != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("inline", createBill.getCustomerName() + "_" + createBill.getBillNo() + ".pdf");

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(pdfBytes);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/intercompany/exchange/billRequest")
    public ResponseEntity<byte[]> generate_intercompany_bill_exchange(@RequestBody ItemExchangeModel itemModel) {
        try {
            Billing bill = itemModel.getBill();
            Billing createBill = itemService.saveIntercompanyillExchange(bill, itemModel.getItemModel());
            String storeId=itemService.getStoreId(itemModel.getUser().getUserId());
            createBill.setBill(bill.getBill());
            String status=service.updateUserCashCollectionReport();
            byte[] pdfBytes = print_bill(createBill.getBillNo(),storeId);

            if (pdfBytes != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("inline", createBill.getCustomerName() + "_" + createBill.getBillNo() + ".pdf");

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(pdfBytes);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }



    @PostMapping("/intercompany/billRequest")
    public ResponseEntity<byte[]> generateInterCompanyBill(@RequestBody Billing bill) {
        try {
            Billing createBill = itemService.saveInterCompanyBilling(bill);
            createBill.setBill(bill.getBill());
            String storeId=itemService.getStoreId(bill.getUserId());
            byte[] pdfBytes = print_bill(createBill.getBillNo(),storeId);
            String status=service.updateUserCashCollectionReport();

            if (pdfBytes != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("inline", createBill.getCustomerName() + "_" + createBill.getBillNo() + ".pdf");

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(pdfBytes);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/getTodayUserCashCollection")
    public ResponseEntity<Integer> getTodayUserCashCollection(@RequestParam("userId") String userId) {

        String storeId=itemService.getStoreId(userId);
        int todaysCollection = itemService.getTodaysCollectionByUser(userId, new Date(),storeId);

        if (todaysCollection >= 0) {

            return ResponseEntity.ok(todaysCollection);
        } else {
            System.out.println("Data Not Found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/getUserList")
    public ResponseEntity<List<User>> getUserList(@RequestParam("storeId") String storeId) {

        List<User> user_info = service.getUserList(storeId);

        if (user_info != null) {

            return ResponseEntity.ok(user_info);
        } else {
            System.out.println("Data Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/getUserSalaryAmount")
    public int getUserSalaryAmount(@RequestParam("userId") String userId,
            @RequestParam("type") String type,
            @RequestParam("hours") int hours) {

        int amount = service.salaryDeduction(userId, type, hours);
        System.out.println(amount);
        return amount;
    }

    @PostMapping("/salary/update")
    public ResponseEntity<String> generateBill(@RequestBody List<User_Salary> salaries,@RequestParam("storeId") String storeId) {

        String status = "";
        try {
            System.out.println(salaries.size());
            status = service.userSalaryUpdate(salaries,storeId);

            return ResponseEntity.ok(status);
        } catch (Exception e) {
            e.printStackTrace();
            status = "FAILED";
            return ResponseEntity.ok(status);
        }
    }

    @GetMapping("/getUserCashCollection")
    public ResponseEntity<List<UserCashCollection>> getUserCashCollection(@RequestParam("storeId") String storeId,
                                                                          @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                                                          @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {

        try {

            List<UserCashCollection> userCashList = service.userCashCollectionReportByDate(storeId,startDate,endDate);

            return ResponseEntity.ok(userCashList);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/salary/generate")
    public ResponseEntity<List<UserMonthlySalary>> generateUserMonthlySalary(@RequestParam("month_fy") String month_fy,@RequestParam("storeId") String storeId) {

        try {

            List<UserMonthlySalary> userMonthlySalary = service.generateUserMonthlySalaries(month_fy,storeId);
            return ResponseEntity.ok(userMonthlySalary);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/salary/paid")
    public ResponseEntity<String> salaryStatusUpdate(@RequestBody UserMonthlySalary usermonthlySalary) {

        String status = "";
        try {

            status = service.userMonthlySalaryChangeStatus(usermonthlySalary);
            return ResponseEntity.ok(status);

        } catch (Exception e) {
            e.printStackTrace();
            status = "Failed";
            return ResponseEntity.ok(status);
        }
    }

    @GetMapping("/salary/user_salary_statement")
    public ResponseEntity<List<User_Salary>> generateUserSalaryStatement(@RequestParam("userId") String userId,
            @RequestParam("month_fy") String month_fy) {

        try {

            List<User_Salary> userSalaryStatement = service.getUserSalaryStatement(userId, month_fy);
            return ResponseEntity.ok(userSalaryStatement);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/filter/getSchool")
    public ResponseEntity<List<String>> getSchoolName(@RequestParam("storeId") String storeId) {

        try {

            List<String> schoolList = service.getSchoolList(storeId);
            return ResponseEntity.ok(schoolList);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/filter/getSchoolNameandCode")
    public ResponseEntity<List<SchoolList>> getSchoolNameandCode(@RequestParam("storeId") String storeId) {

        try {

            List<SchoolList> schoolList = service.getSchoolNameCode(storeId);
            return ResponseEntity.ok(schoolList);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/filter/get_school_search")
    public ResponseEntity<List<String>> getSchoolName(@RequestParam("searchTerm") String searchTerm,
                                                      @RequestParam("storeId") String storeId) {

        try {

            List<String> schoolList = service.getFilteredSchoolList(searchTerm,storeId);
            return ResponseEntity.ok(schoolList);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/filter/item_type")
    public ResponseEntity<List<String>> itemType(@RequestParam("storeId") String storeId) {

        try {

            List<String> items = service.itemTypeList(storeId);
            return ResponseEntity.ok(items);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/filter/school/item_type")
    public ResponseEntity<List<String>> itemType(@RequestParam("schoolCode") String schoolCode,@RequestParam("storeId") String storeId) {

        try {

            List<String> items = service.itemTypeList(schoolCode,storeId);
            return ResponseEntity.ok(items);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/filter/getAllItems")
    public ResponseEntity<List<Items>> getAllItem(@RequestParam("storeId") String storeId) {

        try {

            List<Items> items = service.getAllItemstore(storeId);
            return ResponseEntity.ok(items);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/filter/item_list_school_code")
    public ResponseEntity<List<Items>> itemListBySchoolCode(@RequestParam("schoolCode") String schoolCode,@RequestParam("storeId") String storeId) {

        try {

            List<Items> items = service.itemListBySchool(schoolCode,storeId);
            return ResponseEntity.ok(items);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/filter/item_list_type")
    public ResponseEntity<List<Items>> itemListByItemType(@RequestParam("itemType") String itemType,
                                                          @RequestParam("storeId") String storeId) {

        try {

            List<Items> items = service.itemListByItemType(itemType,storeId);
            return ResponseEntity.ok(items);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/filter/item_category/item_type")
    public ResponseEntity<List<Items>> itemListBySchoolAndType(@RequestParam("schoolCode") String schoolCode,
            @RequestParam("itemType") String itemType,
            @RequestParam("storeId") String storeId) {

        try {

            List<Items> items = service.itemListBySchoolAndType(schoolCode, itemType,storeId);
            return ResponseEntity.ok(items);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @PostMapping("/customer_order_details")
    public ResponseEntity<String> customer_order_detail(@RequestBody CustomerOrderBook order) {
        try {
            String status = service.updateCustomerOrder(order,order.getStoreId());
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/view/customer_order_details")
    public ResponseEntity<List<CustomerOrderBook>> customer_order_detail(@RequestParam("status") String status,@RequestParam("storeId") String storeId) {
        try {
            List<CustomerOrderBook> lst = service.customerOrderDetails(status,storeId);
            return ResponseEntity.ok(lst);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/update/customer_order_details/delivered")
    public ResponseEntity<String> updateOrderDetailDelivered(@RequestParam("orderId") int orderId) {
        try {
            String status = service.updateOrderDetailDelivered(orderId);
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/update/customer_order_details/cancelled")
    public ResponseEntity<String> updateOrderDetailCancelled(@RequestParam("orderId") int orderId) {
        try {
            String status = service.updateOrderDetailCancelled(orderId);
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/create_order")
    public ResponseEntity<String> order_detail(@RequestParam("barcodedId") String barcodedId,@RequestParam("storeId") String storeId) {
        try {
            String status = inventoryService.generate_order(barcodedId,storeId);
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/purchase-order")
    public ResponseEntity<String> purchase_order(@RequestBody PurchaseOrderBook purchaseOrderBook,@RequestParam("storeId") String storeId) {
        try {
            String status = inventoryService.purchase_order(purchaseOrderBook,storeId);
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }



    @GetMapping("/view-order")
    public ResponseEntity<List<PurchaseOrderBook>> viewOrder(@RequestParam("storeId") String storeId) {
        try {
            List<PurchaseOrderBook> order = inventoryService.view_order(storeId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/generate_barcodes")
    public ResponseEntity<byte[]> generateBarcodeId(@RequestParam("itemCode") String itemCode,@RequestParam("storeId") String storeId) {
        try {
            // Get the generated barcode image from the service
            BufferedImage barcodeImage = itemService.generateBarcodeImage(itemCode,storeId);

            // Convert the BufferedImage to a byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(barcodeImage, "png", baos);
            byte[] imageBytes = baos.toByteArray();

            // Set the response headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentLength(imageBytes.length);

            // Return the image bytes in the response
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public byte[] print_bill(int bill_no,String storeId) {
        Billing bill = itemService.getBill(bill_no,storeId);

        List<BillingModel> bills = bill.getBill();
        List<BillingModel> newBill = new ArrayList<>();
        int count = 1;
        int qty=0;
        for (BillingModel billModel : bills) {
            String description = billModel.getItemCategory() + " " + billModel.getItemType() + " " + billModel.getItemColor();
            billModel.setSellPrice(billModel.getPrice());
            billModel.setDescription(description);
            billModel.setSno(count);
            qty=qty+billModel.getQuantity();
            newBill.add(billModel);
            count = count + 1;
        }

        try {
            String pdf_path="";
            if(storeId.equalsIgnoreCase("NX"))
            {
                pdf_path="Bill_NX";
            }
            else {
                pdf_path="Bill_VN";
            }
            InputStream reportTemplate = UserController.class.getResourceAsStream("/static/"+pdf_path+".jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(reportTemplate);
            Map<String, Object> parameters = new HashMap<>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String bill_date = dateFormat.format(bill.getBill_date());
             String total_qty=qty+""; 
            String summary="";
            if(bill.getDiscount()>0)
            {
            int total=bill.getFinal_amount()+bill.getDiscountAmount();
             summary="Total Amount:"+total+"\n"+"Discount:"+bill.getDiscountAmount()+"\n"+"Grand Total:"+bill.getFinal_amount()+"\n"+"Total Qty:"+total_qty;
      
            }
            else{
               summary="Grand Total:"+bill.getFinal_amount()+"\n"+"Total Qty:"+total_qty;
      
            }
            parameters.put("bill_no", bill.getBillNo());
            parameters.put("customer_name", bill.getCustomerName());
            parameters.put("mobile_no", bill.getCustomerMobileNo());
            parameters.put("date", bill_date);
            parameters.put("final_amount", bill.getFinal_amount());
           
            parameters.put("total_qty", total_qty);
            parameters.put("item_summary", summary);

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(newBill);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // Export the JasperPrint object to a byte array
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/search/item_code")
    public ResponseEntity<Items> item_list_code(@RequestParam("barcode") String barcode,@RequestParam("storeId") String storeId) {

        try {

            Items item = itemService.getItemListCode(barcode,storeId);
            return ResponseEntity.ok(item);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    public String printPDF(String filePath) {
        try {

            File pdfFile = new File(filePath);
            if (!pdfFile.exists()) {
                return "File not found: " + filePath;
            }
            FileInputStream fileInputStream = new FileInputStream(pdfFile);
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            printerJob.setPrintable(new PDFPrintable(fileInputStream));
            printerJob.print();

            fileInputStream.close();

            return "PDF printed successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error printing PDF: " + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            return "Unexpected error: " + e.getMessage();
        }
    }

}
