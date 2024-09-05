package com.kmvc.jeesite.modules.businessdatamrg.dao;

import com.kmvc.jeesite.modules.businessdatamrg.entity.SendingCodeItem;
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.*;
import java.util.*;

@MyBatisDao
public interface SendingCodeItemDao extends CrudDao<SendingCodeItem>
{
    SendingCodeItem findByCodeSetIdAndCodeAndOperation(final Map<String, Object> p0);

    List<SendingCodeItem> findSendingCodeItem(final Map<String, Object> p0);

    int findTotalSendingCodeItem(final String p0);

    void batchUpdate(final Map<String, Object> p0);
}
