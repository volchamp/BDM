package com.kmvc.jeesite.modules.datacheckingmrg.service;

import com.kmvc.jeesite.modules.connector.vo.client.olcodeset.CollectCodeSetResHeader;
import com.kmvc.jeesite.modules.datacheckingmrg.dao.ValidationCodeSetTmpDao;
import com.kmvc.jeesite.modules.datacheckingmrg.entity.ValidationCodeSetTmp;
import com.thinkgem.jeesite.common.service.*;
import com.kmvc.jeesite.modules.datacheckingmrg.dao.*;
import com.kmvc.jeesite.modules.datacheckingmrg.entity.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import com.thinkgem.jeesite.common.persistence.*;
import com.kmvc.jeesite.modules.connector.vo.client.olcodeset.*;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class ValidationCodeSetTmpService extends CrudService<ValidationCodeSetTmpDao, ValidationCodeSetTmp>
{
    public ValidationCodeSetTmp get(final String id) {
        return (ValidationCodeSetTmp)super.get(id);
    }

    public List<ValidationCodeSetTmp> findList(final ValidationCodeSetTmp validationCodeSetTmp) {
        return (List<ValidationCodeSetTmp>)super.findList(validationCodeSetTmp);
    }

    public Page<ValidationCodeSetTmp> findPage(final Page<ValidationCodeSetTmp> page, final ValidationCodeSetTmp validationCodeSetTmp) {
        return (Page<ValidationCodeSetTmp>)super.findPage((Page)page, validationCodeSetTmp);
    }

    @Transactional(readOnly = false)
    public void save(final ValidationCodeSetTmp validationCodeSetTmp) {
        super.save(validationCodeSetTmp);
    }

    @Transactional(readOnly = false)
    public void delete(final ValidationCodeSetTmp validationCodeSetTmp) {
        super.delete(validationCodeSetTmp);
    }

    public ValidationCodeSetTmp findBySystemCode(final ValidationCodeSetTmp tmp) {
        return ((ValidationCodeSetTmpDao)this.dao).findBySystemCode(tmp);
    }

    public ValidationCodeSetTmp saveHeader(final CollectCodeSetResHeader colResMsgHeader, ValidationCodeSetTmp validationCodeSetTmp) {
        if (colResMsgHeader.getCurrentPage() == 1) {
            final ValidationCodeSetTmp tmp = this.findBySystemCode(validationCodeSetTmp);
            if (tmp == null) {
                validationCodeSetTmp.setStatus(0);
                validationCodeSetTmp.setReceiveDate(new Date());
                if (colResMsgHeader.getTotalPage() == 1) {
                    validationCodeSetTmp.setFinished(1);
                    validationCodeSetTmp.setFinishDate(new Date());
                }
                else {
                    validationCodeSetTmp.setFinished(0);
                }
                super.save(validationCodeSetTmp);
            }
        }
        else if (colResMsgHeader.getCurrentPage() == colResMsgHeader.getTotalPage() && colResMsgHeader.getTotalPage() != 1) {
            validationCodeSetTmp = this.findBySystemCode(validationCodeSetTmp);
            validationCodeSetTmp.setFinished(1);
            validationCodeSetTmp.setFinishDate(new Date());
            validationCodeSetTmp.setStatus(0);
            validationCodeSetTmp.setId(validationCodeSetTmp.getId());
            super.save(validationCodeSetTmp);
        }
        return validationCodeSetTmp;
    }

    public void deleteBySystemNo(final String systemCode) {
        ((ValidationCodeSetTmpDao)this.dao).deleteBySystemNo(systemCode);
    }
}
