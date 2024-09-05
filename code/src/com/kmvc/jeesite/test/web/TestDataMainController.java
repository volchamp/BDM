package com.kmvc.jeesite.test.web;

import com.kmvc.jeesite.test.entity.TestDataMain;
import com.kmvc.jeesite.test.service.TestDataMainService;
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
@RequestMapping({ "${adminPath}/test/testDataMain" })
public class TestDataMainController extends BaseController
{
    @Autowired
    private TestDataMainService testDataMainService;

    @ModelAttribute
    public TestDataMain get(@RequestParam(required = false) final String id) {
        TestDataMain entity = null;
        if (StringUtils.isNotBlank((CharSequence)id)) {
            entity = this.testDataMainService.get(id);
        }
        if (entity == null) {
            entity = new TestDataMain();
        }
        return entity;
    }

    @RequiresPermissions({ "test:testDataMain:view" })
    @RequestMapping({ "list", "" })
    public String list(final TestDataMain testDataMain, final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        final Page<TestDataMain> page = this.testDataMainService.findPage((Page<TestDataMain>)new Page(request, response), testDataMain);
        model.addAttribute("page", (Object)page);
        return "jeesite/test/testDataMainList";
    }

    @RequiresPermissions({ "test:testDataMain:view" })
    @RequestMapping({ "form" })
    public String form(final TestDataMain testDataMain, final Model model) {
        model.addAttribute("testDataMain", (Object)testDataMain);
        return "jeesite/test/testDataMainForm";
    }

    @RequiresPermissions({ "test:testDataMain:edit" })
    @RequestMapping({ "save" })
    public String save(final TestDataMain testDataMain, final Model model, final RedirectAttributes redirectAttributes) {
        if (!this.beanValidator(model, (Object)testDataMain, new Class[0])) {
            return this.form(testDataMain, model);
        }
        this.testDataMainService.save(testDataMain);
        this.addMessage(redirectAttributes, new String[] { "\u4fdd\u5b58\u4e3b\u5b50\u8868\u6210\u529f" });
        return "redirect:" + Global.getAdminPath() + "/test/testDataMain/?repage";
    }

    @RequiresPermissions({ "test:testDataMain:edit" })
    @RequestMapping({ "delete" })
    public String delete(final TestDataMain testDataMain, final RedirectAttributes redirectAttributes) {
        this.testDataMainService.delete(testDataMain);
        this.addMessage(redirectAttributes, new String[] { "\u5220\u9664\u4e3b\u5b50\u8868\u6210\u529f" });
        return "redirect:" + Global.getAdminPath() + "/test/testDataMain/?repage";
    }
}
