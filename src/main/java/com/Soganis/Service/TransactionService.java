package com.Soganis.Service;

import com.Soganis.Entity.Billing;
import com.Soganis.Entity.TransactionDueListRetail;
import com.Soganis.Entity.Transactions;
import com.Soganis.Entity.InterCompanyPayments;
import com.Soganis.Model.TransactionModel;
import com.Soganis.Repository.InterompanyPaymentRepository;
import com.Soganis.Repository.TransactionDueRepo;
import com.Soganis.Repository.TransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TransactionService {

    @Autowired
    private TransactionsRepository transactionRepo;

    @Autowired
    private InterompanyPaymentRepository interCompanyPaymentRepo;

    @Autowired
    private TransactionDueRepo transactionDueRepo;

    public String createTransactionRetail(Billing bill,String billType, TransactionModel transactionModel, String storeId) {
        Integer lastTransactionId = transactionRepo.findMaxransactionIdByStoreId(storeId);

        // Start new transaction ID
        int newTransactionId = (lastTransactionId == null) ? 1 : lastTransactionId + 1;


        String paymentMode = bill.getPaymentMode();
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
            TransactionDueListRetail record=new TransactionDueListRetail();
            record.setBillNo(bill.getBillNo());
            record.setCustomerName(bill.getCustomerName());
            record.setCustomerMobileNo(bill.getCustomerMobileNo());
            record.setAmount(finalAmount);
            record.setDate(new Date());
            record.setStoreId(bill.getStoreId());
            record.setStatus("Due");
            transactionDueRepo.save(record);
            return "Success";
        }
    }


    public String createTransactionWholesale(Billing bill,String billType, String storeId) {
        Integer lastTransactionId = transactionRepo.findMaxransactionIdByStoreId(storeId);

        // Start new transaction ID
        int newTransactionId = (lastTransactionId == null) ? 1 : lastTransactionId + 1;

        String paymentMode = bill.getPaymentMode();
        int finalAmount = bill.getFinal_amount();

        if (paymentMode.equals("Cash") || paymentMode.equals("Card") || paymentMode.equals("Upi")) {
            createAndSaveTransaction(newTransactionId, bill,billType, paymentMode, "Paid", finalAmount);
            return "Success";
        }
        else if(paymentMode.equals("Due")) {
            InterCompanyPayments interCompanyPayments=new InterCompanyPayments();
            interCompanyPayments.setDescription("Payment  of bill no: "+bill.getBillNo());
            interCompanyPayments.setDate(new Date());
            interCompanyPayments.setStatus("Due");
            interCompanyPayments.setAmount(bill.getFinal_amount());
            interCompanyPayments.setBillBy(storeId);
            interCompanyPayments.setType("Bill");
            interCompanyPayments.setBillTo(bill.getCustomerName());
            interCompanyPayments.setStore_id(storeId);
            interCompanyPaymentRepo.save(interCompanyPayments);
            return "Success";
        }
        else {
            return "Invalid Payment mode";
        }
    }


    public String createTransactionExchange(Billing bill,String billType, String storeId) {
        Integer lastTransactionId = transactionRepo.findMaxransactionIdByStoreId(storeId);

        // Start new transaction ID
        int newTransactionId = (lastTransactionId == null) ? 1 : lastTransactionId + 1;
        String paymentMode = bill.getPaymentMode();
        int finalAmount = bill.getFinal_amount();

        if (paymentMode.equals("Cash") || paymentMode.equals("Card") || paymentMode.equals("Upi")) {

            if(finalAmount>=0)
            {
                String description="Balance amount recevied for exchange";
                createAndSaveTransactionExchange(newTransactionId,description,bill,billType,paymentMode,"Paid",finalAmount);
                return "Success";
            }
            else {
                String description="Refund balance amount for exchange";
                createAndSaveTransactionExchange(newTransactionId,description,bill,billType,"Cash","Paid",finalAmount);
                return "Success";
            }

        }
        else {
            return "Invalid payment mode";
        }
    }




    public String createTreansaction(Transactions transaction,String storeId)
    {
        try{
            Integer lastTransactionId = transactionRepo.findMaxransactionIdByStoreId(storeId);
            int newTransactionId = (lastTransactionId == null) ? 1 : lastTransactionId + 1;
            transaction.setTransactionId(newTransactionId);
            transactionRepo.save(transaction);
            return "Success";


        }catch (Exception e)
        {
            e.printStackTrace();
            return "Failed";
        }
    }

    private void createAndSaveTransactionExchange(int transactionId, String description ,Billing bill,String billType, String mode, String status, int amount) {
        System.out.println(bill);
        Transactions transaction = new Transactions();
        transaction.setTransactionId(transactionId);
        transaction.setDescription(description);
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
