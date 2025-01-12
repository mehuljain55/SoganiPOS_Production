package com.Soganis.Service;


import com.Soganis.Entity.Store;
import com.Soganis.Entity.TransactionDailyRecordModel;
import com.Soganis.Entity.Transactions;
import com.Soganis.Repository.StoreRepository;
import com.Soganis.Repository.TransactionDailyRecordModelRepo;
import com.Soganis.Repository.TransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionDailyService {

    @Autowired
    private TransactionDailyRecordModelRepo transactionDailyRecordModelRepo;

    @Autowired
    private TransactionsRepository transactionsRepository;

    @Autowired
    private StoreRepository storeRepo;

    public void addTransactionDailyRecord()
    {
        List<Store> stores = storeRepo.findAll()
                .stream()
                .filter(store -> "NX".equals(store.getStoreId()) || "VN".equals(store.getStoreId()))
                .collect(Collectors.toList());

        for(Store store:stores)
        {
            List<Transactions> transactionsList=transactionsRepository.findTransactionByModeDateAndStoreId(new Date(),"Cash",store.getStoreId());
            int openingCash=store.getOpeningCash();

            if(transactionsList.size()>0) {

                int closingCash = transactionsList.stream()
                        .mapToInt(Transactions::getAmount)
                        .sum();

                closingCash=closingCash+openingCash;

                TransactionDailyRecordModel transactionDailyRecordModel = new TransactionDailyRecordModel();
                transactionDailyRecordModel.setDate(new Date());
                transactionDailyRecordModel.setStoreId(store.getStoreId());
                transactionDailyRecordModel.setOpeningCash(openingCash);
                transactionDailyRecordModel.setClosingCash(closingCash);

                store.setOpeningCash(closingCash);
                storeRepo.save(store);
                transactionDailyRecordModelRepo.save(transactionDailyRecordModel);

            }
        }
    }


}
