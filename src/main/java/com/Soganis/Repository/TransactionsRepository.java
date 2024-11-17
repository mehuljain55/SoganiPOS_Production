package com.Soganis.Repository;

import com.Soganis.Entity.TransactionId;
import com.Soganis.Entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransactionsRepository extends JpaRepository<Transactions, TransactionId> {

    @Query("SELECT MAX(t.transactionId) FROM Transactions t WHERE t.storeId = :storeId")
    Integer findMaxransactionIdByStoreId(String storeId);

}
