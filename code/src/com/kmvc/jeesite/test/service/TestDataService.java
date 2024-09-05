package com.kmvc.jeesite.test.service;

import com.kmvc.jeesite.test.dao.TestDataDao;
import com.kmvc.jeesite.test.entity.TestData;
import com.thinkgem.jeesite.common.service.*;
import com.kmvc.jeesite.test.dao.*;
import com.kmvc.jeesite.test.entity.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import java.util.*;
import com.thinkgem.jeesite.common.persistence.*;

@Service
@Transactional(readOnly = true)
public class TestDataService extends CrudService<TestDataDao, TestData>
{
    public TestData get(final String id) {
        return (TestData)super.get(id);
    }

    public List<TestData> findList(final TestData testData) {
        return (List<TestData>)super.findList(testData);
    }

    public Page<TestData> findPage(final Page<TestData> page, final TestData testData) {
        return (Page<TestData>)super.findPage((Page)page,testData);
    }

    @Transactional(readOnly = false)
    public void save(final TestData testData) {
        super.save(testData);
    }

    @Transactional(readOnly = false)
    public void delete(final TestData testData) {
        super.delete(testData);
    }
}
