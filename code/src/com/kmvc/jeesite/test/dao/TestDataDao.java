package com.kmvc.jeesite.test.dao;

import com.kmvc.jeesite.test.entity.TestData;
import com.thinkgem.jeesite.common.persistence.*;
import com.kmvc.jeesite.test.entity.*;
import com.thinkgem.jeesite.common.persistence.annotation.*;

@MyBatisDao
public interface TestDataDao extends CrudDao<TestData>
{
}
