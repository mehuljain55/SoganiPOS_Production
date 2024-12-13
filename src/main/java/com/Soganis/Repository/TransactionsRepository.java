package com.Soganis.Repository;

import com.Soganis.Entity.Billing;
import com.Soganis.Entity.TransactionId;
import com.Soganis.Entity.Transactions;
import com.Soganis.Entity.UserCashCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface TransactionsRepository extends JpaRepository<Transactions, TransactionId> {

    @Query("SELECT MAX(t.transactionId) FROM Transactions t WHERE t.storeId = :storeId")
    Integer findMaxransactionIdByStoreId(String storeId);


    @Query("SELECT t FROM Transactions t WHERE t.date BETWEEN :startDate AND :endDate AND t.storeId = :storeId")
    List<Transactions> findTransactionByDateRangeAndStoreId(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("storeId") String storeId);

    @Query("SELECT t FROM Transactions t WHERE t.billNo=:billNo AND t.storeId = :storeId")
    List<Transactions> findTransactionByBill(
            @Param("billNo") int billNo,
            @Param("storeId") String storeId);

    @Query("SELECT t FROM Transactions t WHERE t.userId = :userId AND t.date = :billDate AND t.status='Paid' AND t.storeId=:storeId")
    List<Transactions> findByUserIdAndBillDate(@Param("userId") String userId, @Param("billDate") Date billDate, @Param("storeId") String storeId);


}
