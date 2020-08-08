package com.pithy.free.pageable.imp;

import com.pithy.free.pageable.Pagination;

import java.util.List;

@SuppressWarnings("serial")
public class SimplePagination<T> implements Pagination<T> {

    private boolean spilled = true; // 是否重置溢出的PageNo
    private boolean isOffset; // 是否重置溢出的PageNo
    private Long pageNo; // 索引页
    private Long pageSize; // 分页条数
    private Long pageTotal; // 数据总量
    private Long pageNumber; // 分页总量

    private List<T> data;

    public SimplePagination() {

    }

    public SimplePagination(Long pageNo, Long pageSize, boolean spilled) {
        this(pageNo, pageSize, 0l, null, null);
        this.spilled = spilled;
    }

    public SimplePagination(Long pageNo, Long pageSize) {
        this(pageNo, pageSize, 0l, null, null);
    }

    public SimplePagination(Long pageNo, Long pageSize, Long pageTotal) {
        this(pageNo, pageSize, pageTotal, (pageTotal % pageSize == 0) ? (pageTotal / pageSize) : (pageTotal / pageSize + 1), null);
    }

    public SimplePagination(Long pageNo, Long pageSize, Long pageTotal, List<T> data) {
        this(pageNo, pageSize, pageTotal, (pageTotal % pageSize == 0) ? (pageTotal / pageSize) : (pageTotal / pageSize + 1), data);
    }

    public SimplePagination(Long pageNo, Long pageSize, Long pageTotal, Long pageNumber, List<T> data) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.pageTotal = pageTotal;
        this.pageNumber = pageNumber;
        this.data = data;
    }

    @Override
    public boolean isSpilled() {
        return spilled;
    }

    @Override
    public void setSpilled(boolean spilled) {
        this.spilled = spilled;
    }

    @Override
    public boolean isOffset() {
        return isOffset;
    }

    @Override
    public void setOffset(boolean offset) {
        isOffset = offset;
    }

    @Override
    public Long getPageNo() {
        return pageNo;
    }

    @Override
    public void setPageNo(Long pageNo) {
        this.pageNo = pageNo;
    }

    @Override
    public Long getPageSize() {
        return pageSize;
    }

    @Override
    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public Long getPageTotal() {
        return pageTotal;
    }

    @Override
    public void setPageTotal(Long pageTotal) {
        this.pageTotal = pageTotal;
    }

    @Override
    public Long getPageNumber() {
        return pageNumber;
    }

    @Override
    public void setPageNumber(Long pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public List<T> getData() {
        return data;
    }

    @Override
    public void setData(List<T> data) {
        this.data = data;
    }
}
