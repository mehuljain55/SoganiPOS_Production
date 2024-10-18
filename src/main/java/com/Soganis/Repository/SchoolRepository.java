package com.Soganis.Repository;

import com.Soganis.Entity.SchoolList;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SchoolRepository extends JpaRepository<SchoolList, Integer> {

    @Query("SELECT s.schoolCode FROM SchoolList s WHERE s.schoolName = :schoolName")
    String findSchoolCodeBySchoolName(@Param("schoolName") String schoolName);

    @Query("SELECT s.schoolName FROM SchoolList s where s.storeId=:storeId")
    List<String> findSchoolList(@Param("storeId") String storeId);

}
