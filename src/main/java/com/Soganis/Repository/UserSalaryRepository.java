package com.Soganis.Repository;

import com.Soganis.Entity.User_Salary;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserSalaryRepository extends JpaRepository<User_Salary,Integer> {
    
      @Query("SELECT u FROM User_Salary u WHERE u.userId = :userId " +
           "AND YEAR(u.date) = :year " +
           "AND MONTH(u.date) = :month")
    List<User_Salary> findUserSalaryByYearMonth(@Param("userId") String userId,
                                                @Param("year") int year,
                                                @Param("month") int month);
    
}
