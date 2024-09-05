package com.kmvc.jeesite.modules.mdmlog.service;

import com.kmvc.jeesite.modules.mdmlog.dao.ProcLogDao;
import com.kmvc.jeesite.modules.mdmlog.entity.ProcLog;
import com.thinkgem.jeesite.common.service.*;
import com.kmvc.jeesite.modules.mdmlog.dao.*;
import com.kmvc.jeesite.modules.mdmlog.entity.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import java.util.*;
import com.thinkgem.jeesite.common.persistence.*;

@Service
@Transactional(readOnly = true)
public class ProcLogService extends CrudService<ProcLogDao, ProcLog>
{
    public ProcLog get(final String id) {
        return (ProcLog)super.get(id);
    }

    public List<ProcLog> findList(final ProcLog procLog) {
        return (List<ProcLog>)super.findList(procLog);
    }

    public Page<ProcLog> findPage(final Page<ProcLog> page, final ProcLog procLog) {
        return (Page<ProcLog>)super.findPage((Page)page, procLog);
    }

    @Transactional(readOnly = false)
    public void save(final ProcLog procLog) {
        super.save(procLog);
    }

    @Transactional(readOnly = false)
    public void delete(final ProcLog procLog) {
        super.delete(procLog);
    }
}
