package com.kmvc.jeesite.modules.tesk.dao;

import com.kmvc.jeesite.modules.tesk.entity.Task;
import com.thinkgem.jeesite.common.persistence.*;
import com.kmvc.jeesite.modules.tesk.entity.*;
import com.thinkgem.jeesite.common.persistence.annotation.*;
import java.util.*;
import org.apache.ibatis.annotations.*;
import com.thinkgem.jeesite.modules.sys.entity.*;

@MyBatisDao
public interface TaskDao extends CrudDao<Task>
{
    Task findByCodeSetId(final Map<String, Object> p0);

    List<Task> findBySendingId(final Map<String, Object> p0);

    void deleteByPendingRecordId(final String p0);

    List<Task> findListByTask(final Task p0);

    List<String> findDistinctPendingRecordId();

    List<String> UserByRole(final User p0);

    List<String> MenuIdAll();

    long TaskCounts(final Task p0);

    List<String> MenuIds(@Param("menulist") final List p0, @Param("rolelist") final List p1);

    List<Menu> MenuInFo(final List p0);
}
