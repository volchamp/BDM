package com.kmvc.jeesite.modules.common.dao;

import com.kmvc.jeesite.modules.common.entity.CommonEntity;
import com.thinkgem.jeesite.common.persistence.*;
import com.kmvc.jeesite.modules.common.entity.*;
import com.thinkgem.jeesite.common.persistence.annotation.*;
import java.util.*;
import com.thinkgem.jeesite.modules.sys.entity.*;

@MyBatisDao
public interface CommonDao extends CrudDao<CommonEntity>
{
    CommonEntity getUserById(final String p0);

    CommonEntity getUserByLgName(final CommonEntity p0);

    List<CommonEntity> findUserList(final CommonEntity p0);

    int updIllegalLgById(final CommonEntity p0);

    int updIllegalLgCountById(final CommonEntity p0);

    int updUserById(final CommonEntity p0);

    Office getOffByIdCodeName(final Office p0);
}
