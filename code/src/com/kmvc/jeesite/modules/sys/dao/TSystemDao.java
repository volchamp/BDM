package com.kmvc.jeesite.modules.sys.dao;

import com.kmvc.jeesite.modules.sys.entity.TSystem;
import com.thinkgem.jeesite.common.persistence.*;
import com.thinkgem.jeesite.common.persistence.annotation.*;
import java.util.*;

@MyBatisDao
public interface TSystemDao extends CrudDao<TSystem>
{
    int validExist(final TSystem p0);

    List<TSystem> findSystemByCodeSetId(final String p0);

    List<TSystem> codeSetStatistics(final TSystem p0);

    TSystem findBySystemCode(final String p0);

    int enable(final TSystem p0);

    int countExistSystemName(final TSystem p0);

    int countExistSystemCode(final TSystem p0);

    int countExistSystemShort(final TSystem p0);
}
