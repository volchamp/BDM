package com.kmvc.jeesite.modules.codesetmrg.dao;

import com.kmvc.jeesite.modules.codesetmrg.entity.SplitMerge;
import com.thinkgem.jeesite.common.persistence.*;
import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import com.thinkgem.jeesite.common.persistence.annotation.*;
import java.util.*;

@MyBatisDao
public interface SplitMergeDao extends CrudDao<SplitMerge>
{
    int batInsertSplit(final SplitMerge p0);

    int batInsertMerge(final SplitMerge p0);

    List<SplitMerge> findListByItemId(final SplitMerge p0);
}
