package com.kmvc.jeesite.modules.scheduler;

import com.kmvc.jeesite.modules.codesetmrg.entity.CodeSet;
import com.kmvc.jeesite.modules.codesetmrg.service.CodeSetService;
import com.kmvc.jeesite.modules.connector.webservice.client.SysWebServiceProxy;
import com.kmvc.jeesite.modules.datacheckingmrg.service.ValidationCodeSetService;
import com.kmvc.jeesite.modules.datacheckingmrg.service.ValidationCodeSetTmpService;
import com.kmvc.jeesite.modules.datacheckingmrg.service.ValidationDetailService;
import com.kmvc.jeesite.modules.datacheckingmrg.service.ValidationDetailTmpService;
import com.kmvc.jeesite.modules.mdmlog.entity.LogInout;
import com.kmvc.jeesite.modules.mdmlog.service.LogInoutService;
import com.kmvc.jeesite.modules.sys.entity.TSystem;
import com.kmvc.jeesite.modules.sys.service.TSystemService;
import org.slf4j.*;
import com.thinkgem.jeesite.common.utils.*;
import com.kmvc.jeesite.modules.mdmlog.service.*;
import com.kmvc.jeesite.modules.connector.webservice.client.*;
import com.kmvc.jeesite.modules.mdmlog.entity.*;
import com.kmvc.jeesite.modules.codesetmrg.service.*;
import com.kmvc.jeesite.modules.datacheckingmrg.service.*;

import java.util.*;
import java.util.Calendar;

import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import org.quartz.*;

public class SysWebServiceTimer extends BaseJob
{
    private final Logger logger;

    public SysWebServiceTimer() {
        this.logger = LoggerFactory.getLogger((Class)this.getClass());
    }

    @Override
    public void executeJob(final JobExecutionContext context) throws JobExecutionException {
        try {
            final TSystemService tSystemService = (TSystemService)SpringContextHolder.getBean((Class)TSystemService.class);
            final LogInoutService logInoutService = (LogInoutService)SpringContextHolder.getBean((Class)LogInoutService.class);
            this.logger.info("\u5f00\u59cb\u6267\u884c\u4efb\u52a1");
            final String systemNo = context.getJobDetail().getJobDataMap().getString("systemNo");
            this.logger.info("\u6267\u884c\u6536\u96c6\u4e1a\u52a1\u7cfb\u7edf\u7f16\u7801\uff1a" + systemNo);
            final SysWebServiceProxy proxy = new SysWebServiceProxy();
            if (systemNo != null && !systemNo.isEmpty()) {
                final TSystem system = tSystemService.findBySystemCode(systemNo);
                if (system == null) {
                    final LogInout inoutLog = new LogInout();
                    inoutLog.setDestSysName("MDM\u4e3b\u6570\u636e\u7ba1\u7406\u5e73\u53f0");
                    inoutLog.setRecordAmount(Integer.valueOf(0));
                    inoutLog.setStartDate(Calendar.getInstance().getTime());
                    inoutLog.setEndDate(Calendar.getInstance().getTime());
                    inoutLog.setStatus(Integer.valueOf(0));
                    inoutLog.setFailReason("\u627e\u4e0d\u5230\u4e1a\u52a1\u7cfb\u7edf\u7f16\u7801\u4e3a\uff1a" + systemNo + "\u7684\u4e1a\u52a1\u7cfb\u7edf");
                    inoutLog.setRemarks("\u627e\u4e0d\u5230\u4e1a\u52a1\u7cfb\u7edf\u7f16\u7801\u4e3a\uff1a" + systemNo + "\u7684\u4e1a\u52a1\u7cfb\u7edf");
                    logInoutService.save(inoutLog);
                    this.logger.info("\u627e\u4e0d\u5230\u4e1a\u52a1\u7cfb\u7edf\u7f16\u7801\u4e3a\uff1a" + systemNo + "\u7684\u4e1a\u52a1\u7cfb\u7edf");
                }
                else {
                    final ValidationCodeSetService validationCodeSetService = (ValidationCodeSetService)SpringContextHolder.getBean("validationCodeSetService");
                    final boolean isCheck = validationCodeSetService.findBySystemNoAndDate(systemNo);
                    if (!isCheck) {
                        final CodeSetService codeSetService = (CodeSetService)SpringContextHolder.getBean("codeSetService");
                        final List<CodeSet> codeSetList = (List<CodeSet>)codeSetService.findBySystemId(system.getId());
                        if (codeSetList.size() == 0) {
                            final LogInout logInout = new LogInout();
                            logInout.setDestSysName("MDM\u4e3b\u6570\u636e\u7ba1\u7406\u5e73\u53f0");
                            logInout.setSourceSysName(system.getSystemName());
                            logInout.setRecordAmount(Integer.valueOf(0));
                            logInout.setStartDate(Calendar.getInstance().getTime());
                            logInout.setEndDate(Calendar.getInstance().getTime());
                            logInout.setStatus(Integer.valueOf(0));
                            logInout.setRecordType(Integer.valueOf(0));
                            logInout.setFailReason("\u4e1a\u52a1\u7cfb\u7edf\u3010" + system.getSystemName() + "\u3011\u5728\u3010" + "MDM\u4e3b\u6570\u636e\u7ba1\u7406\u5e73\u53f0" + "\u3011\u4e2d\u6ca1\u6709\u542f\u7528\u7684\u4ee3\u7801\u96c6\u6570\u636e");
                            logInout.setRemarks("\u4e1a\u52a1\u7cfb\u7edf\u3010" + system.getSystemName() + "\u3011\u5728\u3010" + "MDM\u4e3b\u6570\u636e\u7ba1\u7406\u5e73\u53f0" + "\u3011\u4e2d\u6ca1\u6709\u542f\u7528\u7684\u4ee3\u7801\u96c6\u6570\u636e");
                            logInoutService.save(logInout);
                        }
                        else {
                            this.logger.info("\u4ee3\u7406\u53d1\u9001\u4e1a\u52a1\u7cfb\u7edf\uff1a" + system.getSystemShort());
                            final ValidationDetailTmpService validationDetailTmpService = (ValidationDetailTmpService)SpringContextHolder.getBean("validationDetailTmpService");
                            validationDetailTmpService.deleteBySystemNo(systemNo);
                            final ValidationCodeSetTmpService validationCodeSetTmpService = (ValidationCodeSetTmpService)SpringContextHolder.getBean("validationCodeSetTmpService");
                            validationCodeSetTmpService.deleteBySystemNo(systemNo);
                            logInoutService.deleteByToday();
                            proxy.collectSysCodeSets(system);
                            proxy.asyncCommit();
                            final ValidationDetailService validationDetailService = (ValidationDetailService)SpringContextHolder.getBean("validationDetailService");
                            validationDetailService.verificationPro(systemNo);
                        }
                    }
                    else {
                        final LogInout logInout2 = new LogInout();
                        logInout2.setDestSysName("MDM\u4e3b\u6570\u636e\u7ba1\u7406\u5e73\u53f0");
                        logInout2.setSourceSysName(system.getSystemName());
                        logInout2.setRecordAmount(Integer.valueOf(0));
                        logInout2.setStartDate(Calendar.getInstance().getTime());
                        logInout2.setEndDate(Calendar.getInstance().getTime());
                        logInout2.setStatus(Integer.valueOf(0));
                        logInout2.setRecordType(Integer.valueOf(0));
                        logInout2.setFailReason("\u4e1a\u52a1\u7cfb\u7edf\u3010" + system.getSystemName() + "\u3011\u4eca\u5929\u6536\u96c6\u5e76\u5df2\u6838\u67e5\u8fc7\uff0c\u4e0d\u9700\u8981\u518d\u6b21\u8fdb\u884c\u6536\u96c6");
                        logInout2.setRemarks("\u4e1a\u52a1\u7cfb\u7edf\u3010" + system.getSystemName() + "\u3011\u4eca\u5929\u6536\u96c6\u5e76\u5df2\u6838\u67e5\u8fc7\uff0c\u4e0d\u9700\u8981\u518d\u6b21\u8fdb\u884c\u6536\u96c6");
                        logInoutService.save(logInout2);
                        this.logger.info("\u7f16\u7801\u4e3a\uff1a" + systemNo + "\u7684\u4e1a\u52a1\u7cfb\u7edf\u4eca\u5929\u6536\u96c6\u5e76\u5df2\u6838\u67e5\u8fc7\uff0c\u4e0d\u9700\u8981\u518d\u6b21\u8fdb\u884c\u6536\u96c6");
                    }
                }
            }
            else {
                final LogInout logInout3 = new LogInout();
                logInout3.setDestSysName("MDM\u4e3b\u6570\u636e\u7ba1\u7406\u5e73\u53f0");
                logInout3.setRecordAmount(Integer.valueOf(0));
                logInout3.setStartDate(Calendar.getInstance().getTime());
                logInout3.setEndDate(Calendar.getInstance().getTime());
                logInout3.setStatus(Integer.valueOf(0));
                logInout3.setFailReason("\u6267\u884c\u6536\u96c6\u4e1a\u52a1\u7cfb\u7edf\u7f16\u7801\u4e3a\u7a7a");
                logInout3.setRemarks("\u6267\u884c\u6536\u96c6\u4e1a\u52a1\u7cfb\u7edf\u7f16\u7801\u4e3a\u7a7a");
                logInoutService.save(logInout3);
                this.logger.info("\u6267\u884c\u6536\u96c6\u4e1a\u52a1\u7cfb\u7edf\u7f16\u7801\u4e3a\u7a7a");
            }
            this.logger.info("\u6536\u96c6\u7ed3\u675f");
        }
        catch (Exception e) {
            this.logger.error(e.getMessage(), (Throwable)e);
        }
    }
}
