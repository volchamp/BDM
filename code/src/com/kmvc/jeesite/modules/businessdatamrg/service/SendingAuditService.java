package com.kmvc.jeesite.modules.businessdatamrg.service;

import com.kmvc.jeesite.modules.businessdatamrg.dao.SendingCodeItemDao;
import com.kmvc.jeesite.modules.businessdatamrg.dao.SendingCodeSetDao;
import com.kmvc.jeesite.modules.businessdatamrg.entity.SendingCodeItem;
import com.kmvc.jeesite.modules.businessdatamrg.entity.SendingCodeSet;
import com.kmvc.jeesite.modules.connector.webservice.client.SysWebServiceProxy;
import com.kmvc.jeesite.modules.sys.dao.TSystemDao;
import com.kmvc.jeesite.modules.sys.entity.TSystem;
import com.thinkgem.jeesite.common.service.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import com.kmvc.jeesite.modules.businessdatamrg.dao.*;
import org.springframework.beans.factory.annotation.*;
import com.thinkgem.jeesite.common.persistence.*;
import com.kmvc.jeesite.modules.businessdatamrg.entity.*;
import org.apache.commons.lang3.*;
import com.kmvc.jeesite.modules.connector.webservice.client.*;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class SendingAuditService extends CrudService<SendingCodeSetDao, SendingCodeSet>
{
    @Autowired
    private SendingCodeItemDao sendingCodeItemDao;
    @Autowired
    private TSystemDao systemDao;

    public Page<SendingCodeItem> sendingFaildList(final Page<SendingCodeItem> page, final SendingCodeItem sendingCodeItem) {
        sendingCodeItem.setPage((Page)page);
        sendingCodeItem.setSendStatusStr("0,2");
        final List<SendingCodeItem> list = (List<SendingCodeItem>)this.sendingCodeItemDao.findList(sendingCodeItem);
        page.setList((List)list);
        return page;
    }

    @Transactional(readOnly = false, rollbackFor = { Exception.class })
    public String resend(final String sendingCsId) {
        if (StringUtils.isBlank((CharSequence)sendingCsId)) {
            return "sendingCsIdIsBlank";
        }
        final SendingCodeSet sendingCs = (SendingCodeSet)this.get(sendingCsId);
        if (sendingCs == null) {
            return "sendingCsIsNull";
        }
        if (sendingCs.getSendStatus() == 1) {
            return "alreadySendSuccess";
        }
        TSystem system = null;
        if (sendingCs.getDestSysId() != null) {
            system = (TSystem)this.systemDao.get(sendingCs.getDestSysId());
        }
        final SysWebServiceProxy sysWebServiceProxy = new SysWebServiceProxy();
        sysWebServiceProxy.pushCodeSet2Systems(sendingCs.getId(), system);
        if (sendingCs.getSendStatus() != 1) {
            sendingCs.setSendStatus(1);
            sendingCs.setSendDate(new Date());
            ((SendingCodeSetDao)this.dao).update(sendingCs);
        }
        final SendingCodeItem sendingCi = new SendingCodeItem();
        sendingCi.setRecordId(sendingCsId);
        final List<SendingCodeItem> sendingCiList = (List<SendingCodeItem>)this.sendingCodeItemDao.findList(sendingCi);
        for (final SendingCodeItem sci : sendingCiList) {
            if (sci.getSendStatus() != 1) {
                sci.setSendStatus(1);
                this.sendingCodeItemDao.update(sci);
            }
        }
        return "finish";
    }

    public Page<SendingCodeSet> taskScsList(final Page<SendingCodeSet> page, final SendingCodeSet scs) {
        page.setOrderBy("send_date desc");
        scs.setPage((Page)page);
        page.setList((List)((SendingCodeSetDao)this.dao).taskScsList(scs));
        return page;
    }
}
