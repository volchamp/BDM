package com.kmvc.jeesite.modules.codesetmrg.service;

import com.kmvc.jeesite.modules.codesetmrg.dao.DistriRelationshipDao;
import com.kmvc.jeesite.modules.codesetmrg.entity.DistriRelationship;
import com.thinkgem.jeesite.common.service.*;
import com.kmvc.jeesite.modules.codesetmrg.dao.*;
import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class DistriRelationshipService extends CrudService<DistriRelationshipDao, DistriRelationship>
{
    public List<DistriRelationship> findList(final DistriRelationship codeSet) {
        return (List<DistriRelationship>)super.findList(codeSet);
    }

    @Transactional(readOnly = false)
    public void save(final DistriRelationship distriRelationship) {
        super.save(distriRelationship);
    }

    @Transactional(readOnly = false)
    public void delete(final DistriRelationship distriRelationship) {
        super.delete(distriRelationship);
    }

    public List<String> findByItemId(final String itemId) {
        return ((DistriRelationshipDao)this.dao).findByItemId(itemId);
    }
}
