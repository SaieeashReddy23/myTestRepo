package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PaginatedResponseDTO<T> {
    private List<T> content;
    private int totalPages;
    private long totalElements;
    private int currentPage;
    private int pageSize;
    private boolean last;
    private boolean first;
}
