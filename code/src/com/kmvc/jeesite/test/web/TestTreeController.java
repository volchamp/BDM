package com.kmvc.jeesite.test.web;

import com.kmvc.jeesite.test.entity.TestTree;
import com.kmvc.jeesite.test.service.TestTreeService;
import com.thinkgem.jeesite.common.web.*;
import org.springframework.stereotype.*;
import com.kmvc.jeesite.test.service.*;
import org.springframework.beans.factory.annotation.*;
import com.kmvc.jeesite.test.entity.*;
import com.thinkgem.jeesite.common.utils.*;
import javax.servlet.http.*;
import org.springframework.ui.*;
import org.apache.shiro.authz.annotation.*;
import org.springframework.web.servlet.mvc.support.*;
import com.thinkgem.jeesite.common.config.*;
import java.util.*;
import com.google.common.collect.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping({ "${adminPath}/test/testTree" })
public class TestTreeController extends BaseController
{
    @Autowired
    private TestTreeService testTreeService;

    @ModelAttribute
    public TestTree get(@RequestParam(required = false) final String id) {
        TestTree entity = null;
        if (StringUtils.isNotBlank((CharSequence)id)) {
            entity = this.testTreeService.get(id);
        }
        if (entity == null) {
            entity = new TestTree();
        }
        return entity;
    }

    @RequiresPermissions({ "test:testTree:view" })
    @RequestMapping({ "list", "" })
    public String list(final TestTree testTree, final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        final List<TestTree> list = (List<TestTree>)this.testTreeService.findList(testTree);
        model.addAttribute("list", (Object)list);
        return "jeesite/test/testTreeList";
    }

    @RequiresPermissions({ "test:testTree:view" })
    @RequestMapping({ "form" })
    public String form(final TestTree testTree, final Model model) {
        if (testTree.getParent() != null && StringUtils.isNotBlank((CharSequence)testTree.getParent().getId())) {
            testTree.setParent(this.testTreeService.get(testTree.getParent().getId()));
            if (StringUtils.isBlank((CharSequence)testTree.getId())) {
                final TestTree testTreeChild = new TestTree();
                testTreeChild.setParent(new TestTree(testTree.getParent().getId()));
                final List<TestTree> list = (List<TestTree>)this.testTreeService.findList(testTree);
                if (list.size() > 0) {
                    testTree.setSort(list.get(list.size() - 1).getSort());
                    if (testTree.getSort() != null) {
                        testTree.setSort(testTree.getSort() + 30);
                    }
                }
            }
        }
        if (testTree.getSort() == null) {
            testTree.setSort(30);
        }
        model.addAttribute("testTree", (Object)testTree);
        return "jeesite/test/testTreeForm";
    }

    @RequiresPermissions({ "test:testTree:edit" })
    @RequestMapping({ "save" })
    public String save(final TestTree testTree, final Model model, final RedirectAttributes redirectAttributes) {
        if (!this.beanValidator(model, (Object)testTree, new Class[0])) {
            return this.form(testTree, model);
        }
        this.testTreeService.save(testTree);
        this.addMessage(redirectAttributes, new String[] { "\u4fdd\u5b58\u6811\u7ed3\u6784\u6210\u529f" });
        return "redirect:" + Global.getAdminPath() + "/test/testTree/?repage";
    }

    @RequiresPermissions({ "test:testTree:edit" })
    @RequestMapping({ "delete" })
    public String delete(final TestTree testTree, final RedirectAttributes redirectAttributes) {
        this.testTreeService.delete(testTree);
        this.addMessage(redirectAttributes, new String[] { "\u5220\u9664\u6811\u7ed3\u6784\u6210\u529f" });
        return "redirect:" + Global.getAdminPath() + "/test/testTree/?repage";
    }

    @RequiresPermissions({ "user" })
    @ResponseBody
    @RequestMapping({ "treeData" })
    public List<Map<String, Object>> treeData(@RequestParam(required = false) final String extId, final HttpServletResponse response) {
        final List<Map<String, Object>> mapList = Lists.newArrayList();
        final List<TestTree> list = (List<TestTree>)this.testTreeService.findList(new TestTree());
        for (int i = 0; i < list.size(); ++i) {
            final TestTree e = list.get(i);
            if (StringUtils.isBlank((CharSequence)extId) || (extId != null && !extId.equals(e.getId()) && e.getParentIds().indexOf("," + extId + ",") == -1)) {
                final Map<String, Object> map =Maps.newHashMap();
                map.put("id", e.getId());
                map.put("pId", e.getParentId());
                map.put("name", e.getName());
                mapList.add(map);
            }
        }
        return mapList;
    }
}
