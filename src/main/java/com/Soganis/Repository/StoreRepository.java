package com.Soganis.Repository;

import com.Soganis.Entity.Store;
import com.Soganis.Entity.UserCashCollection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository  extends JpaRepository<Store,String> {
}
