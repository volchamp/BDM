package com.kmvc.jeesite.modules.connector.webservice.serve;

import com.kmvc.jeesite.modules.common.JacksonUtil;
import com.kmvc.jeesite.modules.connector.vo.serve.getcodeset.CollectCodeSetReq;
import com.kmvc.jeesite.modules.connector.vo.serve.getcodeset.CollectCodeSetResp;
import com.kmvc.jeesite.modules.connector.vo.serve.inquire.ListCodeSetReq;
import com.kmvc.jeesite.modules.connector.vo.serve.inquire.ListCodeSetResp;
import org.apache.log4j.*;
import com.kmvc.jeesite.modules.connector.vo.serve.inquire.*;
import com.kmvc.jeesite.modules.common.*;
import javax.jws.*;
import com.kmvc.jeesite.modules.connector.vo.serve.getcodeset.*;

@WebService
public class MasterDataEndPointImp implements MasterDataEndPoint
{
    private Logger logger;

    public MasterDataEndPointImp() {
        this.logger = Logger.getLogger((Class)MasterDataEndPointImp.class);
    }

    @WebMethod(operationName = "codeSetQuerySrv")
    @Override
    public String codeSetQuerySrv(final String reqJson) {
        String respJson = null;
        this.logger.info((Object)("json:" + reqJson));
        ListCodeSetReq request = new ListCodeSetReq();
        ListCodeSetResp resp = new ListCodeSetResp();
        try {
            request = JacksonUtil.toBean(reqJson, ListCodeSetReq.class);
        }
        catch (Exception e) {
            e.printStackTrace();
            resp.setResFlag(13);
            resp.setErrorMessage("json\u683c\u5f0f\u9519\u8bef\uff0c\u65e0\u6cd5\u8f6c\u6362\u6210\u76f8\u5e94\u5bf9\u8c61");
            respJson = JacksonUtil.fromObject(resp);
            this.logger.info((Object)("json\u683c\u5f0f\u9519\u8bef\uff0c\u65e0\u6cd5\u8f6c\u6362\u6210\u76f8\u5e94\u5bf9\u8c61\uff0c\u9519\u8bef\u6d88\u606f\uff1a" + e.getMessage()));
            return respJson;
        }
        final ListCodeSetSrv code = new ListCodeSetSrvImpl();
        resp = code.listCodeSet(request);
        respJson = JacksonUtil.fromObject(resp);
        this.logger.info((Object)("\u8fd4\u56dejson:" + respJson));
        return respJson;
    }

    @WebMethod(operationName = "codeSetDetailQuerySrv")
    @Override
    public String codeSetDetailQuerySrv(final String reqJson) {
        String respJson = null;
        this.logger.info((Object)("\u63a5\u5165json:" + reqJson));
        CollectCodeSetReq request = new CollectCodeSetReq();
        CollectCodeSetResp resp = new CollectCodeSetResp();
        try {
            request = JacksonUtil.toBean(reqJson, CollectCodeSetReq.class);
        }
        catch (Exception e) {
            e.printStackTrace();
            resp.setResFlag(13);
            resp.setErrorMessage("json\u683c\u5f0f\u9519\u8bef\uff0c\u65e0\u6cd5\u8f6c\u6362\u6210\u76f8\u5e94\u5bf9\u8c61");
            respJson = JacksonUtil.fromObject(resp);
            this.logger.info((Object)("json\u683c\u5f0f\u9519\u8bef\uff0c\u65e0\u6cd5\u8f6c\u6362\u6210\u76f8\u5e94\u5bf9\u8c61\uff0c\u9519\u8bef\u6d88\u606f\uff1a" + e.getMessage()));
            return respJson;
        }
        final FetchCodeSetDataSrv data = new FetchCodeSetDataSrvImpl();
        resp = data.fetchCodeSetData(request);
        respJson = JacksonUtil.fromObject(resp);
        this.logger.info((Object)("\u8fd4\u56dejson:" + respJson));
        return respJson;
    }
}
