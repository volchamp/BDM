package com.kmvc.jeesite.modules.connector.webservice.serve;

import com.kmvc.jeesite.modules.codesetmrg.entity.CodeSet;
import com.kmvc.jeesite.modules.codesetmrg.service.CodeSetService;
import com.kmvc.jeesite.modules.connector.vo.serve.inquire.ListCodeSetReq;
import com.kmvc.jeesite.modules.connector.vo.serve.inquire.ListCodeSetResp;
import com.kmvc.jeesite.modules.mdmlog.entity.LogInout;
import com.kmvc.jeesite.modules.mdmlog.service.LogInoutService;
import com.kmvc.jeesite.modules.sys.entity.TSystem;
import com.kmvc.jeesite.modules.sys.service.TSystemService;
import org.springframework.transaction.annotation.*;
import org.springframework.stereotype.*;
import org.apache.log4j.*;
import com.kmvc.jeesite.modules.mdmlog.service.*;
import com.thinkgem.jeesite.common.utils.*;
import com.kmvc.jeesite.modules.mdmlog.entity.*;
import com.kmvc.jeesite.modules.codesetmrg.service.*;
import com.kmvc.jeesite.modules.connector.vo.serve.inquire.*;
import java.util.*;

@Transactional(readOnly = true)
@Service
public class ListCodeSetSrvImpl implements ListCodeSetSrv
{
    private Logger logger;

    public ListCodeSetSrvImpl() {
        this.logger = Logger.getLogger((Class)ListCodeSetSrvImpl.class);
    }

    @Transactional(readOnly = false, rollbackFor = { Exception.class })
    @Override
    public ListCodeSetResp listCodeSet(final ListCodeSetReq request) {
        final LogInoutService logInoutService = (LogInoutService)SpringContextHolder.getBean("logInoutService");
        ListCodeSetResp resp = new ListCodeSetResp();
        LogInout logInout = this.msgLogObj(request.getMessageId());
        try {
            if (logInout == null) {
                logInout = new LogInout();
                logInout.setMessageId(request.getMessageId());
                logInout.setCurrentPage(1);
                logInout.setSourceSysName("MDM\u4e3b\u6570\u636e\u7ba1\u7406\u5e73\u53f0");
                logInout.setDestSysName(request.getSystemName());
                logInout.setStartDate(new Date());
                logInout.setRecordType(1);
                logInout.setRemarks("\u54cd\u5e94\u3010" + request.getSystemName() + "\u3011\u7684\u4ee3\u7801\u96c6\u76ee\u5f55\u67e5\u8be2\u670d\u52a1\u8bf7\u6c42\u3002");
                logInoutService.save(logInout);
                final boolean b = this.verify(request);
                if (b) {
                    this.logger.info((Object)("\u5165\u53c2\u9a8c\u8bc1\u6210\u529f\uff0c\u6d88\u606fid\u4e3a\uff1a" + request.getMessageId()));
                    resp = this.getCodeSetList(request);
                }
                else {
                    logInout = this.msgLogObj(request.getMessageId());
                    logInout.setStatus(0);
                    logInout.setFailReason("\u9a8c\u8bc1\u5931\u8d25\uff0c\u3010" + request.getSystemName() + "\u3011\u53d1\u9001\u8bf7\u6c42\u683c\u5f0f\u9519\u8bef");
                    logInout.setEndDate(new Date());
                    logInoutService.save(logInout);
                    this.logger.info((Object)("\u5165\u53c2\u9a8c\u8bc1\u5931\u8d25\uff0c\u6d88\u606fid\u4e3a\uff1a" + request.getMessageId()));
                    resp.setResFlag(16);
                    resp.setErrorMessage("\u8f93\u5165\u5bf9\u8c61\u7684\u5c5e\u6027\u503c\u6821\u9a8c\u4e0d\u5408\u6cd5");
                }
                return resp;
            }
            logInout.setStatus(0);
            logInout.setFailReason("\u3010" + request.getSystemName() + "\u3011\u64cd\u4f5c\u9519\u8bef\uff0c\u6d88\u606fid\uff1a\u3010" + request.getMessageId() + "\u3011\u5fc5\u987b\u552f\u4e00");
            resp.setResFlag(14);
            resp.setErrorMessage("\u64cd\u4f5c\u9519\u8bef\uff01\u4e0d\u540c\u6279\u6b21\u7684\u6d88\u606fid\u5fc5\u987b\u552f\u4e00");
            this.logger.info((Object)("\u64cd\u4f5c\u9519\u8bef\uff0c\u6d88\u606fid\u4e3a\uff1a" + request.getMessageId()));
            logInoutService.save(logInout);
            return resp;
        }
        catch (Exception e) {
            logInout.setStatus(1);
            logInout.setFailReason("\u3010" + request.getSystemName() + "\u3011\u8f93\u51fa\u5931\u8d25\uff0cMDM\u63a5\u6536\u5230\u8bf7\u6c42\u540e\u5904\u7406\u5f02\u5e38,\u5f02\u5e38\u5185\u5bb9\uff1a" + e.getMessage());
            logInout.setEndDate(new Date());
            this.logger.info((Object)("MDM\u63a5\u6536\u5230\u8bf7\u6c42\u540e\u5904\u7406\u5f02\u5e38\uff0c\u6d88\u606fid\u4e3a\uff1a" + request.getMessageId()));
            this.logger.info((Object)("\u5f02\u5e38\u5185\u5bb9\uff1a" + e.getMessage()));
            logInoutService.save(logInout);
            resp.setResFlag(15);
            resp.setErrorMessage("\u8f93\u51fa\u5931\u8d25\uff0cMDM\u63a5\u6536\u5230\u8bf7\u6c42\u540e\u5904\u7406\u5f02\u5e38,\u5f02\u5e38\u5185\u5bb9\u4e3a\uff1a" + e.getMessage());
            e.printStackTrace();
            return resp;
        }
    }

    private LogInout msgLogObj(final String messageId) {
        final LogInoutService logInoutService = (LogInoutService)SpringContextHolder.getBean("logInoutService");
        return logInoutService.getLogByMsg(messageId, 1);
    }

    @Transactional(readOnly = false, rollbackFor = { Exception.class })
    private ListCodeSetResp getCodeSetList(final ListCodeSetReq request) throws Exception {
        final LogInoutService logInoutService = (LogInoutService)SpringContextHolder.getBean("logInoutService");
        final ListCodeSetResp resp = new ListCodeSetResp();
        final CodeSetService codeSetService = (CodeSetService)SpringContextHolder.getBean("codeSetService");
        final TSystemService tSystemService = (TSystemService)SpringContextHolder.getBean((Class)TSystemService.class);
        try {
            final TSystem tSystem = new TSystem();
            tSystem.setSystemCode(request.getSystemCode());
            tSystem.setSystemName(request.getSystemName());
            final List<TSystem> sysList = tSystemService.findList(tSystem);
            if (sysList.size() == 0) {
                resp.setResFlag(17);
                resp.setErrorMessage("\u57fa\u7840\u6570\u636e\u5e73\u53f0\u6839\u636e\u63d0\u4f9b\u7684\u7cfb\u7edf\u7f16\u7801\u548c\u540d\u79f0\u67e5\u627e\u4e0d\u5230\u4e1a\u52a1\u7cfb\u7edf");
                return resp;
            }
            final List<CodeSet> list = codeSetService.findBySystemCode(request);
            final List<com.kmvc.jeesite.modules.connector.vo.serve.inquire.CodeSet> codeSetList = new ArrayList<com.kmvc.jeesite.modules.connector.vo.serve.inquire.CodeSet>();
            for (final CodeSet codeSet : list) {
                final com.kmvc.jeesite.modules.connector.vo.serve.inquire.CodeSet cs = new com.kmvc.jeesite.modules.connector.vo.serve.inquire.CodeSet();
                cs.setCodeSetNo(codeSet.getCodeSetNo());
                cs.setCodeSetName(codeSet.getCodeSetName());
                codeSetList.add(cs);
            }
            resp.setCodeSetCollection(codeSetList);
            resp.setResFlag(0);
            if (list.isEmpty()) {
                resp.setErrorMessage("\u670d\u52a1\u8c03\u7528\u6210\u529f\uff0c\u6839\u636e\u4e1a\u52a1\u7cfb\u7edf\u7f16\u7801\u540d\u79f0\u6761\u4ef6\u6ca1\u6709\u67e5\u8be2\u5230\u4ee3\u7801\u96c6\u76ee\u5f55");
            }
            final LogInout logInout = this.msgLogObj(request.getMessageId());
            logInout.setStatus(1);
            logInout.setRecordAmount(list.size());
            logInout.setEndDate(new Date());
            logInoutService.save(logInout);
            return resp;
        }
        catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Transactional
    public boolean verify(final ListCodeSetReq req) {
        final boolean b = true;
        if (req.getSystemCode().isEmpty()) {
            this.logger.info((Object)"\u4e1a\u52a1\u7cfb\u7edf\u7f16\u7801\u4e3a\u7a7a");
            return false;
        }
        if (req.getSystemName().isEmpty()) {
            this.logger.info((Object)"\u4e1a\u52a1\u7cfb\u7edf\u540d\u79f0\u4e3a\u7a7a");
            return false;
        }
        if (req.getMessageId().isEmpty()) {
            this.logger.info((Object)"\u6d88\u606fid\u4e3a\u7a7a");
            return false;
        }
        final String[] messageId = req.getMessageId().split("_");
        if (!messageId[0].equals(req.getSystemCode())) {
            this.logger.info((Object)"\u6d88\u606fID\u683c\u5f0f\u9519\u8bef");
            return false;
        }
        if (!messageId[1].equals("MDM")) {
            this.logger.info((Object)"\u6d88\u606fID\u683c\u5f0f\u9519\u8bef");
            return false;
        }
        return b;
    }
}
