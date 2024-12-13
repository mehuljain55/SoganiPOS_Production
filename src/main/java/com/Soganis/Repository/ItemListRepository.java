package com.Soganis.Repository;

import com.Soganis.Entity.ItemList;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemListRepository extends JpaRepository<ItemList, Integer> {
   @Query("SELECT i.itemTypeCode FROM ItemList i WHERE i.description = :description and i.storeId=:storeId")
    String findItemTypeCodeByDescription(@Param("description") String description,@Param("storeId") String storeId);
    
    @Query("SELECT i.description FROM ItemList i where i.storeId=:storeId ORDER BY i.description ASC")
    List<String> findItemType(@Param("storeId") String storeId);

    @Query("SELECT i FROM ItemList i where i.storeId=:storeId")
    List<ItemList> findItemListByStoreId(@Param("storeId") String storeId);


}


