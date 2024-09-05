package com.kmvc.jeesite.modules.codesetmrg.dao;

import com.kmvc.jeesite.modules.codesetmrg.entity.PendingCodeItem;
import com.thinkgem.jeesite.common.persistence.*;
import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import com.thinkgem.jeesite.common.persistence.annotation.*;
import java.util.*;

@MyBatisDao
public interface PendingCodeItemDao extends CrudDao<PendingCodeItem>
{
    int findMaxVersion(final Map<String, Object> p0);

    Integer updateProcessStatus(final Map<String, Object> p0);

    List<PendingCodeItem> findByCodeSetIdAndSystemId(final Map<String, Object> p0);

    List<PendingCodeItem> findByCodeSetId(final String p0);

    void deleteByCodeSetId(final String p0);

    List<PendingCodeItem> reviewList(final PendingCodeItem p0);

    List<PendingCodeItem> getCodeToTreeData(final String p0);

    int batUpdByIds(final PendingCodeItem p0);
}
