package com.kmvc.jeesite.modules.codesetmrg.web;

import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import com.kmvc.jeesite.modules.codesetmrg.service.*;
import com.kmvc.jeesite.modules.common.Constant;
import com.kmvc.jeesite.modules.sys.entity.TSystem;
import com.kmvc.jeesite.modules.sys.service.TSystemService;
import com.thinkgem.jeesite.common.web.*;
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import com.kmvc.jeesite.modules.codesetmrg.service.*;
import com.thinkgem.jeesite.modules.sys.service.*;
import javax.servlet.http.*;
import org.springframework.ui.*;
import com.thinkgem.jeesite.common.persistence.*;
import java.util.*;
import com.google.common.collect.*;
import org.springframework.web.bind.annotation.*;
import com.thinkgem.jeesite.common.utils.*;
import com.kmvc.jeesite.modules.common.*;
import com.thinkgem.jeesite.modules.sys.utils.*;
import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import com.thinkgem.jeesite.modules.sys.entity.*;
import org.springframework.web.servlet.mvc.support.*;
import org.apache.shiro.authz.annotation.*;
import com.thinkgem.jeesite.common.config.*;

@Controller
@RequestMapping({ "${adminPath}/codesetmrg/codeSet" })
public class CodeSetController extends BaseController
{
    @Autowired
    private CodeSetService codeSetService;
    @Autowired
    private PendingCodeSetService pendingCodeSetService;
    @Autowired
    private PendingCodeItemService pendingCodeItemService;
    @Autowired
    private CodeSetDataService codeSetDataService;
    @Autowired
    private DataSetCategoryService dataSetCategoryService;
    @Autowired
    private TSystemService tSystemService;
    @Autowired
    private PendingCodesetSystemService pendingCodesetSystemService;
    @Autowired
    private AreaService areaService;

    @ModelAttribute
    public CodeSet get(@RequestParam(required = false) final String id) {
        CodeSet entity = null;
        if (StringUtils.isNotBlank((CharSequence)id)) {
            entity = this.codeSetService.get(id);
        }
        if (entity == null) {
            entity = new CodeSet();
            final User user = entity.getCurrentUser();
            if (user != null && user.getOfficeList().size() > 0) {
                for (final Office office : user.getOfficeList()) {
                    entity.setOfficeIds(office.getId());
                    entity.setOfficeName(office.getName());
                }
            }
        }
        return entity;
    }

    @RequestMapping({ "list", "" })
    public String list(final CodeSet codeSet, final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        final Page<CodeSet> page = (Page<CodeSet>)this.codeSetService.findPage(new Page(request, response), codeSet);
        model.addAttribute("page", (Object)page);
        final List<DataSetCategory> categorys = (List<DataSetCategory>)this.dataSetCategoryService.findAllList(new DataSetCategory());
        model.addAttribute("categorys", (Object)categorys);
        return "modules/codesetmrg/codeSetList";
    }

    @ResponseBody
    @RequestMapping({ "treeData" })
    public List<Map<String, Object>> treeData(@RequestParam(required = false) final String extId, @RequestParam(required = false) final String type, @RequestParam(required = false) final Long grade, @RequestParam(required = false) final Boolean isAll, final HttpServletResponse response) {
        final List<Map<String, Object>> mapList = Lists.newArrayList();
        final List<CodeSet> list = (List<CodeSet>)this.codeSetService.findList(new CodeSet());
        for (int i = 0; i < list.size(); ++i) {
            final CodeSet e = list.get(i);
            Integer operation = e.getOperation();
            if (operation == null) {
                operation = 10;
            }
            Integer processStatus = e.getProcessStatus();
            if (processStatus == null) {
                processStatus = 10;
            }
            final Map<String, Object> map = Maps.newHashMap();
            map.put("id", e.getId());
            map.put("pId", "");
            map.put("operation", operation);
            map.put("processStatus", processStatus);
            map.put("name", e.getCodeSetName());
            mapList.add(map);
        }
        return mapList;
    }

    @RequestMapping({ "form" })
    public String form(final CodeSet codeSet, final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        final String year = request.getParameter("year");
        final String itemCode = request.getParameter("itemCode");
        final String itemName = request.getParameter("itemName");
        final PropertiesLoader propLoader = new PropertiesLoader(new String[] { "jeesite.properties" });
        final String mdm_mode = propLoader.getProperty("mdm_mode");
        final List<DataSetCategory> categorys = (List<DataSetCategory>)this.dataSetCategoryService.findAllList(new DataSetCategory());
        model.addAttribute("categorys", (Object)categorys);
        final boolean mode = Boolean.parseBoolean(mdm_mode);
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
            if (codeSet.getOperation() != null && codeSet.getOperation() != 10) {
                final String codeSetId = codeSet.getId();
                final PendingCodeSet pendingCodeSet = this.pendingCodeSetService.get(codeSetId);
                model.addAttribute("codeSet", (Object)pendingCodeSet);
                pendingCodeItem.setCodeSetNo(pendingCodeSet.getCodeSetNo());
                final List<String> systemIds = (List<String>)this.pendingCodesetSystemService.findByCodesetId(pendingCodeSet.getId());
                model.addAttribute("systemIds", (Object)systemIds);
                final Integer coreFlag = pendingCodeSet.getCoreFlag();
                if (coreFlag != null && coreFlag == Constant.CORE_FLAG_TRUE) {
                    final User curUser = UserUtils.getUser();
                    final Office office = curUser.getOfficeList().get(0);
                    final Area area = office.getArea();
                    final Area curArea = (Area)this.areaService.get(area.getId());
                    if ("3".equals(curArea.getType()) || "4".equals(curArea.getType())) {
                        model.addAttribute("cityCountyDistAndCore", (Object)true);
                    }
                }
            }
            else {
                model.addAttribute("codeSet", (Object)codeSet);
                if (codeSet.getCodeSetNo() != null) {
                    pendingCodeItem.setCodeSetNo(codeSet.getCodeSetNo());
                }
            }
            final List<TSystem> systems = (List<TSystem>)this.tSystemService.findAllList();
            model.addAttribute("systems", (Object)systems);
            final Page<PendingCodeItem> pendingCodeItems = (Page<PendingCodeItem>)this.pendingCodeItemService.findPage(new Page(request, response), pendingCodeItem);
            model.addAttribute("page", (Object)pendingCodeItems);
            return "modules/codesetmrg/codeSetForm";
        }
        model.addAttribute("codeSet", (Object)codeSet);
        final CodeSetData codeSetData = new CodeSetData();
        codeSetData.setCodeSetId(codeSet.getId());
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
        return "modules/codesetmrg/simpleCodeSetForm";
    }

    @RequiresPermissions({ "codesetmrg:codeSet:edit" })
    @RequestMapping({ "save" })
    @ResponseBody
    public CodeSet save(final CodeSet codeSet, final HttpServletRequest request, final HttpServletResponse response, final Model model, final RedirectAttributes redirectAttributes) {
        this.codeSetService.save(codeSet);
        return codeSet;
    }

    @RequiresPermissions({ "codesetmrg:codeSet:edit" })
    @RequestMapping({ "delete" })
    public String delete(final CodeSet codeSet, final RedirectAttributes redirectAttributes) {
        this.codeSetService.delete(codeSet);
        this.addMessage(redirectAttributes, new String[] { "\u5220\u9664\u4ee3\u7801\u96c6\u6210\u529f" });
        return "redirect:" + Global.getAdminPath() + "/codesetmrg/codeSet/?repage";
    }

    @RequestMapping({ "isCodeSetNoExist" })
    @ResponseBody
    public boolean isCodeSetNoExist(final CodeSet codeSet) {
        return this.codeSetService.isCodeSetNoExist(codeSet);
    }
}
