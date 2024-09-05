package com.kmvc.jeesite.modules.datacheckingmrg.service;

import com.kmvc.jeesite.modules.datacheckingmrg.dao.ValidationDetailDao;
import com.kmvc.jeesite.modules.datacheckingmrg.entity.ValidationDetail;
import com.thinkgem.jeesite.common.service.*;
import com.kmvc.jeesite.modules.datacheckingmrg.dao.*;
import com.kmvc.jeesite.modules.datacheckingmrg.entity.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import java.util.*;
import com.thinkgem.jeesite.common.persistence.*;

@Service
@Transactional(readOnly = true)
public class ValidationDetailService extends CrudService<ValidationDetailDao, ValidationDetail>
{
    public ValidationDetail get(final String id) {
        return (ValidationDetail)super.get(id);
    }

    public List<ValidationDetail> findList(final ValidationDetail validationDetail) {
        return (List<ValidationDetail>)super.findList(validationDetail);
    }

    public Page<ValidationDetail> findPage(final Page<ValidationDetail> page, final ValidationDetail validationDetail) {
        return (Page<ValidationDetail>)super.findPage((Page)page, validationDetail);
    }

    @Transactional(readOnly = false)
    public void save(final ValidationDetail validationDetail) {
        super.save(validationDetail);
    }

    @Transactional(readOnly = false)
    public void delete(final ValidationDetail validationDetail) {
        super.delete(validationDetail);
    }

    @Transactional(readOnly = false)
    public void verificationPro(final String systemCode) {
        ((ValidationDetailDao)this.dao).verificationPro(systemCode);
    }
}
