package com.Soganis.Service;

import com.Soganis.Entity.Billing;
import com.Soganis.Entity.BillingModel;
import com.Soganis.Entity.GraphAnalysisModel.SalesDateModel;
import com.Soganis.Entity.Items;
import com.Soganis.Entity.User;
import com.Soganis.Model.SalesReportModel;
import com.Soganis.Model.SalesReportSchoolModel;
import com.Soganis.Repository.BillingModelRepository;
import com.Soganis.Repository.BillingRepository;
import com.Soganis.Repository.ItemsRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalesReportService {

    @Autowired
    private BillingModelRepository billModelRepo;

    @Autowired
    private BillingRepository billRepo;
    
     @Autowired
    private ItemsRepository itemRepo;

    public List<SalesReportModel> getSaleBySchoolAndDate(String schoolCode, Date startDate, Date endDate,String storeId) {
        List<BillingModel> billing = billModelRepo.findBySchoolAndDateRange(schoolCode, startDate, endDate);

        List<SalesReportModel> salesReportList = billingSummary(billing,storeId);
        return salesReportList;

    }

    public List<SalesReportModel> getSaleByItemTypeAndDate(String itemCode, Date startDate, Date endDate,String storeId) {
        List<BillingModel> billing = billModelRepo.findByItemTypeAndDate(itemCode, startDate, endDate);

        List<SalesReportModel> salesReportList = billingSummary(billing,storeId);
        return salesReportList;

    }

     public List<SalesReportModel> getSaleBySchool(String schoolCode,String storeId) {
        List<BillingModel> billing = billModelRepo.findBySchool(schoolCode);
        List<SalesReportModel> salesReportList = billingSummary(billing,storeId);
        return salesReportList;

    }
   
   public List<SalesReportModel> getSaleByItemType(String itemType,String storeId) {
        List<BillingModel> billing = billModelRepo.findByItemType(itemType);
        List<SalesReportModel> salesReportList = billingSummary(billing,storeId);
        return salesReportList;
    }
   
   public List<SalesReportModel> getSaleByDateRange(Date startDate, Date endDate,String storeId) {
        List<BillingModel> billing = billModelRepo.findByDateRange(startDate, endDate);
        List<SalesReportModel> salesReportList = billingSummary(billing,storeId);
        return salesReportList;
    }
   
       public List<SalesReportModel> getSaleBySchoolAndItemType(String itemType,String itemCategory,String storeId) {
        List<BillingModel> billing = billModelRepo.findBySchoolAndItemType(itemType, itemCategory);
        List<SalesReportModel> salesReportList = billingSummary(billing,storeId);
        return salesReportList;
    }
    
    public List<SalesReportModel> getSaleBySchoolAndItemTypeDate(String itemType, String itemCategory, Date startDate, Date endDate,String storeId) {
        List<BillingModel> billing = billModelRepo.findBySchoolAndItemTypeDate(itemType, itemCategory, startDate, endDate);
        List<SalesReportModel> salesReportList = billingSummary(billing,storeId);
        return salesReportList;
    }

    public List<SalesReportSchoolModel> getSalesBySchool(Date startDate,Date endDate,String storeId)
    {
        List<Billing> bills=billRepo.findByBillDateBetweenAndStoreId(startDate,endDate,storeId);
        List<SalesReportSchoolModel> salesModel=calculateTotalSalesBySchool(bills,storeId);
        return  salesModel;
    }


    public List<SalesDateModel> getSalesAnalysisByDate(Date startDate, Date endDate, String storeId) {
        List<Billing> bills = billRepo.findByBillDateBetweenAndStoreId(startDate, endDate, storeId);

        Map<Date, Integer> salesByDate = bills.stream()
                .collect(Collectors.groupingBy(Billing::getBill_date,
                        Collectors.summingInt(Billing::getFinal_amount)));

        List<SalesDateModel> salesList = salesByDate.entrySet().stream()
                .map(entry -> new SalesDateModel(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return salesList;
    }

    public List<SalesDateModel> getSalesAnalysisByMonthFFY(String fy, String storeId) throws ParseException {
        // Parse the fiscal year string to get start and end years
        String[] years = fy.split("-");
        int startYear = Integer.parseInt(years[0]);
        int endYear = Integer.parseInt(years[1]);

        // Set start date as April 1 of the start year
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(startYear + "-04-01");

        // Set end date as March 31 of the end year
        Date endDate = sdf.parse(endYear + "-03-31");

        // Fetch billing data within the date range and for the specified store ID
        List<Billing> bills = billRepo.findByBillDateBetweenAndStoreId(startDate, endDate, storeId);

        // Summarize sales by month and year
        Map<String, Integer> salesByMonth = new HashMap<>();

        for (Billing bill : bills) {
            // Get the month and year from the bill date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(bill.getBill_date());

            String month = new SimpleDateFormat("MMMM").format(calendar.getTime()); // Full month name
            String year = Integer.toString(calendar.get(Calendar.YEAR));

            String monthYearKey = month + " " + year;

            // Sum the sales amounts
            salesByMonth.put(monthYearKey, salesByMonth.getOrDefault(monthYearKey, 0) + bill.getFinal_amount());
        }

        // Convert map entries to SalesDateModel list
        List<SalesDateModel> salesList = salesByMonth.entrySet().stream()
                .map(entry -> {
                    String[] parts = entry.getKey().split(" ");
                    String month = parts[0];
                    String year = parts[1];

                    // Create a dummy date for the sales date (we can use the first day of the month)
                    Date salesDate = null;
                    try {
                        salesDate = new SimpleDateFormat("MMMM yyyy").parse(entry.getKey());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    return new SalesDateModel(salesDate, entry.getValue(), month, year);
                })
                .collect(Collectors.toList());

        return salesList;
    }






    public List<SalesReportModel> billingSummary(List<BillingModel> billingModels, String storeId) {
        List<SalesReportModel> salesReportListFinal = new ArrayList<>();

        // Filter out items with the specific itemBarcodeID before grouping and summarizing
        List<BillingModel> specialItems = billingModels.stream()
                .filter(b -> "SG9999999".equals(b.getItemBarcodeID()))
                .collect(Collectors.toList());

        List<BillingModel> billModelRetail = billingModels.stream()
                .filter(b -> "Retail".equals(b.getBillCategory())) // Filter for retail bill category
                .collect(Collectors.toList());

        List<BillingModel> billModelWholesale = billingModels.stream()
                .filter(b -> "Wholesale".equals(b.getBillCategory())) // Filter for retail bill category
                .collect(Collectors.toList());



        // Summarize other items
        List<SalesReportModel> summarizedItemsRetail = billModelRetail.stream()
                .filter(b -> !"SG9999999".equals(b.getItemBarcodeID())) // Exclude "SG9999999"
                .collect(Collectors.groupingBy(
                        BillingModel::getItemBarcodeID,
                        Collectors.collectingAndThen(
                                Collectors.reducing(
                                        (b1, b2) -> {
                                            BillingModel combined = new BillingModel();
                                            combined.setItemBarcodeID(b1.getItemBarcodeID());
                                            combined.setDescription(b1.getDescription());
                                            combined.setItemType(b1.getItemType());
                                            combined.setItemColor(b1.getItemColor());
                                            combined.setSellPrice(b1.getSellPrice());
                                            combined.setQuantity(b1.getQuantity() + b2.getQuantity());
                                            combined.setFinal_amount(b1.getFinal_amount() + b2.getFinal_amount());
                                            return combined;
                                        }
                                ),
                                b -> new SalesReportModel(
                                        b.get().getItemBarcodeID(),
                                        b.get().getDescription(),
                                        b.get().getItemType(),
                                        b.get().getItemColor(),
                                        b.get().getSellPrice(),
                                        b.get().getQuantity(),
                                        b.get().getFinal_amount()
                                )
                        )
                ))
                .values()
                .stream()
                .collect(Collectors.toList());


        List<SalesReportModel> summarizedItemsWholesale = billModelWholesale.stream()
                .filter(b -> !"SG9999999".equals(b.getItemBarcodeID())) // Exclude "SG9999999"
                .collect(Collectors.groupingBy(
                        BillingModel::getItemBarcodeID,
                        Collectors.collectingAndThen(
                                Collectors.reducing(
                                        (b1, b2) -> {
                                            BillingModel combined = new BillingModel();
                                            combined.setItemBarcodeID(b1.getItemBarcodeID());
                                            combined.setDescription(b1.getDescription());
                                            combined.setItemType(b1.getItemType());
                                            combined.setItemColor(b1.getItemColor());
                                            combined.setSellPrice(b1.getSellPrice());
                                            combined.setQuantity(b1.getQuantity() + b2.getQuantity());
                                            combined.setFinal_amount(b1.getFinal_amount() + b2.getFinal_amount());
                                            return combined;
                                        }
                                ),
                                b -> new SalesReportModel(
                                        b.get().getItemBarcodeID(),
                                        b.get().getDescription(),
                                        b.get().getItemType(),
                                        b.get().getItemColor(),
                                        b.get().getSellPrice(),
                                        b.get().getQuantity(),
                                        b.get().getFinal_amount()
                                )
                        )
                ))
                .values()
                .stream()
                .collect(Collectors.toList());




        // Convert special items to SalesReportModel and add to final list without summarizing
        for (BillingModel specialItem : specialItems) {
         String desc="Custom Item";
            SalesReportModel salesReport = new SalesReportModel(
                    specialItem.getItemBarcodeID(),
                    specialItem.getDescription(),
                    specialItem.getItemType(),
                    specialItem.getItemColor(),
                    specialItem.getSellPrice(),
                    specialItem.getQuantity(),
                    specialItem.getFinal_amount(),
                    specialItem.getBillCategory(),
                    desc
            );
            salesReportListFinal.add(salesReport);
        }

        // Process summarized items and add them to the final list with additional data from itemRepo
        for (SalesReportModel sales : summarizedItemsRetail) {
            String desc="Regular Item";
            Items item = itemRepo.getItemByItemBarcodeID(sales.getItemBarcodeID(), storeId);
            int quantity=0;
            int totalAmount=0;
            int avg_sell_price=0;
            int price=0;
            if(item!=null)
            {
                sales.setDescription(item.getItemName());
                sales.setItemCode(item.getItemCode());
                sales.setItemSize(item.getItemSize());
                price=Integer.parseInt(item.getPrice());
            }else {
                sales.setDescription("Unknown");
                sales.setItemCode("Unknown");
                sales.setItemSize("Unknown");
            }
           quantity=sales.getTotalQuantity();
           totalAmount=sales.getTotalAmount();
           avg_sell_price=totalAmount/quantity;
           sales.setPrice(price);
           sales.setBillType("Retail");
           sales.setDesc(desc);
           sales.setSellPrice(avg_sell_price);
           salesReportListFinal.add(sales);
        }

        for (SalesReportModel sales : summarizedItemsWholesale) {
            Items item = itemRepo.getItemByItemBarcodeID(sales.getItemBarcodeID(), storeId);
            String desc="Regular Item";
            int quantity=0;
            int totalAmount=0;
            int avg_sell_price=0;
            int price=0;
            if(item!=null)
            {
                sales.setDescription(item.getItemName());
                sales.setItemCode(item.getItemCode());
                sales.setItemSize(item.getItemSize());

                price=Integer.parseInt(item.getWholeSalePrice());
            }else {
                sales.setDescription("Unknown");
                sales.setItemCode("Unknown");
                sales.setItemSize("Unknown");
            }
            quantity=sales.getTotalQuantity();
            totalAmount=sales.getTotalAmount();
            avg_sell_price=totalAmount/quantity;
            sales.setPrice(price);
            sales.setSellPrice(avg_sell_price);
            sales.setBillType("Wholesale");
            sales.setDesc(desc);
            salesReportListFinal.add(sales);
        }
        return salesReportListFinal;
    }




    public List<SalesReportSchoolModel> calculateTotalSalesBySchool(List<Billing> bills, String storeId) {
        Map<String, Integer> salesBySchool = bills.stream()
                .collect(Collectors.groupingBy(
                        Billing::getSchoolName,
                        Collectors.summingInt(Billing::getFinal_amount)
                ));

        for (Map.Entry<String, Integer> entry : salesBySchool.entrySet()) {
            String schoolName = entry.getKey();
            Integer totalSales = entry.getValue();
        }

        List<SalesReportSchoolModel> salesReport = salesBySchool.entrySet().stream()
                .map(entry -> {
                    SalesReportSchoolModel report = new SalesReportSchoolModel();
                    report.setSchoolName(entry.getKey());
                    report.setSales(entry.getValue());
                    report.setStoreId(storeId);
                    return report;
                })
                .collect(Collectors.toList());

        return salesReport;
    }


}
