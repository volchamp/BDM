package com.kmvc.jeesite.modules.codesetmrg.web;

import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import com.kmvc.jeesite.modules.codesetmrg.service.*;
import com.kmvc.jeesite.modules.syncPortal.vo.AjaxResult;
import com.kmvc.jeesite.modules.sys.entity.TSystem;
import com.kmvc.jeesite.modules.sys.service.TSystemService;
import com.thinkgem.jeesite.common.web.*;
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import com.kmvc.jeesite.modules.codesetmrg.service.*;
import javax.servlet.http.*;
import org.springframework.ui.*;
import com.thinkgem.jeesite.common.utils.*;
import com.thinkgem.jeesite.common.persistence.*;
import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import org.springframework.web.servlet.mvc.support.*;
import com.thinkgem.jeesite.common.config.*;
import com.kmvc.jeesite.modules.syncPortal.vo.*;
import org.springframework.web.bind.annotation.*;
import org.apache.shiro.authz.annotation.*;
import java.util.*;

@Controller
@RequestMapping({ "${adminPath}/codesetmrg/pedningCodeItem" })
public class PendingCodeItemController extends BaseController
{
    @Autowired
    private TSystemService tSystemService;
    @Autowired
    private PendingCodeItemService pendingCodeItemService;
    @Autowired
    private CodeSetDataService codeSetDataService;
    @Autowired
    private DataSetCategoryService dataSetCategoryService;
    @Autowired
    private CodeSetService codeSetService;
    @Autowired
    private PendingCodeSetService pendingCodeSetService;

    @ModelAttribute
    public PendingCodeItem get(@RequestParam(required = false) final String id) {
        PendingCodeItem entity = null;
        if (StringUtils.isNotBlank((CharSequence)id)) {
            entity = this.pendingCodeItemService.get(id);
        }
        if (entity == null) {
            entity = new PendingCodeItem();
        }
        return entity;
    }

    @RequestMapping({ "list", "" })
    public String list(PendingCodeSet pendingCodeSet, final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        final String origin = request.getParameter("origin");
        if ("taskList".equals(origin)) {
            model.addAttribute("origin", (Object)origin);
        }
        final PropertiesLoader propLoader = new PropertiesLoader(new String[] { "jeesite.properties" });
        final String mdm_mode = propLoader.getProperty("mdm_mode");
        final boolean mode = Boolean.valueOf(mdm_mode);
        if (pendingCodeSet.getId() == null) {
            if (!mode) {
                return "modules/codesetmrg/codeSetDataListSimple";
            }
            return "modules/codesetmrg/codeSetDataList";
        }
        else {
            final String year = request.getParameter("year");
            final String itemCode = request.getParameter("itemCode");
            final String itemName = request.getParameter("itemName");
            final List<DataSetCategory> categorys = (List<DataSetCategory>)this.dataSetCategoryService.findAllList(new DataSetCategory());
            model.addAttribute("categorys", (Object)categorys);
            if (mode) {
                final PendingCodeItem pendingCodeItem = new PendingCodeItem();
                if (StringUtils.isNotBlank((CharSequence)year)) {
                    pendingCodeItem.setYear(Integer.valueOf(year));
                    model.addAttribute("year", (Object)year);
                }
                if (StringUtils.isNotBlank((CharSequence)itemCode)) {
                    pendingCodeItem.setItemCode(itemCode);
                    model.addAttribute("itemCode", (Object)itemCode);
                }
                if (StringUtils.isNotBlank((CharSequence)itemName)) {
                    pendingCodeItem.setItemName(itemName);
                    model.addAttribute("itemName", (Object)itemName);
                }
                if (pendingCodeSet.getOperation() != null && pendingCodeSet.getOperation() != 10) {
                    pendingCodeSet = this.pendingCodeSetService.get(pendingCodeSet.getId());
                    model.addAttribute("codeSet", (Object)pendingCodeSet);
                    pendingCodeItem.setCodeSetNo(pendingCodeSet.getCodeSetNo());
                }
                else {
                    final CodeSet codeSet = this.codeSetService.get(pendingCodeSet.getId());
                    if (codeSet != null) {
                        codeSet.setOperation(pendingCodeSet.getOperation());
                        codeSet.setProcessStatus(pendingCodeSet.getProcessStatus());
                        if (codeSet.getCodeSetNo() != null) {
                            pendingCodeItem.setCodeSetNo(codeSet.getCodeSetNo());
                        }
                    }
                    model.addAttribute("codeSet", (Object)codeSet);
                }
                final List<TSystem> systems = (List<TSystem>)this.tSystemService.findAllList();
                model.addAttribute("systems", (Object)systems);
                final Page<PendingCodeItem> pendingCodeItems = (Page<PendingCodeItem>)this.pendingCodeItemService.findPage(new Page(request, response), pendingCodeItem);
                model.addAttribute("page", (Object)pendingCodeItems);
                model.addAttribute("size", (Object)pendingCodeItems.getList().size());
                return "modules/codesetmrg/codeSetDataList";
            }
            final CodeSet codeSet2 = this.codeSetService.get(pendingCodeSet.getId());
            codeSet2.setOperation(pendingCodeSet.getOperation());
            codeSet2.setProcessStatus(pendingCodeSet.getProcessStatus());
            model.addAttribute("codeSet", (Object)codeSet2);
            final CodeSetData codeSetData = new CodeSetData();
            codeSetData.setCodeSetId(codeSet2.getId());
            codeSetData.setStatus(Integer.valueOf(2));
            if (StringUtils.isNotBlank((CharSequence)year)) {
                codeSetData.setYear(Integer.valueOf(year));
                model.addAttribute("year", (Object)year);
            }
            if (StringUtils.isNotBlank((CharSequence)itemCode)) {
                codeSetData.setItemCode(itemCode);
                model.addAttribute("itemCode", (Object)itemCode);
            }
            if (StringUtils.isNotBlank((CharSequence)itemName)) {
                codeSetData.setItemName(itemName);
                model.addAttribute("itemName", (Object)itemName);
            }
            final Page<CodeSetData> codeSetDatas = (Page<CodeSetData>)this.codeSetDataService.findPage(new Page(request, response), codeSetData);
            model.addAttribute("page", (Object)codeSetDatas);
            model.addAttribute("size", (Object)codeSetDatas.getList().size());
            return "modules/codesetmrg/codeSetDataListSimple";
        }
    }

    @RequestMapping({ "save" })
    public String savePendingCodeItem(final PendingCodeItem pendingCodeItem, final HttpServletRequest request, final HttpServletResponse response, final Model model, final RedirectAttributes redirectAttributes) {
        this.pendingCodeItemService.save(pendingCodeItem);
        this.addMessage(redirectAttributes, new String[] { "\u4fdd\u5b58\u4ee3\u7801\u96c6\u6570\u636e\u6210\u529f" });
        final CodeSet codeSet = new CodeSet();
        codeSet.setId(pendingCodeItem.getCodeSetId());
        codeSet.setOperation(pendingCodeItem.getCodeSetOperation());
        codeSet.setProcessStatus(pendingCodeItem.getCodeSetProcessStatus());
        model.addAttribute("codeSet", (Object)codeSet);
        return "redirect:" + Global.getAdminPath() + "/codesetmrg/pedningCodeItem/list?id=" + pendingCodeItem.getCodeSetId() + "&operation=" + pendingCodeItem.getCodeSetOperation() + "&processStatus=" + pendingCodeItem.getCodeSetProcessStatus();
    }

    @RequestMapping({ "delete" })
    @ResponseBody
    public AjaxResult delete(final PendingCodeItem pendingCodeItem, final RedirectAttributes redirectAttributes) {
        this.pendingCodeItemService.delete(pendingCodeItem);
        final AjaxResult aj = new AjaxResult();
        aj.setData(pendingCodeItem.getCodeSetId());
        return aj;
    }

    @RequestMapping({ "batchDelete" })
    @ResponseBody
    public boolean batchDelete(final PendingCodeItem pendingCodeItem, final RedirectAttributes redirectAttributes) {
        this.pendingCodeItemService.batchDelete(pendingCodeItem);
        return true;
    }

    @RequiresPermissions({ "codesetmrg:codesetdata:edit" })
    @RequestMapping({ "setSystem" })
    @ResponseBody
    public PendingCodeItem setSystem(final PendingCodeItem pendingCodeItem, final HttpServletRequest request, final HttpServletResponse response, final RedirectAttributes redirectAttributes) {
        this.pendingCodeItemService.setSystem(pendingCodeItem);
        this.addMessage(redirectAttributes, new String[] { "\u8bbe\u7f6e\u6210\u529f" });
        return pendingCodeItem;
    }

    @ResponseBody
    @RequestMapping({ "parentCodeTreeData", "" })
    public List<Map<String, Object>> PubTreeData(@RequestParam(required = false) final String codeSetNo, final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        final String itemId = request.getParameter("itemId");
        final List<Map<String, Object>> mapList = (List<Map<String, Object>>)this.pendingCodeItemService.getCodeToTreeData(codeSetNo, itemId);
        model.addAttribute("mapList", (Object)mapList);
        return mapList;
    }

    @ResponseBody
    @RequestMapping({ "findItemTree", "" })
    public List<Map<String, Object>> findItemTree(final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        final String codeSetNo = request.getParameter("codeSetNo");
        final String itemId = request.getParameter("itemId");
        final List<Map<String, Object>> mapList = (List<Map<String, Object>>)this.pendingCodeItemService.findItemTree(codeSetNo, itemId);
        model.addAttribute("mapList", (Object)mapList);
        return mapList;
    }
}
