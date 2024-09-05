package com.kmvc.jeesite.modules.codesetmrg.web;

import com.kmvc.jeesite.modules.codesetmrg.entity.DataSetCategory;
import com.kmvc.jeesite.modules.codesetmrg.entity.PendingCodeItem;
import com.kmvc.jeesite.modules.codesetmrg.entity.PendingCodeSet;
import com.kmvc.jeesite.modules.codesetmrg.service.CodeSetService;
import com.kmvc.jeesite.modules.codesetmrg.service.DataSetCategoryService;
import com.kmvc.jeesite.modules.codesetmrg.service.PendingCodeItemService;
import com.kmvc.jeesite.modules.codesetmrg.service.PendingCodeSetService;
import com.thinkgem.jeesite.common.web.*;
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import com.kmvc.jeesite.modules.codesetmrg.service.*;
import org.springframework.web.servlet.mvc.support.*;
import com.thinkgem.jeesite.common.config.*;
import org.apache.shiro.authz.annotation.*;
import org.springframework.ui.*;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.*;
import com.thinkgem.jeesite.common.persistence.*;
import com.kmvc.jeesite.modules.codesetmrg.entity.*;

@Controller
@RequestMapping({ "${adminPath}/codesetmrg/pendingCodeSet" })
public class PendingCodeSetController extends BaseController
{
    @Autowired
    private CodeSetService codeSetService;
    @Autowired
    private PendingCodeSetService pendingCodeSetService;
    @Autowired
    private PendingCodeItemService pendingCodeItemService;
    @Autowired
    private DataSetCategoryService dataSetCategoryService;

    @RequiresPermissions({ "codesetmrg:codeSet:edit" })
    @RequestMapping({ "savePendingCodeSet" })
    public String savePendingCodeSet(final PendingCodeSet pendingCodeSet, final RedirectAttributes redirectAttributes) {
        this.pendingCodeSetService.save(pendingCodeSet);
        this.addMessage(redirectAttributes, new String[] { "\u4fdd\u5b58\u4ee3\u7801\u96c6\u76ee\u5f55\u6210\u529f" });
        return "redirect:" + Global.getAdminPath() + "/codesetmrg/codeSet/?repage";
    }

    @RequiresPermissions({ "codesetmrg:codeSet:edit" })
    @RequestMapping({ "delete" })
    @ResponseBody
    public boolean delete(final PendingCodeSet pendingCodeSet) {
        boolean isDelSuccess = true;
        try {
            this.pendingCodeSetService.delete(pendingCodeSet);
        }
        catch (Exception e) {
            isDelSuccess = false;
        }
        return isDelSuccess;
    }

    @RequiresPermissions({ "codesetmrg:codeSet:edit" })
    @RequestMapping(value = { "review" }, method = { RequestMethod.POST })
    @ResponseBody
    public Map<String, Object> review(PendingCodeSet pendingCodeSet, final Model model) {
        final Map<String, Object> map = new HashMap<String, Object>();
        try {
            this.pendingCodeSetService.review(pendingCodeSet);
        }
        catch (Exception e) {
            e.printStackTrace();
            map.put("id", "");
            return map;
        }
        pendingCodeSet = (PendingCodeSet)this.pendingCodeSetService.get(pendingCodeSet);
        map.put("id", pendingCodeSet.getId());
        map.put("operation", pendingCodeSet.getOperation());
        return map;
    }

    @RequiresPermissions({ "codesetmrg:codeSet:edit" })
    @RequestMapping({ "audit" })
    @ResponseBody
    public PendingCodeSet audit(PendingCodeSet pendingCodeSet, final Model model) {
        pendingCodeSet = this.pendingCodeSetService.audit(pendingCodeSet);
        return pendingCodeSet;
    }

    @RequestMapping({ "preview" })
    public String preview(PendingCodeSet pendingCodeSet, final Model model, final HttpServletRequest request, final HttpServletResponse response) {
        DataSetCategory dataSetCategory = null;
        Page<PendingCodeItem> page = null;
        pendingCodeSet = this.pendingCodeSetService.get(pendingCodeSet.getId());
        final PendingCodeItem pendingCodeItem = new PendingCodeItem();
        try {
            pendingCodeItem.setCodeSetId(pendingCodeSet.getId());
            dataSetCategory = this.dataSetCategoryService.get(pendingCodeSet.getCodeGroupId());
            page = (Page<PendingCodeItem>)this.pendingCodeItemService.preview(new Page(request, response), pendingCodeItem);
        }
        catch (Exception ex) {}
        model.addAttribute("codeGroupName", (Object)dataSetCategory.getCodeGroupName());
        model.addAttribute("pendingCodeSet", (Object)pendingCodeSet);
        model.addAttribute("page", (Object)page);
        return "modules/codesetmrg/codeSetPreview";
    }

    @RequiresPermissions({ "codesetmrg:codeSet:edit" })
    @RequestMapping({ "checkCodeSetNo" })
    @ResponseBody
    public boolean checkCodeSetNo(final PendingCodeSet pendingCodeSet) {
        final Long count = this.pendingCodeSetService.count(pendingCodeSet);
        return count == null || count.intValue() <= 0;
    }
}
