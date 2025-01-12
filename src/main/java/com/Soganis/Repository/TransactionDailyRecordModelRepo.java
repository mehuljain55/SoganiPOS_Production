package com.Soganis.Repository;


import com.Soganis.Entity.Billing;
import com.Soganis.Entity.TransactionDailyRecordKey;
import com.Soganis.Entity.TransactionDailyRecordModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface TransactionDailyRecordModelRepo extends JpaRepository<TransactionDailyRecordModel, TransactionDailyRecordKey> {

    @Query("SELECT t FROM TransactionDailyRecordModel t WHERE   t.date = :date AND  t.storeId=:storeId")
    List<TransactionDailyRecordModel> findTransactionByStoreDate(@Param("date") Date date, @Param("storeId") String storeId);


}
