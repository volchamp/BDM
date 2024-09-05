package com.kmvc.jeesite.modules.syncPortal.process.ctrl;

import com.kmvc.jeesite.modules.common.Constant;
import com.kmvc.jeesite.modules.syncPortal.ctrl.SynBaseCtrl;
import com.kmvc.jeesite.modules.tesk.entity.Task;
import com.kmvc.jeesite.modules.tesk.service.TaskService;
import com.kmvc.jeesite.modules.syncPortal.ctrl.*;
import org.springframework.stereotype.*;
import org.apache.log4j.*;
import com.kmvc.jeesite.modules.tesk.service.*;
import org.springframework.beans.factory.annotation.*;
import javax.servlet.http.*;
import com.kmvc.jeesite.modules.tesk.entity.*;
import com.kmvc.jeesite.modules.common.*;
import com.thinkgem.jeesite.common.persistence.*;
import com.thinkgem.jeesite.modules.sys.entity.*;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping({ "${frontPath}" })
public class ProcessTodoCtrl extends SynBaseCtrl
{
    Logger log;
    @Autowired
    TaskService taskService;

    public ProcessTodoCtrl() {
        this.log = Logger.getLogger((Class)ProcessTodoCtrl.class);
    }

    @RequestMapping({ "/mdm/portal/todo/list" })
    @ResponseBody
    private long getTodoCountByPortalLoginName(final HttpServletRequest request, final HttpServletResponse response, final String uid) {
        Long taskCount = 0L;
        final Task t = new Task();
        t.setTaskStatus(Constant.TASK_STATUS_START);
        final User user = new User();
        user.setId(uid);
        if (user.isAdmin()) {
            final Page<Task> findPage = (Page<Task>)this.taskService.findPage(new Page(request, response), t);
            taskCount = findPage.getCount();
        }
        else {
            final List<Role> rolelist = new ArrayList<Role>();
            final List<Menu> menulist = new ArrayList<Menu>();
            final List<Menu> menulists = new ArrayList<Menu>();
            final List<String> userByRole = (List<String>)this.taskService.UserByRole(user);
            if (!userByRole.isEmpty()) {
                for (int i = 0; i < userByRole.size(); ++i) {
                    final String string = userByRole.get(i);
                    final Role role = new Role();
                    role.setId(string);
                    rolelist.add(role);
                }
                final List<String> menuIds = (List<String>)this.taskService.MenuIdAll();
                if (!menuIds.isEmpty()) {
                    for (int j = 0; j < menuIds.size(); ++j) {
                        final String string2 = menuIds.get(j);
                        final Menu menu = new Menu();
                        menu.setId(string2);
                        menulist.add(menu);
                    }
                    final List<String> menuIds2 = (List<String>)this.taskService.MenuIds((List)menulist, (List)rolelist);
                    if (!menuIds2.isEmpty()) {
                        for (int k = 0; k < menuIds2.size(); ++k) {
                            final String string3 = menuIds2.get(k);
                            final Menu menu2 = new Menu();
                            menu2.setId(string3);
                            menulists.add(menu2);
                        }
                        final List<Menu> menuInFo = (List<Menu>)this.taskService.MenuInFo((List)menulists);
                        if (!menuInFo.isEmpty()) {
                            for (int l = 0; l < menuInFo.size(); ++l) {
                                final Menu menu2 = menuInFo.get(l);
                                if (menu2.getPermission().equals("businessdatamrg:pendingAudit:first")) {
                                    t.getTaskTypeList().add(Constant.TASK_TYPE_FIRST);
                                }
                                if (menu2.getPermission().equals("businessdatamrg:pendingAudit:review")) {
                                    t.getTaskTypeList().add(Constant.TASK_TYPE_REVIEW);
                                }
                                if (menu2.getPermission().equals("businessdatamrg:sendingAudit:issued")) {
                                    t.getTaskTypeList().add(Constant.TASK_TYPE_ISSUED);
                                }
                                if (menu2.getPermission().equals("datacheckingmrg:validationCodeSet:check")) {
                                    t.getTaskTypeList().add(Constant.TASK_TYPE_CHECK);
                                }
                            }
                            taskCount = this.taskService.TaskCounts(t);
                        }
                    }
                }
            }
        }
        return taskCount;
    }
}
