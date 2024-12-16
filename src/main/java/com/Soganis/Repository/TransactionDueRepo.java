package com.Soganis.Repository;

import com.Soganis.Entity.TransactionDueListRetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionDueRepo extends JpaRepository<TransactionDueListRetail,Integer>
{
    @Query("SELECT t FROM TransactionDueListRetail t WHERE  t.storeId=:storeId AND t.status='Due'")
    List<TransactionDueListRetail> findDueListByStoreId(@Param("storeId") String storeId);

    @Query("SELECT t FROM TransactionDueListRetail t WHERE t.billNo=:billNo AND t.storeId=:storeId")
    TransactionDueListRetail findDueListByBillNoStoreId(@Param("billNo") int billNo,
                                                              @Param("storeId") String storeId);

}
