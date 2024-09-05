package com.kmvc.jeesite.modules.codesetmrg.dao;

import com.kmvc.jeesite.modules.codesetmrg.entity.PendingCodesetSystem;
import com.thinkgem.jeesite.common.persistence.*;
import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import com.thinkgem.jeesite.common.persistence.annotation.*;
import java.util.*;

@MyBatisDao
public interface PendingCodesetSystemDao extends CrudDao<PendingCodesetSystem>
{
    List<String> findByCodesetId(final String p0);

    void deleteByCodesetId(final String p0);
}
