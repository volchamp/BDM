package com.kmvc.jeesite.test.dao;

import com.kmvc.jeesite.test.entity.TestDataChild;
import com.thinkgem.jeesite.common.persistence.*;
import com.thinkgem.jeesite.common.persistence.annotation.*;

@MyBatisDao
public interface TestDataChildDao extends CrudDao<TestDataChild>
{
}
