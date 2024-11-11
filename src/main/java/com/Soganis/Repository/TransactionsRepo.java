package com.Soganis.Repository;

import com.Soganis.Entity.Store;
import com.Soganis.Entity.TransactionId;
import com.Soganis.Entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionsRepo  extends JpaRepository<Transactions, TransactionId> {
}
