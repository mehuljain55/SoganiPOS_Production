package com.Soganis.Service;

import com.Soganis.Entity.BillingModel;
import com.Soganis.Entity.Items;
import com.Soganis.Entity.User;
import com.Soganis.Model.SalesReportModel;
import com.Soganis.Repository.BillingModelRepository;
import com.Soganis.Repository.ItemsRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalesReportService {

    @Autowired
    private BillingModelRepository billModelRepo;
    
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

    public List<SalesReportModel> billingSummary(List<BillingModel> billingModels, String storeId) {
        List<SalesReportModel> salesReportListFinal=new ArrayList<>();
        List<SalesReportModel> salesReportList = billingModels.stream()
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
                                            combined.setTotal_amount(b1.getTotal_amount() + b2.getTotal_amount());
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
                                        b.get().getTotal_amount()
                                       
                                )
                        )
                ))
                .values()
                .stream()
                .collect(Collectors.toList());
        
        for(SalesReportModel sales:salesReportList)
        {
         Items item=itemRepo.getItemByItemBarcodeID(sales.getItemBarcodeID(),storeId);
        String description=item.getItemCategory()+" "+item.getItemType()+" "+item.getItemColor();
        
         sales.setDescription(item.getItemName());
         sales.setItemCode(item.getItemCode());
         sales.setItemSize(item.getItemSize());
         salesReportListFinal.add(sales);
            
        }

        return salesReportListFinal;
    }

}
