package com.kmvc.jeesite.modules.mdmlog.service;

import com.kmvc.jeesite.modules.mdmlog.dao.LogInoutDao;
import com.kmvc.jeesite.modules.mdmlog.entity.LogInout;
import com.thinkgem.jeesite.common.service.*;
import com.kmvc.jeesite.modules.mdmlog.dao.*;
import com.kmvc.jeesite.modules.mdmlog.entity.*;
import org.springframework.stereotype.*;
import com.thinkgem.jeesite.common.persistence.*;
import org.springframework.transaction.annotation.*;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class LogInoutService extends CrudService<LogInoutDao, LogInout>
{
    public LogInout get(final String id) {
        return (LogInout)super.get(id);
    }

    public List<LogInout> findList(final LogInout logInout) {
        final List<LogInout> list = (List<LogInout>)((LogInoutDao)this.dao).findList(logInout);
        return list;
    }

    public Page<LogInout> findPage(final Page<LogInout> page, final LogInout logInout) {
        page.setOrderBy("a.start_date desc");
        logInout.setPage((Page)page);
        final List<LogInout> list = this.findList(logInout);
        page.setList((List)list);
        return page;
    }

    @Transactional(readOnly = false, propagation = Propagation.NOT_SUPPORTED)
    public void save(final LogInout logInout) {
        super.save(logInout);
    }

    @Transactional(readOnly = false)
    public void delete(final LogInout logInout) {
        super.delete(logInout);
    }

    public LogInout getLogByMsg(final String messageId, final int currentPage) {
        final Map<String, Object> param = new HashMap<String, Object>();
        param.put("messageId", messageId);
        param.put("currentPage", currentPage);
        return ((LogInoutDao)this.dao).getLogByMsg((Map)param);
    }

    public void deleteByToday() {
        ((LogInoutDao)this.dao).deleteByToday();
    }
}
