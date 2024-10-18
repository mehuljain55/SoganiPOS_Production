/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.Soganis.Repository;

import com.Soganis.Entity.Billing;
import com.Soganis.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface UserRepository extends JpaRepository<User,String> {

    @Query("SELECT i FROM User i WHERE i.storeId = :storeId")
    List<User> getUserByStoreId(@Param("storeId") String storeId);


}
