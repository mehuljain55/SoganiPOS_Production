package com.Soganis.Service;

import com.Soganis.Entity.*;
import com.Soganis.Model.BarcodeModel;
import com.Soganis.Model.BillViewModel;
import com.Soganis.Model.ItemReturnModel;
import com.Soganis.Repository.*;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    @Autowired
    private ItemsRepository itemRepo;

    @Autowired
    private BillingRepository billRepo;

    @Autowired
    private BillingModelRepository billModelRepository;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private StoreRepository storeRepo;

    @Autowired
    private TransactionService transactionService;



    public List<Items> getAllItems() {
        List<Items> items = itemRepo.findAll();
        return items;
    }

    public List<Items> getAllItems(String searchTerm, int maxResults,String storeId) {
        List<Items> items = itemRepo.findAllFiltered(searchTerm,storeId);
        return items.stream()
                .limit(maxResults)
                .collect(Collectors.toList());
    }
    
    
     public List<BarcodeModel> getAllItemCode(String searchTerm, int maxResults,String storeId) {
        List<Items> items = itemRepo.findAllFiltered(searchTerm,storeId).stream()
                .limit(maxResults)
                .collect(Collectors.toList());
        List<BarcodeModel> barcodeModel=new ArrayList<>();
        
        for(Items item:items)
        {
          BarcodeModel barcode=new BarcodeModel();
          String itemCode=item.getItemCode();
          String descriprion="("+itemCode+")"+item.getItemType()+" "+item.getItemCategory()+" "+item.getItemColor();
          barcode.setItemCode(itemCode);
          barcode.setDescription(descriprion);
          barcodeModel.add(barcode);
          
        }
        
        return barcodeModel;
        
    }

    public String getDiscountStatus(String barcodeId,String storeId)
    {
        String status=itemRepo.getDiscountStatus(barcodeId,storeId);
        return status;
    }

    public Billing saveBilling(Billing billing) {
        try {
            String storeId=getStoreId(billing.getUserId());
            int globalDiscount=billing.getDiscount();
            Integer maxBillNo = billRepo.findMaxBillNoByStoreId(storeId);
            if (maxBillNo == null) {
                maxBillNo = 1;  // Start from 0 if no previous bills
            }
            else{
                maxBillNo=maxBillNo+1;
            }

            int final_amount = 0;
            billing.setBill_date(new Date());
            billing.setBillType("Retail");
            billing.setStoreId(storeId);
            billing.setBillNo(maxBillNo);;
            Billing savedBilling = new Billing();
            savedBilling.setBillNo(maxBillNo);
            savedBilling.setStatus("Fresh");
            List<BillingModel> billingModelList=new ArrayList<>();
            if (billing.getBill() != null) {
                int count=1;
                for (BillingModel billingModel : billing.getBill()) {
                    billingModel.setBilling(savedBilling);
                    billingModel.setBillCategory("Retail");
                    billingModel.setSchoolName(billing.getSchoolName());
                    billingModel.setDiscount(billing.getDiscount());
                    int sellPrice=billingModel.getSellPrice();
                    billingModel.setPrice(sellPrice);
                   
                    
                    if(billing.getDiscount()>0)
                    {    Items item;
                        if(billingModel.getItemBarcodeID().equals("SG9999999"))
                        {
                            item=new Items();
                            String price=billingModel.getSellPrice()+"";
                            item.setPrice(price);
                        }
                        else{
                         item=itemRepo.getItemByItemBarcodeID(billingModel.getItemBarcodeID(),storeId);
                         }

                      int price=Integer.parseInt(item.getPrice());
                      int discount=billing.getDiscount();
                      int discount_price=(sellPrice*discount)/100;
                      sellPrice=sellPrice-discount_price;
                      billingModel.setSellPrice(sellPrice);
                      billingModel.setPrice(price);
                      
                    }

                    String discountType=itemRepo.getDiscountStatus(billingModel.getItemBarcodeID(),storeId);
                    if(discountType!=null && discountType.equals("Yes")&& globalDiscount==0)
                    {
                        Items item=itemRepo.getItemByItemBarcodeID(billingModel.getItemBarcodeID(),storeId);
                        int price=Integer.parseInt(item.getPrice());
                        int discountRate=(price-sellPrice)*100;
                        int discount=discountRate/price;
                        billingModel.setDiscount(discount);
                        billingModel.setPrice(price);
                        billingModel.setSellPrice(sellPrice);

                    }


                    billingModel.setStoreName(storeId);
                    final_amount = final_amount + billingModel.getTotal_amount();
                    billingModel.setBill_date(new Date());
                    if (billingModel.getItemBarcodeID().equals("SG9999999")) {
                        String description = billingModel.getItemCategory() + " " + billingModel.getItemType() + " " + billingModel.getItemSize();
                        billingModel.setDescription(description);
                    } else {
                        Items item = itemRepo.getItemByItemBarcodeID(billingModel.getItemBarcodeID(),storeId);
                        billingModel.setDescription(item.getItemName());
                    }


                    String status = inventoryService.updateInventory(billingModel,storeId);
                    int finalAmount=billingModel.getSellPrice()*billingModel.getQuantity();
                    billingModel.setFinal_amount(finalAmount);
                    billingModel.setStoreName(storeId);
                    billingModelList.add(billingModel);
                 //   billModelRepository.save(billingModelList);
                }
                
                    if (billing.getDiscount() > 0) {
                    int discount = billing.getDiscount();
                    int discountAmount = (final_amount * discount) / 100;

                    int remainder = discountAmount % 10;
                    if (remainder < 5) {
                        discountAmount = discountAmount - remainder + (remainder >= 3 ? 5 : 0);
                    } else {
                        discountAmount = discountAmount - remainder + (remainder >= 8 ? 10 : 5);
                    }
                    savedBilling.setDiscountAmount(discountAmount);
                }
                   
                
                if (billing.getDiscount() > 0) {
                    int discount = billing.getDiscount();
                    savedBilling.setDiscount(discount);
                    int discountAmount = (final_amount * discount) / 100;
                    final_amount = final_amount - discountAmount;
                    int remainder = final_amount % 10;
                    if (remainder < 5) {
                        final_amount = final_amount - remainder + (remainder >= 3 ? 5 : 0);
                    } else {
                        final_amount = final_amount - remainder + (remainder >= 8 ? 10 : 5);
                    }

                }

                if (billing.getDiscount() ==0) {
                    int remainder = final_amount % 10;
                    if (remainder < 5) {
                        final_amount = final_amount - remainder + (remainder >= 3 ? 5 : 0);
                    } else {
                        final_amount = final_amount - remainder + (remainder >= 8 ? 10 : 5);
                    }


                }
                savedBilling.setBillNo(maxBillNo);
                savedBilling.setBill_date(new Date());
                savedBilling.setBillType("Retail");
                savedBilling.setCustomerName(billing.getCustomerName());
                savedBilling.setCustomerMobileNo(billing.getCustomerMobileNo());
                savedBilling.setItem_count(billing.getItem_count());
                savedBilling.setPaymentMode(billing.getPaymentMode());
                savedBilling.setStoreId(billing.getStoreId());
                savedBilling.setUserId(billing.getUserId());
                savedBilling.setSchoolName(billing.getSchoolName());
                savedBilling.setFinal_amount(final_amount);
                savedBilling.setBill(billingModelList);
                savedBilling.setStoreId(storeId);
                billRepo.save(savedBilling);
                savedBilling.setBill(billing.getBill());
            }
            return savedBilling;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Billing saveBillExchange(Billing billing, List<ItemReturnModel> itemList,int billNo) {

        try {

            String storeId=getStoreId(billing.getUserId());
            Integer maxBillNo = billRepo.findMaxBillNoByStoreId(storeId);
            String customerName="";
            String customerMobileNo="";
            if (maxBillNo == null) {
                maxBillNo = 1;  // Start from 0 if no previous bills
            }
            else {
                maxBillNo=maxBillNo+1;
            }
            int final_amount = 0;
            Billing  billingUser=billRepo.getBillByNo(billNo,storeId);
            billingUser.setStatus("Exchanged");
            billRepo.save(billingUser);

            Billing savedBilling = new Billing();
            savedBilling.setBillNo(maxBillNo);
            savedBilling.setBillType("Exchange");
            savedBilling.setStatus("Exchanged");
            savedBilling.setStoreId(storeId);
            savedBilling.setSchoolName(billing.getSchoolName());
            savedBilling.setUserId(billing.getUserId());
            savedBilling.setPaymentMode(billing.getPaymentMode());
            List<BillingModel> bill = billing.getBill();
            List<BillingModel> billingModelList=new ArrayList<>();
            for (ItemReturnModel itemModel : itemList) {
                Items item = itemRepo.getItemByItemBarcodeID(itemModel.getBarcodedId(),storeId);
                int totalAmount = itemModel.getReturn_quantity() * itemModel.getPrice();
                BillingModel billingModel = new BillingModel();
                billingModel.setBilling(savedBilling);
                billingModel.setItemBarcodeID(itemModel.getBarcodedId());
                billingModel.setBill_date(new Date());
                billingModel.setBillCategory("Retail");
                billingModel.setSchoolName(billingUser.getSchoolName());
                billingModel.setSellPrice(itemModel.getPrice());
                billingModel.setItemSize(item.getItemSize());
                billingModel.setItemColor(item.getItemColor());
                billingModel.setItemType(item.getItemType());
                billingModel.setItemCategory(item.getItemCategory());
                billingModel.setQuantity((itemModel.getReturn_quantity()) * -1);
                billingModel.setTotal_amount(totalAmount * -1);
                billingModel.setFinal_amount(totalAmount * -1);
                if(billingModel.getItemBarcodeID().equals("SG9999999"))
                {
                    billingModel.setPrice(billingModel.getSellPrice());
                } else {
                    int price = Integer.parseInt(item.getPrice());
                    billingModel.setPrice(price);
                }
                billingModel.setStoreName(storeId);

                bill.add(billingModel);


                Optional<BillingModel> opt = billModelRepository.findById(itemModel.getSno());
                if (opt.isPresent()) {
                    BillingModel save_bill = opt.get();

                    if(customerMobileNo==null||customerMobileNo=="")
                    {
                        Billing billing1=billRepo.getBillByNo(save_bill.getBilling().getBillNo(),storeId);
                        customerName = billing1.getCustomerName() != null ? billing1.getCustomerName() : "";
                        customerMobileNo = billing1.getCustomerMobileNo() != null ? billing1.getCustomerMobileNo() : "";

                    }
                     save_bill.setStatus("Exchanged");

                    billModelRepository.save(save_bill);

                }

            }

            if (billing.getBill() != null) {
                for (BillingModel billingModel : bill) {
                    billingModel.setBilling(savedBilling);
                    billingModel.setSchoolName(billing.getSchoolName());
                    billingModel.setStoreName(storeId);
                    billingModel.setBillCategory("Retail");
                    final_amount = final_amount + billingModel.getTotal_amount();
                    billingModel.setBill_date(new Date());
                    if (billingModel.getItemBarcodeID().equals("SG9999999")) {
                        String description = billingModel.getItemCategory() + " " + billingModel.getItemType() + " " + billingModel.getItemSize();
                        billingModel.setDescription(description);
                        billingModel.setPrice(billingModel.getSellPrice());

                    } else {
                        Items item = itemRepo.getItemByItemBarcodeID(billingModel.getItemBarcodeID(),storeId);
                        billingModel.setDescription(item.getItemName());
                        billingModel.setPrice(Integer.parseInt(item.getPrice()));


                    }
                    billingModel.setFinal_amount(billingModel.getTotal_amount());
                    String status = inventoryService.updateInventory(billingModel,storeId);
                    System.out.println(status);
                    billingModelList.add(billingModel);


                }
                System.out.println(billing.getBalanceAmount());
                savedBilling.setBill_date(new Date());
                savedBilling.setStatus("Exchanged");
                savedBilling.setFinal_amount(final_amount);
                savedBilling.setBill(billingModelList);
                savedBilling.setCustomerMobileNo(customerMobileNo);
                savedBilling.setCustomerName(customerName);
                savedBilling.setStoreId(storeId);
                billRepo.save(savedBilling);
                savedBilling.setBill(billing.getBill());
            }
            return savedBilling;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public Billing saveIntercompanyillExchange(Billing billing, List<ItemReturnModel> itemList,int billNo) {

        try {

            String storeId=getStoreId(billing.getUserId());
            Integer maxBillNo = billRepo.findMaxBillNoByStoreId(storeId);
            String customerName="";
            String customerMobileNo="";
            if (maxBillNo == null) {
                maxBillNo = 1;  // Start from 0 if no previous bills
            }
            else {
                maxBillNo=maxBillNo+1;
            }
            int final_amount = 0;

            Billing  billingUser=billRepo.getBillByNo(billNo,storeId);
            billingUser.setStatus("Exchanged");
            billRepo.save(billingUser);


            Billing savedBilling = new Billing();
            savedBilling.setBillNo(maxBillNo);
            savedBilling.setBillType("Exchange");
            savedBilling.setStoreId(storeId);
            savedBilling.setSchoolName(billing.getSchoolName());
            savedBilling.setUserId(billing.getUserId());
            savedBilling.setPaymentMode(billing.getPaymentMode());
            List<BillingModel> bill = billing.getBill();
            List<BillingModel> billingModelList=new ArrayList<>();
            for (ItemReturnModel itemModel : itemList) {
                Items item = itemRepo.getItemByItemBarcodeID(itemModel.getBarcodedId(),storeId);
                int totalAmount = itemModel.getReturn_quantity() * itemModel.getPrice();
                BillingModel billingModel = new BillingModel();
                billingModel.setBilling(savedBilling);
                billingModel.setItemBarcodeID(itemModel.getBarcodedId());

                billingModel.setBill_date(new Date());
                billingModel.setBillCategory("Wholesale");
                billingModel.setSellPrice(itemModel.getPrice());
                billingModel.setItemSize(item.getItemSize());
                billingModel.setItemColor(item.getItemColor());
                billingModel.setItemType(item.getItemType());
                billingModel.setItemCategory(item.getItemCategory());
                billingModel.setQuantity((itemModel.getReturn_quantity()) * -1);
                billingModel.setTotal_amount(totalAmount * -1);
                billingModel.setFinal_amount(totalAmount * -1);
                if(billingModel.getItemBarcodeID().equals("SG9999999"))
                {
                    billingModel.setPrice(billingModel.getSellPrice());
                } else {
                    int price = Integer.parseInt(item.getPrice());
                    billingModel.setPrice(price);
                }
                billingModel.setStoreName(storeId);
                bill.add(billingModel);


                Optional<BillingModel> opt = billModelRepository.findById(itemModel.getSno());
                if (opt.isPresent()) {
                    BillingModel save_bill = opt.get();

                    if(customerMobileNo==null||customerMobileNo=="")
                    {
                        Billing billing1=billRepo.getBillByNo(save_bill.getBilling().getBillNo(),storeId);
                        customerName = billing1.getCustomerName() != null ? billing1.getCustomerName() : "";
                        customerMobileNo = billing1.getCustomerMobileNo() != null ? billing1.getCustomerMobileNo() : "";

                    }
                    save_bill.setStatus("Exchanged");

                    billModelRepository.save(save_bill);

                }

            }

            if (billing.getBill() != null) {
                for (BillingModel billingModel : bill) {
                    billingModel.setBilling(savedBilling);
                    billingModel.setBillCategory("Wholesale");
                    billingModel.setStoreName(storeId);
                    final_amount = final_amount + billingModel.getTotal_amount();
                    billingModel.setBill_date(new Date());
                    if (billingModel.getItemBarcodeID().equals("SG9999999")) {
                        String description = billingModel.getItemCategory() + " " + billingModel.getItemType() + " " + billingModel.getItemSize();
                        billingModel.setDescription(description);
                        billingModel.setPrice(billingModel.getSellPrice());

                    } else {
                        Items item = itemRepo.getItemByItemBarcodeID(billingModel.getItemBarcodeID(),storeId);
                        billingModel.setDescription(item.getItemName());
                        billingModel.setPrice(Integer.parseInt(item.getPrice()));

                    }

                    String status = inventoryService.updateInventory(billingModel,storeId);
                    System.out.println(status);
                    billingModelList.add(billingModel);


                }
                System.out.println(billing.getBalanceAmount());
                savedBilling.setBill_date(new Date());
                savedBilling.setFinal_amount(final_amount);
                savedBilling.setBill(billingModelList);
                savedBilling.setCustomerMobileNo(customerMobileNo);
                savedBilling.setCustomerName(customerName);
                savedBilling.setStoreId(storeId);
                billRepo.save(savedBilling);
                savedBilling.setBill(billing.getBill());
            }
            return savedBilling;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public Billing saveInterCompanyBilling(Billing billing) {
        try {

            String storeId=getStoreId(billing.getUserId());
            Integer maxBillNo = billRepo.findMaxBillNoByStoreId(storeId);
            if (maxBillNo == null) {
                maxBillNo = 1;
            }
            else {
                maxBillNo=maxBillNo+1;
            }
            int final_amount = 0;
            Billing savedBilling = new Billing();
            String storeContactNumber=storeRepo.getStoreContact(billing.getCustomerName());
            savedBilling.setBillNo(maxBillNo);
            savedBilling.setBillType("Wholesale");
            savedBilling.setStatus("Fresh");

            savedBilling.setCustomerName(billing.getCustomerName());
            savedBilling.setCustomerMobileNo(storeContactNumber);
            List<BillingModel> billingModelList=new ArrayList<>();
            if (billing.getBill() != null) {
                for (BillingModel billingModel : billing.getBill()) {
                    billingModel.setBilling(savedBilling);
                    final_amount = final_amount + billingModel.getTotal_amount();
                    billingModel.setBill_date(new Date());
                    billingModel.setBillCategory("Wholesale");
                    billingModel.setStoreName(storeId);
                    String status = inventoryService.updateInterCompanyInventory(billingModel,storeId);
                    billingModel.setStoreName(storeId);
                    billingModel.setFinal_amount(final_amount);

                    if (billingModel.getItemBarcodeID().equals("SG9999999")) {
                        String description = billingModel.getItemCategory() + " " + billingModel.getItemType() + " " + billingModel.getItemSize();
                        billingModel.setDescription(description);
                        billingModel.setPrice(billingModel.getSellPrice());

                    } else {
                        Items item = itemRepo.getItemByItemBarcodeID(billingModel.getItemBarcodeID(),storeId);
                        billingModel.setDescription(item.getItemName());
                        int price=Integer.parseInt(item.getWholeSalePrice());
                        billingModel.setPrice(price);

                    }
                    billingModelList.add(billingModel);
                }
                savedBilling.setFinal_amount(final_amount);
                savedBilling.setBill_date(new Date());
                savedBilling.setBill(billingModelList);
                savedBilling.setUserId(billing.getUserId());
                savedBilling.setItem_count(billing.getItem_count());
                savedBilling.setPaymentMode(billing.getPaymentMode());
                savedBilling.setSchoolName(billing.getSchoolName());
                savedBilling.setStoreId(storeId);
                billRepo.save(savedBilling);

            }
            return savedBilling;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public  Billing getBill(int billNo,String storeId)
    {
        BillingId billingId = new BillingId(billNo, storeId);
        Optional<Billing> opt = billRepo.findById(billingId);
        if (opt.isPresent()) {
            Billing bill = opt.get();
            return  bill;
        } else {
           return  null;
        }
    }

    public  List<Billing> getBillByDate(Date startDate, Date endDate,String storeId)
    {

        List<Billing> billingList = billRepo.findByBillDateBetweenAndStoreId(startDate,endDate,storeId);
        return  billingList;
    }



    public BillViewModel getBillByMobileNo(String billId,String storeId) {
        int billNo=Integer.parseInt(billId);
        BillingId billingId = new BillingId(billNo, storeId);
        BillViewModel billViewModel=new BillViewModel();
        Optional<Billing> opt = billRepo.findById(billingId);
        if (opt.isPresent()) {
            Billing bill = opt.get();
            billViewModel.setType("single");
            billViewModel.setBill(bill);
            return billViewModel;
        } else {
            billViewModel.setType("Not Found");
            return billViewModel;
        }
    }



    public BillViewModel getBillList(String mobileNo, String storeId) {

       List<Billing> bills=billRepo.getBillByMobileNo(mobileNo,storeId);
        BillViewModel billViewModel = new BillViewModel();
       if(bills.size()>0) {

           billViewModel.setType("list");
           billViewModel.setBillList(bills);
       }else{
           billViewModel.setType("Not Found");
       }
       return billViewModel;

    }

    public int getTodaysCollectionByUser(String userId, Date date,String storeId) {
        List<Billing> bills = billRepo.findByUserIdAndBillDate(userId, date,storeId);
        int cash_collection = 0;

        for (Billing bill : bills) {
            cash_collection = cash_collection + bill.getFinal_amount();
        }
        return cash_collection;
    }

    public Items getItemListCode(String itemCode,String storeId) {
        Items item = itemRepo.findItemsByItemCode(itemCode,storeId);
        return item;
    }

    public String stockReturn(List<ItemReturnModel> items) {
        try {
            String storeId=getStoreId(items.get(0).getUserId());
            String billType="";
            String userId=items.get(0).getUserId();
            Integer maxBillNo = billRepo.findMaxBillNoByStoreId(storeId);
            if (maxBillNo == null) {
                maxBillNo = 1;  // Start from 0 if no previous bills
            }
            else {
                maxBillNo=maxBillNo+1;
            }
            if (items != null) {
                int bill_no = 0;
                int finalAmount = 0;
                String customerName = "";
                String customerMobileNo = "";
                String school = "";
                System.out.println(items);
                Billing billing = new Billing();
                billing.setBillNo(maxBillNo);
                billing.setStoreId(storeId);
                billRepo.save(billing);


                for (ItemReturnModel itemModel : items) {
                    Optional<BillingModel> opt = billModelRepository.findById(itemModel.getSno());
                    if (opt.isPresent()) {
                        BillingModel billModel = opt.get();
                        Billing bill = billRepo.getBillByNo(billModel.getBilling().getBillNo(),storeId);
                        bill.setStatus("Returned");
                        billRepo.save(bill);
                        billType=billModel.getBillCategory();
                        bill_no = bill.getBillNo();
                        userId = itemModel.getUserId();
                        customerName = bill.getCustomerName();
                        customerMobileNo = bill.getCustomerMobileNo();
                        school = bill.getSchoolName();
                        storeId=getStoreId(itemModel.getUserId());
                        Items item = itemRepo.getItemByItemBarcodeID(billModel.getItemBarcodeID(),storeId);
                        int total_quantity = item.getQuantity() + itemModel.getReturn_quantity();
                        item.setQuantity(total_quantity);
                        int sellPrice = itemModel.getPrice();
                        String description = billModel.getDescription() + " (Returned)";

                        System.out.println(itemModel.getReturn_quantity());
                        System.out.println(sellPrice);
                        int totalAmount = sellPrice * (itemModel.getReturn_quantity());
                        billModel.setStatus("Returned");

                        finalAmount = finalAmount + totalAmount;
                        int quantity = (itemModel.getReturn_quantity() * -1);

                        BillingModel billingModel = new BillingModel();
                        billingModel.setBilling(billing);
                        billingModel.setItemBarcodeID(billModel.getItemBarcodeID());
                        billingModel.setItemCategory(billModel.getItemCategory());
                        billingModel.setDescription(description);
                        billingModel.setSchoolName(school);
                        billingModel.setItemColor(billModel.getItemColor());
                        billingModel.setItemSize(billModel.getItemSize());
                        billingModel.setItemType(billModel.getItemType());
                        billingModel.setBillCategory(billType);
                        billingModel.setSellPrice(sellPrice);

                        if(billingModel.getItemBarcodeID().equals("SG9999999"))
                        {
                            billingModel.setPrice(billingModel.getSellPrice());
                        }

                        else if(billType.equals("Retail"))
                        {
                            int price=Integer.parseInt(item.getPrice());
                            billingModel.setPrice(price);

                        }

                      else  if(billType.equals("Wholesale"))
                        {
                            int price=Integer.parseInt(item.getWholeSalePrice());
                            billingModel.setPrice(price);
                        }

                        billingModel.setBill_date(new Date());
                        billingModel.setTotal_amount((totalAmount) * -1);
                        billingModel.setFinal_amount((totalAmount) * -1);

                        billingModel.setQuantity(quantity);



                        billingModel.setStoreName(storeId);
                        billModelRepository.save(billingModel);
                        itemRepo.save(item);
                    }

                }
                System.out.println(finalAmount);
                billing.setBill_date(new Date());
                billing.setDescription("Return item from bill no " + bill_no);
                billing.setBillType("Return");
                billing.setStatus("Returned");
                billing.setUserId(userId);
                billing.setCustomerName(customerName);
                billing.setCustomerMobileNo(customerMobileNo);
                billing.setSchoolName(school);
                billing.setFinal_amount((finalAmount) * -1);
                billRepo.save(billing);
                Transactions transactions=new Transactions();
                transactions.setBillNo(bill_no);
                transactions.setDescription("Refund for item return");
                transactions.setDate(new Date());
                transactions.setAmount(billing.getFinal_amount());
                transactions.setType(billType);
                transactions.setStatus("Paid");
                transactions.setMode("Cash");
                transactions.setUserId(userId);
                transactions.setStoreId(storeId);
                transactionService.createTreansaction(transactions,storeId);


                return "success";
            }
            return "Failed";

        } catch (Exception e) {
            e.printStackTrace();
            return "failed";
        }
    }

    public String getStoreId(String userId)
    {
        Optional<User> opt=userRepo.findById(userId);
        if(opt.isPresent())
        {
            User user= opt.get();
            return user.getStoreId();
        }
        else{
            return "";
        }

    }

    public String stockDefectReturn(ItemReturnModel itemModel) {
        try {

            String storeId=getStoreId(itemModel.getUserId());
            String billType="";
            Integer maxBillNo = billRepo.findMaxBillNoByStoreId(storeId);
            if (maxBillNo == null) {
                maxBillNo = 0;  // Start from 0 if no previous bills
            }
            else {
                maxBillNo=maxBillNo+1;
            }
            int bill_no = 0;
            int finalAmount = 0;
            String customerName = "";
            String customerMobileNo = "";
            String school = "";
            String userId = "";
            Optional<BillingModel> opt = billModelRepository.findById(itemModel.getSno());
            if (opt.isPresent()) {
                BillingModel billModel = opt.get();
                Billing bill = billRepo.getBillByNo(billModel.getBilling().getBillNo(),storeId);
                bill.setStatus("Defected");
                billRepo.save(bill);

                billType=billModel.getBillCategory();
                bill_no = bill.getBillNo();
                userId = itemModel.getUserId();
                customerName = bill.getCustomerName();
                customerMobileNo = bill.getCustomerMobileNo();
                school = bill.getSchoolName();
                Items item = itemRepo.getItemByItemBarcodeID(billModel.getItemBarcodeID(),storeId);
                int total_quantity = item.getQuantity() + itemModel.getReturn_quantity();
                item.setQuantity(total_quantity);
                int sellPrice = itemModel.getPrice();
                String description = billModel.getDescription() + " (Defected Item)";
                System.out.println(itemModel.getReturn_quantity());
                System.out.println(sellPrice);
                int totalAmount = sellPrice * (itemModel.getReturn_quantity());
                billModel.setStatus("Defected");
                finalAmount = finalAmount + totalAmount;
                int quantity = (itemModel.getReturn_quantity() * -1);

                BillingModel billingModel = new BillingModel();
                billingModel.setItemBarcodeID(billModel.getItemBarcodeID());
                billingModel.setItemCategory(billModel.getItemCategory());
                billingModel.setDescription(description);
                billingModel.setBillCategory(billType);
                billingModel.setSchoolName(school);

                billingModel.setItemColor(billModel.getItemColor());
                billingModel.setItemSize(billModel.getItemSize());
                billingModel.setItemType(billModel.getItemType());
                billingModel.setSellPrice(sellPrice);
                billingModel.setBill_date(new Date());
                billingModel.setTotal_amount((totalAmount) * -1);
                billingModel.setFinal_amount((totalAmount) * -1);
                billingModel.setQuantity(quantity);
                billingModel.setStoreName(storeId);
                if(billingModel.getItemBarcodeID().equals("SG9999999"))
                {
                    billingModel.setPrice(billingModel.getSellPrice());
                }

                else if(billType.equals("Retail"))
                {
                    int price=Integer.parseInt(item.getPrice());
                    billingModel.setPrice(price);

                }

                else  if(billType.equals("Wholesale"))
                {
                    int price=Integer.parseInt(item.getWholeSalePrice());
                    billingModel.setPrice(price);
                }
                System.out.println(finalAmount);
                Billing billing = new Billing();
                billing.setBill_date(new Date());
                billing.setDescription("Defected item from bill no " + bill_no);
                billing.setUserId(userId);
                billing.setStatus("Defected");
                billing.setCustomerName(customerName);
                billing.setBillType("Defetcted Item");
                billing.setCustomerMobileNo(customerMobileNo);
                billing.setSchoolName(school);
                billing.setFinal_amount((finalAmount) * -1);
                billing.setStoreId(storeId);
                billing.setBillNo(maxBillNo);
                billRepo.save(billing);
                billingModel.setBilling(billing);
                billModelRepository.save(billingModel);
                Transactions transactions=new Transactions();
                transactions.setBillNo(bill_no);
                transactions.setDescription("Refund for defected item");
                transactions.setDate(new Date());
                transactions.setAmount(billing.getFinal_amount());
                transactions.setType(billType);
                transactions.setStatus("Paid");
                transactions.setMode("Cash");
                transactions.setUserId(itemModel.getUserId());
                transactions.setStoreId(storeId);
                transactionService.createTreansaction(transactions,storeId);



                return "success";
            }
            return "Failed";

        } catch (Exception e) {
            e.printStackTrace();
            return "failed";
        }
    }

    public BufferedImage generateBarcodeImage(String itemCode,String storeId) {
        Items item = itemRepo.findItemsByItemCode(itemCode,storeId);
        return generateCode(item.getItemCategory(), item.getItemCode(), item.getDescription());
    }

    public BufferedImage generateCode(String school, String itemCode, String itemType) {
        try {
            // Dimensions for 5 cm x 3 cm at 96 DPI
            int dpi = 96;
            int barcodeWidth = (int) (5.0 / 2.54 * dpi);  // Width in pixels
            int barcodeHeight = (int) (2.5 / 2.54 * dpi); // Height in pixels
            int margin = 10;  // Smaller margin for labels

            // Adjusted height for the barcode (reduce the height further)
            int barcodeDisplayHeight = (int) (barcodeHeight * 0.35);  // Further reduced height for the barcode

            // Generate the barcode as a BitMatrix
            BitMatrix bitMatrix = new MultiFormatWriter().encode(itemCode, BarcodeFormat.CODE_128, barcodeWidth, barcodeDisplayHeight);

            // Convert BitMatrix to BufferedImage
            BufferedImage barcodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // Create a new image with extra space for header and footer
            BufferedImage finalImage = new BufferedImage(barcodeWidth, barcodeHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = finalImage.createGraphics();

            // Set background color
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, barcodeWidth, barcodeHeight);

            // Draw the header
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.PLAIN, 10)); // Smaller font for small image
            g.drawString(school, (barcodeWidth - g.getFontMetrics().stringWidth(school)) / 2, margin + 5); // Adjusted position

            // Draw the barcode
            int barcodeYPosition = margin + 15; // Adjusted position to move up
            g.drawImage(barcodeImage, 0, barcodeYPosition, null);

            // Draw the barcode text (footer part 1)
            g.drawString(itemCode, (barcodeWidth - g.getFontMetrics().stringWidth(itemCode)) / 2, barcodeHeight - margin - 15); // Adjusted position

            // Draw the item type text (footer part 2)
            g.drawString(itemType, (barcodeWidth - g.getFontMetrics().stringWidth(itemType)) / 2, barcodeHeight - margin - 5); // Adjusted position

            g.dispose();

            // Return the final image instead of saving it to a file
            return finalImage;

        } catch (Exception e) {
            e.printStackTrace();
            return null;  // In case of error, return null
        }
    }
}
