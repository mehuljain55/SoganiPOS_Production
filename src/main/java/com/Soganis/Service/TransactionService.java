package com.Soganis.Service;

import com.Soganis.Entity.Billing;
import com.Soganis.Entity.Transactions;
import com.Soganis.Model.TransactionModel;
import com.Soganis.Repository.TransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TransactionService {

    @Autowired
    private TransactionsRepository transactionRepo;

    public String createTransactionRetail(Billing bill, TransactionModel transactionModel, String storeId) {
        Integer lastTransactionId = transactionRepo.findMaxransactionIdByStoreId(storeId);

        // Start new transaction ID
        int newTransactionId = (lastTransactionId == null) ? 1 : lastTransactionId + 1;

        String paymentMode = bill.getPaymentMode();
        String billType="Retail";
        int finalAmount = bill.getFinal_amount();

        if (paymentMode.equals("Cash") || paymentMode.equals("Card") || paymentMode.equals("Upi")) {
            createAndSaveTransaction(newTransactionId, bill,billType, paymentMode, "Paid", finalAmount);
            return "Success";
        } else if (paymentMode.equals("Partial")) {
            if (transactionModel.getCash() > 0) {
                createAndSaveTransaction(newTransactionId++, bill,billType, "Cash", "Paid", transactionModel.getCash());
            }
            if (transactionModel.getUpi() > 0) {
                createAndSaveTransaction(newTransactionId++, bill, billType,"Upi", "Paid", transactionModel.getUpi());
            }
            if (transactionModel.getCard() > 0) {
                createAndSaveTransaction(newTransactionId++, bill, billType, "Card", "Paid", transactionModel.getCard());
            }
            return "Success";
        } else {
            createAndSaveTransaction(newTransactionId, bill, billType,"Due", "Due", finalAmount);
            return "Success";
        }
    }

    private void createAndSaveTransaction(int transactionId, Billing bill,String billType, String mode, String status, int amount) {
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
