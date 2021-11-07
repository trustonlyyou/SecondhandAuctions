package com.secondhandauctions.utils;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class PageDTO {

    private int startPage;
    private int endPage;
    private boolean prev;
    private boolean next;

    private int total;
    private Criteria criteria;

    public PageDTO(Criteria criteria, int total) {
        this.criteria = criteria;
        this.total = total;

        this.endPage = (int) Math.ceil(criteria.getPageNum() / 10.0) * 10;

        this.startPage = endPage - 9;

        this.prev = this.startPage > 1;

        int realEnd = (int)(Math.ceil((total * 1.0) / criteria.getAmount()));

        this.endPage = realEnd <= endPage ? realEnd : endPage;

        this.next = this.endPage < realEnd;

    }
}
