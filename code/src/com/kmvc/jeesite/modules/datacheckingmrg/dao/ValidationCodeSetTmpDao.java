package com.kmvc.jeesite.modules.datacheckingmrg.dao;

import com.kmvc.jeesite.modules.datacheckingmrg.entity.ValidationCodeSetTmp;
import com.thinkgem.jeesite.common.persistence.*;
import com.kmvc.jeesite.modules.datacheckingmrg.entity.*;
import com.thinkgem.jeesite.common.persistence.annotation.*;

@MyBatisDao
public interface ValidationCodeSetTmpDao extends CrudDao<ValidationCodeSetTmp>
{
    ValidationCodeSetTmp findBySystemCode(final ValidationCodeSetTmp p0);

    void deleteBySystemNo(final String p0);
}
