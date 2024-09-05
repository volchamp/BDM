package com.kmvc.jeesite.modules.codesetmrg.service;

import com.kmvc.jeesite.modules.codesetmrg.dao.CodeSetDataDao;
import com.kmvc.jeesite.modules.codesetmrg.dao.PendingCodeItemDao;
import com.kmvc.jeesite.modules.codesetmrg.dao.PendingCodeSetDao;
import com.kmvc.jeesite.modules.codesetmrg.dao.SplitMergeDao;
import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import com.kmvc.jeesite.modules.common.Constant;
import com.kmvc.jeesite.modules.common.exception.BizException;
import com.thinkgem.jeesite.common.service.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import org.springframework.beans.factory.annotation.*;
import com.kmvc.jeesite.modules.codesetmrg.dao.*;
import com.thinkgem.jeesite.common.persistence.*;
import com.thinkgem.jeesite.common.utils.*;
import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import com.kmvc.jeesite.modules.common.*;
import com.kmvc.jeesite.modules.common.exception.*;
import java.text.*;
import java.util.*;
import com.google.common.collect.*;

@Service
@Transactional(readOnly = true)
public class PendingCodeItemService extends CrudService<PendingCodeItemDao, PendingCodeItem>
{
    @Autowired
    private CodeSetCacheMappingService codeSetCacheMappingService;
    @Autowired
    private CodeSetDataService codeSetDataService;
    @Autowired
    private CodeSetService codeSetService;
    @Autowired
    private DistriRelationshipService distriRelationshipService;
    @Autowired
    private PendingCodeItemDao pendingCodeItemDao;
    @Autowired
    private SplitMergeDao splitMergeDao;
    @Autowired
    private PendingCodeSetDao pendingCodeSetDao;
    @Autowired
    private CodeSetDataDao codeSetDataDao;

    public PendingCodeItem get(final String id) {
        return (PendingCodeItem)super.get(id);
    }

    public List<PendingCodeItem> findList(final PendingCodeItem pendingCodeItem) {
        return (List<PendingCodeItem>)super.findList(pendingCodeItem);
    }

    public Page<PendingCodeItem> findPage(final Page<PendingCodeItem> page, final PendingCodeItem pendingCodeItem) {
        return (Page<PendingCodeItem>)super.findPage((Page)page, pendingCodeItem);
    }

    @Transactional(readOnly = false)
    public void save(PendingCodeItem pendingCodeItem) {
        if (pendingCodeItem.getCodeSetOperation() != null && pendingCodeItem.getCodeSetOperation() == 10) {
            final CodeSet codeSet = this.codeSetService.get(pendingCodeItem.getCodeSetId());
            final PendingCodeSetService pendingCodeSetService = (PendingCodeSetService)SpringContextHolder.getBean((Class)PendingCodeSetService.class);
            codeSet.setProcessStatus(10);
            final PendingCodeSet pendingCodeSet = pendingCodeSetService.toPendingCodeSet(codeSet);
            pendingCodeSetService.save(pendingCodeSet);
            pendingCodeItem.setCodeSetOperation(pendingCodeSet.getOperation());
            pendingCodeItem.setCodeSetProcessStatus(pendingCodeSet.getProcessStatus());
            pendingCodeItem.setCodeSetId(pendingCodeSet.getId());
        }
        if (pendingCodeItem.getOperation() == null) {
            pendingCodeItem.setOperation(0);
        }
        if (pendingCodeItem.getOperation() != 2) {
            pendingCodeItem.setProcessStatus(0);
            final Integer version = this.findMaxVersion(pendingCodeItem.getItemCode(), pendingCodeItem.getCodeSetNo());
            pendingCodeItem.setVersion(version + 1);
            pendingCodeItem.setStatus(1);
            if (pendingCodeItem.getOperation() == -1) {
                pendingCodeItem.setItemId(pendingCodeItem.getId());
                pendingCodeItem.setIsNewRecord(true);
                pendingCodeItem.setId(IdGen.uuid());
                final List<String> oldSystemMapping = this.distriRelationshipService.findByItemId(pendingCodeItem.getItemId());
                final CodeSetData oldCodeSetData = this.codeSetDataService.get(pendingCodeItem.getItemId());
                pendingCodeItem.setCol1(oldCodeSetData.getCol1());
                final Integer opertion = this.compare(oldCodeSetData, pendingCodeItem);
                if (opertion == 4) {
                    if (pendingCodeItem.getSystemIds() != null && pendingCodeItem.getSystemIds().size() > 0) {
                        final Integer str = this.compareOpertion(pendingCodeItem.getSystemIds(), oldSystemMapping);
                        if (str != null) {
                            pendingCodeItem.setOperation(str);
                        }
                        else {
                            pendingCodeItem.setOperation(opertion);
                        }
                    }
                    else {
                        pendingCodeItem.setOperation(opertion);
                    }
                }
                else {
                    pendingCodeItem.setOperation(opertion);
                }
            }
            this.codeSetCacheMappingService.deleteByItemId(pendingCodeItem.getId());
        }
        super.save(pendingCodeItem);
        final List<String> systemIds = pendingCodeItem.getSystemIds();
        if (systemIds != null) {
            for (final String systemId : systemIds) {
                final CodeSetCacheMapping c = new CodeSetCacheMapping();
                c.setSystemId(systemId);
                c.setItemId(pendingCodeItem.getId());
                c.setIsNewRecord(true);
                this.codeSetCacheMappingService.save(c);
            }
        }
        final String splitMerge = pendingCodeItem.getSplitMerge();
        final String smType = pendingCodeItem.getSmType();
        if ("split".equals(smType) || "merge".equals(smType)) {
            final List<String> itemIdList = Arrays.asList(splitMerge.split(","));
            final SplitMerge sm = new SplitMerge();
            sm.setItemIdList(itemIdList);
            final PendingCodeSet pendingCodeSet2 = (PendingCodeSet)this.pendingCodeSetDao.get(pendingCodeItem.getCodeSetId());
            if (pendingCodeSet2 != null) {
                sm.setCodeSetId(pendingCodeSet2.getId());
                sm.setCodeSetNo(pendingCodeSet2.getCodeSetNo());
                sm.setCodeSetName(pendingCodeSet2.getCodeSetName());
            }
            else {
                final CodeSet codeSet2 = this.codeSetService.get(pendingCodeItem.getCodeSetId());
                if (codeSet2 != null) {
                    sm.setCodeSetId(codeSet2.getId());
                    sm.setCodeSetNo(codeSet2.getCodeSetNo());
                    sm.setCodeSetName(codeSet2.getCodeSetName());
                }
            }
            if ("split".equals(smType)) {
                sm.setOldItemId(pendingCodeItem.getId());
                sm.setOldItemCode(pendingCodeItem.getItemCode());
                sm.setOldItemName(pendingCodeItem.getItemName());
                if (StringUtils.isBlank((CharSequence)pendingCodeItem.getCol1())) {
                    pendingCodeItem = this.get(pendingCodeItem.getId());
                }
                sm.setOldKindCode(pendingCodeItem.getCol1());
                this.splitMergeDao.batInsertSplit(sm);
                pendingCodeItem.setStatus(0);
                pendingCodeItem.setOperation(2);
                super.save(pendingCodeItem);
            }
            else {
                sm.setNewItemId(pendingCodeItem.getId());
                sm.setNewItemCode(pendingCodeItem.getItemCode());
                sm.setNewItemName(pendingCodeItem.getItemName());
                if (StringUtils.isBlank((CharSequence)pendingCodeItem.getCol1())) {
                    pendingCodeItem = this.get(pendingCodeItem.getId());
                }
                sm.setNewKindCode(pendingCodeItem.getCol1());
                this.splitMergeDao.batInsertMerge(sm);
                pendingCodeItem.setStatus(0);
                pendingCodeItem.setOperation(2);
                pendingCodeItem.setSelectedRow(itemIdList);
                ((PendingCodeItemDao)this.dao).batUpdByIds(pendingCodeItem);
                final CodeSetData codeSetData = new CodeSetData();
                codeSetData.setStatus(0);
                codeSetData.setSelectedRow(itemIdList);
                this.codeSetDataDao.batUpdByIds(codeSetData);
            }
        }
    }

    @Transactional(readOnly = false)
    public void delete(final PendingCodeItem pendingCodeItem) {
        if (pendingCodeItem.getCodeSetOperation() != null && pendingCodeItem.getCodeSetOperation() == 10) {
            final PendingCodeSetService pendingCodeSetService = (PendingCodeSetService)SpringContextHolder.getBean("pendingCodeSetService");
            final CodeSet codeSet = this.codeSetService.get(pendingCodeItem.getCodeSetId());
            final PendingCodeSet pendingCodeSet = pendingCodeSetService.toPendingCodeSet(codeSet);
            pendingCodeSet.setProcessStatus(10);
            pendingCodeSetService.save(pendingCodeSet);
            pendingCodeItem.setCodeSetId(pendingCodeSet.getId());
            final CodeSetData codeSetData = this.codeSetDataService.get(pendingCodeItem.getId());
            if (codeSetData != null) {
                codeSetData.setCodeSetId(pendingCodeSet.getId());
                final List<String> systemIds = this.distriRelationshipService.findByItemId(pendingCodeItem.getId());
                final PendingCodeItem p = this.toPendingCodeItem(codeSetData);
                p.setSystemIds(systemIds);
                p.setOperation(2);
                p.setStatus(codeSetData.getStatus());
                p.setVersion(codeSetData.getVersion());
                p.setProcessStatus(Constant.PENDING_STATUS_SAVE);
                this.save(p);
            }
        }
        else if (pendingCodeItem.getOperation() == -1) {
            final CodeSetData codeSetData2 = this.codeSetDataService.get(pendingCodeItem.getId());
            codeSetData2.setCodeSetId(pendingCodeItem.getCodeSetId());
            final List<String> systemIds2 = this.distriRelationshipService.findByItemId(pendingCodeItem.getId());
            final PendingCodeItem p2 = this.toPendingCodeItem(codeSetData2);
            p2.setSystemIds(systemIds2);
            p2.setOperation(2);
            p2.setStatus(codeSetData2.getStatus());
            p2.setVersion(codeSetData2.getVersion());
            p2.setProcessStatus(Constant.PENDING_STATUS_SAVE);
            this.save(p2);
        }
        else {
            this.codeSetCacheMappingService.deleteByItemId(pendingCodeItem.getId());
            super.delete(pendingCodeItem);
        }
    }

    public int findMaxVersion(final String itemCode, final String codeSetNo) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("itemCode", itemCode);
        params.put("codeSetNo", codeSetNo);
        return ((PendingCodeItemDao)this.dao).findMaxVersion(params);
    }

    @Transactional(readOnly = false)
    public Integer updateProcessStatus(final String codeSetId, final Integer processStatus) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("codeSetId", codeSetId);
        params.put("processStatus", processStatus);
        return ((PendingCodeItemDao)this.dao).updateProcessStatus(params);
    }

    public List<PendingCodeItem> findByCodeSetId(final String codeSetId) {
        return ((PendingCodeItemDao)this.dao).findByCodeSetId(codeSetId);
    }

    @Transactional(readOnly = false)
    public void deleteByCodeSetId(final String codeSetId) {
        ((PendingCodeItemDao)this.dao).deleteByCodeSetId(codeSetId);
    }

    public List<PendingCodeItem> findByCodeSetIdAndSystemId(final String codeSetId, final String systemId) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("codeSetId", codeSetId);
        params.put("systemId", systemId);
        return ((PendingCodeItemDao)this.dao).findByCodeSetIdAndSystemId(params);
    }

    public void codeSetHistoryImportSave(final CodeSetData codeSetData) {
        final List<String> codeSetIds = codeSetData.getSelectedRow();
        String codeSetId = codeSetData.getCodeSetId();
        if (codeSetData.getOperation() == 10) {
            final CodeSet codeSet = this.codeSetService.get(codeSetId);
            final PendingCodeSetService pendingCodeSetService = (PendingCodeSetService)SpringContextHolder.getBean("pendingCodeSetService");
            codeSet.setProcessStatus(10);
            final PendingCodeSet pendingCodeSet = pendingCodeSetService.toPendingCodeSet(codeSet);
            pendingCodeSetService.save(pendingCodeSet);
            codeSetId = pendingCodeSet.getId();
        }
        for (final String id : codeSetIds) {
            final CodeSetData c = this.codeSetDataService.get(id);
            c.setYear(codeSetData.getYear());
            c.setValidStartDate(codeSetData.getValidStartDate());
            c.setValidEndDate(codeSetData.getValidEndDate());
            final PendingCodeItem p = this.toPendingCodeItem(c);
            p.setCodeSetId(codeSetId);
            p.setProcessStatus(Constant.PENDING_STATUS_SAVE);
            p.setStatus(1);
            p.setOperation(1);
            p.setCodeSetNo(codeSetData.getCodeSetNo());
            final List<String> systemIds = this.distriRelationshipService.findByItemId(c.getId());
            p.setSystemIds(systemIds);
            this.save(p);
        }
    }

    public boolean carryOver(final CodeSetData codeSetData) {
        try {
            BizException.throwWhenNull(codeSetData.getCodeSetId(), "\u4ee3\u7801\u96c6\u76ee\u5f55ID\u4e0d\u80fd\u4e3a\u7a7a");
            String codeSetId = codeSetData.getCodeSetId();
            if (codeSetData.getOperation() != null && codeSetData.getOperation() == 10) {
                final CodeSet codeSet = this.codeSetService.get(codeSetId);
                final PendingCodeSetService pendingCodeSetService = (PendingCodeSetService)SpringContextHolder.getBean("pendingCodeSetService");
                codeSet.setProcessStatus(10);
                final PendingCodeSet pendingCodeSet = pendingCodeSetService.toPendingCodeSet(codeSet);
                pendingCodeSetService.save(pendingCodeSet);
                codeSetId = pendingCodeSet.getId();
            }
            final CodeSet codeSet = this.codeSetService.findByCode(codeSetData.getCodeSetNo());
            codeSetData.setStatus(2);
            codeSetData.setCodeSetId(codeSet.getId());
            final List<CodeSetData> codeSetDatas = this.codeSetDataService.findList(codeSetData);
            final Calendar calendar = Calendar.getInstance();
            Date start = new Date(System.currentTimeMillis());
            calendar.setTime(start);
            calendar.add(1, 1);
            calendar.set(2, 0);
            calendar.set(5, 31);
            start = calendar.getTime();
            final int year = calendar.get(1);
            Date end = new Date(System.currentTimeMillis());
            calendar.setTime(end);
            calendar.add(1, 1);
            calendar.set(2, 11);
            calendar.set(5, 31);
            end = calendar.getTime();
            for (final CodeSetData c : codeSetDatas) {
                final PendingCodeItem p = this.toPendingCodeItem(c);
                p.setYear(year);
                p.setValidStartDate(start);
                p.setValidEndDate(end);
                p.setCodeSetId(codeSetId);
                p.setProcessStatus(0);
                p.setStatus(1);
                p.setOperation(1);
                p.setCodeSetNo(codeSetData.getCodeSetNo());
                final List<String> systemIds = this.distriRelationshipService.findByItemId(c.getId());
                p.setSystemIds(systemIds);
                this.save(p);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Page<PendingCodeItem> preview(final Page<PendingCodeItem> page, final PendingCodeItem pendingCodeItem) {
        final List<PendingCodeItem> list = this.pendingCodeItemDao.reviewList(pendingCodeItem);
        page.setList((List)list);
        return page;
    }

    public PendingCodeItem toPendingCodeItem(final CodeSetData codeSetData) {
        final PendingCodeItem item = new PendingCodeItem();
        item.setCodeSetId(codeSetData.getCodeSetId());
        item.setItemCode(codeSetData.getItemCode());
        item.setParentItemCode(codeSetData.getParentItemCode());
        item.setItemName(codeSetData.getItemName());
        item.setYear(codeSetData.getYear());
        item.setValidStartDate(codeSetData.getValidStartDate());
        item.setValidEndDate(codeSetData.getValidEndDate());
        item.setStatus(codeSetData.getStatus());
        item.setVersion(codeSetData.getVersion());
        item.setItemCodeSort(codeSetData.getItemCodeSort());
        item.setItemId(codeSetData.getId());
        item.setCol1(codeSetData.getCol1());
        return item;
    }

    public Integer compare(final CodeSetData oldCodeSetData, final PendingCodeItem newCodeSetData) {
        if (!oldCodeSetData.getItemCode().equals(newCodeSetData.getItemCode())) {
            return 1;
        }
        if (!oldCodeSetData.getItemName().equals(newCodeSetData.getItemName())) {
            return 1;
        }
        if (oldCodeSetData.getYear() == null || !oldCodeSetData.getYear().equals(newCodeSetData.getYear())) {
            return 1;
        }
        if (oldCodeSetData.getRemarks() != null && oldCodeSetData.getRemarks().equals(newCodeSetData.getRemarks())) {
            final SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd");
            if (oldCodeSetData.getValidEndDate() == null && newCodeSetData.getValidEndDate() == null) {
                final String str3 = format.format(oldCodeSetData.getValidStartDate());
                final String str4 = format.format(newCodeSetData.getValidStartDate());
                final int result2 = str3.compareTo(str4);
                if (result2 == 0) {
                    return 4;
                }
                return 1;
            }
            else {
                String str5 = "";
                String str6 = "";
                if (oldCodeSetData.getValidEndDate() != null) {
                    str5 = format.format(oldCodeSetData.getValidEndDate());
                }
                if (newCodeSetData.getValidEndDate() != null) {
                    str6 = format.format(newCodeSetData.getValidEndDate());
                }
                final int result3 = str5.compareTo(str6);
                if (result3 == 0) {
                    return 4;
                }
                return 1;
            }
        }
        else {
            if (!StringUtils.isBlank((CharSequence)oldCodeSetData.getRemarks()) || !StringUtils.isBlank((CharSequence)newCodeSetData.getRemarks())) {
                return 1;
            }
            final SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd");
            if (oldCodeSetData.getValidEndDate() == null && newCodeSetData.getValidEndDate() == null) {
                final String str3 = format.format(oldCodeSetData.getValidStartDate());
                final String str4 = format.format(newCodeSetData.getValidStartDate());
                final int result2 = str3.compareTo(str4);
                if (result2 == 0) {
                    return 4;
                }
                return 1;
            }
            else {
                String str5 = "";
                String str6 = "";
                if (oldCodeSetData.getValidEndDate() != null) {
                    str5 = format.format(oldCodeSetData.getValidEndDate());
                }
                if (newCodeSetData.getValidEndDate() != null) {
                    str6 = format.format(newCodeSetData.getValidEndDate());
                }
                final int result3 = str5.compareTo(str6);
                if (result3 == 0) {
                    return 4;
                }
                return 1;
            }
        }
    }

    public Integer compareOpertion(final List<String> newSystemMapping, final List<String> oldSystemMapping) {
        final List<String> newTmp = new ArrayList<String>(newSystemMapping);
        final List<String> oldTmp = new ArrayList<String>(oldSystemMapping);
        for (int i = 0; i < newTmp.size(); ++i) {
            final String org = newTmp.get(i);
            for (int j = 0; j < oldTmp.size(); ++j) {
                final String ep = oldTmp.get(j).toString();
                if (org.equals(ep)) {
                    newTmp.remove(i);
                    --i;
                }
            }
        }
        if (newTmp.size() > 0) {
            return 5;
        }
        for (int i = 0; i < oldTmp.size(); ++i) {
            final String org = oldTmp.get(i);
            for (int j = 0; j < newSystemMapping.size(); ++j) {
                final String ep = newSystemMapping.get(j);
                if (org.equals(ep)) {
                    oldTmp.remove(i);
                    --i;
                }
            }
        }
        if (oldTmp.size() > 0) {
            return 4;
        }
        return null;
    }

    public void setSystem(final PendingCodeItem pendingCodeItem) {
        if (pendingCodeItem.getCodeSetOperation() != null && pendingCodeItem.getCodeSetOperation() == 10) {
            final CodeSet codeSet = this.codeSetService.get(pendingCodeItem.getCodeSetId());
            final PendingCodeSetService pendingCodeSetService = (PendingCodeSetService)SpringContextHolder.getBean((Class)PendingCodeSetService.class);
            codeSet.setProcessStatus(10);
            final PendingCodeSet pendingCodeSet = pendingCodeSetService.toPendingCodeSet(codeSet);
            pendingCodeSetService.save(pendingCodeSet);
            pendingCodeItem.setCodeSetId(pendingCodeSet.getId());
        }
        if (pendingCodeItem.getSelectedRow() != null && pendingCodeItem.getSelectedRow().size() > 0) {
            if (pendingCodeItem.getCodeSetOperation() == 10) {
                final List<String> selectRows = pendingCodeItem.getSelectedRow();
                for (final String row : selectRows) {
                    final String[] ids = row.split("#");
                    final CodeSetData codeSetData = this.codeSetDataService.get(ids[0]);
                    codeSetData.setCodeSetId(pendingCodeItem.getCodeSetId());
                    final PendingCodeItem p = this.toPendingCodeItem(codeSetData);
                    p.setSystemIds(pendingCodeItem.getSystemIds());
                    p.setCodeSetNo(pendingCodeItem.getCodeSetNo());
                    this.save(p);
                }
            }
            else {
                final List<String> selectRows = pendingCodeItem.getSelectedRow();
                for (final String row : selectRows) {
                    final String[] ids = row.split("#");
                    if ("-1".equals(ids[1])) {
                        final CodeSetData codeSetData = this.codeSetDataService.get(ids[0]);
                        codeSetData.setCodeSetId(pendingCodeItem.getCodeSetId());
                        final PendingCodeItem p = this.toPendingCodeItem(codeSetData);
                        p.setSystemIds(pendingCodeItem.getSystemIds());
                        p.setOperation(2);
                        p.setStatus(codeSetData.getStatus());
                        p.setVersion(codeSetData.getVersion());
                        p.setProcessStatus(Constant.PENDING_STATUS_SAVE);
                        this.save(p);
                    }
                    else {
                        this.codeSetCacheMappingService.deleteByItemId(ids[0]);
                        for (final String systemId : pendingCodeItem.getSystemIds()) {
                            final CodeSetCacheMapping c = new CodeSetCacheMapping();
                            c.setItemId(ids[0]);
                            c.setSystemId(systemId);
                            c.setIsNewRecord(true);
                            this.codeSetCacheMappingService.save(c);
                        }
                    }
                }
            }
        }
        else {
            final List<PendingCodeItem> pendingCodeItems = (List<PendingCodeItem>)((PendingCodeItemDao)this.dao).findList(pendingCodeItem);
            final List<String> systemIds = pendingCodeItem.getSystemIds();
            for (final PendingCodeItem p2 : pendingCodeItems) {
                if (p2.getOperation() != -1) {
                    this.codeSetCacheMappingService.deleteByItemId(p2.getId());
                    for (final String systemId : systemIds) {
                        final CodeSetCacheMapping c = new CodeSetCacheMapping();
                        c.setItemId(p2.getId());
                        c.setSystemId(systemId);
                        c.setIsNewRecord(true);
                        this.codeSetCacheMappingService.save(c);
                    }
                }
                else {
                    p2.setSystemIds(systemIds);
                    p2.setCodeSetId(pendingCodeItem.getCodeSetId());
                    p2.setCodeSetNo(pendingCodeItem.getCodeSetNo());
                    this.save(p2);
                }
            }
        }
    }

    public List<Map<String, Object>> getCodeToTreeData(final String codeSetNo, final String itemId) {
        final List<Map<String, Object>> mapList =Lists.newArrayList();
        final List<PendingCodeItem> ps = ((PendingCodeItemDao)this.dao).getCodeToTreeData(codeSetNo);
        for (final PendingCodeItem p : ps) {
            if (!p.getId().equals(itemId)) {
                final Map<String, Object> map = Maps.newHashMap();
                map.put("id", p.getItemCode());
                map.put("pId", p.getParentItemCode());
                map.put("name", p.getItemCode());
                mapList.add(map);
            }
        }
        return mapList;
    }

    public List<Map<String, Object>> findItemTree(final String codeSetNo, final String itemId) {
        final List<Map<String, Object>> mapList = Lists.newArrayList();
        final List<PendingCodeItem> ps = ((PendingCodeItemDao)this.dao).getCodeToTreeData(codeSetNo);
        final List<SplitMerge> allSmList = (List<SplitMerge>)this.splitMergeDao.findAllList(new SplitMerge());
        for (final PendingCodeItem p : ps) {
            boolean isSmExistItem = false;
            for (final SplitMerge sm : allSmList) {
                if (sm.getOldItemId().equals(p.getId()) || sm.getNewItemId().equals(p.getId())) {
                    isSmExistItem = true;
                    break;
                }
            }
            if (!p.getId().equals(itemId) && !isSmExistItem) {
                final Map<String, Object> map = Maps.newHashMap();
                map.put("id", p.getId());
                map.put("pId", p.getParentItemCode());
                map.put("name", p.getItemCode());
                mapList.add(map);
            }
        }
        return mapList;
    }

    public void batchDelete(final PendingCodeItem pendingCodeItem) {
        if (pendingCodeItem.getCodeSetOperation() == 10) {
            final PendingCodeSetService pendingCodeSetService = (PendingCodeSetService)SpringContextHolder.getBean("pendingCodeSetService");
            final CodeSet codeSet = this.codeSetService.get(pendingCodeItem.getCodeSetId());
            final PendingCodeSet pendingCodeSet = pendingCodeSetService.toPendingCodeSet(codeSet);
            pendingCodeSet.setProcessStatus(10);
            pendingCodeSetService.save(pendingCodeSet);
            final List<String> selectRows = pendingCodeItem.getSelectedRow();
            for (final String row : selectRows) {
                final String[] ids = row.split("#");
                final CodeSetData codeSetData = this.codeSetDataService.get(ids[0]);
                if (codeSetData != null) {
                    codeSetData.setCodeSetId(pendingCodeSet.getId());
                    final List<String> systemIds = this.distriRelationshipService.findByItemId(ids[0]);
                    final PendingCodeItem p = this.toPendingCodeItem(codeSetData);
                    p.setSystemIds(systemIds);
                    p.setOperation(2);
                    p.setStatus(codeSetData.getStatus());
                    p.setVersion(codeSetData.getVersion());
                    p.setProcessStatus(Constant.PENDING_STATUS_SAVE);
                    this.save(p);
                }
            }
        }
        else {
            final List<String> selectRows2 = pendingCodeItem.getSelectedRow();
            for (final String row2 : selectRows2) {
                final String[] ids2 = row2.split("#");
                if ("-1".equals(ids2[1])) {
                    final CodeSetData codeSetData2 = this.codeSetDataService.get(ids2[0]);
                    codeSetData2.setCodeSetId(pendingCodeItem.getCodeSetId());
                    final List<String> systemIds2 = this.distriRelationshipService.findByItemId(ids2[0]);
                    final PendingCodeItem p2 = this.toPendingCodeItem(codeSetData2);
                    p2.setSystemIds(systemIds2);
                    p2.setOperation(2);
                    p2.setStatus(codeSetData2.getStatus());
                    p2.setVersion(codeSetData2.getVersion());
                    p2.setProcessStatus(Constant.PENDING_STATUS_SAVE);
                    this.save(p2);
                }
                else {
                    this.codeSetCacheMappingService.deleteByItemId(ids2[0]);
                    ((PendingCodeItemDao)this.dao).delete(ids2[0]);
                }
            }
        }
    }
}
