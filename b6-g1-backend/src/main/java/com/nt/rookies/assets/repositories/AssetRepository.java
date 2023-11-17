package com.nt.rookies.assets.repositories;

import com.nt.rookies.assets.entities.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetRepository extends PagingAndSortingRepository<Asset, Integer> {

    /**
     * Find All asset by locationID have paging
     *
     * @param locationId request locationId of asset
     * @param pageable   request paging(page size, page number)
     * @return Page<Asset>
     */
    @Query("select a from Asset a where a.locationId.locationId = ?1 and a.state != 'RECYCLED' ")
    Page<Asset> findAll(Integer locationId, Pageable pageable);

    /**
     * Search asset by assetId or assetName and locationId have paging
     *
     * @param locationId request locationId of asset
     * @param searchTerm request keyword to search asset
     * @param pageable   request paging(page size, page number)
     * @return Page<Asset>
     */
    @Query("select a from Asset a where a.locationId.locationId = ?1 and (lower(a.assetId) like lower(concat('%', ?2, '%')) or lower(a.name) like lower(concat('%', ?2, '%'))) and a.state != 'RECYCLED'  ")
    Page<Asset> findBySearch(Integer locationId, String searchTerm, Pageable pageable);

    /**
     * Fillter asset by category and locationId have paging
     *
     * @param locationId request locationId of asset
     * @param cateFill   request categoryId to fillter asset
     * @param pageable   request paging(page size, page number)
     * @return Page<Asset>
     */
    @Query("select a from Asset a where a.locationId.locationId = ?1 and a.category.categoryId = ?2 and a.state != 'RECYCLED' ")
    Page<Asset> findByCategory(Integer locationId, Integer cateFill, Pageable pageable);

    /**
     * Fillter asset by state and locationId have paging
     *
     * @param locationId request locationId of asset
     * @param stateFill  request asset state to fillter asset
     * @param pageable   request paging(page size, page number)
     * @return Page<Asset>
     */
    @Query("select a from Asset a where a.locationId.locationId = ?1 and lower(a.state) like lower(?2) ")
    Page<Asset> findByState(Integer locationId, String stateFill, Pageable pageable);

    /**
     * Fillter asset by category, state and locationId have paging
     *
     * @param locationId request locationId of asset
     * @param cateFill   request categoryId to fillter asset
     * @param stateFill  request asset state to fillter asset
     * @param pageable   request paging(page size, page number)
     * @return Page<Asset>
     */
    @Query("select a from Asset a where a.locationId.locationId = ?1 and a.category.categoryId = ?2 " +
            "and lower(a.state) like lower(?3) ")
    Page<Asset> findByCategoryAndState(Integer locationId, Integer cateFill, String stateFill, Pageable pageable);

    /**
     * Search asset by assetId or assetName and fillter by category and locationId have paging
     *
     * @param locationId request locationId of asset
     * @param searchTerm request keyword to search asset
     * @param cateFill   request categoryId to fillter asset
     * @param pageable   request paging(page size, page number)
     * @return Page<Asset>
     */
    @Query("select a from Asset a where a.locationId.locationId = ?1 and a.category.categoryId = ?3 " +
            "and (lower(a.assetId) like lower(concat('%', ?2, '%')) or lower(a.name) like lower(concat('%', ?2, '%'))) and a.state != 'RECYCLED' ")
    Page<Asset> findByCategoryAndSearch
    (Integer locationId, String searchTerm, Integer cateFill, Pageable pageable);

    /**
     * Search asset by assetId or assetName and fillter by state and locationId have paging
     *
     * @param locationId request locationId of asset
     * @param searchTerm request keyword to search asset
     * @param stateFill  request asset state to fillter asset
     * @param pageable   request paging(page size, page number)
     * @return Page<Asset>
     */
    @Query("select a from Asset a where a.locationId.locationId = ?1 and lower(a.state) like lower(?3) " +
            "and (lower(a.assetId) like lower(concat('%', ?2, '%')) or lower(a.name) like lower(concat('%', ?2, '%'))) ")
    Page<Asset> findByStateAndSearch
    (Integer locationId, String searchTerm, String stateFill, Pageable pageable);

    /**
     * Search asset by assetId or assetName and fillter by state, category and locationId have paging
     *
     * @param locationId request locationId of asset
     * @param searchTerm request keyword to search asset
     * @param cateFill   request categoryId to fillter asset
     * @param stateFill  request asset state to fillter asset
     * @param pageable   request paging(page size, page number)
     * @return Page<Asset>
     */
    @Query("select a from Asset a where a.locationId.locationId = ?1 and a.category.categoryId = ?3 " +
            "and lower(a.state) like lower(?4) and (lower(a.assetId) like lower(concat('%', ?2, '%')) or lower(a.name) like lower(concat('%', ?2, '%')))")
    Page<Asset> findAll
    (Integer locationId, String searchTerm, Integer cateFill, String stateFill, Pageable pageable);

    /**
     * Find asset by assetId
     *
     * @param id assetId
     * @return Asset
     */
    public Optional<Asset> findByAssetId(String id);

    public List<Asset> findAll();

    /**
     * Count number of asset by category and state
     * @param categoryId id of category
     * @param state state of asset
     * @return Integer
     */
    @Query("select count(a.category.categoryId) from Asset a where a.category.categoryId = ?1 and a.state = upper(?2) and a.locationId.locationId = ?3 group by a.category, a.state")
    public Optional<Integer> getQuantity(Integer categoryId, String state, Integer locationId);

    /**
     * Count total asset was ever assigned to anyone
     * @param assetId id of asset
     * @return Integer
     */
    @Query("SELECT count(a) FROM Assignment a where a.state = 'DELETED' and lower(a.assetId.assetId) = lower(?1) ")
    public Optional<Integer> getHistoryAsset(String assetId);

}
