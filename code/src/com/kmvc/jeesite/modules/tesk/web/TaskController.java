package com.kmvc.jeesite.modules.tesk.web;

import com.kmvc.jeesite.modules.common.Constant;
import com.kmvc.jeesite.modules.common.Util;
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
import javax.servlet.http.*;
import org.springframework.ui.*;
import com.thinkgem.jeesite.common.persistence.*;
import org.springframework.web.servlet.mvc.support.*;
import com.thinkgem.jeesite.common.config.*;
import org.springframework.web.bind.annotation.*;
import com.kmvc.jeesite.modules.mdmlog.entity.*;
import com.thinkgem.jeesite.common.utils.*;
import com.thinkgem.jeesite.modules.sys.entity.*;
import java.util.*;
import java.io.*;
import com.kmvc.jeesite.modules.common.*;

@Controller
@RequestMapping({ "${adminPath}/tesk/task" })
public class TaskController extends BaseController
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

    @RequestMapping({ "list", "" })
    public String list(final Task task, final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        final Page<Task> page = (Page<Task>)this.taskService.findPage(new Page(request, response), task);
        model.addAttribute("page", (Object)page);
        return "modules/tesk/taskList";
    }

    @RequestMapping({ "form" })
    public String form(final Task task, final Model model) {
        model.addAttribute("task", (Object)task);
        return "modules/tesk/taskForm";
    }

    @RequestMapping({ "save" })
    public String save(final Task task, final Model model, final RedirectAttributes redirectAttributes) {
        if (!this.beanValidator(model, (Object)task, new Class[0])) {
            return this.form(task, model);
        }
        this.taskService.save(task);
        this.addMessage(redirectAttributes, new String[] { "\u4fdd\u5b58\u4ee3\u7801\u96c6\u4efb\u52a1\u6210\u529f" });
        return "redirect:" + Global.getAdminPath() + "/tesk/task/?repage";
    }

    @RequestMapping({ "delete" })
    public String delete(final Task task, final RedirectAttributes redirectAttributes) {
        this.taskService.delete(task);
        this.addMessage(redirectAttributes, new String[] { "\u5220\u9664\u4ee3\u7801\u96c6\u4efb\u52a1\u6210\u529f" });
        return "redirect:" + Global.getAdminPath() + "/tesk/task/?repage";
    }

    @RequestMapping(value = { "assign" }, method = { RequestMethod.POST })
    @ResponseBody
    public boolean assign(final Task task) {
        boolean flag = true;
        try {
            task.setIsNewRecord(false);
            this.taskService.save(task);
        }
        catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    @RequestMapping({ "test" })
    public String index(final HttpServletRequest request, final HttpServletResponse response, final Model model) {
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
        final String loginName = request.getParameter("loginName");
        User curUser = null;
        List<Menu> menuList = new ArrayList<Menu>();
        if (StringUtils.isNotBlank((CharSequence)loginName)) {
            curUser = this.systemService.getUserByLoginName(loginName);
        }
        else {
            curUser = this.systemService.getUserByLoginName("thinkgem");
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

    @RequestMapping({ "menuTree" })
    public String menuTree(final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        final String parentId = request.getParameter("parentId");
        List<Menu> childMenuList = new ArrayList<Menu>();
        final Menu m = new Menu();
        m.setParentIds(parentId);
        childMenuList = (List<Menu>)this.menuDao.findByParentIdsLike(m);
        model.addAttribute("menuList2", (Object)childMenuList);
        return "modules/sys/menuTree";
    }

    @RequestMapping({ "taskHisList" })
    public String taskHisList(final Task task, final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        final String method = request.getMethod();
        String codeSetName1 = request.getParameter("codeSetName");
        String systemName = request.getParameter("systemName");
        final String codeSetName2 = task.getCodeSetName();
        if (" ".equals(task.getSystemId())) {
            task.setSystemId((String)null);
        }
        try {
            if ("GET".equals(method)) {
                codeSetName1 = new String(codeSetName1.getBytes("ISO-8859-1"), "utf-8");
                systemName = new String(systemName.getBytes("ISO-8859-1"), "utf-8");
            }
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (Util.isContainChinese(codeSetName1)) {
            task.setCodeSetName(codeSetName1);
        }
        if (Util.isContainChinese(systemName)) {
            task.setSystemName(systemName);
        }
        final Page<Task> page = (Page<Task>)this.taskService.findPageByTask(new Page(request, response), task);
        model.addAttribute("page", (Object)page);
        return "modules/tesk/taskHisList";
    }
}
