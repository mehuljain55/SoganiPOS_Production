package com.Soganis.Repository;

import com.Soganis.Entity.User;
import com.Soganis.Entity.UserCashCollection;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserCashCollectionRepository extends JpaRepository<UserCashCollection,String> {
    
}
