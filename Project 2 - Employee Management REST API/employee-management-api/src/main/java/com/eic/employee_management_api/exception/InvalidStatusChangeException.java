package com.eic.employee_management_api.exception;

public class InvalidStatusChangeException extends RuntimeException
{
    public InvalidStatusChangeException(String message)
    {
        super(message);
    }
}
