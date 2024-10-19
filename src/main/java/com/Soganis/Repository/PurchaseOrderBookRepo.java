package com.Soganis.Repository;

import com.Soganis.Entity.PurchaseOrderBook;
import com.Soganis.Entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PurchaseOrderBookRepo extends JpaRepository<PurchaseOrderBook,Integer> {
 
     @Query("SELECT p FROM PurchaseOrderBook p WHERE p.status='Not generated' and p.storeId=:storeId")
    List<PurchaseOrderBook> findItemsWithStatusNotGenerated(@Param("storeId") String storeId);
    
}
