package com.Soganis.Repository;

import com.Soganis.Entity.UserMonthlySalary;
import com.Soganis.Entity.UserMonthlySalaryId;
import com.Soganis.Entity.User_Salary;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserMonthlySalaryRepository extends JpaRepository<UserMonthlySalary,UserMonthlySalaryId> {
    
}
