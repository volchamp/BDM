package com.kmvc.jeesite.test.service;

import com.kmvc.jeesite.test.dao.TestDataChildDao;
import com.kmvc.jeesite.test.dao.TestDataMainDao;
import com.kmvc.jeesite.test.entity.TestDataChild;
import com.kmvc.jeesite.test.entity.TestDataMain;
import com.thinkgem.jeesite.common.service.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import com.kmvc.jeesite.test.dao.*;
import org.springframework.beans.factory.annotation.*;
import com.kmvc.jeesite.test.entity.*;
import com.thinkgem.jeesite.common.persistence.*;
import com.thinkgem.jeesite.common.utils.*;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class TestDataMainService extends CrudService<TestDataMainDao, TestDataMain>
{
    @Autowired
    private TestDataChildDao testDataChildDao;

    public TestDataMain get(final String id) {
        final TestDataMain testDataMain = (TestDataMain)super.get(id);
        testDataMain.setTestDataChildList(this.testDataChildDao.findList(new TestDataChild(testDataMain)));
        return testDataMain;
    }

    public List<TestDataMain> findList(final TestDataMain testDataMain) {
        return (List<TestDataMain>)super.findList(testDataMain);
    }

    public Page<TestDataMain> findPage(final Page<TestDataMain> page, final TestDataMain testDataMain) {
        return (Page<TestDataMain>)super.findPage((Page)page, testDataMain);
    }

    @Transactional(readOnly = false)
    public void save(final TestDataMain testDataMain) {
        super.save(testDataMain);
        for (final TestDataChild testDataChild : testDataMain.getTestDataChildList()) {
            if (testDataChild.getId() == null) {
                continue;
            }
            if ("0".equals(testDataChild.getDelFlag())) {
                if (StringUtils.isBlank((CharSequence)testDataChild.getId())) {
                    testDataChild.setTestDataMain(testDataMain);
                    testDataChild.preInsert();
                    this.testDataChildDao.insert(testDataChild);
                }
                else {
                    testDataChild.preUpdate();
                    this.testDataChildDao.update(testDataChild);
                }
            }
            else {
                this.testDataChildDao.delete(testDataChild);
            }
        }
    }

    @Transactional(readOnly = false)
    public void delete(final TestDataMain testDataMain) {
        super.delete(testDataMain);
        this.testDataChildDao.delete(new TestDataChild(testDataMain));
    }
}
