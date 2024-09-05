package com.kmvc.jeesite.modules.datacheckingmrg.service;

import com.kmvc.jeesite.modules.connector.vo.client.olcodeset.CollectCodeSetData;
import com.kmvc.jeesite.modules.datacheckingmrg.dao.ValidationDetailTmpDao;
import com.kmvc.jeesite.modules.datacheckingmrg.entity.ValidationCodeSetTmp;
import com.kmvc.jeesite.modules.datacheckingmrg.entity.ValidationDetailTmp;
import com.thinkgem.jeesite.common.service.*;
import com.kmvc.jeesite.modules.datacheckingmrg.dao.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import com.thinkgem.jeesite.common.persistence.*;
import com.kmvc.jeesite.modules.connector.vo.client.olcodeset.*;
import com.kmvc.jeesite.modules.datacheckingmrg.entity.*;
import com.thinkgem.jeesite.common.utils.*;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class ValidationDetailTmpService extends CrudService<ValidationDetailTmpDao, ValidationDetailTmp>
{
    public ValidationDetailTmp get(final String id) {
        return (ValidationDetailTmp)super.get(id);
    }

    public List<ValidationDetailTmp> findList(final ValidationDetailTmp validationDetailTmp) {
        return (List<ValidationDetailTmp>)super.findList(validationDetailTmp);
    }

    public Page<ValidationDetailTmp> findPage(final Page<ValidationDetailTmp> page, final ValidationDetailTmp validationDetailTmp) {
        return (Page<ValidationDetailTmp>)super.findPage((Page)page,validationDetailTmp);
    }

    @Transactional(readOnly = false)
    public void save(final ValidationDetailTmp validationDetailTmp) {
        super.save(validationDetailTmp);
    }

    @Transactional(readOnly = false)
    public void delete(final ValidationDetailTmp validationDetailTmp) {
        super.delete(validationDetailTmp);
    }

    @Transactional(readOnly = false)
    public void saveBody(final List<CollectCodeSetData> codeEntityCollection, ValidationCodeSetTmp validationCodeSetTmp) {
        final ValidationCodeSetTmpService validationCodeSetTmpService = (ValidationCodeSetTmpService)SpringContextHolder.getBean("validationCodeSetTmpService");
        validationCodeSetTmp = validationCodeSetTmpService.findBySystemCode(validationCodeSetTmp);
        for (final CollectCodeSetData collectCodeSetData : codeEntityCollection) {
            final ValidationDetailTmp detailTmp = new ValidationDetailTmp();
            detailTmp.setItemCode(collectCodeSetData.getCode());
            detailTmp.setParentItemCode(collectCodeSetData.getParentCode());
            detailTmp.setItemName(collectCodeSetData.getName());
            if (collectCodeSetData.getYear() != null && !collectCodeSetData.getYear().isEmpty()) {
                detailTmp.setYear(Integer.valueOf(collectCodeSetData.getYear()));
            }
            if (collectCodeSetData.getValidStartDate() != null) {
                detailTmp.setValidStartTime(collectCodeSetData.getValidStartDate());
            }
            if (collectCodeSetData.getValidStartDate() != null) {
                detailTmp.setValidEndTime(collectCodeSetData.getValidEndDate());
            }
            detailTmp.setRecordId(validationCodeSetTmp.getId());
            this.save(detailTmp);
        }
    }

    public void deleteBySystemNo(final String systemNo) {
        ((ValidationDetailTmpDao)this.dao).deleteBySystemNo(systemNo);
    }
}
