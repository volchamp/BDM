package com.kmvc.jeesite.modules.codesetmrg.dao;

import com.kmvc.jeesite.modules.codesetmrg.entity.CodeSetCacheMapping;
import com.thinkgem.jeesite.common.persistence.*;
import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import com.thinkgem.jeesite.common.persistence.annotation.*;
import java.util.*;

@MyBatisDao
public interface CodeSetCacheMappingDao extends CrudDao<CodeSetCacheMapping>
{
    List<String> findByItemId(final String p0);

    List<String> findByCodesetId(final String p0);

    void deleteByItemId(final String p0);

    void deleteByCodeSetId(final String p0);
}
