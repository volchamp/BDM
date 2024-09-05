package com.kmvc.jeesite.modules.codesetmrg.dao;

import com.kmvc.jeesite.modules.codesetmrg.entity.PendingCodeSet;
import com.thinkgem.jeesite.common.persistence.*;
import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import com.thinkgem.jeesite.common.persistence.annotation.*;
import java.util.*;

@MyBatisDao
public interface PendingCodeSetDao extends CrudDao<PendingCodeSet>
{
    PendingCodeSet findByCodeAndStatus(final Map<String, Object> p0);

    void updateProcessStatus(final Map<String, Object> p0);

    List<PendingCodeSet> taskPendingCodeSetList(final PendingCodeSet p0);

    List<PendingCodeSet> taskPendingCodeSetAllList(final PendingCodeSet p0);

    Long count(final PendingCodeSet p0);

    PendingCodeSet getByProcInstId(final String p0);

    PendingCodeSet getByManyAttr(final PendingCodeSet p0);
}
