package com.Soganis.Service;

import com.Soganis.Entity.Billing;
import com.Soganis.Entity.TransactionDueListRetail;
import com.Soganis.Entity.Transactions;
import com.Soganis.Model.PaymentModel;
import com.Soganis.Repository.BillingRepository;
import com.Soganis.Repository.TransactionDueRepo;
import com.Soganis.Repository.TransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private TransactionDueRepo transactionDueRepo;



    @Autowired
    private TransactionsRepository transactionRepo;

    @Autowired
    private BillingRepository billRepo;

    public List<TransactionDueListRetail> dueListRetail(String storeId)
    {
         return transactionDueRepo.findDueListByStoreId(storeId);
    }

    public String duesPayment(PaymentModel paymentModel)
    {
        Integer lastTransactionId = transactionRepo.findMaxransactionIdByStoreId(paymentModel.getStoreId());
        int newTransactionId = (lastTransactionId == null) ? 1 : lastTransactionId + 1;

        Optional<TransactionDueListRetail> opt=transactionDueRepo.findById(paymentModel.getPaymentId());
        if(opt.isPresent())
        {
            TransactionDueListRetail transactionDueRetail=opt.get();
            Billing bill=billRepo.getBillByNo(transactionDueRetail.getBillNo(),transactionDueRetail.getStoreId());
            bill.setPaymentMode(paymentModel.getPaymentMode());
            transactionDueRetail.setStatus("Paid");
            billRepo.save(bill);
            transactionDueRepo.save(transactionDueRetail);
            createAndSaveTransaction(newTransactionId,bill, bill.getBillType(),paymentModel.getPaymentMode(),"Paid",transactionDueRetail.getAmount());
            return "Bill Cleared";
        }else {
            return "Unable to find bill";
        }
    }


    private void createAndSaveTransaction(int transactionId, Billing bill, String billType, String mode, String status, int amount) {
        Transactions transaction = new Transactions();
        transaction.setTransactionId(transactionId);
        transaction.setDate(new Date());
        transaction.setBillNo(bill.getBillNo());
        transaction.setMode(mode);
        transaction.setType(billType);
        transaction.setStatus(status);
        transaction.setAmount(amount);
        transaction.setUserId(bill.getUserId());
        transaction.setStoreId(bill.getStoreId());
        transactionRepo.save(transaction);
    }

}
