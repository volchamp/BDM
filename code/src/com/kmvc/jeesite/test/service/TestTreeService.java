package com.kmvc.jeesite.test.service;

import com.kmvc.jeesite.test.dao.TestTreeDao;
import com.kmvc.jeesite.test.entity.TestTree;
import com.thinkgem.jeesite.common.service.*;
import com.kmvc.jeesite.test.dao.*;
import com.kmvc.jeesite.test.entity.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import java.util.*;
import com.thinkgem.jeesite.common.utils.*;

@Service
@Transactional(readOnly = true)
public class TestTreeService extends TreeService<TestTreeDao, TestTree>
{
    public TestTree get(final String id) {
        return (TestTree)super.get(id);
    }

    public List<TestTree> findList(final TestTree testTree) {
        if (StringUtils.isNotBlank((CharSequence)testTree.getParentIds())) {
            testTree.setParentIds("," + testTree.getParentIds() + ",");
        }
        return (List<TestTree>)super.findList(testTree);
    }

    @Transactional(readOnly = false)
    public void save(final TestTree testTree) {
        super.save(testTree);
    }

    @Transactional(readOnly = false)
    public void delete(final TestTree testTree) {
        super.delete(testTree);
    }
}
