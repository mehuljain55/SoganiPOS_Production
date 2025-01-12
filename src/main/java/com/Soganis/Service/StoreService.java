package com.Soganis.Service;

import com.Soganis.Entity.Store;
import com.Soganis.Entity.TransactionDueListRetail;
import com.Soganis.Entity.Transactions;
import com.Soganis.Repository.StoreRepository;
import com.Soganis.Repository.TransactionDueRepo;
import com.Soganis.Repository.TransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private TransactionDueRepo transactionDueRepo;

    @Autowired
    private TransactionsRepository transactionsRepository;



    public Map<String,Integer> getStoreInfo(String storeId) {
        Optional<Store> optionalStore = storeRepository.findById(storeId);
        if (optionalStore.isPresent()) {
            Store store = optionalStore.get();

            List<TransactionDueListRetail> transactionDueListRetails = transactionDueRepo.findDueListByStoreId(store.getStoreId());
            List<Transactions> transactionsCashList = transactionsRepository.findTransactionByModeDateAndStoreId(new Date(), "Cash", store.getStoreId());
            List<Transactions> transactionsUpiList = transactionsRepository.findTransactionByModeDateAndStoreId(new Date(), "Upi", store.getStoreId());
            List<Transactions> transactionsCardList = transactionsRepository.findTransactionByModeDateAndStoreId(new Date(), "Card", store.getStoreId());

            int todayCashCollection = transactionsCashList.isEmpty() ? 0 : transactionsCashList.stream()
                    .mapToInt(Transactions::getAmount)
                    .sum();

            int todayUpiCollection = transactionsUpiList.isEmpty() ? 0 : transactionsUpiList.stream()
                    .mapToInt(Transactions::getAmount)
                    .sum();

            int todayCardCollection = transactionsCardList.isEmpty() ? 0 : transactionsCardList.stream()
                    .mapToInt(Transactions::getAmount)
                    .sum();

            int totalRetailDue = transactionDueListRetails.isEmpty() ? 0 : transactionDueListRetails.stream()
                    .mapToInt(TransactionDueListRetail::getAmount)
                    .sum();

            // Prepare the result map
            Map<String, Integer> result = new HashMap<>();
            result.put("cash", todayCashCollection);
            result.put("upi", todayUpiCollection);
            result.put("card", todayCardCollection);
            result.put("retailDue", totalRetailDue);

            return result;

        }
    return null;
    }

}
