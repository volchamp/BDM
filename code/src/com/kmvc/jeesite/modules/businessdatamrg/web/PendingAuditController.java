package com.kmvc.jeesite.modules.businessdatamrg.web;

import com.kmvc.jeesite.modules.businessdatamrg.service.PendingAuditService;
import com.kmvc.jeesite.modules.codesetmrg.entity.DataSetCategory;
import com.kmvc.jeesite.modules.codesetmrg.entity.PendingCodeItem;
import com.kmvc.jeesite.modules.codesetmrg.entity.PendingCodeSet;
import com.kmvc.jeesite.modules.codesetmrg.service.DataSetCategoryService;
import com.kmvc.jeesite.modules.codesetmrg.service.PendingCodeSetService;
import com.kmvc.jeesite.modules.common.Constant;
import com.thinkgem.jeesite.common.web.*;
import org.springframework.stereotype.*;
import com.kmvc.jeesite.modules.businessdatamrg.service.*;
import org.springframework.beans.factory.annotation.*;
import com.kmvc.jeesite.modules.codesetmrg.service.*;
import com.thinkgem.jeesite.common.utils.*;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.*;
import org.springframework.ui.*;
import com.thinkgem.jeesite.common.persistence.*;
import com.kmvc.jeesite.modules.common.*;
import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import org.springframework.web.servlet.mvc.support.*;
import com.thinkgem.jeesite.common.config.*;

@Controller
@RequestMapping({ "${adminPath}/businessdatamrg/pendingAudit" })
public class PendingAuditController extends BaseController
{
    @Autowired
    private PendingAuditService pendingAuditService;
    @Autowired
    private DataSetCategoryService dataSetCategoryService;
    @Autowired
    private PendingCodeSetService pendingCodeSetService;

    @ModelAttribute
    public PendingCodeSet get(@RequestParam(required = false) final String id) {
        PendingCodeSet entity = null;
        if (StringUtils.isNotBlank((CharSequence)id)) {
            entity = this.pendingAuditService.get(id);
        }
        if (entity == null) {
            entity = new PendingCodeSet();
        }
        return entity;
    }

    @RequestMapping({ "list", "" })
    public String list(final PendingCodeSet pendingCodeSet, final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        Page<PendingCodeSet> page = null;
        try {
            page = (Page<PendingCodeSet>)this.pendingAuditService.taskPendingCodeSetList(new Page(request, response), pendingCodeSet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("page", (Object)page);
        return "modules/businessdatamrg/pendingAuditList";
    }

    @RequestMapping({ "form" })
    public String form(PendingCodeSet pendingCodeSet, final Model model, final HttpServletRequest request, final HttpServletResponse response) {
        final String processInstID = request.getParameter("processInstID");
        final String workItemID = request.getParameter("workItemID");
        if (StringUtils.isNotBlank((CharSequence)processInstID)) {
            final PendingCodeSet pcsByProcInstId = this.pendingCodeSetService.getByProcInstId(processInstID);
            if (pcsByProcInstId != null) {
                pcsByProcInstId.setTaskStatus(Constant.TASK_STATUS_START);
                pendingCodeSet = pcsByProcInstId;
                pendingCodeSet.setWorkItemId(workItemID);
            }
        }
        DataSetCategory dataSetCategory = null;
        Page<PendingCodeItem> page = null;
        final PendingCodeItem pendingCodeItem = new PendingCodeItem();
        String codeGroupName = "";
        try {
            if (StringUtils.isNotBlank((CharSequence)pendingCodeSet.getId()) && StringUtils.isNotBlank((CharSequence)pendingCodeSet.getCodeGroupId())) {
                pendingCodeItem.setCodeSetId(pendingCodeSet.getId());
                pendingCodeItem.setCreateBy(pendingCodeSet.getCreateBy());
                dataSetCategory = this.dataSetCategoryService.get(pendingCodeSet.getCodeGroupId());
                codeGroupName = dataSetCategory.getCodeGroupName();
                page = (Page<PendingCodeItem>)this.pendingAuditService.reviewList(new Page(request, response), pendingCodeItem);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("origin", (Object)request.getParameter("origin"));
        model.addAttribute("codeGroupName", (Object)codeGroupName);
        model.addAttribute("pendingCodeSet", (Object)pendingCodeSet);
        model.addAttribute("page", (Object)page);
        return "modules/businessdatamrg/pendingAuditForm";
    }

    @RequestMapping({ "review" })
    public String review(final PendingCodeSet pendingCodeSet, final Model model, final RedirectAttributes redirectAttributes, final HttpServletRequest request) {
        boolean flag = false;
        final String workItemId = request.getParameter("workItemId");
        try {
            if (StringUtils.isBlank((CharSequence)pendingCodeSet.getWorkItemId())) {
                pendingCodeSet.setWorkItemId(workItemId);
            }
            if (StringUtils.isBlank((CharSequence)pendingCodeSet.getSystemId())) {
                pendingCodeSet.setSystemId(request.getParameter("systemId"));
            }
            flag = this.pendingAuditService.pendingAudit(pendingCodeSet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (flag) {
            this.addMessage(redirectAttributes, new String[] { pendingCodeSet.getCodeSetName() + "\u5ba1\u6838\u6210\u529f" });
        }
        else {
            this.addMessage(redirectAttributes, new String[] { pendingCodeSet.getCodeSetName() + "\u62b1\u6b49\uff0c\u5ba1\u6838\u5931\u8d25\uff0c\u8bf7\u7a0d\u540e\u518d\u8bd5\uff01" });
        }
        if ("taskList".equals(request.getParameter("origin"))) {
            redirectAttributes.addAttribute("taskStatus", (Object)0);
            return "redirect:" + Global.getAdminPath() + "/tesk/task/?repage";
        }
        if (StringUtils.isNotBlank((CharSequence)workItemId)) {
            return "redirect:" + Global.getAdminPath() + "/bps/workFlow/toDoList?processType=basicDataAudit";
        }
        return "redirect:" + Global.getAdminPath() + "/businessdatamrg/pendingAudit/?repage";
    }
}
