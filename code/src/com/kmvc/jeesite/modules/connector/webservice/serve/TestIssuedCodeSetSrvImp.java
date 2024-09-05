package com.kmvc.jeesite.modules.connector.webservice.serve;

import com.kmvc.jeesite.modules.common.JacksonUtil;
import com.kmvc.jeesite.modules.connector.vo.client.mascodeset.PushCodeSetData;
import com.kmvc.jeesite.modules.connector.vo.client.mascodeset.PushCodeSetReq;
import com.kmvc.jeesite.modules.connector.vo.client.mascodeset.PushCodeSetReqBody;
import com.kmvc.jeesite.modules.connector.vo.serve.getcodeset.*;
import org.apache.log4j.*;
import com.kmvc.jeesite.modules.common.*;
import com.google.common.collect.*;
import com.kmvc.jeesite.modules.connector.vo.client.mascodeset.*;
import javax.jws.*;
import java.util.*;
import com.kmvc.jeesite.modules.connector.vo.serve.getcodeset.*;

public class TestIssuedCodeSetSrvImp implements TestIssuedCodeSetSrv
{
    private Logger logger;

    public TestIssuedCodeSetSrvImp() {
        this.logger = Logger.getLogger((Class)TestIssuedCodeSetSrvImp.class);
    }

    @WebMethod(operationName = "codeSetDistSrv")
    @Override
    public String codeSetDistSrv(final String reqJson) {
        Map<String, Object> result = null;
        try {
            this.logger.info((Object)("----------json:" + reqJson));
            final PushCodeSetReq pushCodeSetReq = JacksonUtil.toBean(reqJson, PushCodeSetReq.class);
            final PushCodeSetReqBody pushCodeSetReqBody = pushCodeSetReq.getPushCodeSetReqBody();
            this.logger.info((Object)("-----------" + pushCodeSetReqBody));
            final List<PushCodeSetData> pushCodeSetDatas = (List<PushCodeSetData>)pushCodeSetReqBody.getCodeEntities();
            this.logger.info((Object)("=====" + pushCodeSetDatas));
            result = Maps.newHashMap();
            result.put("resFlag", 0);
            result.put("errorMessage", "\u63a5\u6536\u5904\u7406\u6210\u529f");
        }
        catch (Exception e) {
            this.logger.info((Object)"\u6570\u636e\u5e93\u56de\u6eda\u5b8c\u6210");
            result =Maps.newHashMap();
            result.put("resFlag", 1);
            result.put("errorMessage", "\u63a5\u6536\u5904\u7406\u5931\u8d25");
        }
        return JacksonUtil.fromObject(result);
    }

    @WebMethod(operationName = "codeSetCollSrv")
    @Override
    public String codeSetCollSrv(final String reqJson) {
        final CollectCodeSetResp result = new CollectCodeSetResp();
        try {
            this.logger.info((Object)("json:" + reqJson));
            final CollectCodeSetReq collectCodeSetReq = JacksonUtil.toBean(reqJson, CollectCodeSetReq.class);
            final CollectCodeSetReqHeader reqHeader = collectCodeSetReq.getCollectCodeSetReqHeader();
            final CollectCodeSetReqBody reqBody = collectCodeSetReq.getCollectCodeSetReqBody();
            final int currentPage = reqHeader.getCurrentPage();
            final int pageSize = reqHeader.getPageSize();
            final String code = reqBody.getCodeSetNo();
            final String name = reqBody.getCodeSetName();
            final int count = 1000;
            int totalPage = count / pageSize;
            if (count % pageSize != 0) {
                ++totalPage;
                if (count < currentPage) {
                    totalPage = 1;
                }
            }
            this.logger.info((Object)"\u5206\u9875\u67e5\u8be2\u4ee3\u7801\u96c6\u6570\u636e");
            final CollectCodeSetResHeader header = new CollectCodeSetResHeader();
            header.setCurrentPage(currentPage);
            header.setPageSize(pageSize);
            header.setTotalPage(totalPage);
            result.setCollectCodeSetResHeader(header);
            final CollectCodeSetResBody body = new CollectCodeSetResBody();
            final CollectCodeSetData codeSetData = new CollectCodeSetData();
            codeSetData.setCode("101001");
            codeSetData.setName("\u5385\u957f\u5ba4");
            codeSetData.setParenCode("101");
            codeSetData.setYear("2017");
            codeSetData.setValidStartDate(new Date());
            codeSetData.setValidEndDate(new Date());
            final List<CollectCodeSetData> codeSetDatas = new ArrayList<CollectCodeSetData>();
            codeSetDatas.add(codeSetData);
            body.setCodeEntityCollection(codeSetDatas);
            result.setCollectCodeSetResBody(body);
            result.setResFlag(0);
            result.setErrorMessage("\u4f20\u8f93\u6210\u529f");
        }
        catch (Exception e) {
            result.setResFlag(1);
            result.setErrorMessage("\u4f20\u8f93\u5931\u8d25\uff1a" + e.getMessage());
        }
        return JacksonUtil.fromObject(result);
    }
}
