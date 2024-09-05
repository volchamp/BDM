package com.kmvc.jeesite.modules.connector.webservice.serve;

import com.kmvc.jeesite.modules.codesetmrg.entity.CodeSetData;
import com.kmvc.jeesite.modules.codesetmrg.service.CodeSetDataService;
import com.kmvc.jeesite.modules.connector.vo.serve.getcodeset.*;
import com.kmvc.jeesite.modules.mdmlog.entity.LogInout;
import com.kmvc.jeesite.modules.mdmlog.service.LogInoutService;
import org.springframework.transaction.annotation.*;
import org.springframework.stereotype.*;
import org.apache.log4j.*;
import com.kmvc.jeesite.modules.mdmlog.service.*;
import com.thinkgem.jeesite.common.utils.*;
import com.kmvc.jeesite.modules.mdmlog.entity.*;
import com.kmvc.jeesite.modules.codesetmrg.service.*;
import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import java.util.*;
import com.kmvc.jeesite.modules.connector.vo.serve.getcodeset.*;

@Transactional(readOnly = true)
@Service
public class FetchCodeSetDataSrvImpl implements FetchCodeSetDataSrv
{
    private Logger logger;

    public FetchCodeSetDataSrvImpl() {
        this.logger = Logger.getLogger((Class)FetchCodeSetDataSrvImpl.class);
    }

    @Transactional(readOnly = false, rollbackFor = { Exception.class })
    @Override
    public CollectCodeSetResp fetchCodeSetData(final CollectCodeSetReq request) {
        final LogInoutService logInoutService = (LogInoutService)SpringContextHolder.getBean("logInoutService");
        CollectCodeSetResp resp = new CollectCodeSetResp();
        LogInout inoutLog = this.msgLogObj(request.getCollectCodeSetReqHeader().getMessageId(), request.getCollectCodeSetReqHeader().getCurrentPage());
        try {
            if (inoutLog == null) {
                inoutLog = new LogInout();
                inoutLog.setMessageId(request.getCollectCodeSetReqHeader().getMessageId());
                inoutLog.setCurrentPage(request.getCollectCodeSetReqHeader().getCurrentPage());
                inoutLog.setSourceSysName("MDM\u4e3b\u6570\u636e\u7ba1\u7406\u5e73\u53f0");
                inoutLog.setDestSysName(request.getCollectCodeSetReqBody().getSystemName());
                inoutLog.setCodeSetName(request.getCollectCodeSetReqBody().getCodeSetName());
                inoutLog.setStartDate(new Date());
                inoutLog.setRecordType(1);
                inoutLog.setRemarks("\u54cd\u5e94\u3010" + request.getCollectCodeSetReqBody().getSystemName() + "\u3011\u7684\u4ee3\u7801\u96c6\u660e\u7ec6\u67e5\u8be2\u670d\u52a1\u8bf7\u6c42\u3002");
                logInoutService.save(inoutLog);
                final boolean b = this.verify(request);
                if (b) {
                    this.logger.info((Object)("\u5165\u53c2\u9a8c\u8bc1\u6210\u529f\uff0c\u6d88\u606fid\u4e3a\uff1a" + request.getCollectCodeSetReqHeader().getMessageId()));
                    resp = this.getCodeSetDataList(request);
                }
                else {
                    inoutLog = this.msgLogObj(request.getCollectCodeSetReqHeader().getMessageId(), request.getCollectCodeSetReqHeader().getCurrentPage());
                    inoutLog.setStatus(0);
                    inoutLog.setFailReason("\u3010" + request.getCollectCodeSetReqBody().getSystemName() + "\u3011\uff1a\u8bf7\u6c42\u683c\u5f0f\u9519\u8bef\u3002");
                    inoutLog.setEndDate(new Date());
                    logInoutService.save(inoutLog);
                    this.logger.info((Object)("\u5165\u53c2\u9a8c\u8bc1\u5931\u8d25\uff0c\u6d88\u606fid\u4e3a\uff1a" + request.getCollectCodeSetReqHeader().getMessageId()));
                    resp.setResFlag(16);
                    resp.setErrorMessage("\u8f93\u5165\u5bf9\u8c61\u7684\u5c5e\u6027\u503c\u6821\u9a8c\u4e0d\u5408\u6cd5");
                }
                return resp;
            }
            inoutLog.setStatus(0);
            inoutLog.setFailReason("\u3010" + request.getCollectCodeSetReqBody().getSystemName() + "\u3011\u64cd\u4f5c\u9519\u8bef\uff0c\u6d88\u606fid\uff1a\u3010" + request.getCollectCodeSetReqHeader().getMessageId() + "\u3011\u5fc5\u987b\u552f\u4e00");
            resp.setResFlag(14);
            resp.setErrorMessage("\u64cd\u4f5c\u9519\u8bef\uff01\u4e0d\u540c\u6279\u6b21\u7684\u6d88\u606fid\u5fc5\u987b\u552f\u4e00");
            this.logger.info((Object)("\u64cd\u4f5c\u9519\u8bef\uff0c\u6d88\u606fid\u4e3a\uff1a" + request.getCollectCodeSetReqHeader().getMessageId()));
            logInoutService.save(inoutLog);
            return resp;
        }
        catch (Exception e) {
            inoutLog = this.msgLogObj(request.getCollectCodeSetReqHeader().getMessageId(), request.getCollectCodeSetReqHeader().getCurrentPage());
            inoutLog.setStatus(1);
            inoutLog.setFailReason("\u3010" + request.getCollectCodeSetReqBody().getSystemName() + "\u3011\u8f93\u51fa\u5931\u8d25\uff0cMDM\u63a5\u6536\u5230\u8bf7\u6c42\u540e\u5904\u7406\u5f02\u5e38,\u5f02\u5e38\u5185\u5bb9\uff1a" + e.getMessage());
            inoutLog.setEndDate(new Date());
            this.logger.info((Object)("MDM\u63a5\u6536\u5230\u8bf7\u6c42\u540e\u5904\u7406\u5f02\u5e38\uff0c\u6d88\u606fid\u4e3a\uff1a" + request.getCollectCodeSetReqHeader().getMessageId()));
            this.logger.info((Object)("\u5f02\u5e38\u5185\u5bb9\uff1a" + e.getMessage()));
            logInoutService.save(inoutLog);
            resp.setResFlag(18);
            resp.setErrorMessage("\u8f93\u51fa\u5931\u8d25\uff0cMDM\u63a5\u6536\u5230\u8bf7\u6c42\u540e\u5904\u7406\u5f02\u5e38,\u5f02\u5e38\u5185\u5bb9\u4e3a\uff1a" + e.getMessage());
            e.printStackTrace();
            return resp;
        }
    }

    private LogInout msgLogObj(final String messageId, final int currentPage) {
        final LogInoutService logInoutService = (LogInoutService)SpringContextHolder.getBean("logInoutService");
        return logInoutService.getLogByMsg(messageId, currentPage);
    }

    @Transactional(readOnly = false, rollbackFor = { Exception.class })
    private CollectCodeSetResp getCodeSetDataList(final CollectCodeSetReq request) throws Exception {
        final CodeSetDataService codeSetDataService = (CodeSetDataService)SpringContextHolder.getBean("codeSetDataService");
        final LogInoutService logInoutService = (LogInoutService)SpringContextHolder.getBean("logInoutService");
        final CollectCodeSetResp resp = new CollectCodeSetResp();
        try {
            final CollectCodeSetResHeader header = new CollectCodeSetResHeader();
            header.setPageSize(1000);
            header.setCurrentPage(request.getCollectCodeSetReqHeader().getCurrentPage());
            final int totalPage = codeSetDataService.getTotalPage(request.getCollectCodeSetReqBody());
            header.setTotalPage(totalPage);
            final CollectCodeSetResBody body = new CollectCodeSetResBody();
            final List<CollectCodeSetData> list = new ArrayList<CollectCodeSetData>();
            final List<CodeSetData> dataList = codeSetDataService.getCodeSetDataList(header, request.getCollectCodeSetReqBody());
            for (final CodeSetData codeSetData : dataList) {
                final CollectCodeSetData entity = new CollectCodeSetData();
                entity.setCode(codeSetData.getItemCode());
                entity.setParenCode(codeSetData.getParentItemCode());
                entity.setName(codeSetData.getItemName());
                entity.setValidEndDate(codeSetData.getValidEndDate());
                entity.setValidStartDate(codeSetData.getValidStartDate());
                entity.setYear(codeSetData.getYear().toString());
                entity.setInnerCode(codeSetData.getCol1());
                entity.setOldRelation(codeSetData.getSplitMerge());
                list.add(entity);
            }
            resp.setResFlag(0);
            if (dataList.isEmpty()) {
                resp.setErrorMessage("\u8fde\u63a5\u6210\u529f\uff0c\u6839\u636e\u4e1a\u52a1\u7cfb\u7edf\u7f16\u7801\u540d\u79f0\u3001\u4ee3\u7801\u96c6\u76ee\u5f55\u7f16\u7801\u540d\u79f0\u3001\u8d77\u6b62\u8bb0\u5f55\u6570\u6761\u4ef6\u6ca1\u6709\u67e5\u8be2\u5230\u4ee3\u7801\u96c6\u6570\u636e");
            }
            body.setCodeEntityCollection(list);
            resp.setCollectCodeSetResBody(body);
            resp.setCollectCodeSetResHeader(header);
            final LogInout logInout = this.msgLogObj(request.getCollectCodeSetReqHeader().getMessageId(), request.getCollectCodeSetReqHeader().getCurrentPage());
            logInout.setStatus(1);
            logInout.setRecordAmount(dataList.size());
            logInout.setEndDate(new Date());
            logInoutService.save(logInout);
            return resp;
        }
        catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Transactional
    private boolean verify(final CollectCodeSetReq request) {
        final boolean b = true;
        final CollectCodeSetReqHeader header = request.getCollectCodeSetReqHeader();
        if (header.getMessageId().isEmpty()) {
            this.logger.info((Object)"\u5165\u53c2\u6d88\u606fid\u4e3a\u7a7a");
            return false;
        }
        if (header.getCurrentPage() < 1) {
            this.logger.info((Object)"\u5f53\u524d\u9875\u5fc5\u987b\u5927\u4e8e0");
            return false;
        }
        if (header.getPageSize() != 1000) {
            this.logger.info((Object)"\u6bcf\u9875\u8bb0\u5f55\u6570\u5fc5\u987b\u4e3a1000\u6761\u6570\u636e");
            return false;
        }
        final CollectCodeSetReqBody body = request.getCollectCodeSetReqBody();
        if (body.getCodeSetName().isEmpty()) {
            this.logger.info((Object)"\u4ee3\u7801\u96c6\u76ee\u5f55\u540d\u79f0\u4e3a\u7a7a");
            return false;
        }
        if (body.getCodeSetNo().isEmpty()) {
            this.logger.info((Object)"\u4ee3\u7801\u96c6\u76ee\u5f55\u7f16\u7801\u4e3a\u7a7a");
            return false;
        }
        if (body.getSystemCode().isEmpty()) {
            this.logger.info((Object)"\u4e1a\u52a1\u7cfb\u7edf\u7f16\u7801\u4e3a\u7a7a");
            return false;
        }
        if (body.getSystemName().isEmpty()) {
            this.logger.info((Object)"\u4e1a\u52a1\u7cfb\u7edf\u540d\u79f0\u4e3a\u7a7a");
            return false;
        }
        final String[] messageId = header.getMessageId().split("_");
        if (!messageId[1].equals(body.getSystemCode())) {
            this.logger.info((Object)"\u6d88\u606fID\u683c\u5f0f\u9519\u8bef");
            return false;
        }
        if (!messageId[0].equals("MDM")) {
            this.logger.info((Object)"\u6d88\u606fID\u683c\u5f0f\u9519\u8bef");
            return false;
        }
        if (!messageId[2].equals(body.getCodeSetNo())) {
            this.logger.info((Object)"\u6d88\u606fID\u683c\u5f0f\u9519\u8bef");
            return false;
        }
        final CodeSetDataService codeSetDataService = (CodeSetDataService)SpringContextHolder.getBean("codeSetDataService");
        final int totalPage = codeSetDataService.getTotalPage(request.getCollectCodeSetReqBody());
        if (request.getCollectCodeSetReqHeader().getCurrentPage() > totalPage && totalPage > 0) {
            this.logger.info((Object)"\u5f53\u524d\u9875\u5927\u4e8e\u603b\u9875\u6570");
            return false;
        }
        return b;
    }
}
