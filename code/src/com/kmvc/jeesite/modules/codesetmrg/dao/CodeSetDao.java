package com.kmvc.jeesite.modules.codesetmrg.dao;

import com.kmvc.jeesite.modules.codesetmrg.entity.CodeSet;
import com.thinkgem.jeesite.common.persistence.*;
import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import com.thinkgem.jeesite.common.persistence.annotation.*;
import java.util.*;

@MyBatisDao
public interface CodeSetDao extends CrudDao<CodeSet>
{
    CodeSet findByCode(final String p0);

    CodeSet findByCode2(final String p0);

    List<CodeSet> finBySystemId(final String p0);

    List<CodeSet> findOfficeIdByUserId(final String p0);

    List<CodeSet> findBySystemCode(final Map<String, Object> p0);

    List<CodeSet> findListSimple(final CodeSet p0);

    Long count(final CodeSet p0);

    Long countByNo(final CodeSet p0);

    int disable(final CodeSet p0);

    int enable(final CodeSet p0);
}
