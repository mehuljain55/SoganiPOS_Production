package com.Soganis.Repository;

import com.Soganis.Entity.CustomerOrderBook;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerOrderRepo extends JpaRepository<CustomerOrderBook,Integer> {
      @Query("SELECT cob FROM CustomerOrderBook cob WHERE cob.status = :status")
    List<CustomerOrderBook> findByStatus(@Param("status") String status);
}
