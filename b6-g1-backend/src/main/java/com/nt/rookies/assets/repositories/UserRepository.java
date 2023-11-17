package com.nt.rookies.assets.repositories;

import com.nt.rookies.assets.entities.Role;
import com.nt.rookies.assets.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, String> {
    @Query("select b from User b where b.active = true and b.locationId.locationId = ?1 and b.role= ?2 and (( UPPER(b.firstName) like CONCAT('%',?3,'%') and UPPER(b.lastName) like CONCAT('%',?4,'%'))  or  UPPER(b.staffCode) like CONCAT('%',?5,'%'))")
    Page<User> findByFilterAndSearchTermAndSorting
            (Integer locationId, Role role, String searchFirstName, String searchLastName, String searchStaffCode, Pageable pageable);

    @Query("select b from User b where b.active = true and b.locationId.locationId = ?1 and (( UPPER(b.firstName) like CONCAT('%',?2,'%') and UPPER(b.lastName) like CONCAT('%',?3,'%'))  or  UPPER(b.staffCode) like CONCAT('%',?4,'%'))")
    Page<User> findBySearchTermAndSorting
            (Integer locationId, String searchFirstName, String searchLastName, String searchStaffCode, Pageable pageable);

    public Optional<User> findByStaffCode(String staffCode);

    public Optional<User> findByUsername(String username);

    public List<User> findAll();

    @Query("SELECT u FROM User u " +
            "WHERE UPPER(u.username) LIKE UPPER(CONCAT(?1, '%')) " +
            "ORDER BY u.updatedAt DESC")
    List<User> findUsersContainUsername(String username);

}
