package com.nt.rookies.assets.repositories;

import com.nt.rookies.assets.entities.*;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class UserRepositoryTests {

    private static UserRepository userRepository;

    private TestEntityManager entityManager;

    @Autowired
    public UserRepositoryTests(UserRepository userRepository, TestEntityManager entityManager) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.entityManager = entityManager;
    }

    @Test
    public void testFindByFilterAndSearchTermAndSorting(){
        int locationId = 1;
        Pageable pageable = PageRequest.of(1,10, Sort.by(Sort.Direction.ASC,"staffCode"));
        Role role = Role.valueOf("ADMIN");
        String searchFirstName ="Vang";
        String searchLastName ="Do Van";
        String searchStaffCode="SD0000";
        List<User> actual = userRepository.findByFilterAndSearchTermAndSorting(locationId,role,searchFirstName,searchLastName,searchStaffCode,pageable).getContent();

        assertNotNull(actual);
    }

    @Test
    public void testFindBySearchTermAndSorting(){
        int locationId = 1;
        Pageable pageable = PageRequest.of(1,10, Sort.by(Sort.Direction.ASC,"staffCode"));
        String searchFirstName ="Vang";
        String searchLastName ="Do Van";
        String searchStaffCode="SD0000";
        List<User> actual = userRepository.findBySearchTermAndSorting(locationId,searchFirstName,searchLastName,searchStaffCode,pageable).getContent();

        assertNotNull(actual);
    }

    @Test
    public void testSaveUser() {
        User user1 = new User("vangdv",
                "$2a$10$AeTtdbOZ2EM47Sykk5Nx8eGBNqihHC5YIoXkmesaqIklgYPtInNzi",
                "Vang", "Do Van", LocalDate.now(), Gender.MALE, "vangdo@gmail.com",
                LocalDateTime.now(), "root", true, entityManager.find(Location.class, 1), Role.ADMIN, true);
        User savedUser1 = userRepository.save(user1);
        assertThat(savedUser1.getUsername()).isEqualTo(user1.getUsername());

        User user2 = new User("vangdv32",
                "$2a$10$AeTtdbOZ2EM47Sykk5Nx8eGBNqihHC5YIoXkmesaqIklgYPtInNzi",
                "Vang", "Do Van", LocalDate.now(), Gender.MALE, "vangdo@gmail.com",
                LocalDateTime.now(), "root", true, entityManager.find(Location.class, 1), Role.ADMIN, true);
        User savedUser2 = userRepository.save(user2);
        assertThat(savedUser2.getUsername()).isEqualTo(user2.getUsername());
    }

    @Test
    public void testFindByUsername() {
        Optional<User> user = userRepository.findByUsername("vangdv");
        assertThat(user.get().getUsername()).isEqualTo("vangdv");
    }

    @Test
    public void testFindAll() {
        List<User> users = userRepository.findAll();
        users.forEach(u -> System.out.println(u.getUsername()));
    }

    @Test
    public void testFindUsersContainUsername() {
        User user1 = new User("chungbui",
                "password",
                "Chung", "Bui", LocalDate.now(), Gender.MALE, "chungbui@gmail.com",
                LocalDateTime.now(), "root", true,
                entityManager.find(Location.class, 1),
                Role.ADMIN, true, LocalDateTime.parse("2022-08-04T10:11:30"));
        User user2 = new User("chungbui1",
                "password1",
                "Chung", "Bui", LocalDate.now(), Gender.MALE, "chungbui1@gmail.com",
                LocalDateTime.now(), "root", true,
                entityManager.find(Location.class, 1),
                Role.ADMIN, true, LocalDateTime.now());

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> users = userRepository.findUsersContainUsername("chungbui");
        assertThat(users.get(0).getUsername()).isEqualTo("chungbui1");
    }

}
