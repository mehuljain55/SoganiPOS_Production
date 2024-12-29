package com.Soganis.Repository;

import com.Soganis.Entity.InventoryUpdateHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InventoryUpdateHistoryRepo extends JpaRepository<InventoryUpdateHistory,Integer> {

    @Query("SELECT i FROM InventoryUpdateHistory i WHERE i.storeId = :storeId ORDER BY i.date DESC, i.sno DESC")
    List<InventoryUpdateHistory> findLatestRecord(@Param("storeId") String storeId);


}
