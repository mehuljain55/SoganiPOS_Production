package com.Soganis.Service;

import com.Soganis.Entity.*;
import com.Soganis.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BillModificationService {

    @Autowired
    private TransactionsRepository transactionRepo;

    @Autowired
    private BillingRepository billRepository;

    @Autowired
    private BillingModelRepository billModelRepo;

    @Autowired
    private ItemsRepository itemRepo;

    @Autowired
    private TransactionDueRepo transactionDueRepo;


    public String cancelBillUser(int billNo,String storeId)
    {
        try {
            Billing bill = billRepository.getBillByNo(billNo, storeId);

                List<BillingModel> billingModelList = billModelRepo.findBillByStore(billNo, storeId);
                List<Transactions> transactionsList = transactionRepo.findTransactionByBill(billNo, storeId);
                TransactionDueListRetail transactionDueRetail=transactionDueRepo.findDueListByBillNoStoreId(billNo,storeId);

                for (BillingModel billingModel : billingModelList) {
                    Items items=itemRepo.getItemByItemBarcodeID(billingModel.getItemBarcodeID(),storeId);
                    if(items!=null)
                    {
                        int qty=items.getQuantity()+billingModel.getQuantity();
                        items.setQuantity(qty);
                        itemRepo.save(items);
                    }
                    billModelRepo.delete(billingModel);
                }

                for (Transactions record : transactionsList) {
                    transactionRepo.delete(record);
                }
                transactionDueRepo.delete(transactionDueRetail);
                billRepository.delete(bill);
                return "Success";

        }catch (Exception e)
        {
            e.printStackTrace();
            return "Failed";
        }
    }
}
