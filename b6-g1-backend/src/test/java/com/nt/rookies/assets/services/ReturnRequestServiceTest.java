package com.nt.rookies.assets.services;

import com.nt.rookies.assets.dtos.*;
import com.nt.rookies.assets.entities.*;
import com.nt.rookies.assets.repositories.ReturnRequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ReturnRequestServiceTest {

    @Mock
    private ReturnRequestRepository returnRequestRepository;

    @Mock
    private ReturnRequestService returnRequestService;
    private static ReturnRequestDto returnRequestDto = new ReturnRequestDto(1, new Assignment(), "note", LocalDateTime.now(), new UserDto(), LocalDateTime.now(), ReturnRequestState.COMPLETED, new UserDto());
    private static ReturnRequestResponse returnRequestResponse = new ReturnRequestResponse(new ArrayList<>(), 1,10, 10, 10, true);
    @Test
    public void testCreateReturnRequest() {
        Integer assigmentId = 1;
        String note = "note";
        String username = "vangdv";

        Mockito.when(returnRequestService.createReturnRequest(assigmentId, username)).thenReturn(returnRequestDto);

        ReturnRequestDto result = returnRequestService.createReturnRequest(assigmentId, username);
        Mockito.verify(returnRequestService, Mockito.times(1)).createReturnRequest(assigmentId, username);

        assertThat(result.getRequestId()).isEqualTo(assigmentId);

    }

    @Test
    public void testAcceptReturnRequest() {
        Integer returnRequestId = 1;
        String acceptedBy = "binhnv";

        //ReturnRequestRequestDto returnRequestRequestDto = new ReturnRequestRequestDto(1, "note");
        Mockito.when(returnRequestService.acceptReturnRequest(returnRequestId, acceptedBy)).thenReturn(returnRequestDto);

        ReturnRequestDto result = returnRequestService.acceptReturnRequest(returnRequestId, acceptedBy);
        Mockito.verify(returnRequestService, Mockito.times(1)).acceptReturnRequest(returnRequestId, acceptedBy);

        assertThat(result.getRequestId()).isEqualTo(returnRequestId);

    }
}
