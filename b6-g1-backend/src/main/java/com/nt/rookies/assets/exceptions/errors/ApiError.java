package com.nt.rookies.assets.exceptions.errors;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ApiError {
    private LocalDateTime timestamp =  LocalDateTime.now();
    private int status;
    private String message;
    private String details;

    private Map<String, String> validationErrors;

    public ApiError(int status, String message, String details) {
        this.status = status;
        this.message = message;
        this.details = details;
    }


}
