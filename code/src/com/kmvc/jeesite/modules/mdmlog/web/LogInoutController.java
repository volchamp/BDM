package com.kmvc.jeesite.modules.mdmlog.web;

import com.kmvc.jeesite.modules.mdmlog.entity.LogInout;
import com.kmvc.jeesite.modules.mdmlog.service.LogInoutService;
import com.thinkgem.jeesite.common.web.*;
import org.springframework.stereotype.*;
import com.kmvc.jeesite.modules.mdmlog.service.*;
import org.springframework.beans.factory.annotation.*;
import com.kmvc.jeesite.modules.mdmlog.entity.*;
import com.thinkgem.jeesite.common.utils.*;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.*;
import org.springframework.ui.*;
import com.thinkgem.jeesite.common.persistence.*;
import org.springframework.web.servlet.mvc.support.*;
import com.thinkgem.jeesite.common.config.*;

@Controller
@RequestMapping({ "${adminPath}/inoutlog/logInout" })
public class LogInoutController extends BaseController
{
    @Autowired
    private LogInoutService logInoutService;

    @ModelAttribute
    public LogInout get(@RequestParam(required = false) final String id) {
        LogInout entity = null;
        if (StringUtils.isNotBlank((CharSequence)id)) {
            entity = this.logInoutService.get(id);
        }
        if (entity == null) {
            entity = new LogInout();
        }
        return entity;
    }

    @RequestMapping({ "list", "" })
    public String list(final LogInout logInout, final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        final Page<LogInout> page = this.logInoutService.findPage((Page<LogInout>)new Page(request, response), logInout);
        model.addAttribute("page", (Object)page);
        return "modules/mdmlog/logInoutList";
    }

    @RequestMapping({ "form" })
    public String form(final LogInout logInout, final Model model) {
        model.addAttribute("logInout", (Object)logInout);
        return "modules/mdmlog/logInoutForm";
    }

    @RequestMapping({ "save" })
    public String save(final LogInout logInout, final Model model, final RedirectAttributes redirectAttributes) {
        if (!this.beanValidator(model, (Object)logInout, new Class[0])) {
            return this.form(logInout, model);
        }
        this.logInoutService.save(logInout);
        this.addMessage(redirectAttributes, new String[] { "\u4fdd\u5b58\u63a5\u53e3\u6536\u53d1\u65e5\u5fd7\u6210\u529f" });
        return "redirect:" + Global.getAdminPath() + "/inoutlog/logInout/?repage";
    }

    @RequestMapping({ "delete" })
    public String delete(final LogInout logInout, final RedirectAttributes redirectAttributes) {
        this.logInoutService.delete(logInout);
        this.addMessage(redirectAttributes, new String[] { "\u5220\u9664\u63a5\u53e3\u6536\u53d1\u65e5\u5fd7\u6210\u529f" });
        return "redirect:" + Global.getAdminPath() + "/inoutlog/logInout/?repage";
    }
}
