package com.bs.sapphire.exceptions;

import java.io.Serializable;

public record ErrorResponse(
        int status,
        String message,
        String errorCode
) implements Serializable {
}
