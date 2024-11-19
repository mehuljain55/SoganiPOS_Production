package com.Soganis.Repository;

import com.Soganis.Entity.Store;
import com.Soganis.Entity.UserCashCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreRepository  extends JpaRepository<Store,String> {

    @Query("SELECT s.mobileNo FROM Store s WHERE  s.storeId=:storeId")
    String getStoreContact(@Param("storeId") String storeId);

}
