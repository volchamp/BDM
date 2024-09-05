package com.kmvc.jeesite.modules.datacheckingmrg.web;

import com.kmvc.jeesite.modules.datacheckingmrg.entity.ValidationCodeSetTmp;
import com.kmvc.jeesite.modules.datacheckingmrg.service.ValidationCodeSetTmpService;
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
@RequestMapping({ "${adminPath}/datacheckingmrg/validationCodeSetTmp" })
public class ValidationCodeSetTmpController extends BaseController
{
    @Autowired
    private ValidationCodeSetTmpService validationCodeSetTmpService;

    @ModelAttribute
    public ValidationCodeSetTmp get(@RequestParam(required = false) final String id) {
        ValidationCodeSetTmp entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = this.validationCodeSetTmpService.get(id);
        }
        if (entity == null) {
            entity = new ValidationCodeSetTmp();
        }
        return entity;
    }

    @RequiresPermissions({ "datacheckingmrg:validationCodeSetTmp:view" })
    @RequestMapping({ "list", "" })
    public String list(final ValidationCodeSetTmp validationCodeSetTmp, final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        final Page<ValidationCodeSetTmp> page = (Page<ValidationCodeSetTmp>)this.validationCodeSetTmpService.findPage(new Page(request, response), validationCodeSetTmp);
        model.addAttribute("page", page);
        return "modules/datacheckingmrg/validationCodeSetTmpList";
    }

    @RequiresPermissions({ "datacheckingmrg:validationCodeSetTmp:view" })
    @RequestMapping({ "form" })
    public String form(final ValidationCodeSetTmp validationCodeSetTmp, final Model model) {
        model.addAttribute("validationCodeSetTmp", validationCodeSetTmp);
        return "modules/datacheckingmrg/validationCodeSetTmpForm";
    }

    @RequiresPermissions({ "datacheckingmrg:validationCodeSetTmp:edit" })
    @RequestMapping({ "save" })
    public String save(final ValidationCodeSetTmp validationCodeSetTmp, final Model model, final RedirectAttributes redirectAttributes) {
        if (!this.beanValidator(model, validationCodeSetTmp, new Class[0])) {
            return this.form(validationCodeSetTmp, model);
        }
        this.validationCodeSetTmpService.save(validationCodeSetTmp);
        this.addMessage(redirectAttributes, new String[] { "\u4fdd\u5b58\u68c0\u6838\u4ee3\u7801\u96c6\u76ee\u5f55\u4e34\u65f6\u8868\u6210\u529f" });
        return "redirect:" + Global.getAdminPath() + "/datacheckingmrg/validationCodeSetTmp/?repage";
    }

    @RequiresPermissions({ "datacheckingmrg:validationCodeSetTmp:edit" })
    @RequestMapping({ "delete" })
    public String delete(final ValidationCodeSetTmp validationCodeSetTmp, final RedirectAttributes redirectAttributes) {
        this.validationCodeSetTmpService.delete(validationCodeSetTmp);
        this.addMessage(redirectAttributes, new String[] { "\u5220\u9664\u68c0\u6838\u4ee3\u7801\u96c6\u76ee\u5f55\u4e34\u65f6\u8868\u6210\u529f" });
        return "redirect:" + Global.getAdminPath() + "/datacheckingmrg/validationCodeSetTmp/?repage";
    }
}
