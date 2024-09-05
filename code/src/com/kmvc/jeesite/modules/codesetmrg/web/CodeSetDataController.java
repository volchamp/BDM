package com.kmvc.jeesite.modules.codesetmrg.web;

import com.kmvc.jeesite.modules.businessdatamrg.service.SendingCodeSetService;
import com.kmvc.jeesite.modules.codesetmrg.dao.SplitMergeDao;
import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import com.kmvc.jeesite.modules.codesetmrg.service.*;
import com.kmvc.jeesite.modules.common.Util;
import com.kmvc.jeesite.modules.common.exception.BizException;
import com.kmvc.jeesite.modules.sys.entity.TSystem;
import com.kmvc.jeesite.modules.sys.service.TSystemService;
import com.thinkgem.jeesite.common.web.*;
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import com.kmvc.jeesite.modules.businessdatamrg.service.*;
import com.kmvc.jeesite.modules.codesetmrg.dao.*;
import javax.servlet.http.*;
import org.springframework.ui.*;
import com.thinkgem.jeesite.common.persistence.*;
import org.springframework.web.servlet.mvc.support.*;
import org.apache.shiro.authz.annotation.*;
import com.kmvc.jeesite.modules.common.exception.*;
import org.springframework.web.bind.annotation.*;
import com.google.common.collect.*;
import java.text.*;
import org.springframework.web.multipart.*;
import com.thinkgem.jeesite.common.utils.excel.*;
import com.kmvc.jeesite.modules.codesetmrg.service.*;
import com.thinkgem.jeesite.common.utils.*;
import com.kmvc.jeesite.modules.common.*;
import java.math.*;
import com.thinkgem.jeesite.common.beanvalidator.*;
import javax.validation.*;
import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import java.util.*;

@Controller
@RequestMapping({ "${adminPath}/codesetmrg/codeSetData" })
public class CodeSetDataController extends BaseController
{
    @Autowired
    private CodeSetDataService codeSetDataService;
    @Autowired
    private SendingCodeSetService sendingCodeSetService;
    @Autowired
    private TSystemService tSystemService;
    @Autowired
    private PendingCodeItemService pendingCodeItemService;
    @Autowired
    private CodeSetCacheMappingService codeSetCacheMappingService;
    @Autowired
    private DistriRelationshipService distriRelationshipService;
    @Autowired
    private PendingCodeSetService pendingCodeSetService;
    @Autowired
    private SplitMergeDao splitMergeDao;

    @ModelAttribute
    public CodeSetData get(@RequestParam(required = false) final String id) {
        CodeSetData entity = null;
        if (StringUtils.isNotBlank((CharSequence)id)) {
            entity = this.codeSetDataService.get(id);
        }
        if (entity == null) {
            entity = new CodeSetData();
        }
        return entity;
    }

    @RequestMapping({ "list", "" })
    public String list(final CodeSetData codeSetData, final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        final Page<CodeSetData> page = (Page<CodeSetData>)this.codeSetDataService.findPage(new Page(request, response), codeSetData);
        model.addAttribute("page", (Object)page);
        return "modules/codesetmrg/codeSetDataList";
    }

    @RequestMapping({ "form" })
    public String form(final CodeSetData codeSetData, final Model model, final HttpServletRequest request) {
        final PropertiesLoader propLoader = new PropertiesLoader(new String[] { "jeesite.properties" });
        final String mdm_mode = propLoader.getProperty("mdm_mode");
        final boolean mode = Boolean.valueOf(mdm_mode);
        if (mode) {
            final List<TSystem> systems = (List<TSystem>)this.tSystemService.findAllList();
            model.addAttribute("systems", (Object)systems);
            final String codeSetOperation = request.getParameter("codeSetOperation");
            final String codeSetProcessStatus = request.getParameter("codeSetProcessStatus");
            if (codeSetData.getOperation() != null && codeSetData.getOperation() != -1) {
                final PendingCodeItem pendingCodeItem = this.pendingCodeItemService.get(codeSetData.getId());
                final List<String> systemIds = (List<String>)this.codeSetCacheMappingService.findByItemId(pendingCodeItem.getId());
                pendingCodeItem.setSystemIds((List)systemIds);
                pendingCodeItem.setCodeSetNo(codeSetData.getCodeSetNo());
                model.addAttribute("codeSetData", (Object)pendingCodeItem);
                model.addAttribute("systemIds", (Object)systemIds);
            }
            else {
                final List<String> systemIds2 = (List<String>)this.distriRelationshipService.findByItemId(codeSetData.getId());
                codeSetData.setSystemIds((List)systemIds2);
                model.addAttribute("systemIds", (Object)systemIds2);
                model.addAttribute("codeSetData", (Object)codeSetData);
            }
            model.addAttribute("codeSetOperation", (Object)codeSetOperation);
            model.addAttribute("codeSetProcessStatus", (Object)codeSetProcessStatus);
            model.addAttribute("codeSetNo", (Object)codeSetData.getCodeSetNo());
            final SplitMerge smCriteria = new SplitMerge();
            smCriteria.setOldItemId(codeSetData.getId());
            smCriteria.setNewItemId(codeSetData.getId());
            final List<SplitMerge> smList = (List<SplitMerge>)this.splitMergeDao.findListByItemId(smCriteria);
            model.addAttribute("smSize", (Object)smList.size());
            return "modules/codesetmrg/codeSetDataForm";
        }
        model.addAttribute("codeSetData", (Object)codeSetData);
        return "modules/codesetmrg/simpleCodeSetDataForm";
    }

    @RequiresPermissions({ "codesetmrg:codesetdata:edit" })
    @RequestMapping({ "save" })
    @ResponseBody
    public CodeSetData save(final CodeSetData codeSetData, final Model model, final RedirectAttributes redirectAttributes) {
        this.codeSetDataService.save(codeSetData);
        return codeSetData;
    }

    @RequiresPermissions({ "codesetmrg:codesetdata:edit" })
    @RequestMapping({ "delete" })
    @ResponseBody
    public boolean delete(final String id, final RedirectAttributes redirectAttributes) {
        BizException.throwWhenNull((Object)id, "\u4e3b\u952eID\u4e0d\u80fd\u4e3a\u7a7a");
        this.codeSetDataService.updateStatusById(id, Integer.valueOf(0));
        return true;
    }

    @RequestMapping({ "codeSetHistoryImport" })
    public String codeSetHistoryImport(final CodeSetData codeSetData, final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        codeSetData.setStatus(Integer.valueOf(0));
        final Page<CodeSetData> page = (Page<CodeSetData>)this.codeSetDataService.findHistory(new Page(request, response), codeSetData);
        model.addAttribute("page", (Object)page);
        model.addAttribute("codeSetData", (Object)codeSetData);
        return "modules/codesetmrg/codeSetDataHistoryList";
    }

    @RequestMapping({ "codeSetHistoryImportSave" })
    @ResponseBody
    public boolean codeSetHistoryImportSave(final CodeSetData codeSetData, final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        this.pendingCodeItemService.codeSetHistoryImportSave(codeSetData);
        return true;
    }

    @RequestMapping({ "carryOver" })
    @ResponseBody
    public boolean carryOver(final CodeSetData codeSetData) {
        return this.pendingCodeItemService.carryOver(codeSetData);
    }

    @RequiresPermissions({ "codesetmrg:codesetdata:export" })
    @RequestMapping(value = { "export" }, method = { RequestMethod.POST })
    public String exportFile(final CodeSetData codeSetData, final HttpServletRequest request, final HttpServletResponse response, final RedirectAttributes redirectAttributes) {
        final String str = request.getParameter("operation");
        if (str != null && str.length() > 0) {
            final Integer operation = Integer.valueOf(str);
            codeSetData.setOperation(operation);
        }
        try {
            codeSetData.setStatus(Integer.valueOf(2));
            codeSetData.setCodeSetId(codeSetData.getId());
            final String fileName = "\u4ee3\u7801\u96c6\u6570\u636e" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            final Page<CodeSetData> page = (Page<CodeSetData>)this.codeSetDataService.findPage(new Page(request, response, -1), codeSetData);
            if (page.getList().size() > 0) {
                new ExportExcel("\u4ee3\u7801\u96c6\u6570\u636e", (Class)CodeSetData.class).setDataList(page.getList()).write(response, fileName).dispose();
            }
            else {
                this.addMessage(redirectAttributes, new String[] { "\u62b1\u6b49\uff0c\u5bfc\u51fa\u5931\u8d25\uff0c\u6ca1\u6709\u7b26\u5408\u6761\u4ef6\u7684\u4ee3\u7801\u96c6\u6570\u636e\u53ef\u5bfc\u51fa\uff01" });
            }
            return "redirect:" + this.adminPath + "/codesetmrg/pedningCodeItem/list?id=" + codeSetData.getCodeSetId() + "&operation=" + codeSetData.getOperation() + "&processStatus=" + codeSetData.getProcessStatus();
        }
        catch (Exception e) {
            this.addMessage(redirectAttributes, new String[] { "\u5bfc\u51fa\u4ee3\u7801\u6570\u636e\u5931\u8d25\uff01\u5931\u8d25\u4fe1\u606f\uff1a" + e.getMessage() });
            return "redirect:" + this.adminPath + "/codesetmrg/codeSet/form?id=" + codeSetData.getCodeSetId() + "&operation=" + codeSetData.getOperation() + "&codeSetNo=" + codeSetData.getCodeSetNo();
        }
    }

    @RequiresPermissions({ "codesetmrg:codesetdata:edit" })
    @RequestMapping({ "import/template" })
    public String importFileTemplate(final HttpServletResponse response, final RedirectAttributes redirectAttributes) {
        try {
            final String fileName = "\u4ee3\u7801\u96c6\u6570\u636e\u5bfc\u5165\u6a21\u677f.xlsx";
            final List<CodeSetData> list = Lists.newArrayList();
            this.listAddObj(list);
            new ExportExcel("\u4ee3\u7801\u96c6\u6570\u636e", (Class)CodeSetData.class, 2, new int[0]).setDataList((List)list).write(response, fileName).dispose();
            return null;
        }
        catch (Exception e) {
            this.addMessage(redirectAttributes, new String[] { "\u5bfc\u5165\u6a21\u677f\u4e0b\u8f7d\u5931\u8d25\uff01\u5931\u8d25\u4fe1\u606f\uff1a" + e.getMessage() });
            return null;
        }
    }

    private void listAddObj(final List<CodeSetData> list) throws ParseException {
        final CodeSetData csData = new CodeSetData();
        csData.setItemCode("dmjbm1(\u6e29\u99a8\u63d0\u793a:\u672c\u884c\u6570\u636e\u4e3a\u6837\u4f8b\uff0c\u672c\u884c\u4e5f\u53c2\u4e0e\u5bfc\u5165)");
        csData.setParentItemCode("sjdmjbm1(\u6e29\u99a8\u63d0\u793a:\u672c\u884c\u6570\u636e\u4e3a\u6837\u4f8b)");
        csData.setItemName("\u4ee3\u7801\u96c6\u540d\u79f0(\u6e29\u99a8\u63d0\u793a:\u672c\u884c\u6570\u636e\u4e3a\u6837\u4f8b)");
        csData.setVersion(Integer.valueOf(1));
        csData.setYear(Integer.valueOf(Calendar.getInstance().get(1)));
        csData.setValidStartDate(new Date());
        csData.setValidEndDate(new Date());
        list.add(csData);
    }

    @RequiresPermissions({ "codesetmrg:codesetdata:edit" })
    @RequestMapping(value = { "import" }, method = { RequestMethod.POST })
    public String importFile(final CodeSetData codeSetData, final HttpServletRequest request, final MultipartFile file, final RedirectAttributes redirectAttributes) {
        try {
            final PropertiesLoader propLoader = new PropertiesLoader(new String[] { "jeesite.properties" });
            final String mdm_mode = propLoader.getProperty("mdm_mode");
            final boolean mode = Boolean.valueOf(mdm_mode);
            int successNum = 0;
            int failureNum = 0;
            final StringBuilder failureMsg = new StringBuilder();
            final ImportExcel ei = new ImportExcel(file, 1, 0);
            final List<CodeSetData> list = (List<CodeSetData>)ei.getDataList((Class)CodeSetData.class, new int[0]);
            if (mode) {
                if (codeSetData.getOperation() != null && codeSetData.getOperation() == 10) {
                    final CodeSetService codeSetService = (CodeSetService)SpringContextHolder.getBean("codeSetService");
                    final CodeSet c = codeSetService.get(codeSetData.getCodeSetId());
                    c.setProcessStatus(Integer.valueOf(10));
                    final PendingCodeSetService pendingCodeSetService = (PendingCodeSetService)SpringContextHolder.getBean("pendingCodeSetService");
                    final PendingCodeSet pendingCodeSet = pendingCodeSetService.toPendingCodeSet(c);
                    pendingCodeSetService.save(pendingCodeSet);
                    codeSetData.setCodeSetId(pendingCodeSet.getId());
                    codeSetData.setOperation(Integer.valueOf(1));
                }
                for (final CodeSetData csd : list) {
                    final String itemCode = csd.getItemCode();
                    try {
                        if (!StringUtils.isNotBlank((CharSequence)itemCode) || !StringUtils.isNotBlank((CharSequence)csd.getItemName())) {
                            continue;
                        }
                        if (Util.isScientific(itemCode)) {
                            final BigDecimal b = new BigDecimal(itemCode);
                            csd.setItemCode(b.toPlainString());
                        }
                        final PendingCodeItem pendingCodeItem = this.pendingCodeItemService.toPendingCodeItem(csd);
                        BeanValidators.validateWithException(this.validator, (Object)csd, new Class[0]);
                        pendingCodeItem.setCodeSetId(codeSetData.getCodeSetId());
                        pendingCodeItem.setCodeSetNo(codeSetData.getCodeSetNo());
                        this.pendingCodeItemService.save(pendingCodeItem);
                        ++successNum;
                    }
                    catch (ConstraintViolationException ex) {
                        failureMsg.append(" \u5bfc\u5165\u5931\u8d25\uff1a");
                        final List<String> messageList = (List<String>)BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
                        for (final String message : messageList) {
                            failureMsg.append(message + "; ");
                            ++failureNum;
                        }
                    }
                    catch (Exception ex2) {
                        failureMsg.append("\u5bfc\u5165\u5931\u8d25\uff1a" + ex2.getMessage());
                    }
                }
            }
            else {
                for (final CodeSetData csd : list) {
                    try {
                        if (!StringUtils.isNotBlank((CharSequence)csd.getItemCode()) || !StringUtils.isNotBlank((CharSequence)csd.getItemName())) {
                            continue;
                        }
                        BeanValidators.validateWithException(this.validator, (Object)csd, new Class[0]);
                        csd.setCodeSetId(codeSetData.getCodeSetId());
                        csd.setCodeSetNo(codeSetData.getCodeSetNo());
                        this.codeSetDataService.save(csd);
                        ++successNum;
                    }
                    catch (ConstraintViolationException ex3) {
                        failureMsg.append(" \u5bfc\u5165\u5931\u8d25\uff1a");
                        final List<String> messageList2 = (List<String>)BeanValidators.extractPropertyAndMessageAsList(ex3, ": ");
                        for (final String message2 : messageList2) {
                            failureMsg.append(message2 + "; ");
                            ++failureNum;
                        }
                    }
                    catch (Exception ex4) {
                        failureMsg.append("\u5bfc\u5165\u5931\u8d25\uff1a" + ex4.getMessage());
                    }
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "\uff0c\u5931\u8d25 " + failureNum + " \u6761\u4ee3\u7801\u96c6\uff0c\u5bfc\u5165\u4fe1\u606f\u5982\u4e0b\uff1a");
            }
            this.addMessage(redirectAttributes, new String[] { "\u5df2\u6210\u529f\u5bfc\u5165 " + successNum + " \u6761\u4ee3\u7801\u96c6" + (Object)failureMsg });
        }
        catch (Exception e) {
            e.printStackTrace();
            this.addMessage(redirectAttributes, new String[] { "\u5bfc\u5165\u4ee3\u7801\u96c6\u5931\u8d25\uff01\u5931\u8d25\u4fe1\u606f\uff1a" + e.getMessage() });
        }
        return "redirect:" + this.adminPath + "/codesetmrg/pedningCodeItem/list?id=" + codeSetData.getCodeSetId() + "&operation=" + codeSetData.getOperation();
    }

    @RequestMapping({ "sendingSystemCodeSet" })
    @ResponseBody
    public boolean sendingSystemCodeSet(final CodeSetData codeSetData) {
        this.sendingCodeSetService.sendingData(codeSetData);
        return true;
    }

    @RequestMapping({ "index" })
    public String index(final Model model, final PendingCodeItem pendingCodeItem, final HttpServletRequest request, final HttpServletResponse response) {
        final String processInstID = request.getParameter("processInstID");
        if (StringUtils.isNotBlank((CharSequence)processInstID)) {
            final PendingCodeSet pcsByProcInstId = this.pendingCodeSetService.getByProcInstId(processInstID);
            if (pcsByProcInstId != null) {
                model.addAttribute("codeSet", (Object)pcsByProcInstId);
            }
        }
        if (!Util.isMdmModeComplex(true)) {
            return "modules/codesetmrg/codeSetDataIndexSimple";
        }
        if (pendingCodeItem.getCodeSetId() != null) {
            final PendingCodeSet pendingCodeSet = this.pendingCodeSetService.get(pendingCodeItem.getCodeSetId());
            model.addAttribute("codeSet", (Object)pendingCodeSet);
        }
        final String origin = request.getParameter("origin");
        if ("taskList".equals(origin)) {
            model.addAttribute("origin", (Object)origin);
        }
        return "modules/codesetmrg/codeSetDataIndex";
    }
}
