package com.kmvc.jeesite.modules.datacheckingmrg.dao;

import com.kmvc.jeesite.modules.datacheckingmrg.entity.ValidationCodeSet;
import com.thinkgem.jeesite.common.persistence.*;
import com.kmvc.jeesite.modules.datacheckingmrg.entity.*;
import com.thinkgem.jeesite.common.persistence.annotation.*;
import java.util.*;

@MyBatisDao
public interface ValidationCodeSetDao extends CrudDao<ValidationCodeSet>
{
    List<ValidationCodeSet> findSystemNoAndDate(final String p0);
}
