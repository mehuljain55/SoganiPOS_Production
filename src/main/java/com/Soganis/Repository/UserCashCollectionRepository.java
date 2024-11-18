package com.Soganis.Repository;

import com.Soganis.Entity.User;
import com.Soganis.Entity.UserCashCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;


public interface UserCashCollectionRepository extends JpaRepository<UserCashCollection,String> {

    @Query("SELECT u FROM UserCashCollection u WHERE u.collection_date BETWEEN :startDate AND :endDate AND u.storeId = :storeId")
    List<UserCashCollection> findByDateRangeAndStoreId(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("storeId") String storeId);

}
