package com.kmvc.jeesite.modules.bps.web;

import com.thinkgem.jeesite.common.web.*;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import com.eos.workflow.data.*;
import javax.servlet.http.*;
import org.springframework.ui.*;
import com.thinkgem.jeesite.modules.sys.utils.*;
import com.thinkgem.jeesite.common.persistence.*;
import com.primeton.das.entity.impl.hibernate.criterion.*;
import com.eos.workflow.helper.*;
import java.util.*;
import com.primeton.workflow.api.*;
import com.thinkgem.jeesite.modules.sys.entity.*;
import com.eos.workflow.api.*;
import com.eos.das.entity.*;
import com.thinkgem.jeesite.common.config.*;

@Controller
@RequestMapping({ "${adminPath}/bps/workFlow" })
public class WorkFlowController extends BaseController
{
    private static final String processDefID_basicDataAudit;

    @RequestMapping({ "toDoList" })
    public String toDoList(final WFWorkItem wfWorkItem, final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        final User user = UserUtils.getUser();
        final String processType = request.getParameter("processType");
        String processDefID = null;
        if ("basicDataAudit".equals(processType)) {
            processDefID = WorkFlowController.processDefID_basicDataAudit;
        }
        final PageCond pageCond = new PageCond();
        final Page<WFWorkItem> page = (Page<WFWorkItem>)new Page(request, response);
        try {
            final IBPSServiceClient client = BPSServiceClientFactory.getDefaultClient();
            final IWFWorklistQueryManager worklistQueryManager = client.getWorklistQueryManager();
            final IDASCriteria workItem = DASManager.createCriteria("com.eos.workflow.data.WFWorkItem");
            workItem.add((Criterion)ExpressionHelper.eq("processDefID", (Object)processDefID));
            workItem.desc("startTime");
            pageCond.setIsCount(true);
            pageCond.setCurrentPage(page.getPageNo());
            pageCond.setBegin((page.getPageNo() - 1) * page.getPageSize());
            pageCond.setLength(page.getPageSize());
            List<WFWorkItem> workItems = null;
            workItems = (List<WFWorkItem>)worklistQueryManager.queryPersonWorkItemsCriteria(user.getId(), "ALL", "ALL", workItem, pageCond);
            final ResultList<WFWorkItem> tmps = (ResultList<WFWorkItem>)workItems;
            page.setCount((long)tmps.getPageCond().getCount());
            page.setList((List)workItems);
            model.addAttribute("page", (Object)page);
        }
        catch (WFServiceException e) {
            e.printStackTrace();
        }
        return "modules/bps/taskList";
    }

    static {
        processDefID_basicDataAudit = Global.getConfig("processDefID_basicDataAudit");
    }
}
