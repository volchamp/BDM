package com.kmvc.jeesite.modules.codesetmrg.service;

import com.kmvc.jeesite.modules.codesetmrg.dao.CodeSetCacheMappingDao;
import com.kmvc.jeesite.modules.codesetmrg.entity.CodeSetCacheMapping;
import com.thinkgem.jeesite.common.service.*;
import com.kmvc.jeesite.modules.codesetmrg.dao.*;
import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class CodeSetCacheMappingService extends CrudService<CodeSetCacheMappingDao, CodeSetCacheMapping>
{
    public List<CodeSetCacheMapping> findList(final CodeSetCacheMapping codeSetCacheMapping) {
        return (List<CodeSetCacheMapping>)super.findList(codeSetCacheMapping);
    }

    @Transactional(readOnly = false)
    public void save(final CodeSetCacheMapping codeSetCacheMapping) {
        super.save(codeSetCacheMapping);
    }

    @Transactional(readOnly = false)
    public void delete(final CodeSetCacheMapping codeSetCacheMapping) {
        super.delete(codeSetCacheMapping);
    }

    public List<String> findByItemId(final String itemId) {
        return ((CodeSetCacheMappingDao)this.dao).findByItemId(itemId);
    }

    public List<String> findByCodesetId(final String pendingCodesetId) {
        return ((CodeSetCacheMappingDao)this.dao).findByCodesetId(pendingCodesetId);
    }

    @Transactional(readOnly = false)
    public void deleteByItemId(final String itemId) {
        ((CodeSetCacheMappingDao)this.dao).deleteByItemId(itemId);
    }

    @Transactional(readOnly = false)
    public void deleteByCodeSetId(final String codeSetId) {
        ((CodeSetCacheMappingDao)this.dao).deleteByCodeSetId(codeSetId);
    }
}
