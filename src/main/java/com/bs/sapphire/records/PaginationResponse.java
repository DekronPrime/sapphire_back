package com.bs.sapphire.records;

import java.io.Serializable;
import java.util.List;

public record PaginationResponse<T>(
    List<T> content,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean hasNext
) implements Serializable {
}
