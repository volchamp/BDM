package com.kmvc.jeesite.modules.datacheckingmrg.web;

import com.kmvc.jeesite.modules.datacheckingmrg.entity.ValidationDetailTmp;
import com.kmvc.jeesite.modules.datacheckingmrg.service.ValidationDetailTmpService;
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
@RequestMapping({ "${adminPath}/datacheckingmrg/validationDetailTmp" })
public class ValidationDetailTmpController extends BaseController
{
    @Autowired
    private ValidationDetailTmpService validationDetailTmpService;

    @ModelAttribute
    public ValidationDetailTmp get(@RequestParam(required = false) final String id) {
        ValidationDetailTmp entity = null;
        if (StringUtils.isNotBlank((CharSequence)id)) {
            entity = this.validationDetailTmpService.get(id);
        }
        if (entity == null) {
            entity = new ValidationDetailTmp();
        }
        return entity;
    }

    @RequiresPermissions({ "datacheckingmrg:validationDetailTmp:view" })
    @RequestMapping({ "list", "" })
    public String list(final ValidationDetailTmp validationDetailTmp, final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        final Page<ValidationDetailTmp> page = (Page<ValidationDetailTmp>)this.validationDetailTmpService.findPage(new Page(request, response), validationDetailTmp);
        model.addAttribute("page", page);
        return "modules/datacheckingmrg/validationDetailTmpList";
    }

    @RequiresPermissions({ "datacheckingmrg:validationDetailTmp:view" })
    @RequestMapping({ "form" })
    public String form(final ValidationDetailTmp validationDetailTmp, final Model model) {
        model.addAttribute("validationDetailTmp", validationDetailTmp);
        return "modules/datacheckingmrg/validationDetailTmpForm";
    }

    @RequiresPermissions({ "datacheckingmrg:validationDetailTmp:edit" })
    @RequestMapping({ "save" })
    public String save(final ValidationDetailTmp validationDetailTmp, final Model model, final RedirectAttributes redirectAttributes) {
        if (!this.beanValidator(model, validationDetailTmp, new Class[0])) {
            return this.form(validationDetailTmp, model);
        }
        this.validationDetailTmpService.save(validationDetailTmp);
        this.addMessage(redirectAttributes, new String[] { "\u4fdd\u5b58\u68c0\u6838\u4ee3\u7801\u96c6\u6570\u636e\u4e34\u65f6\u8868\u6210\u529f" });
        return "redirect:" + Global.getAdminPath() + "/datacheckingmrg/validationDetailTmp/?repage";
    }

    @RequiresPermissions({ "datacheckingmrg:validationDetailTmp:edit" })
    @RequestMapping({ "delete" })
    public String delete(final ValidationDetailTmp validationDetailTmp, final RedirectAttributes redirectAttributes) {
        this.validationDetailTmpService.delete(validationDetailTmp);
        this.addMessage(redirectAttributes, new String[] { "\u5220\u9664\u68c0\u6838\u4ee3\u7801\u96c6\u6570\u636e\u4e34\u65f6\u8868\u6210\u529f" });
        return "redirect:" + Global.getAdminPath() + "/datacheckingmrg/validationDetailTmp/?repage";
    }
}
