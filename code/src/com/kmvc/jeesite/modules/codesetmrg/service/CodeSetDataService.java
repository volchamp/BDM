package com.kmvc.jeesite.modules.codesetmrg.service;

import com.kmvc.jeesite.modules.codesetmrg.dao.CodeSetDataDao;
import com.kmvc.jeesite.modules.codesetmrg.entity.CodeSetData;
import com.kmvc.jeesite.modules.codesetmrg.entity.DistriRelationship;
import com.kmvc.jeesite.modules.codesetmrg.entity.PendingCodeItem;
import com.kmvc.jeesite.modules.connector.vo.serve.getcodeset.CollectCodeSetReqBody;
import com.kmvc.jeesite.modules.connector.vo.serve.getcodeset.CollectCodeSetResHeader;
import com.thinkgem.jeesite.common.service.*;
import com.kmvc.jeesite.modules.codesetmrg.dao.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.springframework.beans.factory.annotation.*;
import com.thinkgem.jeesite.common.persistence.*;
import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import java.util.*;
import com.thinkgem.jeesite.common.utils.*;
import com.kmvc.jeesite.modules.connector.vo.serve.getcodeset.*;
import com.google.common.collect.*;

@Service
@Transactional(readOnly = true)
public class CodeSetDataService extends CrudService<CodeSetDataDao, CodeSetData>
{
    @Autowired
    private DistriRelationshipService distriRelationshipService;
    @Autowired
    private CodeSetCacheMappingService CodeSetCacheMappingService;

    public CodeSetData get(final String id) {
        return (CodeSetData)super.get(id);
    }

    public List<CodeSetData> findList(final CodeSetData codeSetData) {
        return (List<CodeSetData>)super.findList(codeSetData);
    }

    public Page<CodeSetData> findPage(final Page<CodeSetData> page, final CodeSetData codeSetData) {
        page.setOrderBy("a.item_code_sort");
        return (Page<CodeSetData>)super.findPage((Page)page, codeSetData);
    }

    @Transactional(readOnly = false)
    public void save(final CodeSetData codeSetData) {
        codeSetData.setStatus(2);
        final int version = this.findMaxVersion(codeSetData);
        codeSetData.setVersion(version + 1);
        super.save(codeSetData);
    }

    @Transactional(readOnly = false)
    public void delete(final CodeSetData codeSetData) {
        super.delete(codeSetData);
    }

    @Transactional(readOnly = false, rollbackFor = { Exception.class })
    public void saveCodeSetData(final List<PendingCodeItem> pendingCodeItems, final String codeSetId) {
        try {
            for (final PendingCodeItem p : pendingCodeItems) {
                p.setCodeSetId(codeSetId);
                final CodeSetData codeSetData = this.toCodeSetData(p);
                if (p.getOperation() == 2) {
                    this.updateStatusById(p.getItemId(), 0);
                }
                else {
                    super.save(codeSetData);
                    final List<String> systemsIds = this.CodeSetCacheMappingService.findByItemId(p.getId());
                    if (systemsIds == null) {
                        continue;
                    }
                    for (final String systemId : systemsIds) {
                        final DistriRelationship dr = new DistriRelationship();
                        dr.setSystemId(systemId);
                        dr.setItemId(codeSetData.getId());
                        dr.setIsNewRecord(true);
                        if (this.distriRelationshipService.findList(dr).size() == 0) {
                            this.distriRelationshipService.save(dr);
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.logger.info("\u5ba1\u6838\u901a\u8fc7\uff0c\u65b0\u589e\u5230\u6b63\u5f0f\u5e93\u5b8c\u6210");
    }

    public void updateStatusById(final String itemId, final Integer status) {
        final Map<String, Object> param = new HashMap<String, Object>();
        param.put("itemId", itemId);
        param.put("status", status);
        ((CodeSetDataDao)this.dao).updateStatusById(param);
    }

    public CodeSetData toCodeSetData(final PendingCodeItem item) {
        final CodeSetData codeSetData = new CodeSetData();
        codeSetData.setCodeSetId(item.getCodeSetId());
        codeSetData.setItemCode(item.getItemCode());
        codeSetData.setParentItemCode(item.getParentItemCode());
        codeSetData.setItemName(item.getItemName());
        codeSetData.setYear(item.getYear());
        codeSetData.setValidStartDate(item.getValidStartDate());
        codeSetData.setValidEndDate(item.getValidEndDate());
        codeSetData.setStatus(item.getStatus());
        codeSetData.setVersion(item.getVersion());
        codeSetData.setItemCodeSort(item.getItemCodeSort());
        codeSetData.setRemarks(item.getRemarks());
        codeSetData.setCol1(item.getCol1());
        if (StringUtils.isNotBlank((CharSequence)item.getItemId())) {
            codeSetData.setId(item.getItemId());
        }
        return codeSetData;
    }

    public Page<CodeSetData> findHistory(final Page<CodeSetData> page, final CodeSetData codeSetData) {
        page.setOrderBy("a.item_code_sort");
        codeSetData.setPage((Page)page);
        page.setList((List)((CodeSetDataDao)this.dao).findHistory(codeSetData));
        return page;
    }

    public int findMaxVersion(final CodeSetData codeSetData) {
        return ((CodeSetDataDao)this.dao).findMaxVersion(codeSetData);
    }

    @Transactional(readOnly = false)
    public void validtimeCheck() {
        ((CodeSetDataDao)this.dao).validtimeCheck();
    }

    public int getTotalPage(final CollectCodeSetReqBody collectCodeSetReqBody) {
        final Map<String, Object> param = new HashMap<String, Object>();
        param.put("systemCode", collectCodeSetReqBody.getSystemCode());
        param.put("systemName", collectCodeSetReqBody.getSystemName());
        param.put("codeSetNo", collectCodeSetReqBody.getCodeSetNo());
        param.put("codeSetName", collectCodeSetReqBody.getCodeSetName());
        return ((CodeSetDataDao)this.dao).getTotalPage(param);
    }

    public List<CodeSetData> getCodeSetDataList(final CollectCodeSetResHeader header, final CollectCodeSetReqBody collectCodeSetReqBody) {
        final Map<String, Object> param = new HashMap<String, Object>();
        param.put("systemCode", collectCodeSetReqBody.getSystemCode());
        param.put("systemName", collectCodeSetReqBody.getSystemName());
        param.put("codeSetNo", collectCodeSetReqBody.getCodeSetNo());
        param.put("codeSetName", collectCodeSetReqBody.getCodeSetName());
        param.put("start", header.getPageSize() * (header.getCurrentPage() - 1) + 1);
        param.put("end", header.getCurrentPage() * header.getPageSize());
        return ((CodeSetDataDao)this.dao).getCodeSetDataList(param);
    }

    public List<CodeSetData> findListByCodeSetData(final String systemId, final String codeSetNo) {
        final Map<String, Object> param = Maps.newHashMap();
        param.put("systemId", systemId);
        param.put("codeSetNo", codeSetNo);
        return ((CodeSetDataDao)this.dao).findListByCodeSetData(param);
    }

    public List<CodeSetData> findBySystemIdAndCodeSetId(final String systemId, final List<String> ids) {
        final Map<String, Object> param = Maps.newHashMap();
        param.put("systemId", systemId);
        param.put("ids", ids);
        return ((CodeSetDataDao)this.dao).findBySystemIdAndCodeSetId(param);
    }
}
