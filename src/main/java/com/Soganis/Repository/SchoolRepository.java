package com.Soganis.Repository;

import com.Soganis.Entity.SchoolList;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SchoolRepository extends JpaRepository<SchoolList, Integer> {

    @Query("SELECT s.schoolCode FROM SchoolList s WHERE s.schoolName = :schoolName and s.storeId=:storeId")
    String findSchoolCodeBySchoolName(@Param("schoolName") String schoolName,@Param("storeId") String storeId);

    @Query("SELECT s.schoolName FROM SchoolList s where s.storeId=:storeId")
    List<String> findSchoolList(@Param("storeId") String storeId);


    @Query("SELECT s FROM SchoolList s where s.storeId=:storeId")
    List<SchoolList> findSchoolNameCode(@Param("storeId") String storeId);


    @Query("SELECT  s.schoolName  FROM SchoolList s WHERE (:searchTerm IS NULL OR s.schoolName LIKE CONCAT(:searchTerm, '%') OR s.schoolCode LIKE CONCAT(:searchTerm, '%')) AND s.storeId = :storeId ORDER BY s.schoolName ASC")
    List<String> findAllSchool(@Param("searchTerm") String searchTerm, @Param("storeId") String storeId);


}
