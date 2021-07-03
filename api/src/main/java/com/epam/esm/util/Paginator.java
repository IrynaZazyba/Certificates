package com.epam.esm.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * The class {@code Paginator} allows to realize pagination,
 * introduce default values to currentPage and recordsPerPages
 * parameters
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@Builder
public class Paginator {

    private static final int DEFAULT_RECORDS_PER_PAGE = 10;
    private static final int DEFAULT_CURRENT_PAGE = 1;

    private Integer currentPage;
    private Integer recordsPerPage;
    private Integer start;
    private Integer countPages;

    public Paginator(Integer recordsPerPage, Integer currentPage) {
        setCurrentPage(currentPage);
        setRecordsPerPage(recordsPerPage);
        setStart();
    }

    public void setCountPages(Integer countBooksRecords) {
        int nOfPages = countBooksRecords / recordsPerPage;
        if (countBooksRecords % recordsPerPage > 0) {
            nOfPages++;
        }
        this.countPages = nOfPages;
    }

    private void setStart() {
        this.start = this.currentPage * this.recordsPerPage - this.recordsPerPage;
    }

    private void setCurrentPage(Integer currentPage) {
        if (currentPage != null) {
            this.currentPage = currentPage > 0 ? currentPage : DEFAULT_CURRENT_PAGE;
        } else {
            this.currentPage = DEFAULT_CURRENT_PAGE;
        }
    }

    private void setRecordsPerPage(Integer recordsPerPage) {
        if (recordsPerPage != null) {
            this.recordsPerPage = recordsPerPage > 0 ? recordsPerPage : DEFAULT_RECORDS_PER_PAGE;
        } else {
            this.recordsPerPage = DEFAULT_RECORDS_PER_PAGE;
        }
    }

}