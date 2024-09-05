package com.kmvc.jeesite.modules.codesetmrg.service;

import com.kmvc.jeesite.modules.codesetmrg.dao.PendingCodesetSystemDao;
import com.kmvc.jeesite.modules.codesetmrg.entity.PendingCodesetSystem;
import com.thinkgem.jeesite.common.service.*;
import com.kmvc.jeesite.modules.codesetmrg.dao.*;
import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import java.util.*;
import com.thinkgem.jeesite.common.persistence.*;

@Service
@Transactional(readOnly = true)
public class PendingCodesetSystemService extends CrudService<PendingCodesetSystemDao, PendingCodesetSystem>
{
    public PendingCodesetSystem get(final String id) {
        return (PendingCodesetSystem)super.get(id);
    }

    public List<PendingCodesetSystem> findList(final PendingCodesetSystem pendingCodesetSystem) {
        return (List<PendingCodesetSystem>)super.findList(pendingCodesetSystem);
    }

    public Page<PendingCodesetSystem> findPage(final Page<PendingCodesetSystem> page, final PendingCodesetSystem pendingCodesetSystem) {
        return (Page<PendingCodesetSystem>)super.findPage((Page)page, pendingCodesetSystem);
    }

    public List<String> findByCodesetId(final String pendingCodesetId) {
        return ((PendingCodesetSystemDao)this.dao).findByCodesetId(pendingCodesetId);
    }

    @Transactional(readOnly = false)
    public void save(final PendingCodesetSystem pendingCodesetSystem) {
        super.save(pendingCodesetSystem);
    }

    @Transactional(readOnly = false)
    public void delete(final PendingCodesetSystem pendingCodesetSystem) {
        super.delete(pendingCodesetSystem);
    }

    @Transactional(readOnly = false)
    public void deleteByCodesetId(final String pendingCodesetId) {
        ((PendingCodesetSystemDao)this.dao).deleteByCodesetId(pendingCodesetId);
    }
}
