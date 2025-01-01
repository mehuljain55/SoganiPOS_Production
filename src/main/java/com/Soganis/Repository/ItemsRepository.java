package com.Soganis.Repository;

import com.Soganis.Entity.Items;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemsRepository extends JpaRepository<Items, Integer> {

    @Query("SELECT i FROM Items i WHERE (:searchTerm IS NULL OR i.itemCode LIKE CONCAT(:searchTerm, '%')) and i.storeId=:storeId ORDER BY i.itemCode ASC, i.itemSize ASC")
    List<Items> findAllFiltered(@Param("searchTerm") String searchTerm,@Param("storeId") String storeId);


    @Query("SELECT i FROM Items i WHERE i.itemSize = :itemSize " +
                                        "and i.itemColor=:itemColor " +
                                        "and i.schoolCode=:schoolCode " +
                                        "and i.itemType=:itemType "+
                                        "and i.storeId = :storeId")
    List<Items> getItemBySchoolCodeTypeSizeColor(@Param("schoolCode") String schoolCode,
                                                 @Param("itemType") String itemType,
                                                 @Param("itemSize") String itemSize,
                                                 @Param("itemColor") String itemColor,
                                                 @Param("storeId") String storeId);



    @Query("SELECT i FROM Items i WHERE i.itemBarcodeID = :itemBarcodeID and i.storeId = :storeId")
    Items getItemByItemBarcodeID(@Param("itemBarcodeID") String itemBarcodeID,@Param("storeId") String storeId);

    @Query("SELECT i FROM Items i WHERE i.group_id = :groupId and i.storeId = :storeId")
    List<Items> getItemByGroupID(@Param("groupId") String groupId,@Param("storeId") String storeId);


    @Query("SELECT i FROM Items i WHERE i.group_id = :groupId AND i.storeId=:storeId")
    List<Items> findItemsByGroupId(@Param("groupId") String groupId,@Param("storeId") String storeId);

    @Query("SELECT i FROM Items i WHERE i.itemCode = :itemCode and i.storeId=:storeId")
    Items findItemsByItemCode(@Param("itemCode") String itemCode,@Param("storeId") String storeId);

    @Query("SELECT i.itemCode FROM Items i WHERE i.itemCategory = :itemCategory and i.itemType=:itemType and i.itemColor=:itemColor and i.storeId=:storeId")
    List<String> findItemBySchoolType(@Param("itemCategory") String itemCategory,
                                      @Param("itemType") String itemType,
                                      @Param("itemColor") String itemColor,
                                      @Param("storeId") String storeId);

    @Query("SELECT DISTINCT i.itemCategory FROM Items i where i.storeId=:storeId ORDER BY i.itemCategory ASC")
    List<String> findDistinctItemCategories(@Param("storeId")String storeId);

    @Query("SELECT DISTINCT i.itemType FROM Items i where i.storeId=:storeId ORDER BY i.itemType ASC")
    List<String> findDistinctItemTypes(@Param("storeId") String storeId);


    @Query("SELECT DISTINCT i.itemSize FROM Items i WHERE (i.itemType = :itemType and i.storeId=:storeId) ORDER BY i.itemSize ASC")
    List<String> findDistinctItemSizeByItemType(@Param("itemType") String itemType,@Param("storeId") String storeId);

    @Query("SELECT DISTINCT i.itemSize FROM Items i WHERE (i.itemType IN :itemType AND i.storeId = :storeId) ORDER BY i.itemSize ASC")
    List<String> findDistinctItemSizeByItemTypeInList(@Param("itemType") List<String> itemType, @Param("storeId") String storeId);

    @Query("SELECT DISTINCT i.itemSize FROM Items i WHERE (i.itemType IN :itemType AND i.storeId = :storeId AND i.generalGroupId='') ORDER BY i.itemSize ASC")
    List<String> findDistinctItemSizeByItemTypeInListNonGroup(@Param("itemType") List<String> itemType, @Param("storeId") String storeId);


    @Query("SELECT DISTINCT i.generalGroupId FROM Items i WHERE (i.itemType=:itemType AND i.storeId = :storeId AND i.generalGroupId!='') ORDER BY i.generalGroupId ASC")
    List<String> findDistinctGroupId(@Param("itemType") String itemType, @Param("storeId") String storeId);


    @Query("SELECT DISTINCT i.itemSize FROM Items i WHERE (i.itemType=:itemType AND i.storeId = :storeId AND i.generalGroupId!='') ORDER BY i.itemSize ASC")
    List<String> findDistinctItemSizeByItemTypeInListGroupData(@Param("itemType") String itemType, @Param("storeId") String storeId);


    @Query("SELECT DISTINCT i.itemTypeCode FROM Items i WHERE i.itemType = :itemType and i.storeId=:storeId")
    String findDistinctItemTypeCode(@Param("itemType") String itemType,@Param("storeId") String storeId);

    @Query("SELECT DISTINCT i.schoolCode FROM Items i WHERE i.itemType = :itemType")
    List<String> findDistinctSchoolCodeByItemType(@Param("itemType") String itemType);

    @Query("SELECT DISTINCT i.itemType FROM Items i where i.itemCategory=:itemCategory and i.storeId=:storeId")
    List<String> findDistinctItemTypesBySchool(@Param("itemCategory") String itemCategory,@Param("storeId") String storeId);

    @Query("SELECT DISTINCT i.itemCategory FROM Items i where i.itemType=:itemType and i.storeId=:storeId")
    List<String> findDistinctSchoolByType(@Param("itemType") String itemType,@Param("storeId") String storeId);

    @Query("SELECT DISTINCT i.itemCategory FROM Items i WHERE i.itemType IN :itemType AND i.storeId = :storeId")
    List<String> findDistinctSchoolByTypeInList(@Param("itemType") List<String> itemType, @Param("storeId") String storeId);

    @Query("SELECT DISTINCT i.itemCategory FROM Items i WHERE i.itemType IN :itemType AND i.storeId = :storeId AND i.generalGroupId=''")
    List<String> findDistinctSchoolByTypeInListNonGroup(@Param("itemType") List<String> itemType, @Param("storeId") String storeId);

    @Query("SELECT DISTINCT i.itemCategory FROM Items i WHERE i.storeId = :storeId AND i.generalGroupId=:groupId")
    List<String> findDistinctSchoolByTypeInListGroupData(@Param("groupId") String groupId, @Param("storeId") String storeId);


    @Query("SELECT  DISTINCT i.itemColor  FROM Items i where i.itemCategory = :itemCategory and i.itemType=:itemType and i.storeId=:storeId")
    List<String> findDistinctItemColor(@Param("itemCategory") String itemCategory,
            @Param("itemType") String itemType,
            @Param("storeId") String storeId);

    @Query("SELECT  DISTINCT i.itemColor  FROM Items i where i.itemCategory = :itemCategory and i.itemType=:itemType and i.storeId=:storeId and i.generalGroupId=''")
    List<String> findDistinctItemColorNonGroup(@Param("itemCategory") String itemCategory,
                                       @Param("itemType") String itemType,
                                       @Param("storeId") String storeId);

    @Query("SELECT  DISTINCT i.itemColor  FROM Items i where i.itemCategory = :itemCategory and i.itemType=:itemType and i.generalGroupId=:groupId and i.storeId=:storeId and i.generalGroupId!=''")
    List<String> findDistinctItemColorGroupData(@Param("itemCategory") String itemCategory,
                                               @Param("itemType") String itemType,
                                                @Param("groupId") String groupId,
                                                @Param("storeId") String storeId);



    @Query("SELECT  DISTINCT i.itemColor  FROM Items i where i.itemCategory = :itemCategory and i.itemType=:itemType")
    List<String> findDistinctItemColorInList(@Param("itemCategory") String itemCategory,
                                       @Param("itemType") String itemType);

    @Query("SELECT i FROM Items i WHERE i.itemCategory = :itemCategory and i.storeId=:storeId ORDER BY i.itemType ASC,i.itemColor ASC, i.itemSize ASC")
    List<Items> findItemsBySchool(@Param("itemCategory") String itemCategory,@Param("storeId") String storeId);

    @Query("SELECT i FROM Items i WHERE i.itemType = :itemType and i.storeId=:storeId ORDER BY i.itemCategory ASC,i.itemColor ASC, i.itemSize ASC")
    List<Items> findItemsByItemType(@Param("itemType") String itemType,@Param("storeId") String storeId);

    @Query("SELECT DISTINCT i.schoolCode FROM Items i where i.itemCategory=:itemCategory and i.storeId=:storeId")
    String findDistinctSchoolCode(@Param("itemCategory") String itemCategory,@Param("storeId") String storeId);

    @Query("SELECT DISTINCT i.schoolCode FROM Items i where i.itemCategory=:itemCategory and i.storeId=:storeId AND i.generalGroupId=''")
    String findDistinctSchoolCodeNonGroup(@Param("itemCategory") String itemCategory,@Param("storeId") String storeId);

    @Query("SELECT DISTINCT i.schoolCode FROM Items i where i.itemCategory=:itemCategory and i.storeId=:storeId AND i.generalGroupId!=''")
    String findDistinctSchoolCodeGroupData(@Param("itemCategory") String itemCategory,@Param("storeId") String storeId);


    @Query("SELECT DISTINCT i.itemType FROM Items i where i.generalGroupId!=''")
    List<String> findDistinctItemTypeListGroupData();



    @Query("SELECT i FROM Items i WHERE i.itemCategory = :itemCategory and i.itemType=:itemType and i.storeId=:storeId")
    List<Items> findItemsBySchoolAndType(@Param("itemCategory") String itemCategory,
                                         @Param("itemType") String itemType,
                                         @Param("storeId")String storeId);

    @Query("SELECT i FROM Items i WHERE i.storeId=:storeId")
    List<Items> getAllItemsStores(@Param("storeId")String storeId);

    @Query("SELECT i FROM Items i WHERE i.itemCode = :itemCode and i.storeId=:storeId")
    List<Items> checkItemCodeForNewItem(@Param("itemCode") String itemCode,@Param("storeId") String storeId);

    @Query("SELECT distinct i.itemColor FROM Items i WHERE i.itemCategory = :itemCategory and i.itemType = :itemType and i.storeId=:storeId  ORDER BY i.itemColor ASC")
    List<String> itemColorBySchoolType(@Param("itemCategory") String itemCategory,
                                      @Param("itemType") String itemType,
                                      @Param("storeId") String storeId);


    @Query("SELECT  i.discount FROM Items i where i.itemBarcodeID=:itemBarcodeID and i.storeId=:storeId")
    String getDiscountStatus(@Param("itemBarcodeID") String itemBarcodeID,@Param("storeId") String storeId);



    @Query("SELECT i FROM Items i WHERE i.schoolCode = :schoolCode AND " +
            "i.itemType = :itemType AND i.itemSize = :itemSize AND " +
            "i.itemColor = :itemColor AND i.storeId = :storeId")
    Items findItemInventoryUpdate(
            @Param("schoolCode") String schoolCode,
            @Param("itemType") String itemType,
            @Param("itemSize") String itemSize,
            @Param("itemColor") String itemColor,
            @Param("storeId") String storeId);


}
