package com.kmvc.jeesite.modules.codesetmrg.service;

import com.kmvc.jeesite.modules.codesetmrg.dao.SplitMergeDao;
import com.kmvc.jeesite.modules.codesetmrg.entity.SplitMerge;
import com.thinkgem.jeesite.common.service.*;
import com.kmvc.jeesite.modules.codesetmrg.dao.*;
import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import com.thinkgem.jeesite.common.persistence.*;
import com.thinkgem.jeesite.common.utils.*;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class SplitMergeService extends CrudService<SplitMergeDao, SplitMerge>
{
    public SplitMerge get(final String id) {
        return (SplitMerge)super.get(id);
    }

    public List<SplitMerge> findList(final SplitMerge splitMerge) {
        return (List<SplitMerge>)super.findList(splitMerge);
    }

    public Page<SplitMerge> findPage(final Page<SplitMerge> page, final SplitMerge splitMerge) {
        return (Page<SplitMerge>)super.findPage((Page)page, splitMerge);
    }

    @Transactional(readOnly = false)
    public void save(final SplitMerge splitMerge) {
        super.save(splitMerge);
    }

    @Transactional(readOnly = false)
    public void delete(final SplitMerge splitMerge) {
        super.delete(splitMerge);
    }

    public Page<SplitMerge> findChangeRelationPage(final Page<SplitMerge> page, final SplitMerge splitMerge) {
        final String itemId = splitMerge.getOldItemId();
        splitMerge.setPage((Page)page);
        final List<SplitMerge> smList = (List<SplitMerge>)((SplitMergeDao)this.dao).findListByItemId(splitMerge);
        if (smList.size() == 1) {
            final SplitMerge smCriteria = new SplitMerge();
            final SplitMerge firstSm = smList.get(0);
            if (itemId.equals(firstSm.getOldItemId())) {
                smCriteria.setNewItemId(firstSm.getNewItemId());
            }
            else if (itemId.equals(firstSm.getNewItemId())) {
                smCriteria.setOldItemId(firstSm.getOldItemId());
            }
            page.setList(((SplitMergeDao)this.dao).findList(smCriteria));
        }
        else {
            page.setList((List)smList);
        }
        return page;
    }

    public String findOldItemCodeStr(final SplitMerge smCriteria) {
        final List<SplitMerge> smList = (List<SplitMerge>)((SplitMergeDao)this.dao).findList(smCriteria);
        final List<String> oldItemCodeList = new ArrayList<String>();
        for (final SplitMerge sm : smList) {
            oldItemCodeList.add(sm.getOldItemCode());
        }
        return StringUtils.join((Iterable)oldItemCodeList, ",");
    }
}
