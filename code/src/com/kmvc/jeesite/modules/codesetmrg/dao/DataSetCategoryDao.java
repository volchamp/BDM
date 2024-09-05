package com.kmvc.jeesite.modules.codesetmrg.dao;

import com.kmvc.jeesite.modules.codesetmrg.entity.DataSetCategory;
import com.thinkgem.jeesite.common.persistence.*;
import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import com.thinkgem.jeesite.common.persistence.annotation.*;
import java.util.*;

@MyBatisDao
public interface DataSetCategoryDao extends CrudDao<DataSetCategory>
{
    int validExistCode(final Map<String, Object> p0);

    int checkName(final DataSetCategory p0);
}
