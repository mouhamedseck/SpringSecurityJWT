
package com.mseck.exception;

import lombok.Data;

@Data
public class ErrorResponseDTO extends Exception{
    private String message;

    public ErrorResponseDTO(String message) {
        this.message = message;
    }

   
}

