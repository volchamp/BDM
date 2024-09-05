package com.kmvc.jeesite.modules.sys.service;

import com.kmvc.jeesite.modules.sys.dao.TSystemDao;
import com.kmvc.jeesite.modules.sys.entity.TSystem;
import com.thinkgem.jeesite.common.service.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import com.thinkgem.jeesite.common.utils.*;
import java.util.*;
import com.thinkgem.jeesite.common.persistence.*;

@Service
@Transactional(readOnly = true)
public class TSystemService extends CrudService<TSystemDao, TSystem>
{
    public TSystem get(final String id) {
        final TSystem system = (TSystem)super.get(id);
        final String receivers = system.getReceivers();
        if (StringUtils.isNotBlank((CharSequence)receivers) && receivers.startsWith(",")) {
            system.setReceivers(receivers.substring(1, receivers.length() - 1));
        }
        return system;
    }

    public List<TSystem> findList(final TSystem tSystem) {
        return (List<TSystem>)super.findList(tSystem);
    }

    public Page<TSystem> findPage(final Page<TSystem> page, final TSystem tSystem) {
        tSystem.setDelFlag((String)null);
        return (Page<TSystem>)super.findPage((Page)page, tSystem);
    }

    @Transactional(readOnly = false)
    public void save(final TSystem tSystem) {
        final String receivers = tSystem.getReceivers();
        if (StringUtils.isNotBlank((CharSequence)receivers) && !receivers.startsWith(",")) {
            tSystem.setReceivers("," + receivers + ",");
        }
        super.save(tSystem);
    }

    @Transactional(readOnly = false)
    public void delete(final TSystem tSystem) {
        super.delete(tSystem);
    }

    @Transactional(readOnly = false)
    public void enable(final TSystem tSystem) {
        ((TSystemDao)this.dao).enable(tSystem);
    }

    public boolean isSystemNameExist(final TSystem tSystem) {
        return ((TSystemDao)this.dao).countExistSystemName(tSystem) > 0;
    }

    public boolean isSystemCodeExist(final TSystem tSystem) {
        return ((TSystemDao)this.dao).countExistSystemCode(tSystem) > 0;
    }

    public boolean isSystemShortExist(final TSystem tSystem) {
        return ((TSystemDao)this.dao).countExistSystemShort(tSystem) > 0;
    }

    public boolean validExist(final TSystem tSystem) {
        return ((TSystemDao)this.dao).validExist(tSystem) > 0;
    }

    public List<TSystem> findAllList() {
        return (List<TSystem>)((TSystemDao)this.dao).findAllList(new TSystem());
    }

    public List<TSystem> findSystemByCodeSetId(final String codeSetId) {
        return ((TSystemDao)this.dao).findSystemByCodeSetId(codeSetId);
    }

    public List<TSystem> codeSetStatistics(final TSystem tSystem) {
        return ((TSystemDao)this.dao).codeSetStatistics(tSystem);
    }

    public TSystem findBySystemCode(final String systemCode) {
        return ((TSystemDao)this.dao).findBySystemCode(systemCode);
    }
}
