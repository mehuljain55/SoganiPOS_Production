package com.Soganis.Repository;

import com.Soganis.Entity.TransactionDueListRetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionDueRepo extends JpaRepository<TransactionDueListRetail,Integer> {
}
