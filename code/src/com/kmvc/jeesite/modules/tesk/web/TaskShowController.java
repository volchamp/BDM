package com.kmvc.jeesite.modules.tesk.web;

import com.kmvc.jeesite.modules.common.Constant;
import com.kmvc.jeesite.modules.mdmlog.entity.LogInout;
import com.kmvc.jeesite.modules.mdmlog.service.LogInoutService;
import com.kmvc.jeesite.modules.tesk.entity.Task;
import com.kmvc.jeesite.modules.tesk.service.TaskService;
import com.thinkgem.jeesite.common.web.*;
import org.springframework.stereotype.*;
import com.kmvc.jeesite.modules.tesk.service.*;
import org.springframework.beans.factory.annotation.*;
import com.kmvc.jeesite.modules.mdmlog.service.*;
import com.thinkgem.jeesite.modules.sys.service.*;
import com.thinkgem.jeesite.modules.sys.dao.*;
import com.kmvc.jeesite.modules.tesk.entity.*;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.*;
import org.springframework.ui.*;
import com.thinkgem.jeesite.common.config.*;
import org.apache.shiro.*;
import org.apache.shiro.subject.*;
import com.thinkgem.jeesite.common.persistence.*;
import com.kmvc.jeesite.modules.common.*;
import com.kmvc.jeesite.modules.mdmlog.entity.*;
import com.thinkgem.jeesite.common.utils.*;
import com.thinkgem.jeesite.modules.sys.entity.*;
import java.util.*;

@Controller
@RequestMapping({ "${frontPath}/tesk/show" })
public class TaskShowController extends BaseController
{
    @Autowired
    private TaskService taskService;
    @Autowired
    private LogInoutService logInoutService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private MenuDao menuDao;

    @ModelAttribute
    public Task get(@RequestParam(required = false) final String id) {
        Task entity = null;
        if (StringUtils.isNotBlank((CharSequence)id)) {
            entity = this.taskService.get(id);
        }
        if (entity == null) {
            entity = new Task();
        }
        return entity;
    }

    @RequestMapping({ "test1" })
    public String index(final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        String password = request.getParameter("p");
        String loginName = request.getParameter("ln");
        if (StringUtils.isBlank((CharSequence)password)) {
            password = Global.getConfig("password");
        }
        if (StringUtils.isBlank((CharSequence)loginName)) {
            loginName = Global.getConfig("loginName");
        }
        if (!StringUtils.isNotEmpty((CharSequence)password)) {
            password = "admin";
        }
        if (!StringUtils.isNotEmpty((CharSequence)loginName)) {
            loginName = "thinkgem";
        }
        //final UsernamePasswordToken token = new UsernamePasswordToken(loginName, password.toCharArray(), true, StringUtils.getRemoteAddr(request), "validateCode", false);
        final Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            subject.logout();
        }
        //subject.login(token);
        return "modules/sys/sysIndex";
    }

    @RequestMapping({ "indexToEachOt" })
    public String indexToEachOt(final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        final Task task = new Task();
        final Page<Task> taskPage = (Page<Task>)new Page(request, response);
        taskPage.setPageSize(5);
        task.setTaskStatus(Constant.TASK_STATUS_START);
        final Page<Task> pageToDoTask = (Page<Task>)this.taskService.findPage((Page)taskPage, task);
        model.addAttribute("pageToDoTask", (Object)pageToDoTask);
        final Page<Task> taskPage2 = (Page<Task>)new Page(request, response);
        taskPage2.setPageSize(5);
        task.setTaskStatus(Constant.TASK_STATUS_STOP);
        final Page<Task> pageDoneTask = (Page<Task>)this.taskService.findPage((Page)taskPage2, task);
        model.addAttribute("pageDoneTask", (Object)pageDoneTask);
        final Page<LogInout> logInoutPage = (Page<LogInout>)new Page(request, response);
        logInoutPage.setPageSize(5);
        final Page<LogInout> pageLogInout = (Page<LogInout>)this.logInoutService.findPage((Page)logInoutPage, new LogInout());
        model.addAttribute("pageLogInout", (Object)pageLogInout);
        return "modules/tesk/indexToEachOt";
    }

    @RequestMapping({ "mdmIndex" })
    public String mdmIndex(final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        final String loginName = request.getParameter("uid");
        User curUser = null;
        List<Menu> menuList = new ArrayList<Menu>();
        if (StringUtils.isNotBlank((CharSequence)loginName)) {
            curUser = this.systemService.getUser(loginName);
        }
        else {
            curUser = this.systemService.getUser(loginName);
        }
        if (curUser.isAdmin()) {
            menuList = (List<Menu>)this.menuDao.findAllList(new Menu());
        }
        else {
            final Menu m = new Menu();
            m.setUserId(curUser.getId());
            menuList = (List<Menu>)this.menuDao.findByUserId(m);
        }
        CookieUtils.setCookie(response, "LOGINED", "true");
        model.addAttribute("curUser", (Object)curUser);
        model.addAttribute("menuList", (Object)menuList);
        return "modules/sys/mdmIndex";
    }
}
