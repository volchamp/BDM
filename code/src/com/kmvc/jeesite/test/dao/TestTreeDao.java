package com.kmvc.jeesite.test.dao;

import com.kmvc.jeesite.test.entity.TestTree;
import com.thinkgem.jeesite.common.persistence.*;
import com.kmvc.jeesite.test.entity.*;
import com.thinkgem.jeesite.common.persistence.annotation.*;

@MyBatisDao
public interface TestTreeDao extends TreeDao<TestTree>
{
}
