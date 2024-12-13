package com.Soganis.Service;

import com.Soganis.Entity.Billing;
import com.Soganis.Entity.BillingModel;
import com.Soganis.Entity.Transactions;
import com.Soganis.Repository.BillingModelRepository;
import com.Soganis.Repository.BillingRepository;
import com.Soganis.Repository.TransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BillModificationService {

    @Autowired
    private TransactionsRepository transactionRepo;

    @Autowired
    private BillingRepository billRepository;

    @Autowired
    private BillingModelRepository billModelRepo;

    public String cancelBillUser(int billNo,String storeId)
    {
        try {
            Billing bill = billRepository.getBillByNo(billNo, storeId);

                List<BillingModel> billingModelList = billModelRepo.findBillByStore(billNo, storeId);
                List<Transactions> transactionsList = transactionRepo.findTransactionByBill(billNo, storeId);

                for (BillingModel billingModel : billingModelList) {
                    billModelRepo.delete(billingModel);
                }

                for (Transactions record : transactionsList) {
                    transactionRepo.delete(record);
                }
                billRepository.delete(bill);
                return "Success";


        }catch (Exception e)
        {
            e.printStackTrace();
            return "Failed";
        }
    }

}
