package com.Soganis.Repository;

import com.Soganis.Entity.Billing;
import com.Soganis.Entity.BillingId;
import com.Soganis.Entity.Items;
import com.Soganis.Entity.User;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BillingRepository extends JpaRepository<Billing, BillingId> {
    
    @Query("SELECT b FROM Billing b WHERE b.userId = :userId AND b.bill_date = :billDate AND b.paymentMode!='Due' AND b.storeId=:storeId")
    List<Billing> findByUserIdAndBillDate(@Param("userId") String userId, @Param("billDate") Date billDate,@Param("storeId") String storeId);
    
    @Query("SELECT b FROM Billing b WHERE b.billNo = :billNo and b.storeId=:storeId")
    Billing getBillByNo(@Param("billNo") int billNo,@Param("storeId") String storeId);

    @Query("SELECT b FROM Billing b WHERE b.customerMobileNo = :customerMobileNo and b.storeId = :storeId and (b.billType != 'Return' and b.billType != 'Defected Item')")
    List<Billing> getBillByMobileNo(@Param("customerMobileNo") String customerMobileNo, @Param("storeId") String storeId);


    @Query("SELECT MAX(b.billNo) FROM Billing b WHERE b.storeId = :storeId")
    Integer findMaxBillNoByStoreId(@Param("storeId") String storeId);

    @Query("SELECT b FROM Billing b WHERE b.bill_date BETWEEN :startDate AND :endDate AND b.storeId = :storeId")
    List<Billing> findByBillDateBetweenAndStoreId(@Param("startDate") Date startDate,
                                                  @Param("endDate") Date endDate,
                                                  @Param("storeId") String storeId);
    
}
