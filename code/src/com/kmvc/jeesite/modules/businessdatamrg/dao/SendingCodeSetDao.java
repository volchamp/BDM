package com.kmvc.jeesite.modules.businessdatamrg.dao;

import com.kmvc.jeesite.modules.businessdatamrg.entity.SendingCodeSet;
import com.thinkgem.jeesite.common.persistence.*;
import com.kmvc.jeesite.modules.businessdatamrg.entity.*;
import com.thinkgem.jeesite.common.persistence.annotation.*;
import java.util.*;

@MyBatisDao
public interface SendingCodeSetDao extends CrudDao<SendingCodeSet>
{
    List<SendingCodeSet> findBySystemIdAndStatus(final Map<String, Object> p0);

    SendingCodeSet findSendingCodeSet(final String p0);

    void updateStatus(final Map<String, Object> p0);

    List<SendingCodeSet> taskScsList(final SendingCodeSet p0);
}
