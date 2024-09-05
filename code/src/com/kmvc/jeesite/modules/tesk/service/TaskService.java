package com.kmvc.jeesite.modules.tesk.service;

import com.kmvc.jeesite.modules.common.Constant;
import com.kmvc.jeesite.modules.sys.entity.TSystem;
import com.kmvc.jeesite.modules.sys.service.TSystemService;
import com.kmvc.jeesite.modules.tesk.dao.TaskDao;
import com.kmvc.jeesite.modules.tesk.entity.Task;
import com.thinkgem.jeesite.common.service.*;
import com.kmvc.jeesite.modules.tesk.dao.*;
import com.kmvc.jeesite.modules.tesk.entity.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.springframework.beans.factory.annotation.*;
import com.thinkgem.jeesite.common.persistence.*;
import com.thinkgem.jeesite.modules.sys.utils.*;
import com.kmvc.jeesite.modules.common.*;
import org.apache.shiro.subject.*;
import java.util.*;
import com.thinkgem.jeesite.modules.sys.entity.*;

@Service
@Transactional(readOnly = true)
public class TaskService extends CrudService<TaskDao, Task>
{
    @Autowired
    private TSystemService tSystemService;

    public Task get(final String id) {
        return (Task)super.get(id);
    }

    public List<Task> findList(final Task task) {
        return (List<Task>)super.findList(task);
    }

    public Page<Task> findPage(final Page<Task> page, final Task task) {
        if (task.getTaskStatus() == 0) {
            page.setOrderBy("\"taskStartDate\" desc");
        }
        else if (task.getTaskStatus() == 1) {
            page.setOrderBy("\"taskCompleteDate\" desc");
        }
        final User curUser = UserUtils.getUser();
        final Subject subject = UserUtils.getSubject();
        if (subject != null && !curUser.isAdmin()) {
            if (subject.isPermitted("businessdatamrg:pendingAudit:first")) {
                task.getTaskTypeList().add(Constant.TASK_TYPE_FIRST);
            }
            if (subject.isPermitted("businessdatamrg:pendingAudit:review")) {
                task.getTaskTypeList().add(Constant.TASK_TYPE_REVIEW);
            }
            if (subject.isPermitted("businessdatamrg:sendingAudit:issued")) {
                task.getTaskTypeList().add(Constant.TASK_TYPE_ISSUED);
            }
            if (subject.isPermitted("datacheckingmrg:validationCodeSet:check")) {
                task.getTaskTypeList().add(Constant.TASK_TYPE_CHECK);
            }
        }
        if (task.getTaskStatus() != null && !curUser.isAdmin()) {
            task.setTaskHandlerId(curUser.getId());
            task.setCurrentUser(curUser);
        }
        final TSystem system = new TSystem();
        system.setReceivers("," + curUser.getId() + ",");
        final List<TSystem> systemList = this.tSystemService.findList(system);
        for (final TSystem sys : systemList) {
            task.getSystemIdList().add(sys.getId());
        }
        if (systemList.size() > 0) {
            task.getSystemIdList().add(" ");
        }
        return (Page<Task>)super.findPage((Page)page, task);
    }

    @Transactional(readOnly = false)
    public void save(final Task task) {
        super.save(task);
    }

    @Transactional(readOnly = false)
    public void delete(final Task task) {
        super.delete(task);
    }

    public Task findByCodeSetId(final String codeSetId, final Integer taskStatus) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("pendingRecordId", codeSetId);
        params.put("taskStatus", taskStatus);
        return ((TaskDao)this.dao).findByCodeSetId((Map)params);
    }

    public List<Task> findBySendingId(final String recordId, final Integer taskStatus) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("sendingRecordId", recordId);
        params.put("taskStatus", taskStatus);
        return (List<Task>)((TaskDao)this.dao).findBySendingId((Map)params);
    }

    public List<Task> findListByTask(final Task task) {
        return (List<Task>)((TaskDao)this.dao).findListByTask(task);
    }

    public List<String> findDistinctPendingRecordId() {
        return (List<String>)((TaskDao)this.dao).findDistinctPendingRecordId();
    }

    public Page<Task> findPageByTask(final Page<Task> page, final Task entity) {
        page.setOrderBy("a.TASK_START_DATE, a.task_type_id");
        entity.setPage((Page)page);
        page.setList(((TaskDao)this.dao).findListByTask(entity));
        return page;
    }

    public List<String> UserByRole(final User user) {
        final List<String> userByRole = (List<String>)((TaskDao)this.dao).UserByRole(user);
        return userByRole;
    }

    public List<String> MenuIdAll() {
        final List<String> menuId = (List<String>)((TaskDao)this.dao).MenuIdAll();
        return menuId;
    }

    public long TaskCounts(final Task task) {
        final long taskCount = ((TaskDao)this.dao).TaskCounts(task);
        return taskCount;
    }

    public List<String> MenuIds(final List menu, final List role) {
        final List<String> menuIds = (List<String>)((TaskDao)this.dao).MenuIds(menu, role);
        return menuIds;
    }

    public List<Menu> MenuInFo(final List menu) {
        final List<Menu> menuInFo = (List<Menu>)((TaskDao)this.dao).MenuInFo(menu);
        return menuInFo;
    }
}
