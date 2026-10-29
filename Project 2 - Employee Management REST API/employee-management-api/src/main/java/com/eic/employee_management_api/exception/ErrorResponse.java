package com.eic.employee_management_api.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL) // Only Non Null values are displayed when record is returned
public record ErrorResponse(LocalDateTime timestamp,
                            int status,
                            String error,
                            String message,
                            String path,
                            Map <String, String> details) // Key-Value Pair -> (Field, Message for that field)
{
}
