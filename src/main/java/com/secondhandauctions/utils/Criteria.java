package com.secondhandauctions.utils;

public class Criteria {

    private int pageNum; // 현재 페이지 번호
    private int amount; // 페이지당 출력할 게시글 수

    public Criteria() {
        this(1,10);
    }

    public Criteria(int pageNum, int amount) {
        super();
        this.pageNum = pageNum;
        this.amount = amount;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Criteria{" +
                "pageNum=" + pageNum +
                ", amount=" + amount +
                '}';
    }
}
