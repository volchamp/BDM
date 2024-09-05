package com.kmvc.jeesite.modules.datacheckingmrg.web;

import com.kmvc.jeesite.modules.datacheckingmrg.entity.ValidationDetail;
import com.kmvc.jeesite.modules.datacheckingmrg.service.ValidationDetailService;
import com.thinkgem.jeesite.common.web.*;
import org.springframework.stereotype.*;
import com.kmvc.jeesite.modules.datacheckingmrg.service.*;
import org.springframework.beans.factory.annotation.*;
import com.kmvc.jeesite.modules.datacheckingmrg.entity.*;
import com.thinkgem.jeesite.common.utils.*;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.*;
import org.springframework.ui.*;
import com.thinkgem.jeesite.common.persistence.*;
import org.apache.shiro.authz.annotation.*;
import org.springframework.web.servlet.mvc.support.*;
import com.thinkgem.jeesite.common.config.*;

@Controller
@RequestMapping({ "${adminPath}/datacheckingmrg/validationDetail" })
public class ValidationDetailController extends BaseController
{
    @Autowired
    private ValidationDetailService validationDetailService;

    @ModelAttribute
    public ValidationDetail get(@RequestParam(required = false) final String id) {
        ValidationDetail entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = this.validationDetailService.get(id);
        }
        if (entity == null) {
            entity = new ValidationDetail();
        }
        return entity;
    }

    @RequiresPermissions({ "datacheckingmrg:validationDetail:view" })
    @RequestMapping({ "list", "" })
    public String list(final ValidationDetail validationDetail, final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        final Page<ValidationDetail> page = (Page<ValidationDetail>)this.validationDetailService.findPage(new Page(request, response), validationDetail);
        model.addAttribute("page", page);
        return "modules/datacheckingmrg/validationDetailList";
    }

    @RequiresPermissions({ "datacheckingmrg:validationDetail:view" })
    @RequestMapping({ "form" })
    public String form(final ValidationDetail validationDetail, final Model model) {
        model.addAttribute("validationDetail", validationDetail);
        return "modules/datacheckingmrg/validationDetailForm";
    }

    @RequiresPermissions({ "datacheckingmrg:validationDetail:edit" })
    @RequestMapping({ "save" })
    public String save(final ValidationDetail validationDetail, final Model model, final RedirectAttributes redirectAttributes) {
        if (!this.beanValidator(model, validationDetail, new Class[0])) {
            return this.form(validationDetail, model);
        }
        this.validationDetailService.save(validationDetail);
        this.addMessage(redirectAttributes, new String[] { "\u4fdd\u5b58\u5f02\u5e38\u4ee3\u7801\u96c6\u6570\u636e\u6210\u529f" });
        return "redirect:" + Global.getAdminPath() + "/datacheckingmrg/validationDetail/?repage";
    }

    @RequiresPermissions({ "datacheckingmrg:validationDetail:edit" })
    @RequestMapping({ "delete" })
    public String delete(final ValidationDetail validationDetail, final RedirectAttributes redirectAttributes) {
        this.validationDetailService.delete(validationDetail);
        this.addMessage(redirectAttributes, new String[] { "\u5220\u9664\u5f02\u5e38\u4ee3\u7801\u96c6\u6570\u636e\u6210\u529f" });
        return "redirect:" + Global.getAdminPath() + "/datacheckingmrg/validationDetail/?repage";
    }
}
