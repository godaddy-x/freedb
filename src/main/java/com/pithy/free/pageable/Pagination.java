package com.pithy.free.pageable;

import java.io.Serializable;
import java.util.List;

public interface Pagination<T> extends Serializable {

    public boolean isSpilled();

    public void setSpilled(boolean spilled);

    public boolean isOffset();

    public void setOffset(boolean isOffset);

    public Long getPageNo();

    public void setPageNo(Long pageNo);

    public Long getPageSize();

    public void setPageSize(Long pageSize);

    public Long getPageTotal();

    public void setPageTotal(Long pageTotal);

    public Long getPageNumber();

    public void setPageNumber(Long pageNumber);

    public List<T> getData();

    public void setData(List<T> data);

}
