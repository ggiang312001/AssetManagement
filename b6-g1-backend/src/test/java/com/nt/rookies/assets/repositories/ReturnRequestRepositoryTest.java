package com.nt.rookies.assets.repositories;

import com.nt.rookies.assets.entities.Assignment;
import com.nt.rookies.assets.entities.ReturnRequest;
import com.nt.rookies.assets.entities.ReturnRequestState;
import com.nt.rookies.assets.entities.User;
import com.nt.rookies.assets.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase
public class ReturnRequestRepositoryTest {
    private ReturnRequestRepository returnRequestRepository;
    private TestEntityManager testEntityManager;

    @Autowired
    public ReturnRequestRepositoryTest(ReturnRequestRepository returnRequestRepository, TestEntityManager testEntityManager) {
        this.returnRequestRepository = returnRequestRepository;
        this.testEntityManager = testEntityManager;
    }

    @Test
    public void testFindById() {
        int request_id = 1;
        ReturnRequest returnRequest =  returnRequestRepository.findById(request_id).orElseThrow(()-> new NotFoundException("Request with id "+ request_id + "not found"));
        assertEquals(1, returnRequest.getRequestId());

    }
    @Test
    public void testSaveReturnRequest(){
        ReturnRequest returnRequest  = new ReturnRequest(1, new Assignment(), "note", LocalDateTime.now(), new User(), new User(), LocalDateTime.now(), ReturnRequestState.COMPLETED);
        returnRequest.setRequestId(1);
        returnRequest.setAssignmentId(new Assignment());
        returnRequest.setRequestNote("note");
        returnRequest.setCreatedAt(LocalDateTime.now());
        returnRequest.setCreatedBy(new User());
        returnRequest.setReturnedDate(LocalDateTime.now());
        returnRequest.setState(ReturnRequestState.COMPLETED);

        returnRequestRepository.save(returnRequest);
        assertThat(returnRequest.getRequestId()).isEqualTo(1);
    }
}
