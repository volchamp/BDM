package com.kmvc.jeesite.modules.mdmlog.dao;

import com.kmvc.jeesite.modules.mdmlog.entity.LogInout;
import com.thinkgem.jeesite.common.persistence.*;
import com.kmvc.jeesite.modules.mdmlog.entity.*;
import com.thinkgem.jeesite.common.persistence.annotation.*;
import java.util.*;

@MyBatisDao
public interface LogInoutDao extends CrudDao<LogInout>
{
    LogInout getLogByMsg(final Map<String, Object> p0);

    void deleteByToday();
}
