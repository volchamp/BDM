package com.kmvc.jeesite.test.web;

import com.kmvc.jeesite.test.entity.TestData;
import com.kmvc.jeesite.test.service.TestDataService;
import com.thinkgem.jeesite.common.web.*;
import org.springframework.stereotype.*;
import com.kmvc.jeesite.test.service.*;
import org.springframework.beans.factory.annotation.*;
import com.kmvc.jeesite.test.entity.*;
import com.thinkgem.jeesite.common.utils.*;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.*;
import org.springframework.ui.*;
import com.thinkgem.jeesite.common.persistence.*;
import org.apache.shiro.authz.annotation.*;
import org.springframework.web.servlet.mvc.support.*;
import com.thinkgem.jeesite.common.config.*;

@Controller
@RequestMapping({ "${adminPath}/test/testData" })
public class TestDataController extends BaseController
{
    @Autowired
    private TestDataService testDataService;

    @ModelAttribute
    public TestData get(@RequestParam(required = false) final String id) {
        TestData entity = null;
        if (StringUtils.isNotBlank((CharSequence)id)) {
            entity = this.testDataService.get(id);
        }
        if (entity == null) {
            entity = new TestData();
        }
        return entity;
    }

    @RequiresPermissions({ "test:testData:view" })
    @RequestMapping({ "list", "" })
    public String list(final TestData testData, final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        final Page<TestData> page = this.testDataService.findPage((Page<TestData>)new Page(request, response), testData);
        model.addAttribute("page", (Object)page);
        return "jeesite/test/testDataList";
    }

    @RequiresPermissions({ "test:testData:view" })
    @RequestMapping({ "form" })
    public String form(final TestData testData, final Model model) {
        model.addAttribute("testData", (Object)testData);
        return "jeesite/test/testDataForm";
    }

    @RequiresPermissions({ "test:testData:edit" })
    @RequestMapping({ "save" })
    public String save(final TestData testData, final Model model, final RedirectAttributes redirectAttributes) {
        if (!this.beanValidator(model, (Object)testData, new Class[0])) {
            return this.form(testData, model);
        }
        this.testDataService.save(testData);
        this.addMessage(redirectAttributes, new String[] { "\u4fdd\u5b58\u5355\u8868\u6210\u529f" });
        return "redirect:" + Global.getAdminPath() + "/test/testData/?repage";
    }

    @RequiresPermissions({ "test:testData:edit" })
    @RequestMapping({ "delete" })
    public String delete(final TestData testData, final RedirectAttributes redirectAttributes) {
        this.testDataService.delete(testData);
        this.addMessage(redirectAttributes, new String[] { "\u5220\u9664\u5355\u8868\u6210\u529f" });
        return "redirect:" + Global.getAdminPath() + "/test/testData/?repage";
    }
}
