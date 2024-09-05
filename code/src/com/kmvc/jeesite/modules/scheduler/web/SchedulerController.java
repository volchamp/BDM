package com.kmvc.jeesite.modules.scheduler.web;

import com.kmvc.jeesite.modules.scheduler.entity.ParameterObj;
import com.kmvc.jeesite.modules.scheduler.service.SchedulerService;
import com.thinkgem.jeesite.common.web.*;
import org.springframework.stereotype.*;
import com.kmvc.jeesite.modules.scheduler.service.*;
import org.springframework.beans.factory.annotation.*;
import javax.servlet.http.*;
import org.springframework.ui.*;
import org.apache.shiro.authz.annotation.*;
import org.springframework.web.servlet.mvc.support.*;
import org.springframework.web.servlet.*;
import com.thinkgem.jeesite.common.config.*;
import com.kmvc.jeesite.modules.scheduler.entity.*;
import org.springframework.web.bind.annotation.*;
import org.quartz.*;
import com.thinkgem.jeesite.common.utils.*;
import java.util.*;
import java.text.*;
import org.apache.commons.lang3.StringUtils;
import java.io.*;

@Controller
@RequestMapping({ "${adminPath}/sys/scheduler" })
public class SchedulerController extends BaseController
{
    @Autowired
    private SchedulerService schedulerService;

    @RequiresPermissions({ "sys:scheduler:view" })
    @RequestMapping({ "list", "" })
    public String list(final HttpServletRequest request, final Model model) throws SchedulerException {
        final boolean isInStandbyMode = this.schedulerService.isInStandbyMode();
        final List<JobDetail> list = this.schedulerService.getJobList();
        model.addAttribute("list", (Object)list);
        model.addAttribute("isStandby", (Object)isInStandbyMode);
        return "modules/scheduler/schedulerList";
    }

    @RequiresPermissions({ "sys:scheduler:edit" })
    @RequestMapping({ "addJob{viewName}" })
    public ModelAndView addJob(final HttpServletRequest request, @PathVariable("viewName") final String viewName, final RedirectAttributes redirectAttributes) throws Exception {
        String msg = "\u6dfb\u52a0\u4efb\u52a1\u6210\u529f!";
        final ModelAndView mv = new ModelAndView();
        String jobName = request.getParameter("jobName");
        if ("1".equals(viewName)) {
            mv.setViewName("/modules/scheduler/jobAdd");
            mv.addObject("isAdd", (Object)1);
        }
        else if ("2".equals(viewName)) {
            try {
                final String className = request.getParameter("className");
                final String parameterJson = request.getParameter("parameterJson");
                final String description = request.getParameter("description");
                final boolean isExist = this.schedulerService.isJobExists(jobName);
                if (isExist) {
                    msg = "\u4efb\u52a1\u540d\u79f0\u5df2\u7ecf\u5b58\u5728\uff0c\u6dfb\u52a0\u5931\u8d25!";
                }
                else {
                    this.schedulerService.addJob(jobName, className, parameterJson, description);
                }
            }
            catch (ClassNotFoundException ex) {
                msg = "\u6dfb\u52a0\u4efb\u52a1\u5931\u8d25!";
            }
            catch (Exception ex2) {
                msg = "\u6dfb\u52a0\u4efb\u52a1\u5931\u8d25!";
            }
            this.addMessage(redirectAttributes, new String[] { msg });
            mv.setViewName("redirect:" + Global.getAdminPath() + "/sys/scheduler/?repage");
        }
        else if ("3".equals(viewName)) {
            jobName = request.getParameter("jobName");
            final JobDetail jobDetail = this.schedulerService.getJobDetail(jobName);
            jobDetail.getJobBuilder();
            final JobDataMap jobDataMap = jobDetail.getJobDataMap();
            final List<ParameterObj> list = new ArrayList<ParameterObj>();
            for (final Map.Entry<String, Object> entry : jobDataMap.entrySet()) {
                final ParameterObj param = new ParameterObj();
                param.setName(entry.getKey());
                final String typeName = entry.getValue().getClass().getName();
                if ("java.lang.Integer".equals(typeName)) {
                    param.setType("int");
                }
                else if ("java.lang.Long".equals(typeName)) {
                    param.setType("long");
                }
                else if ("java.lang.Float".equals(typeName)) {
                    param.setType("float");
                }
                else if ("java.lang.Boolean".equals(typeName)) {
                    param.setType("boolean");
                }
                else {
                    param.setType("string");
                }
                param.setValue(String.valueOf(entry.getValue()));
                list.add(param);
            }
            mv.addObject("jobDetail", (Object)jobDetail);
            mv.addObject("isAdd", (Object)3);
            mv.addObject("list", (Object)list);
            mv.setViewName("/modules/scheduler/jobAdd");
        }
        else if ("4".equals(viewName)) {
            try {
                final String parameterJson2 = request.getParameter("parameterJson");
                this.schedulerService.updateJob(jobName, parameterJson2);
                msg = "\u4fee\u6539\u4efb\u52a1\u6210\u529f";
            }
            catch (ClassNotFoundException ex) {
                msg = "\u4fee\u6539\u4efb\u52a1\u5931\u8d25";
            }
            catch (Exception ex2) {
                msg = "\u4fee\u6539\u4efb\u52a1\u5931\u8d25";
            }
            this.addMessage(redirectAttributes, new String[] { msg });
            mv.setViewName("redirect:" + Global.getAdminPath() + "/sys/scheduler/?repage");
        }
        return mv;
    }

    @RequestMapping({ "delJob" })
    public String delJob(final HttpServletRequest request, final RedirectAttributes redirectAttributes) throws IOException, SchedulerException, ClassNotFoundException {
        String msg = "\u5220\u9664\u4efb\u52a1\u6210\u529f";
        try {
            final String jobName = this.TurnParam(request, "jobName");
            this.schedulerService.delJob(jobName);
        }
        catch (Exception e) {
            msg = "\u5220\u9664\u4efb\u52a1\u5931\u8d25";
        }
        this.addMessage(redirectAttributes, new String[] { msg });
        return "redirect:" + Global.getAdminPath() + "/sys/scheduler/?repage";
    }

    @RequestMapping({ "executeJob" })
    public String executeJob(final HttpServletRequest request, final RedirectAttributes redirectAttributes) throws IOException {
        final String jobName = request.getParameter("jobName");
        String msg = "\u6267\u884c\u4efb\u52a1\u6210\u529f";
        try {
            this.schedulerService.executeJob(jobName);
        }
        catch (SchedulerException e) {
            msg = "\u6267\u884c\u4efb\u52a1\u5931\u8d25!";
        }
        this.addMessage(redirectAttributes, new String[] { msg });
        return "redirect:" + Global.getAdminPath() + "/sys/scheduler/?repage";
    }

    @RequestMapping({ "validClass" })
    @ResponseBody
    public boolean validClass(@RequestParam(required = true, defaultValue = "") final String className) throws Exception {
        final boolean rtn = isClass(className);
        return rtn;
    }

    @RequestMapping({ "getTriggersByJob" })
    public ModelAndView getTriggersByJob(final HttpServletRequest request) throws SchedulerException {
        final String jobName = this.TurnParam(request, "jobName");
        final String message = this.TurnParam(request, "message");
        final List<Trigger> list = this.schedulerService.getTriggersByJob(jobName);
        for (int i = list.size() - 1; i > 0; --i) {
            final Trigger trigger = list.get(i);
            final String group = trigger.getKey().getGroup();
            if (!"group1".equals(group)) {
                list.remove(i);
            }
        }
        final HashMap<String, Trigger.TriggerState> mapState = this.schedulerService.getTriggerStatus(list);
        final ModelAndView mv = new ModelAndView();
        mv.setViewName("/modules/scheduler/triggerList");
        mv.addObject("list", (Object)list);
        mv.addObject("message", (Object)message);
        mv.addObject("mapState", (Object)mapState);
        mv.addObject("jobName", (Object)jobName);
        mv.addObject("STATE_NORMAL", (Object)Trigger.TriggerState.NORMAL);
        mv.addObject("STATE_BLOCKED", (Object)Trigger.TriggerState.BLOCKED);
        mv.addObject("STATE_PAUSED", (Object)Trigger.TriggerState.PAUSED);
        mv.addObject("STATE_ERROR", (Object)Trigger.TriggerState.ERROR);
        mv.addObject("STATE_NONE", (Object)Trigger.TriggerState.NONE);
        mv.addObject("STATE_COMPLETE", (Object)Trigger.TriggerState.COMPLETE);
        return mv;
    }

    @RequestMapping({ "addTrigger{viewName}" })
    public ModelAndView addTrigger(final HttpServletRequest request, @PathVariable final String viewName, final RedirectAttributes redirectAttributes) throws IOException, SchedulerException, ParseException {
        final ModelAndView mv = new ModelAndView();
        String msg = "\u6dfb\u52a0\u8ba1\u5212\u6210\u529f!";
        String jobName = this.TurnParam(request, "jobName");
        String trigName = request.getParameter("trigName");
        if ("1".equals(viewName)) {
            mv.addObject("jobName", (Object)jobName);
            mv.addObject("isAdd", (Object)1);
            mv.setViewName("/modules/scheduler/triggerAdd");
            return mv;
        }
        if ("2".equals(viewName)) {
            jobName = request.getParameter("jobName");
            final String planJson = request.getParameter("planJson");
            mv.addObject("jobName", (Object)jobName);
            final boolean rtn = this.schedulerService.isTriggerExists(trigName);
            if (rtn) {
                msg = "\u6307\u5b9a\u7684\u8ba1\u5212\u540d\u79f0\u5df2\u7ecf\u5b58\u5728!";
                mv.addObject("message", (Object)msg);
                mv.setViewName("redirect:" + Global.getAdminPath() + "/sys/scheduler/getTriggersByJob");
                return mv;
            }
            this.schedulerService.addTrigger(jobName, trigName, planJson);
            mv.addObject("message", (Object)msg);
            mv.addObject("jobName", (Object)jobName);
            mv.setViewName("redirect:" + Global.getAdminPath() + "/sys/scheduler/getTriggersByJob");
            return mv;
        }
        else {
            if ("3".equals(viewName)) {
                trigName = this.TurnParam(request, "trigName");
                final Trigger trigger = this.schedulerService.getTrigger(trigName);
                final String className = trigger.getClass().getName();
                String planTypeVal = "1";
                if ("org.quartz.impl.triggers.CronTriggerImpl".equals(className)) {
                    final CronTrigger cronTrigger = (CronTrigger)trigger;
                    final String cron = cronTrigger.getCronExpression();
                    final String[] timeArr = cron.split(" ");
                    if (trigger.getDescription().indexOf("CronTrigger") > -1) {
                        planTypeVal = "6";
                        mv.addObject("cron", (Object)cron);
                    }
                    else if (("?".equals(timeArr[5]) && !"?".equals(timeArr[3]) && !"*".equals(timeArr[3])) || ("?".equals(timeArr[5]) && "L".equals(timeArr[3]))) {
                        planTypeVal = "5";
                    }
                    else if (!"?".equals(timeArr[5]) && "?".equals(timeArr[3])) {
                        planTypeVal = "4";
                        mv.addObject("chkWeek", (Object)timeArr[5]);
                    }
                    else {
                        planTypeVal = "3";
                    }
                    mv.addObject("seconds", (Object)timeArr[0]);
                    mv.addObject("minutes", (Object)timeArr[1]);
                    mv.addObject("hours", (Object)timeArr[2]);
                    mv.addObject("days", (Object)timeArr[3]);
                }
                else if ("org.quartz.impl.triggers.CalendarIntervalTriggerImpl".equals(className)) {
                    planTypeVal = "2";
                    final CalendarIntervalTrigger calendarIntervalTrigger = (CalendarIntervalTrigger)trigger;
                    mv.addObject("repeatInterval", (Object)calendarIntervalTrigger.getRepeatInterval());
                }
                else {
                    final Date date = trigger.getStartTime();
                    final String dateStr = DateUtils.formatDate(date, new Object[] { "yyyy-MM-dd" });
                    mv.addObject("date", (Object)date);
                    mv.addObject("dateStr", (Object)dateStr);
                }
                mv.addObject("planTypeVal", (Object)planTypeVal);
                mv.setViewName("/modules/scheduler/triggerAdd");
                mv.addObject("trigger", (Object)trigger);
                mv.addObject("jobName", (Object)jobName);
                mv.addObject("isAdd", (Object)3);
                return mv;
            }
            if ("4".equals(viewName)) {
                jobName = request.getParameter("jobName");
                final String planJson = request.getParameter("planJson");
                this.schedulerService.updateTrigger(jobName, trigName, planJson);
                mv.addObject("message", (Object)"\u4fee\u6539\u8ba1\u5212\u6210\u529f");
                mv.addObject("jobName", (Object)jobName);
                mv.setViewName("redirect:" + Global.getAdminPath() + "/sys/scheduler/getTriggersByJob");
                return mv;
            }
            return mv;
        }
    }

    @RequestMapping({ "delTrigger" })
    public ModelAndView delTrigger(final HttpServletRequest request, final RedirectAttributes redirectAttributes) throws IOException, SchedulerException, ClassNotFoundException {
        final ModelAndView mv = new ModelAndView();
        String msg = "\u5220\u9664\u8ba1\u5212\u6210\u529f";
        final String jobName = request.getParameter("jobName");
        try {
            final String trigName = this.TurnParam(request, "trigName");
            this.schedulerService.delTrigger(trigName);
        }
        catch (Exception e) {
            msg = "\u5220\u9664\u8ba1\u5212\u5931\u8d25";
        }
        this.addMessage(redirectAttributes, new String[] { msg });
        mv.setViewName("redirect:" + Global.getAdminPath() + "/sys/scheduler/getTriggersByJob?jobName=" + jobName);
        return mv;
    }

    @RequestMapping({ "toggleTriggerRun" })
    public String toggleTriggerRun(final HttpServletRequest request, final RedirectAttributes redirectAttributes) throws IOException {
        final String trigName = this.TurnParam(request, "trigName");
        final String jobName = request.getParameter("jobName");
        String msg = "\u542f\u52a8\u6210\u529f";
        try {
            this.schedulerService.toggleTriggerRun(trigName);
        }
        catch (Exception e) {
            msg = "\u542f\u52a8\u5931\u8d25!";
        }
        this.addMessage(redirectAttributes, new String[] { msg });
        return "redirect:" + Global.getAdminPath() + "/sys/scheduler/getTriggersByJob?jobName=" + jobName;
    }

    @RequestMapping({ "changeStart" })
    public String changeStart(@RequestParam final boolean isStandby, final HttpServletRequest request, final RedirectAttributes redirectAttributes) throws Exception {
        String msg = "";
        try {
            if (isStandby) {
                this.schedulerService.start();
                msg = "\u542f\u52a8\u5b9a\u65f6\u5668\u6210\u529f!";
            }
            else {
                this.schedulerService.shutdown();
                msg = "\u505c\u6b62\u5b9a\u65f6\u5668\u6210\u529f!";
            }
            this.addMessage(redirectAttributes, new String[] { msg });
        }
        catch (Exception e) {
            e.printStackTrace();
            if (isStandby) {
                msg = "\u542f\u52a8\u5b9a\u65f6\u5668\u5931\u8d25:";
            }
            else {
                msg = "\u505c\u6b62\u5b9a\u65f6\u5668\u5931\u8d25:";
            }
            msg += e.getMessage();
        }
        this.addMessage(redirectAttributes, new String[] { msg });
        return "redirect:" + Global.getAdminPath() + "/sys/scheduler/?repage";
    }

    private String TurnParam(final HttpServletRequest request, String str) {
        try {
            str = request.getParameter(str);
            if (StringUtils.isNoneBlank(new CharSequence[] { str })) {
                str = new String(str.getBytes("ISO-8859-1"), "utf-8");
            }
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    private static boolean isClass(final String className) {
        try {
            Class.forName(className);
            return true;
        }
        catch (ClassNotFoundException ex) {
            return false;
        }
    }
}
