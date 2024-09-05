package com.kmvc.jeesite.modules.businessdatamrg.web;

import com.kmvc.jeesite.modules.businessdatamrg.entity.SendingCodeItem;
import com.kmvc.jeesite.modules.businessdatamrg.entity.SendingCodeSet;
import com.kmvc.jeesite.modules.businessdatamrg.service.SendingAuditService;
import com.kmvc.jeesite.modules.businessdatamrg.service.SendingCodeItemService;
import com.thinkgem.jeesite.common.web.*;
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import com.kmvc.jeesite.modules.businessdatamrg.service.*;
import com.thinkgem.jeesite.common.utils.*;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.*;
import org.springframework.ui.*;
import com.thinkgem.jeesite.common.persistence.*;
import com.kmvc.jeesite.modules.businessdatamrg.entity.*;
import org.springframework.web.servlet.mvc.support.*;
import com.thinkgem.jeesite.common.config.*;

@Controller
@RequestMapping({ "${adminPath}/businessdatamrg/sendingAudit" })
public class SendingAuditController extends BaseController
{
    @Autowired
    private SendingAuditService sendingAuditService;
    @Autowired
    private SendingCodeItemService sendingCodeItemService;

    @ModelAttribute
    public SendingCodeSet get(@RequestParam(required = false) final String id) {
        SendingCodeSet entity = null;
        if (StringUtils.isNotBlank((CharSequence)id)) {
            entity = (SendingCodeSet)this.sendingAuditService.get(id);
        }
        if (entity == null) {
            entity = new SendingCodeSet();
        }
        return entity;
    }

    @RequestMapping({ "list", "" })
    public String list(final SendingCodeSet sendingCodeSet, final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        final Page<SendingCodeSet> page = (Page<SendingCodeSet>)this.sendingAuditService.taskScsList(new Page(request, response), sendingCodeSet);
        model.addAttribute("page", (Object)page);
        return "modules/businessdatamrg/sendingAuditList";
    }

    @RequestMapping({ "form" })
    public String form(final SendingCodeSet sendingCodeSet, final Model model, final HttpServletRequest request, final HttpServletResponse response) {
        Page<SendingCodeItem> page = null;
        final SendingCodeItem sendingCodeItem = new SendingCodeItem();
        try {
            if (StringUtils.isNotBlank((CharSequence)sendingCodeSet.getId())) {
                sendingCodeItem.setRecordId(sendingCodeSet.getId());
                page = (Page<SendingCodeItem>)this.sendingCodeItemService.findPage(new Page(request, response), sendingCodeItem);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("origin", (Object)request.getParameter("origin"));
        model.addAttribute("sendingCodeSet", (Object)sendingCodeSet);
        model.addAttribute("page", (Object)page);
        return "modules/businessdatamrg/sendingAuditForm";
    }

    @RequestMapping({ "resend" })
    public String resend(final HttpServletRequest request, final RedirectAttributes redirectAttributes) {
        final String sendingCsId = request.getParameter("id");
        final String result = this.sendingAuditService.resend(sendingCsId);
        if ("sendingCsIdIsBlank".equals(result)) {
            this.addMessage(redirectAttributes, new String[] { "\u62b1\u6b49\uff0c\u53d1\u9001\u5931\u8d25\uff0c\u6570\u636e\u5206\u53d1\u4e3b\u952e\u7f3a\u5931\uff0c\u8bf7\u8054\u7cfb\u7cfb\u7edf\u7ba1\u7406\u5458\uff01" });
        }
        else if ("sendingCsIsNull".equals(result)) {
            this.addMessage(redirectAttributes, new String[] { "\u62b1\u6b49\uff0c\u53d1\u9001\u5931\u8d25\uff0c\u6570\u636e\u5206\u53d1\u5b9e\u4f53\u4fe1\u606f\u7f3a\u5931\uff0c\u8bf7\u8054\u7cfb\u7cfb\u7edf\u7ba1\u7406\u5458\uff01" });
        }
        else if ("alreadySendSuccess".equals(result)) {
            this.addMessage(redirectAttributes, new String[] { "\u62b1\u6b49\uff0c\u53d1\u9001\u5931\u8d25\uff0c\u56e0\u4e3a\u6b64\u6570\u636e\u4e4b\u524d\u5df2\u53d1\u9001\u6210\u529f" });
        }
        else if ("finish".equals(result)) {
            try {
                Thread.sleep(2900L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.addMessage(redirectAttributes, new String[] { "\u64cd\u4f5c\u5df2\u5b8c\u6210" });
        }
        if ("taskList".equals(request.getParameter("origin"))) {
            redirectAttributes.addAttribute("taskStatus", (Object)0);
            return "redirect:" + Global.getAdminPath() + "/tesk/task/?repage";
        }
        return "redirect:" + this.adminPath + "/businessdatamrg/sendingAudit/list?repage";
    }
}
