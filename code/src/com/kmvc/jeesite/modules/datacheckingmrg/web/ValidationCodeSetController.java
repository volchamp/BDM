package com.kmvc.jeesite.modules.datacheckingmrg.web;

import com.kmvc.jeesite.modules.datacheckingmrg.entity.ValidationCodeSet;
import com.kmvc.jeesite.modules.datacheckingmrg.entity.ValidationDetail;
import com.kmvc.jeesite.modules.datacheckingmrg.service.ValidationCodeSetService;
import com.kmvc.jeesite.modules.datacheckingmrg.service.ValidationDetailService;
import com.kmvc.jeesite.modules.sys.entity.TSystem;
import com.kmvc.jeesite.modules.sys.service.TSystemService;
import com.thinkgem.jeesite.common.web.*;
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import com.kmvc.jeesite.modules.datacheckingmrg.service.*;

import javax.servlet.http.*;
import org.springframework.ui.*;
import com.thinkgem.jeesite.common.persistence.*;
import java.util.*;

import com.kmvc.jeesite.modules.datacheckingmrg.entity.*;
import org.springframework.web.servlet.mvc.support.*;
import com.thinkgem.jeesite.common.utils.*;
import com.thinkgem.jeesite.common.utils.excel.*;
import org.springframework.web.bind.annotation.*;
import com.thinkgem.jeesite.common.config.*;

@Controller
@RequestMapping({ "${adminPath}/datacheckingmrg/validationCodeSet" })
public class ValidationCodeSetController extends BaseController
{
    @Autowired
    private ValidationCodeSetService validationCodeSetService;
    @Autowired
    private ValidationDetailService validationDetailService;
    @Autowired
    private TSystemService systemService;

    @ModelAttribute
    public ValidationCodeSet get(@RequestParam(required = false) final String id) {
        ValidationCodeSet entity = null;
        if (StringUtils.isNotBlank((CharSequence)id)) {
            entity = this.validationCodeSetService.get(id);
        }
        if (entity == null) {
            entity = new ValidationCodeSet();
        }
        return entity;
    }

    @RequestMapping({ "list", "" })
    public String list(final ValidationCodeSet validationCodeSet, final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        final Page<ValidationCodeSet> page = (Page<ValidationCodeSet>)this.validationCodeSetService.findPage(new Page(request, response), validationCodeSet);
        model.addAttribute("page", page);
        final List<TSystem> systemList = this.systemService.findAllList();
        model.addAttribute("systemList", systemList);
        return "modules/datacheckingmrg/validationCodeSetList";
    }

    @RequestMapping({ "form" })
    public String form(final ValidationCodeSet validationCodeSet, final Model model, final HttpServletRequest request, final HttpServletResponse response, final ValidationDetail validationDetail) {
        model.addAttribute("validationCodeSet",validationCodeSet);
        validationDetail.setRecordId(validationCodeSet.getId());
        final Page<ValidationDetail> page = (Page<ValidationDetail>)this.validationDetailService.findPage(new Page(request, response), validationDetail);
        model.addAttribute("origin",request.getParameter("origin"));
        model.addAttribute("page", page);
        return "modules/datacheckingmrg/validationCodeSetForm";
    }

    @RequestMapping(value = { "export" }, method = { RequestMethod.POST })
    public String exportFile(final HttpServletRequest request, final HttpServletResponse response, final RedirectAttributes redirectAttributes) {
        try {
            final String fileName = "\u5f02\u5e38\u4ee3\u7801\u96c6\u6570\u636e" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            final String recordId = request.getParameter("recordId");
            final String taskHandlerId = request.getParameter("taskHandlerId2");
            final ValidationDetail valDet = new ValidationDetail();
            valDet.setRecordId(recordId);
            final Page<ValidationDetail> page = (Page<ValidationDetail>)this.validationDetailService.findPage(new Page(request, response), valDet);
            final List<ValidationDetail> list = (List<ValidationDetail>)page.getList();
            for (int i = 0; i < list.size(); ++i) {
                list.get(i).setNum(Integer.valueOf(i + 1));
            }
            new ExportExcel("\u5f02\u5e38\u4ee3\u7801\u96c6\u6570\u636e", (Class)ValidationDetail.class).setDataList((List)list).write(response, fileName).dispose();
            return "redirect:" + this.adminPath + "/datacheckingmrg/validationCodeSet/form?id=" + recordId + "&taskHandlerId=" + taskHandlerId;
        }
        catch (Exception e) {
            this.addMessage(redirectAttributes, new String[] { "\u5bfc\u51fa\u5f02\u5e38\u4ee3\u7801\u96c6\u6570\u636e\u5931\u8d25\uff01\u5931\u8d25\u4fe1\u606f\uff1a" + e.getMessage() });
            return "redirect:" + this.adminPath + "/datacheckingmrg/validationCodeSet/list?repage";
        }
    }

    @RequestMapping(value = { "handle" }, method = { RequestMethod.POST })
    public String handle(final HttpServletRequest request, final HttpServletResponse response, final RedirectAttributes redirectAttributes, final ValidationCodeSet validationCodeSet) {
        final String result = this.validationCodeSetService.handle(validationCodeSet);
        if ("valCsIsNull".equals(result)) {
            this.addMessage(redirectAttributes, new String[] { "\u62b1\u6b49\uff0c\u5904\u7406\u5931\u8d25\uff0c\u6838\u67e5\u4ee3\u7801\u96c6\u76ee\u5f55\u4fe1\u606f\u7f3a\u5931\uff0c\u8bf7\u8054\u7cfb\u7cfb\u7edf\u7ba1\u7406\u5458\uff01" });
        }
        else if ("alreadyHandle".equals(result)) {
            this.addMessage(redirectAttributes, new String[] { "\u62b1\u6b49\uff0c\u5904\u7406\u5931\u8d25\uff0c\u6b64\u6570\u636e\u4e4b\u524d\u5df2\u88ab\u5904\u7406" });
        }
        else if ("finish".equals(result)) {
            this.addMessage(redirectAttributes, new String[] { "\u64cd\u4f5c\u5df2\u5b8c\u6210" });
        }
        if ("taskList".equals(request.getParameter("origin"))) {
            redirectAttributes.addAttribute("taskStatus", (Object)0);
            return "redirect:" + Global.getAdminPath() + "/tesk/task/?repage";
        }
        return "redirect:" + this.adminPath + "/datacheckingmrg/validationCodeSet/list?repage";
    }

    @RequestMapping(value = { "instantVal" }, method = { RequestMethod.POST })
    public String instantVal(final HttpServletRequest request, final HttpServletResponse response, final RedirectAttributes redirectAttributes, final ValidationCodeSet validationCodeSet) {
        final String result = this.validationCodeSetService.instantVal();
        if ("systemListIsBlank".equals(result)) {
            this.addMessage(redirectAttributes, new String[] { "\u62b1\u6b49\uff0c\u60a8\u6ca1\u6709\u53ef\u7528\u4e8e\u6838\u67e5\u7684\u4e1a\u52a1\u7cfb\u7edf\uff01" });
        }
        else if ("finish".equals(result)) {
            this.addMessage(redirectAttributes, new String[] { "\u64cd\u4f5c\u5df2\u5b8c\u6210\uff0c\u4e14\u5df2\u81ea\u52a8\u5237\u65b0\u4e00\u6b21\u6838\u67e5\u7ed3\u679c\u5217\u8868\uff0c\u60a8\u8fd8\u53ef\u4ee5\u7a0d\u540e\u624b\u52a8\u5237\u65b0" });
        }
        return "redirect:" + this.adminPath + "/datacheckingmrg/validationCodeSet/list?repage";
    }
}
