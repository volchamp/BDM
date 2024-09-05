package com.kmvc.jeesite.modules.businessdatamrg.service;

import com.kmvc.jeesite.modules.businessdatamrg.dao.SendingCodeSetDao;
import com.kmvc.jeesite.modules.businessdatamrg.entity.SendingCodeItem;
import com.kmvc.jeesite.modules.businessdatamrg.entity.SendingCodeSet;
import com.kmvc.jeesite.modules.codesetmrg.dao.CodeSetDao;
import com.kmvc.jeesite.modules.codesetmrg.entity.CodeSet;
import com.kmvc.jeesite.modules.codesetmrg.entity.CodeSetData;
import com.kmvc.jeesite.modules.codesetmrg.service.CodeSetDataService;
import com.kmvc.jeesite.modules.codesetmrg.service.CodeSetService;
import com.kmvc.jeesite.modules.common.exception.BizException;
import com.kmvc.jeesite.modules.connector.webservice.client.SysWebServiceProxy;
import com.kmvc.jeesite.modules.sys.entity.TSystem;
import com.kmvc.jeesite.modules.sys.service.TSystemService;
import com.thinkgem.jeesite.common.service.*;
import com.kmvc.jeesite.modules.businessdatamrg.dao.*;
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import com.kmvc.jeesite.modules.codesetmrg.service.*;
import com.thinkgem.jeesite.modules.sys.dao.*;
import com.kmvc.jeesite.modules.codesetmrg.dao.*;
import com.google.common.collect.*;
import com.thinkgem.jeesite.modules.sys.utils.*;
import org.apache.commons.lang3.StringUtils;
import com.thinkgem.jeesite.common.persistence.*;
import org.springframework.transaction.annotation.*;
import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import com.kmvc.jeesite.modules.common.exception.*;
import java.util.*;
import com.kmvc.jeesite.modules.businessdatamrg.entity.*;
import com.kmvc.jeesite.modules.connector.webservice.client.*;
import com.thinkgem.jeesite.common.utils.*;
import com.thinkgem.jeesite.modules.sys.entity.*;

@Service
@Transactional(readOnly = true)
public class SendingCodeSetService extends CrudService<SendingCodeSetDao, SendingCodeSet>
{
    @Autowired
    private CodeSetService codeSetService;
    @Autowired
    private CodeSetDataService codeSetDataService;
    @Autowired
    private TSystemService tSystemService;
    @Autowired
    private SendingCodeItemService sendingCodeItemService;
    @Autowired
    private OfficeDao officeDao;
    @Autowired
    private CodeSetDao codeSetDao;

    public SendingCodeSet get(final String id) {
        return (SendingCodeSet)super.get(id);
    }

    public List<SendingCodeSet> findList(final SendingCodeSet sendingCodeSet) {
        final List<SendingCodeSet> codeSetList = (List<SendingCodeSet>)((SendingCodeSetDao)this.dao).findList(sendingCodeSet);
        final List<SendingCodeSet> codeSetListResult = Lists.newArrayList();
        final String curUserId = UserUtils.getUser().getId();
        for (final SendingCodeSet cs : codeSetList) {
            if (StringUtils.isNotBlank((CharSequence)cs.getOfficeIds())) {
                final List<CodeSet> codesetList2 = this.codeSetDao.findOfficeIdByUserId(curUserId);
                for (final CodeSet cs2 : codesetList2) {
                    if (cs.getOfficeIds().contains(cs2.getOfficeIds())) {
                        codeSetListResult.add(cs);
                        break;
                    }
                }
            }
            else {
                codeSetListResult.add(cs);
            }
        }
        return codeSetListResult;
    }

    public Page<SendingCodeSet> findPage(final Page<SendingCodeSet> page, final SendingCodeSet sendingCodeSet) {
        sendingCodeSet.setPage((Page)page);
        page.setList((List)this.findList(sendingCodeSet));
        return page;
    }

    @Transactional(readOnly = false, propagation = Propagation.NOT_SUPPORTED)
    public void save(final SendingCodeSet sendingCodeSet) {
        final String officeIds = sendingCodeSet.getOfficeIds();
        if (StringUtils.isNotBlank((CharSequence)officeIds) && !officeIds.contains(",")) {
            final Office office = (Office)this.officeDao.get(officeIds);
            sendingCodeSet.setOfficeIds(officeIds + "," + office.getParentIds());
        }
        super.save(sendingCodeSet);
    }

    @Transactional(readOnly = false, propagation = Propagation.NOT_SUPPORTED)
    public void delete(final SendingCodeSet sendingCodeSet) {
        super.delete(sendingCodeSet);
    }

    public SendingCodeSet findBySystemIdAndStatus(final String systemId, final String codeSetId, final Integer status) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("systemId", systemId);
        params.put("codeSetId", codeSetId);
        params.put("status", status);
        final List<SendingCodeSet> s = ((SendingCodeSetDao)this.dao).findBySystemIdAndStatus(params);
        if (s.size() > 0) {
            return s.get(0);
        }
        return null;
    }

    public SendingCodeSet findSendingCodeSet(final String id) {
        return ((SendingCodeSetDao)this.dao).findSendingCodeSet(id);
    }

    @Transactional(readOnly = false, propagation = Propagation.NOT_SUPPORTED)
    public void updateStatus(final String id, final int sendSuccess) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        params.put("sendStatus", sendSuccess);
        ((SendingCodeSetDao)this.dao).updateStatus(params);
    }

    @Transactional(readOnly = false, propagation = Propagation.NOT_SUPPORTED)
    public void sendingData(final CodeSetData codeSetData) {
        BizException.throwWhenNull(codeSetData.getCodeSetNo(), "\u4ee3\u7801\u96c6\u76ee\u5f55\u7f16\u7801\u4e0d\u80fd\u4e3a\u7a7a");
        final String codeSetNo = codeSetData.getCodeSetNo();
        final CodeSet codeSet = this.codeSetService.findByCode(codeSetNo);
        for (int i = 0; i < codeSetData.getSystemIds().size(); ++i) {
            final String systemId = codeSetData.getSystemIds().get(i);
            final TSystem system = this.tSystemService.get(systemId);
            List<CodeSetData> list = Lists.newArrayList();
            if (codeSetData.getSelectedRow() != null && codeSetData.getSelectedRow().size() > 0) {
                final List<String> ids = Lists.newArrayList();
                for (final String temp : codeSetData.getSelectedRow()) {
                    ids.add(temp.split("#")[0]);
                }
                list = this.codeSetDataService.findBySystemIdAndCodeSetId(systemId, ids);
            }
            else {
                list = this.codeSetDataService.findListByCodeSetData(systemId, codeSetNo);
            }
            if (list.size() != 0) {
                final SendingCodeSet sendingCodeSet = new SendingCodeSet();
                sendingCodeSet.setDestSysId(system.getId());
                sendingCodeSet.setDestSysCode(system.getSystemCode());
                sendingCodeSet.setDestSysName(system.getSystemName());
                sendingCodeSet.setCodeSetId(codeSet.getId());
                sendingCodeSet.setCodeSetNo(codeSet.getCodeSetNo());
                sendingCodeSet.setCodeSetName(codeSet.getCodeSetName());
                sendingCodeSet.setSendDate(new Date());
                sendingCodeSet.setSendStatus(0);
                sendingCodeSet.setRemarks(codeSet.getRemarks());
                sendingCodeSet.setOperation(1);
                sendingCodeSet.setOfficeIds(codeSet.getOfficeIds());
                this.save(sendingCodeSet);
                SendingCodeItem sendingCodeItem = null;
                for (final CodeSetData c : list) {
                    sendingCodeItem = new SendingCodeItem();
                    sendingCodeItem.setItemCode(c.getItemCode());
                    sendingCodeItem.setParentItemCode(c.getParentItemCode());
                    sendingCodeItem.setItemName(c.getItemName());
                    sendingCodeItem.setRecordId(sendingCodeSet.getId());
                    sendingCodeItem.setYear(c.getYear());
                    sendingCodeItem.setValidStartDate(c.getValidStartDate());
                    sendingCodeItem.setValidEndDate(c.getValidEndDate());
                    sendingCodeItem.setStatus(c.getStatus());
                    sendingCodeItem.setSendStatus(0);
                    sendingCodeItem.setOperation(0);
                    sendingCodeItem.setItemCodeSort(c.getItemCodeSort());
                    sendingCodeItem.setCol1(c.getCol1());
                    this.sendingCodeItemService.save(sendingCodeItem);
                }
                final SysWebServiceProxy sysWebService = (SysWebServiceProxy)SpringContextHolder.getBean("sysWebServiceProxy");
                sysWebService.pushCodeSet2Systems(sendingCodeSet.getId(), system);
            }
        }
    }
}
