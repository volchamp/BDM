package com.kmvc.jeesite.modules.mdmlog.web;

import com.kmvc.jeesite.modules.mdmlog.entity.ProcLog;
import com.kmvc.jeesite.modules.mdmlog.service.ProcLogService;
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
import org.apache.shiro.authz.annotation.*;
import org.springframework.web.servlet.mvc.support.*;
import com.thinkgem.jeesite.common.config.*;

@Controller
@RequestMapping({ "${adminPath}/mdmlog/procLog" })
public class ProcLogController extends BaseController
{
    @Autowired
    private ProcLogService procLogService;

    @ModelAttribute
    public ProcLog get(@RequestParam(required = false) final String id) {
        ProcLog entity = null;
        if (StringUtils.isNotBlank((CharSequence)id)) {
            entity = this.procLogService.get(id);
        }
        if (entity == null) {
            entity = new ProcLog();
        }
        return entity;
    }

    @RequiresPermissions({ "mdmlog:procLog:view" })
    @RequestMapping({ "list", "" })
    public String list(final ProcLog procLog, final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        final Page<ProcLog> page = this.procLogService.findPage((Page<ProcLog>)new Page(request, response), procLog);
        model.addAttribute("page",page);
        return "modules/mdmlog/procLogList";
    }

    @RequiresPermissions({ "mdmlog:procLog:view" })
    @RequestMapping({ "form" })
    public String form(final ProcLog procLog, final Model model) {
        model.addAttribute("procLog", procLog);
        return "modules/mdmlog/procLogForm";
    }

    @RequiresPermissions({ "mdmlog:procLog:edit" })
    @RequestMapping({ "save" })
    public String save(final ProcLog procLog, final Model model, final RedirectAttributes redirectAttributes) {
        if (!this.beanValidator(model, procLog, new Class[0])) {
            return this.form(procLog, model);
        }
        this.procLogService.save(procLog);
        this.addMessage(redirectAttributes, new String[] { "\u4fdd\u5b58\u5b58\u50a8\u8fc7\u7a0b\u65e5\u5fd7\u6210\u529f" });
        return "redirect:" + Global.getAdminPath() + "/mdmlog/procLog/?repage";
    }

    @RequiresPermissions({ "mdmlog:procLog:edit" })
    @RequestMapping({ "delete" })
    public String delete(final ProcLog procLog, final RedirectAttributes redirectAttributes) {
        this.procLogService.delete(procLog);
        this.addMessage(redirectAttributes, new String[] { "\u5220\u9664\u5b58\u50a8\u8fc7\u7a0b\u65e5\u5fd7\u6210\u529f" });
        return "redirect:" + Global.getAdminPath() + "/mdmlog/procLog/?repage";
    }
}
