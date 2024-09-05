package com.kmvc.jeesite.modules.mdmlog.dao;

import com.kmvc.jeesite.modules.mdmlog.entity.ProcLog;
import com.thinkgem.jeesite.common.persistence.*;
import com.kmvc.jeesite.modules.mdmlog.entity.*;
import com.thinkgem.jeesite.common.persistence.annotation.*;

@MyBatisDao
public interface ProcLogDao extends CrudDao<ProcLog>
{
}
