package com.Soganis.Repository;

import com.Soganis.Entity.BillingModel;
import com.Soganis.Entity.Items;
import com.Soganis.Entity.User;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BillingModelRepository extends JpaRepository<BillingModel, Integer> {

    @Query("SELECT b FROM BillingModel b WHERE b.itemCategory = :itemCategory AND b.bill_date BETWEEN :startDate AND :endDate")
    List<BillingModel> findBySchoolAndDateRange(
            @Param("itemCategory") String itemCategory,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

    @Query("SELECT b FROM BillingModel b WHERE b.itemCategory = :itemCategory")
    List<BillingModel> findBySchool(@Param("itemCategory") String itemCategory);

    @Query("SELECT b FROM BillingModel b WHERE b.itemType = :itemType")
    List<BillingModel> findByItemType(@Param("itemType") String itemType);
    
    
    @Query("SELECT b FROM BillingModel b WHERE  b.bill_date BETWEEN :startDate AND :endDate")
    List<BillingModel> findByDateRange(@Param("startDate") Date startDate,@Param("endDate") Date endDate);

    @Query("SELECT b FROM BillingModel b WHERE b.itemType = :itemType AND b.bill_date BETWEEN :startDate AND :endDate")
    List<BillingModel> findByItemTypeAndDate(
            @Param("itemType") String itemType,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

    @Query("SELECT b FROM BillingModel b WHERE b.itemType = :itemType and b.itemCategory=:itemCategory   AND b.bill_date BETWEEN :startDate AND :endDate")
    List<BillingModel> findBySchoolAndItemTypeDate(
            @Param("itemType") String itemType,
            @Param("itemCategory") String itemCategory,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);
    
     @Query("SELECT b FROM BillingModel b WHERE b.itemType = :itemType and b.itemCategory=:itemCategory")
    List<BillingModel> findBySchoolAndItemType(@Param("itemType") String itemType,@Param("itemCategory") String itemCategory);

}
