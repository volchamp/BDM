package com.kmvc.jeesite.modules.connector.webservice.client;

import com.kmvc.jeesite.modules.codesetmrg.entity.CodeSet;
import com.kmvc.jeesite.modules.codesetmrg.service.CodeSetService;
import com.kmvc.jeesite.modules.common.JacksonUtil;
import com.kmvc.jeesite.modules.common.Methods;
import com.kmvc.jeesite.modules.connector.vo.client.olcodeset.CollectCodeSetReq;
import com.kmvc.jeesite.modules.connector.vo.client.olcodeset.CollectCodeSetReqBody;
import com.kmvc.jeesite.modules.connector.vo.client.olcodeset.CollectCodeSetReqHeader;
import com.kmvc.jeesite.modules.connector.vo.client.olcodeset.CollectCodeSetResp;
import com.kmvc.jeesite.modules.datacheckingmrg.entity.ValidationCodeSetTmp;
import com.kmvc.jeesite.modules.datacheckingmrg.service.ValidationCodeSetTmpService;
import com.kmvc.jeesite.modules.datacheckingmrg.service.ValidationDetailTmpService;
import com.kmvc.jeesite.modules.mdmlog.entity.LogInout;
import com.kmvc.jeesite.modules.mdmlog.service.LogInoutService;
import com.kmvc.jeesite.modules.sys.entity.TSystem;
import org.springframework.transaction.annotation.*;
import org.springframework.stereotype.*;
import com.kmvc.jeesite.modules.mdmlog.service.*;
import org.springframework.beans.factory.annotation.*;
import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import com.kmvc.jeesite.modules.connector.vo.client.olcodeset.*;
import com.kmvc.jeesite.modules.datacheckingmrg.entity.*;
import com.kmvc.jeesite.modules.codesetmrg.service.*;
import com.kmvc.jeesite.modules.mdmlog.entity.*;
import com.kmvc.jeesite.modules.datacheckingmrg.service.*;
import com.kmvc.jeesite.modules.common.*;
import org.apache.cxf.jaxws.endpoint.dynamic.*;
import org.apache.cxf.transports.http.configuration.*;
import org.apache.cxf.transport.http.*;
import javax.xml.namespace.*;
import com.thinkgem.jeesite.common.utils.*;
import java.io.*;
import org.apache.log4j.*;
import java.util.*;
import org.apache.cxf.endpoint.*;
import org.apache.cxf.service.model.*;

@Transactional(readOnly = true)
@Service
public class CollectCodeSetSrvImpl implements CollectCodeSetSrv
{
    private Logger logger;
    @Autowired
    private LogInoutService logInoutService;

    public CollectCodeSetSrvImpl() {
        this.logger = Logger.getLogger((Class)CollectCodeSetSrvImpl.class);
    }

    public CollectCodeSetReq getCollectCodeSetSrvRequest(final CodeSet codeSet, final int page, final String systemCode, final String msgId) {
        final CollectCodeSetReqBody body = new CollectCodeSetReqBody();
        body.setCodeSetNo(codeSet.getCodeSetNo());
        body.setCodeSetName(codeSet.getCodeSetName());
        final CollectCodeSetReqHeader header = new CollectCodeSetReqHeader();
        if (msgId == null) {
            final Methods method = new Methods();
            final String date = method.getDate("yyyyMMddHHmmssSSS");
            header.setMessageId("MDM_" + systemCode + "_" + codeSet.getCodeSetNo() + "_" + date);
        }
        else {
            header.setMessageId(msgId);
        }
        header.setPageSize(1000);
        if (page == 1) {
            header.setCurrentPage(1);
        }
        else {
            header.setCurrentPage(page);
        }
        final CollectCodeSetReq req = new CollectCodeSetReq();
        req.setCollectCodeSetReqBody(body);
        req.setCollectCodeSetReqHeader(header);
        this.logger.info((Object)"\u83b7\u53d6\u5165\u53c2\u5b8c\u6210");
        return req;
    }

    public void collectCodeSetSrvResponse(final CollectCodeSetResp response, final ValidationCodeSetTmp validationCodeSetTmp) {
        final ValidationDetailTmpService validationDetailTmpService = (ValidationDetailTmpService)SpringContextHolder.getBean("validationDetailTmpService");
        validationDetailTmpService.saveBody((List)response.getCollectCodeSetResBody().getCodeEntityCollection(), validationCodeSetTmp);
    }

    public ValidationCodeSetTmp findData(final String codeSetNo, final TSystem system) {
        final CodeSetService codeSetService = (CodeSetService)SpringContextHolder.getBean("codeSetService");
        final ValidationCodeSetTmp tmp = new ValidationCodeSetTmp();
        final CodeSet codeSet = codeSetService.findByCode(codeSetNo);
        tmp.setCodeSetNo(codeSet.getCodeSetNo());
        tmp.setCodeSetName(codeSet.getCodeSetName());
        tmp.setSystemCode(system.getSystemCode());
        tmp.setSystemName(system.getSystemName());
        this.logger.info((Object)"\u6570\u636e\u5c01\u88c5\u5230\u4ee3\u7801\u96c6\u76ee\u5f55\u68c0\u6838\u4e34\u65f6\u5bf9\u8c61\u4e2d\u5b8c\u6210");
        return tmp;
    }

    @Override
    public void collectSysCodeSets(final TSystem system, final int page) {
        final CodeSetService codeSetService = (CodeSetService)SpringContextHolder.getBean("codeSetService");
        final List<CodeSet> list = codeSetService.findBySystemId(system.getId());
        for (final CodeSet codeSet : list) {
            final String msgId = null;
            this.saveValidationCodeSetTmp(page, msgId, system, 1, codeSet);
        }
    }

    public LogInout getInoutLog(final TSystem system, final CollectCodeSetReq req, final CodeSet codeSet) {
        LogInout log = new LogInout();
        log.setDestSysName("MDM\u4e3b\u6570\u636e\u7ba1\u7406\u5e73\u53f0");
        log.setSourceSysName(system.getSystemName());
        log.setCodeSetName(req.getCollectCodeSetReqBody().getCodeSetName());
        log.setStartDate(new Date());
        log.setMessageId(req.getCollectCodeSetReqHeader().getMessageId());
        log.setCurrentPage(Integer.valueOf(req.getCollectCodeSetReqHeader().getCurrentPage()));
        log.setRecordType(Integer.valueOf(0));
        this.logInoutService.save(log);
        log = this.msgLogObj(req.getCollectCodeSetReqHeader().getMessageId(), req.getCollectCodeSetReqHeader().getCurrentPage());
        this.logger.info((Object)"\u63a5\u53e3\u8f93\u5165\u53c2\u6570\u53d1\u9001\u6210\u529f\u540e\u8bb0\u5f55\u65e5\u5fd7\u8bb0\u5f55\u53d1\u9001\u65f6\u95f4\u5b8c\u6210");
        return log;
    }

    private LogInout msgLogObj(final String messageId, final int currentPage) {
        return this.logInoutService.getLogByMsg(messageId, currentPage);
    }

    public void saveValidationCodeSetTmp(final int page, final String msgId, final TSystem system, final int num, final CodeSet codeSet) {
        final ValidationCodeSetTmpService validationCodeSetTmpService = (ValidationCodeSetTmpService)SpringContextHolder.getBean("validationCodeSetTmpService");
        LogInout log = null;
        String flag = "0";
        try {
            final CollectCodeSetReq req = this.getCollectCodeSetSrvRequest(codeSet, page, system.getSystemCode(), msgId);
            log = this.getInoutLog(system, req, codeSet);
            final String reqJson = JacksonUtil.fromObject(req);
            this.logger.info((Object)("\u5165\u53c2\uff1a" + reqJson));
            String respJson = null;
            this.logger.info((Object)"\u5f00\u59cb\u52a8\u6001\u83b7\u53d6\u670d\u52a1");
            this.logger.info((Object)("\u8fdc\u7a0bURL\uff1a" + system.getServiceAddr()));
            final JaxWsDynamicClientFactory factory = JaxWsDynamicClientFactory.newInstance();
            final HTTPClientPolicy prolicy = new HTTPClientPolicy();
            this.logger.info((Object)"\u52a0\u8f7d\u7b56\u7565");
            prolicy.setConnectionTimeout(45000L);
            this.logger.info((Object)"\u8bbe\u7f6e\u8fde\u63a5\u8d85\u65f6\u65f6\u95f4 45s");
            prolicy.setReceiveTimeout(45000L);
            this.logger.info((Object)"\u8bbe\u7f6e\u8bf7\u6c42\u8d85\u65f6\u65f6\u95f445s");
            final Client client = factory.createClient(system.getServiceAddr());
            this.logger.info((Object)"========create client");
            final HTTPConduit conduit = (HTTPConduit)client.getConduit();
            conduit.setClient(prolicy);
            this.logger.info((Object)"\u52a8\u6001\u83b7\u53d6\u670d\u52a1\u5b8c\u6210");
            this.logger.info((Object)"\u5f00\u59cb\u5904\u7406 WebService\u63a5\u53e3\u548c\u5b9e\u73b0\u7c7bnamespace\u4e0d\u540c\u7684\u60c5\u51b5");
            final Endpoint endpoint = client.getEndpoint();
            QName opName = new QName(endpoint.getService().getName().getNamespaceURI(), "codeSetCollSrv");
            final BindingInfo bindingInfo = endpoint.getEndpointInfo().getBinding();
            if (bindingInfo.getOperation(opName) == null) {
                for (final BindingOperationInfo operationInfo : bindingInfo.getOperations()) {
                    if ("codeSetCollSrv".equals(operationInfo.getName().getLocalPart())) {
                        opName = operationInfo.getName();
                        break;
                    }
                }
            }
            this.logger.info((Object)"\u5904\u7406 WebService\u63a5\u53e3\u548c\u5b9e\u73b0\u7c7bnamespace\u4e0d\u540c\u7684\u60c5\u51b5\u5b8c\u6210");
            if (StringUtils.isBlank((CharSequence)system.getServiceAddr()) || client == null || bindingInfo.getOperation(opName) == null) {
                this.logger.info((Object)"=======\u4e1a\u52a1\u7cfb\u7edf\u670d\u52a1\u5730\u5740\u65e0\u6548");
            }
            final Object[] object = client.invoke(opName, new Object[] { reqJson });
            this.logger.info((Object)"\u8c03\u7528\u670d\u52a1\u65b9\u6cd5\u5b8c\u6210");
            respJson = object[0].toString();
            this.logger.info((Object)("\u8fd4\u56dejson\uff1a" + respJson));
            final CollectCodeSetResp response = JacksonUtil.toBean(respJson, CollectCodeSetResp.class);
            if (response == null || response.getCollectCodeSetResBody() == null || response.getCollectCodeSetResBody().getCodeEntityCollection() == null) {
                log.setEndDate(new Date());
                log.setStatus(Integer.valueOf(0));
                log.setFailReason("\u670d\u52a1\u7aef\u65e0\u8fd4\u56de\u6570\u636e");
                this.logger.info((Object)"\u8fd4\u56de\u6570\u636e\u7a7a");
            }
            else {
                log.setEndDate(new Date());
                if (response.getResFlag().equals("0")) {
                    flag = "1";
                }
                else {
                    flag = "0";
                }
                log.setStatus(Integer.valueOf(flag));
                log.setFailReason(response.getErrorMessage());
                log.setRecordAmount(Integer.valueOf(response.getCollectCodeSetResBody().getCodeEntityCollection().size()));
                this.logger.info((Object)"\u8fd4\u56de\u6570\u636e\u6210\u529f");
            }
            if (flag.equals("1")) {
                ValidationCodeSetTmp validationCodeSetTmp = this.findData(req.getCollectCodeSetReqBody().getCodeSetNo(), system);
                validationCodeSetTmp = validationCodeSetTmpService.saveHeader(response.getCollectCodeSetResHeader(), validationCodeSetTmp);
                this.collectCodeSetSrvResponse(response, validationCodeSetTmp);
                log.setRemarks("\u6570\u636e\u6536\u96c6\u6210\u529f");
                this.logInoutService.save(log);
                if (response.getCollectCodeSetResHeader().getCurrentPage() < response.getCollectCodeSetResHeader().getTotalPage()) {
                    this.saveValidationCodeSetTmp(response.getCollectCodeSetResHeader().getCurrentPage() + 1, req.getCollectCodeSetReqHeader().getMessageId(), system, 1, codeSet);
                }
            }
            else {
                log.setRemarks("\u63a5\u53e3\u4f20\u8f93\u6210\u529f\uff0c\u6570\u636e\u6536\u96c6\u5931\u8d25");
                this.logger.info((Object)"\u63a5\u53e3\u4f20\u8f93\u6210\u529f\uff0c\u6570\u636e\u6536\u96c6\u5931\u8d25");
                this.logInoutService.save(log);
            }
        }
        catch (Exception e) {
            this.logger.info((Object)"-----------error------------");
            this.logger.error((Object)e.getMessage(), (Throwable)e);
            if (num > 3) {
                log.setFailReason("\u8fde\u63a5\u5931\u8d25");
            }
            else {
                log.setFailReason("\u7b2c" + num + "\u6b21\u6267\u884c\u5931\u8d25\uff0c\u51c6\u5907\u6267\u884c\u4e0b\u4e00\u6b21\u670d\u52a1");
                log.setEndDate(new Date());
                final StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw, true));
                final String strs = sw.toString();
                log.setRemarks(strs.substring(0, 3000));
                log.setStatus(Integer.valueOf(0));
                log.setStatus(Integer.valueOf(0));
                this.logInoutService.save(log);
                this.logger.info((Object)("\u6267\u884c\u7b2c" + num + "\u6b21\u5931\u8d25"));
                this.logger.info((Object)"\u7b49\u5f8520\u5206\u949f");
                try {
                    final long date = 1200000L;
                    Thread.sleep(date);
                }
                catch (InterruptedException ei) {
                    ei.printStackTrace();
                    this.logger.log((Priority)Level.WARN, (Object)"Interrupted!", (Throwable)ei);
                    Thread.currentThread().interrupt();
                }
                this.saveValidationCodeSetTmp(page, null, system, num + 1, codeSet);
            }
            log.setEndDate(new Date());
            final String argStr = Arrays.toString(e.getStackTrace());
            log.setRemarks(argStr);
            log.setStatus(Integer.valueOf(0));
            this.logInoutService.save(log);
        }
    }
}
