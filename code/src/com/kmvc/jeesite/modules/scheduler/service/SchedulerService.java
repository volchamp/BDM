package com.kmvc.jeesite.modules.scheduler.service;

import com.kmvc.jeesite.modules.common.JacksonUtil;
import com.kmvc.jeesite.modules.scheduler.entity.ParameterObj;
import com.kmvc.jeesite.modules.scheduler.entity.PlanObject;
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import org.quartz.impl.matchers.*;
import java.text.*;
import com.kmvc.jeesite.modules.common.*;
import com.thinkgem.jeesite.common.utils.*;
import java.util.*;
import com.kmvc.jeesite.modules.scheduler.entity.*;
import org.quartz.*;
import org.springframework.transaction.annotation.*;

@Service
public class SchedulerService
{
    @Autowired
    Scheduler scheduler;
    private static HashMap<String, String> mapWeek;

    public void setScheduler(final Scheduler s) {
        this.scheduler = s;
    }

    public boolean addJob(final String jobName, final String className, final String parameterJson, final String description) throws SchedulerException, ClassNotFoundException {
        if (this.scheduler == null) {
            return false;
        }
        final Class<Job> cls = (Class<Job>)Class.forName(className);
        final JobBuilder jb = JobBuilder.newJob((Class)cls);
        jb.withIdentity(jobName, "group1");
        if (StringUtils.isNotEmpty((CharSequence)parameterJson)) {
            this.setJobMap(parameterJson, jb);
        }
        jb.storeDurably();
        jb.withDescription(description);
        final JobDetail jobDetail = jb.build();
        this.scheduler.addJob(jobDetail, true);
        return true;
    }

    public boolean isJobExists(final String jobName) throws SchedulerException {
        if (this.scheduler == null) {
            return false;
        }
        final JobKey key = new JobKey(jobName, "group1");
        return this.scheduler.checkExists(key);
    }

    public JobDetail getJobDetail(final String jobName) throws SchedulerException {
        if (this.scheduler == null) {
            return null;
        }
        final JobKey key = new JobKey(jobName, "group1");
        final JobDetail detail = this.scheduler.getJobDetail(key);
        return detail;
    }

    public List<JobDetail> getJobList() throws SchedulerException {
        if (this.scheduler == null) {
            return new ArrayList<JobDetail>();
        }
        final List<JobDetail> list = new ArrayList<JobDetail>();
        final GroupMatcher<JobKey> matcher = (GroupMatcher<JobKey>)GroupMatcher.groupEquals("group1");
        final Set<JobKey> set = (Set<JobKey>)this.scheduler.getJobKeys((GroupMatcher)matcher);
        for (final JobKey jobKey : set) {
            final JobDetail detail = this.scheduler.getJobDetail(jobKey);
            list.add(detail);
        }
        return list;
    }

    public List<Trigger> getTriggersByJob(final String jobName) throws SchedulerException {
        if (this.scheduler == null) {
            return new ArrayList<Trigger>();
        }
        final JobKey key = new JobKey(jobName, "group1");
        return (List<Trigger>)this.scheduler.getTriggersOfJob(key);
    }

    public HashMap<String, Trigger.TriggerState> getTriggerStatus(final List<Trigger> list) throws SchedulerException {
        if (this.scheduler == null) {
            return new HashMap<String, Trigger.TriggerState>();
        }
        final HashMap<String, Trigger.TriggerState> map = new HashMap<String, Trigger.TriggerState>();
        for (final Trigger trigger : list) {
            final TriggerKey key = trigger.getKey();
            final Trigger.TriggerState state = this.scheduler.getTriggerState(key);
            map.put(key.getName(), state);
        }
        return map;
    }

    public boolean isTriggerExists(final String trigName) throws SchedulerException {
        if (this.scheduler == null) {
            return false;
        }
        final TriggerKey triggerKey = new TriggerKey(trigName, "group1");
        return this.scheduler.checkExists(triggerKey);
    }

    public void addTrigger(final String jobName, final String trigName, final String planJson) throws SchedulerException, ParseException {
        if (this.scheduler == null) {
            return;
        }
        final JobKey jobKey = new JobKey(jobName, "group1");
        final TriggerBuilder<Trigger> tb = (TriggerBuilder<Trigger>)TriggerBuilder.newTrigger();
        tb.withIdentity(trigName, "group1");
        this.setTrigBuilder(planJson, tb);
        tb.forJob(jobKey);
        final Trigger trig = tb.build();
        this.scheduler.scheduleJob(trig);
    }

    public void addTrigger(final String jobName, final String trigName, final int minute) throws SchedulerException {
        if (this.scheduler == null) {
            return;
        }
        final JobKey jobKey = new JobKey(jobName, "group1");
        final TriggerBuilder<Trigger> tb = (TriggerBuilder<Trigger>)TriggerBuilder.newTrigger();
        tb.withIdentity(trigName, "group1");
        final ScheduleBuilder sb = (ScheduleBuilder)CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInMinutes(minute);
        tb.startNow();
        tb.withSchedule(sb);
        tb.withDescription("\u6bcf:" + minute + "\u5206\u949f\u6267\u884c!");
        tb.forJob(jobKey);
        final Trigger trig = tb.build();
        this.scheduler.scheduleJob(trig);
    }

    public void updateTrigger(final String jobName, final String trigName, final String planJson) throws SchedulerException, ParseException {
        if (this.scheduler == null) {
            return;
        }
        final JobKey jobKey = new JobKey(jobName, "group1");
        final TriggerKey key = new TriggerKey(trigName, "group1");
        final Trigger trigger = this.scheduler.getTrigger(key);
        final TriggerBuilder<Trigger> tb = (TriggerBuilder<Trigger>)trigger.getTriggerBuilder();
        this.setTrigBuilder(planJson, tb);
        tb.forJob(jobKey);
        final Trigger trig = tb.build();
        this.scheduler.rescheduleJob(key, trig);
    }

    private void setTrigBuilder(final String planJson, final TriggerBuilder<Trigger> tb) throws ParseException {
        final PlanObject planObject = (PlanObject) JacksonUtil.toBean(planJson, (Class)PlanObject.class);
        final int type = planObject.getType();
        final String value = planObject.getTimeInterval();
        switch (type) {
            case 1: {
                final Date date = DateUtils.parseDate(value, new String[] { "yyyy-MM-dd HH:mm:ss" });
                tb.startAt(date);
                tb.withDescription("\u6267\u884c\u4e00\u6b21,\u6267\u884c\u65f6\u95f4\uff1a" + date.toLocaleString());
                break;
            }
            case 2: {
                final int minute = Integer.parseInt(value);
                final ScheduleBuilder sb = (ScheduleBuilder)CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInMinutes(minute);
                tb.startNow();
                tb.withSchedule(sb);
                tb.withDescription("\u6bcf\uff1a" + minute + "\u5206\u949f\u6267\u884c!");
                break;
            }
            case 3: {
                final String[] aryTime = value.split(":");
                final int hour = Integer.parseInt(aryTime[0]);
                final int m = Integer.parseInt(aryTime[1]);
                final ScheduleBuilder sb2 = (ScheduleBuilder)CronScheduleBuilder.dailyAtHourAndMinute(hour, m);
                tb.startNow();
                tb.withSchedule(sb2);
                tb.withDescription("\u6bcf\u5929\uff1a" + hour + ":" + m + "\u6267\u884c!");
                break;
            }
            case 4: {
                final String[] aryExpression = value.split("[|]");
                final String week = aryExpression[0];
                final String[] aryTime2 = aryExpression[1].split(":");
                final String h1 = aryTime2[0];
                final String m2 = aryTime2[1];
                final String cronExperssion = "0 " + m2 + " " + h1 + " ? * " + week;
                final ScheduleBuilder sb3 = (ScheduleBuilder)CronScheduleBuilder.cronSchedule(cronExperssion);
                tb.startNow();
                tb.withSchedule(sb3);
                final String weekName = this.getWeek(week);
                tb.withDescription("\u6bcf\u5468\uff1a" + weekName + "," + h1 + ":" + m2 + "\u6267\u884c!");
                break;
            }
            case 5: {
                final String[] aryExpression2 = value.split("[|]");
                final String day = aryExpression2[0];
                final String[] aryTime3 = aryExpression2[1].split(":");
                final String h2 = aryTime3[0];
                final String m3 = aryTime3[1];
                final String cronExperssion2 = "0 " + m3 + " " + h2 + " " + day + " * ?";
                final ScheduleBuilder sb4 = (ScheduleBuilder)CronScheduleBuilder.cronSchedule(cronExperssion2);
                tb.startNow();
                tb.withSchedule(sb4);
                final String dayName = this.getDay(day);
                tb.withDescription("\u6bcf\u6708\uff1a" + dayName + "," + h2 + ":" + m3 + "\u6267\u884c!");
                break;
            }
            case 6: {
                final ScheduleBuilder sb5 = (ScheduleBuilder)CronScheduleBuilder.cronSchedule(value);
                tb.startNow();
                tb.withSchedule(sb5);
                tb.withDescription("CronTrigger\u8868\u8fbe\u5f0f\uff1a" + value);
                break;
            }
        }
    }

    private String getDay(final String day) {
        final String[] aryDay = day.split(",");
        final int len = aryDay.length;
        String str = "";
        for (int i = 0; i < len; ++i) {
            String tmp = aryDay[i];
            tmp = (tmp.equals("L") ? "\u6700\u540e\u4e00\u5929" : tmp);
            if (i < len - 1) {
                str = str + tmp + ",";
            }
            else {
                str += tmp;
            }
        }
        return str;
    }

    private String getWeek(final String week) {
        final String[] aryWeek = week.split(",");
        final int len = aryWeek.length;
        String str = "";
        for (int i = 0; i < len; ++i) {
            if (i < len - 1) {
                str = str + SchedulerService.mapWeek.get(aryWeek[i]) + ",";
            }
            else {
                str += SchedulerService.mapWeek.get(aryWeek[i]);
            }
        }
        return str;
    }

    private void setJobMap(final String json, final JobBuilder jb) {
        final List<ParameterObj> list = (List<ParameterObj>)JacksonUtil.toListBean(json, (Class)ParameterObj.class);
        for (int i = 0; i < list.size(); ++i) {
            final ParameterObj obj = list.get(i);
            final String type = obj.getType();
            final String name = obj.getName();
            final String value = obj.getValue();
            if (type.equals("int")) {
                if (StringUtils.isEmpty((CharSequence)value)) {
                    jb.usingJobData(name, Integer.valueOf(0));
                }
                else {
                    jb.usingJobData(name, Integer.valueOf(Integer.parseInt(value)));
                }
            }
            else if (type.equals("long")) {
                if (StringUtils.isEmpty((CharSequence)value)) {
                    jb.usingJobData(name, Integer.valueOf(0));
                }
                else {
                    jb.usingJobData(name, Long.valueOf(Long.parseLong(value)));
                }
            }
            else if (type.equals("float")) {
                if (StringUtils.isEmpty((CharSequence)value)) {
                    jb.usingJobData(name, Float.valueOf(0.0f));
                }
                else {
                    jb.usingJobData(name, Float.valueOf(Float.parseFloat(value)));
                }
            }
            else if (type.equals("boolean")) {
                if (StringUtils.isEmpty((CharSequence)value)) {
                    jb.usingJobData(name, Boolean.valueOf(false));
                }
                else {
                    jb.usingJobData(name, Boolean.valueOf(Boolean.parseBoolean(value)));
                }
            }
            else {
                jb.usingJobData(name, value);
            }
        }
    }

    public void delJob(final String jobName) throws SchedulerException {
        if (this.scheduler == null) {
            return;
        }
        final JobKey key = new JobKey(jobName, "group1");
        this.scheduler.deleteJob(key);
    }

    @Transactional(readOnly = false, rollbackFor = { Exception.class })
    public boolean updateJob(final String jobName, final String parameterJson) throws SchedulerException, ClassNotFoundException {
        if (this.scheduler == null) {
            return false;
        }
        final JobKey key = new JobKey(jobName, "group1");
        final JobDetail detail = this.scheduler.getJobDetail(key);
        final JobBuilder jb = detail.getJobBuilder();
        jb.setJobData(new JobDataMap());
        if (StringUtils.isNotEmpty((CharSequence)parameterJson) && !parameterJson.equals("[]")) {
            this.setJobMap(parameterJson, jb);
        }
        jb.storeDurably();
        final JobDetail jobDetail = jb.build();
        this.scheduler.addJob(jobDetail, true);
        return true;
    }

    public Trigger getTrigger(final String triggerName) throws SchedulerException {
        if (this.scheduler == null) {
            return null;
        }
        final TriggerKey key = new TriggerKey(triggerName, "group1");
        final Trigger trigger = this.scheduler.getTrigger(key);
        return trigger;
    }

    public void delTrigger(final String triggerName) throws SchedulerException {
        if (this.scheduler == null) {
            return;
        }
        final TriggerKey key = new TriggerKey(triggerName, "group1");
        this.scheduler.unscheduleJob(key);
    }

    public void toggleTriggerRun(final String triggerName) throws SchedulerException {
        if (this.scheduler == null) {
            return;
        }
        final TriggerKey key = new TriggerKey(triggerName, "group1");
        final Trigger.TriggerState state = this.scheduler.getTriggerState(key);
        if (state == Trigger.TriggerState.PAUSED) {
            this.scheduler.resumeTrigger(key);
        }
        else if (state == Trigger.TriggerState.NORMAL) {
            this.scheduler.pauseTrigger(key);
        }
    }

    public void executeJob(final String jobName) throws SchedulerException {
        if (this.scheduler == null) {
            return;
        }
        final JobKey key = new JobKey(jobName, "group1");
        this.scheduler.triggerJob(key);
    }

    public void start() throws SchedulerException {
        this.scheduler.start();
    }

    public void shutdown() throws SchedulerException {
        this.scheduler.standby();
    }

    public boolean isStarted() throws SchedulerException {
        return this.scheduler.isStarted();
    }

    public boolean isInStandbyMode() throws SchedulerException {
        return this.scheduler.isInStandbyMode();
    }

    static {
        (SchedulerService.mapWeek = new HashMap<String, String>()).put("MON", "\u661f\u671f\u4e00");
        SchedulerService.mapWeek.put("TUE", "\u661f\u671f\u4e8c");
        SchedulerService.mapWeek.put("WED", "\u661f\u671f\u4e09");
        SchedulerService.mapWeek.put("THU", "\u661f\u671f\u56db");
        SchedulerService.mapWeek.put("FRI", "\u661f\u671f\u4e94");
        SchedulerService.mapWeek.put("SAT", "\u661f\u671f\u516d");
        SchedulerService.mapWeek.put("SUN", "\u661f\u671f\u65e5");
    }
}
