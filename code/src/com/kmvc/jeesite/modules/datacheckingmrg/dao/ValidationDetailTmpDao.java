package com.kmvc.jeesite.modules.datacheckingmrg.dao;

import com.kmvc.jeesite.modules.datacheckingmrg.entity.ValidationDetailTmp;
import com.thinkgem.jeesite.common.persistence.*;
import com.kmvc.jeesite.modules.datacheckingmrg.entity.*;
import com.thinkgem.jeesite.common.persistence.annotation.*;

@MyBatisDao
public interface ValidationDetailTmpDao extends CrudDao<ValidationDetailTmp>
{
    void deleteBySystemNo(final String p0);
}
