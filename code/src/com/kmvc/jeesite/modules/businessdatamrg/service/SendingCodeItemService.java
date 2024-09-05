package com.kmvc.jeesite.modules.businessdatamrg.service;

import com.kmvc.jeesite.common.service.CrudService;
import com.kmvc.jeesite.modules.businessdatamrg.dao.SendingCodeItemDao;
import com.kmvc.jeesite.modules.businessdatamrg.entity.SendingCodeItem;
import com.thinkgem.jeesite.common.persistence.Page;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class SendingCodeItemService extends CrudService<SendingCodeItemDao, SendingCodeItem>
{
    public SendingCodeItem get(final String id) {
        return (SendingCodeItem)super.get(id);
    }

    public List<SendingCodeItem> findList(final SendingCodeItem sendingCodeItem) {
        return (List<SendingCodeItem>)super.findList(sendingCodeItem);
    }

    public Page<SendingCodeItem> findPage(final Page<SendingCodeItem> page, final SendingCodeItem sendingCodeItem) {
        return (Page<SendingCodeItem>)super.findPage((Page)page, sendingCodeItem);
    }

    @Transactional(readOnly = false, propagation = Propagation.NOT_SUPPORTED)
    public void save(final SendingCodeItem sendingCodeItem) {
        super.save(sendingCodeItem);
    }

    @Transactional(readOnly = false, propagation = Propagation.NOT_SUPPORTED)
    public void delete(final SendingCodeItem sendingCodeItem) {
        super.delete(sendingCodeItem);
    }

    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public SendingCodeItem findByCodeSetIdAndCodeAndOperation(final String codeSetId, final String codeSetNo, final int operation) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("codeSetId", codeSetId);
        params.put("codeSetNo", codeSetNo);
        params.put("operation", operation);
        return ((SendingCodeItemDao)this.dao).findByCodeSetIdAndCodeAndOperation(params);
    }

    public List<SendingCodeItem> findSendingCodeItem(final String id, final int currentPage) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("codeSetId", id);
        params.put("s_rowno", 1000 * (currentPage - 1) + 1);
        params.put("e_rowno", 1000 * currentPage);
        return ((SendingCodeItemDao)this.dao).findSendingCodeItem(params);
    }

    public int findTotalSendingCodeItem(final String id) {
        return ((SendingCodeItemDao)this.dao).findTotalSendingCodeItem(id);
    }

    @Transactional(readOnly = false, propagation = Propagation.NOT_SUPPORTED)
    public void batchUpdate(final String recordId, final int sendStatus) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("recordId", recordId);
        params.put("sendStatus", sendStatus);
        ((SendingCodeItemDao)this.dao).batchUpdate(params);
    }
}
