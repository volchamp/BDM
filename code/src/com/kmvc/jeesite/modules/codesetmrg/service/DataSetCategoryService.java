package com.kmvc.jeesite.modules.codesetmrg.service;

import com.kmvc.jeesite.modules.codesetmrg.dao.DataSetCategoryDao;
import com.kmvc.jeesite.modules.codesetmrg.entity.DataSetCategory;
import com.thinkgem.jeesite.common.service.*;
import com.kmvc.jeesite.modules.codesetmrg.dao.*;
import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import com.thinkgem.jeesite.common.persistence.*;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class DataSetCategoryService extends CrudService<DataSetCategoryDao, DataSetCategory>
{
    public DataSetCategory get(final String id) {
        return (DataSetCategory)super.get(id);
    }

    public List<DataSetCategory> findList(final DataSetCategory dataSetCategory) {
        return (List<DataSetCategory>)super.findList(dataSetCategory);
    }

    public Page<DataSetCategory> findPage(final Page<DataSetCategory> page, final DataSetCategory dataSetCategory) {
        Page<DataSetCategory> list = null;
        try {
            list = (Page<DataSetCategory>)super.findPage((Page)page, dataSetCategory);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Transactional(readOnly = false)
    public void save(final DataSetCategory dataSetCategory) {
        final Integer codeGroupSort = dataSetCategory.getCodeGroupSort();
        if (codeGroupSort == null || String.valueOf(codeGroupSort).trim().length() == 0) {
            dataSetCategory.setCodeGroupSort(Integer.valueOf(0));
        }
        super.save(dataSetCategory);
    }

    @Transactional(readOnly = false)
    public void delete(final DataSetCategory dataSetCategory) {
        super.delete(dataSetCategory);
    }

    public boolean validExistCode(final String id, final String code) {
        final Map<String, Object> params = new HashMap<String, Object>();
        final String delFlag = "0";
        params.put("id", id);
        params.put("delFlag", delFlag);
        params.put("code", code);
        return ((DataSetCategoryDao)this.dao).validExistCode((Map)params) > 0;
    }

    public boolean checkName(final DataSetCategory dataSetCategory) {
        return ((DataSetCategoryDao)this.dao).checkName(dataSetCategory) > 0;
    }

    public List<DataSetCategory> findAllList(final DataSetCategory dataSetCategory) {
        return (List<DataSetCategory>)((DataSetCategoryDao)this.dao).findAllList(dataSetCategory);
    }
}
