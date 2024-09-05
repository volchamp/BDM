package com.kmvc.jeesite.modules.codesetmrg.web;

import com.kmvc.jeesite.modules.codesetmrg.entity.PendingCodesetSystem;
import com.kmvc.jeesite.modules.codesetmrg.service.PendingCodesetSystemService;
import com.thinkgem.jeesite.common.web.*;
import org.springframework.stereotype.*;
import com.kmvc.jeesite.modules.codesetmrg.service.*;
import org.springframework.beans.factory.annotation.*;
import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import com.thinkgem.jeesite.common.utils.*;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.*;
import org.springframework.ui.*;
import com.thinkgem.jeesite.common.persistence.*;
import org.apache.shiro.authz.annotation.*;
import org.springframework.web.servlet.mvc.support.*;
import com.thinkgem.jeesite.common.config.*;

@Controller
@RequestMapping({ "${adminPath}/codesetmrg/pendingCodesetSystem" })
public class PendingCodesetSystemController extends BaseController
{
    @Autowired
    private PendingCodesetSystemService pendingCodesetSystemService;

    @ModelAttribute
    public PendingCodesetSystem get(@RequestParam(required = false) final String id) {
        PendingCodesetSystem entity = null;
        if (StringUtils.isNotBlank((CharSequence)id)) {
            entity = this.pendingCodesetSystemService.get(id);
        }
        if (entity == null) {
            entity = new PendingCodesetSystem();
        }
        return entity;
    }

    @RequiresPermissions({ "codesetmrg:pendingCodesetSystem:view" })
    @RequestMapping({ "list", "" })
    public String list(final PendingCodesetSystem pendingCodesetSystem, final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        final Page<PendingCodesetSystem> page = (Page<PendingCodesetSystem>)this.pendingCodesetSystemService.findPage(new Page(request, response), pendingCodesetSystem);
        model.addAttribute("page", (Object)page);
        return "modules/codesetmrg/pendingCodesetSystemList";
    }

    @RequiresPermissions({ "codesetmrg:pendingCodesetSystem:view" })
    @RequestMapping({ "form" })
    public String form(final PendingCodesetSystem pendingCodesetSystem, final Model model) {
        model.addAttribute("pendingCodesetSystem", (Object)pendingCodesetSystem);
        return "modules/codesetmrg/pendingCodesetSystemForm";
    }

    @RequiresPermissions({ "codesetmrg:pendingCodesetSystem:edit" })
    @RequestMapping({ "save" })
    public String save(final PendingCodesetSystem pendingCodesetSystem, final Model model, final RedirectAttributes redirectAttributes) {
        if (!this.beanValidator(model, (Object)pendingCodesetSystem, new Class[0])) {
            return this.form(pendingCodesetSystem, model);
        }
        this.pendingCodesetSystemService.save(pendingCodesetSystem);
        this.addMessage(redirectAttributes, new String[] { "\u4fdd\u5b58\u4e1a\u52a1\u7cfb\u7edf\u4e0e\u5f85\u5904\u7406\u4ee3\u7801\u96c6\u76ee\u5f55\u5173\u7cfb\u6210\u529f" });
        return "redirect:" + Global.getAdminPath() + "/codesetmrg/pendingCodesetSystem/?repage";
    }

    @RequiresPermissions({ "codesetmrg:pendingCodesetSystem:edit" })
    @RequestMapping({ "delete" })
    public String delete(final PendingCodesetSystem pendingCodesetSystem, final RedirectAttributes redirectAttributes) {
        this.pendingCodesetSystemService.delete(pendingCodesetSystem);
        this.addMessage(redirectAttributes, new String[] { "\u5220\u9664\u4e1a\u52a1\u7cfb\u7edf\u4e0e\u5f85\u5904\u7406\u4ee3\u7801\u96c6\u76ee\u5f55\u5173\u7cfb\u6210\u529f" });
        return "redirect:" + Global.getAdminPath() + "/codesetmrg/pendingCodesetSystem/?repage";
    }
}
