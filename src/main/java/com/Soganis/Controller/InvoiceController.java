package com.Soganis.Controller;

import com.Soganis.Entity.Billing;
import com.Soganis.Entity.BillingModel;
import com.Soganis.Service.ItemService;
import com.Soganis.Service.UserService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {

   @Autowired
   private UserService userService;

   @Autowired
   private ItemService itemService;



   @GetMapping("/customerBillingList")
   public ResponseEntity<List<Billing>> customerBilling(@RequestParam("mobileNo") String mobileNo, @RequestParam("storeId") String storeId) {
      List<Billing> billings = userService.getBillByMobileNo(mobileNo, storeId);

      if (billings != null && !billings.isEmpty()) {
         return new ResponseEntity<>(billings, HttpStatus.OK);
      } else {
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
   }

   @GetMapping("/getBill")
   public ResponseEntity<byte[]> generateBill(@RequestParam("billNo") int billNo, @RequestParam("storeId") String storeId) {
      try {


         byte[] pdfBytes = print_bill(billNo,storeId);

         if (pdfBytes != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "Bill" + "_" +billNo + ".pdf");

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

   @GetMapping("/getBillByDate")
   public ResponseEntity<List<Billing>> getBillByDate(
           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
           @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
           @RequestParam("storeId") String storeId) {

      List<Billing> bills = itemService.getBillByDate(startDate, endDate, storeId);
      if (bills != null && !bills.isEmpty()) {
         return ResponseEntity.ok().body(bills);
      } else {
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
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
         int globalDiscount=bill.getDiscount();
         String discountType=itemService.getDiscountStatus(billModel.getItemBarcodeID(),storeId);
         if(discountType!=null && discountType.equals("Yes")&& globalDiscount==0 )
         {
            billModel.setSellPrice(billModel.getSellPrice());
         }
         else{
            billModel.setSellPrice(billModel.getPrice());
         }

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
}
