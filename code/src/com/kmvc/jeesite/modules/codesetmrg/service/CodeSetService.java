package com.kmvc.jeesite.modules.codesetmrg.service;

import com.kmvc.jeesite.modules.codesetmrg.dao.CodeSetDao;
import com.kmvc.jeesite.modules.common.Util;
import com.kmvc.jeesite.modules.connector.vo.serve.inquire.ListCodeSetReq;
import com.thinkgem.jeesite.common.service.*;
import com.kmvc.jeesite.modules.codesetmrg.dao.*;
import com.kmvc.jeesite.modules.codesetmrg.entity.CodeSet;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import com.thinkgem.jeesite.modules.sys.dao.*;
import org.springframework.beans.factory.annotation.*;
import org.apache.commons.lang3.*;
import com.thinkgem.jeesite.modules.sys.utils.*;
import com.kmvc.jeesite.modules.common.*;
import com.thinkgem.jeesite.modules.sys.entity.*;
import com.thinkgem.jeesite.common.persistence.*;
import com.kmvc.jeesite.modules.connector.vo.serve.inquire.*;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class CodeSetService extends CrudService<CodeSetDao, CodeSet>
{
    @Autowired
    private OfficeDao officeDao;

    public CodeSet get(final String id) {
        final CodeSet codeSet = (CodeSet)super.get(id);
        if (codeSet == null) {
            return null;
        }
        final String officeIds = codeSet.getOfficeIds();
        if (StringUtils.isNotBlank((CharSequence)officeIds)) {
            if (officeIds.contains(",")) {
                final String[] offIdArray = officeIds.split(",");
                final Office office = (Office)this.officeDao.get(offIdArray[0]);
                if (office != null) {
                    codeSet.setOfficeName(office.getName());
                    codeSet.setAreaName(office.getArea().getName());
                }
            }
            else {
                final Office office2 = (Office)this.officeDao.get(officeIds);
                if (office2 != null) {
                    codeSet.setOfficeName(office2.getName());
                    codeSet.setAreaName(office2.getArea().getName());
                }
            }
        }
        return codeSet;
    }

    public List<CodeSet> findList(final CodeSet codeSet) {
        final User curUser = UserUtils.getUser();
        String curOfficeId = "";
        if (curUser.getOrganization() != null) {
            curOfficeId = curUser.getOrganization().getId();
        }
        else {
            final List<CodeSet> codesetList2 = ((CodeSetDao)this.dao).findOfficeIdByUserId(curUser.getId());
            curOfficeId = codesetList2.get(0).getOfficeIds();
        }
        codeSet.setOfficeIds(curOfficeId);
        if (!Util.isMdmModeComplex(true)) {
            return ((CodeSetDao)this.dao).findListSimple(codeSet);
        }
        return (List<CodeSet>)super.findList(codeSet);
    }

    public Page<CodeSet> findPage(final Page<CodeSet> page, final CodeSet codeSet) {
        codeSet.setPage((Page)page);
        final List<CodeSet> list = this.findList(codeSet);
        if (page.getCount() == 0L) {
            page.setCount((long)list.size());
        }
        page.setList((List)list);
        return page;
    }

    @Transactional(readOnly = false)
    public void save(final CodeSet codeSet) {
        final String officeIds = codeSet.getOfficeIds();
        if (StringUtils.isNotBlank((CharSequence)officeIds) && !officeIds.contains(",")) {
            final Office office = (Office)this.officeDao.get(officeIds);
            codeSet.setOfficeIds(officeIds + "," + office.getParentIds());
        }
        super.save(codeSet);
    }

    @Transactional(readOnly = false)
    public void delete(final CodeSet codeSet) {
        super.delete(codeSet);
    }

    public CodeSet findByCode(final String codeSetNo) {
        return ((CodeSetDao)this.dao).findByCode(codeSetNo);
    }

    public List<CodeSet> findBySystemId(final String systemId) {
        return ((CodeSetDao)this.dao).finBySystemId(systemId);
    }

    public List<CodeSet> findBySystemCode(final ListCodeSetReq request) {
        final Map<String, Object> param = new HashMap<String, Object>();
        param.put("systemCode", request.getSystemCode());
        param.put("systemName", request.getSystemName());
        return ((CodeSetDao)this.dao).findBySystemCode(param);
    }

    public Long count(final CodeSet codeSet) {
        return ((CodeSetDao)this.dao).count(codeSet);
    }

    public boolean isCodeSetNoExist(final CodeSet codeSet) {
        final Long count = ((CodeSetDao)this.dao).countByNo(codeSet);
        boolean isExist = true;
        if (count == null || count == 0L) {
            isExist = false;
        }
        return isExist;
    }
}
