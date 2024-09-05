package com.kmvc.jeesite.modules.datacheckingmrg.dao;

import com.kmvc.jeesite.modules.datacheckingmrg.entity.ValidationDetail;
import com.thinkgem.jeesite.common.persistence.*;
import com.kmvc.jeesite.modules.datacheckingmrg.entity.*;
import com.thinkgem.jeesite.common.persistence.annotation.*;
import java.util.*;

@MyBatisDao
public interface ValidationDetailDao extends CrudDao<ValidationDetail>
{
    void verificationPro(final String p0);

    void verificationPro2(final Map p0);
}
