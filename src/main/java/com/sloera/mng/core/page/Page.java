package com.sloera.mng.core.page;

import java.io.Serializable;
import java.util.List;

public class Page<E> implements Serializable {

    private List<E> list;
    private int page;
    private int rows;
    private int totalPage;
    private long totalRow;

    public Page(List<E> var1, int var2, int var3, int var4, long var5){
        this.list = var1;
        this.page = var2;
        this.rows = var3;
        this.totalPage = var4;
        this.totalRow = var5;
    }

    public List<E> getList() {
        return list;
    }

    public int getPage() {
        return page;
    }

    public int getRows() {
        return rows;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public long getTotalRow() {
        return totalRow;
    }
}
