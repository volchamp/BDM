package com.kmvc.jeesite.modules.codesetmrg.web;

import com.kmvc.jeesite.modules.codesetmrg.entity.SplitMerge;
import com.kmvc.jeesite.modules.codesetmrg.service.SplitMergeService;
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
import org.springframework.web.servlet.mvc.support.*;
import com.thinkgem.jeesite.common.config.*;

@Controller
@RequestMapping({ "${adminPath}/codesetmrg/splitMerge" })
public class SplitMergeController extends BaseController
{
    @Autowired
    private SplitMergeService splitMergeService;

    @ModelAttribute
    public SplitMerge get(@RequestParam(required = false) final String id) {
        SplitMerge entity = null;
        if (StringUtils.isNotBlank((CharSequence)id)) {
            entity = this.splitMergeService.get(id);
        }
        if (entity == null) {
            entity = new SplitMerge();
        }
        return entity;
    }

    @RequestMapping({ "list", "" })
    public String list(final SplitMerge splitMerge, final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        final Page<SplitMerge> page = (Page<SplitMerge>)this.splitMergeService.findPage(new Page(request, response), splitMerge);
        model.addAttribute("page", (Object)page);
        return "modules/codesetmrg/splitMergeList";
    }

    @RequestMapping({ "form" })
    public String form(final SplitMerge splitMerge, final Model model) {
        model.addAttribute("splitMerge", (Object)splitMerge);
        return "modules/codesetmrg/splitMergeForm";
    }

    @RequestMapping({ "save" })
    public String save(final SplitMerge splitMerge, final Model model, final RedirectAttributes redirectAttributes) {
        if (!this.beanValidator(model, (Object)splitMerge, new Class[0])) {
            return this.form(splitMerge, model);
        }
        this.splitMergeService.save(splitMerge);
        this.addMessage(redirectAttributes, new String[] { "\u4fdd\u5b58\u4ee3\u7801\u96c6\u62c6\u5206\u5408\u5e76\u5173\u7cfb\u6210\u529f" });
        return "redirect:" + Global.getAdminPath() + "/codesetmrg/splitMerge/?repage";
    }

    @RequestMapping({ "delete" })
    public String delete(final SplitMerge splitMerge, final RedirectAttributes redirectAttributes) {
        this.splitMergeService.delete(splitMerge);
        this.addMessage(redirectAttributes, new String[] { "\u5220\u9664\u4ee3\u7801\u96c6\u62c6\u5206\u5408\u5e76\u5173\u7cfb\u6210\u529f" });
        return "redirect:" + Global.getAdminPath() + "/codesetmrg/splitMerge/?repage";
    }

    @RequestMapping({ "changeRelationList" })
    public String changeRelationList(final SplitMerge splitMerge, final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        final Page<SplitMerge> page = (Page<SplitMerge>)this.splitMergeService.findChangeRelationPage(new Page(request, response), splitMerge);
        model.addAttribute("page", (Object)page);
        return "modules/codesetmrg/splitMergeList";
    }
}
