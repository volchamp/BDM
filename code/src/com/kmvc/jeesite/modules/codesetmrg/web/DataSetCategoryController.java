package com.kmvc.jeesite.modules.codesetmrg.web;

import com.kmvc.jeesite.modules.codesetmrg.dao.CodeSetDao;
import com.kmvc.jeesite.modules.codesetmrg.entity.CodeSet;
import com.kmvc.jeesite.modules.codesetmrg.entity.DataSetCategory;
import com.kmvc.jeesite.modules.codesetmrg.service.DataSetCategoryService;
import com.thinkgem.jeesite.common.web.*;
import org.springframework.stereotype.*;
import com.kmvc.jeesite.modules.codesetmrg.service.*;
import org.springframework.beans.factory.annotation.*;
import com.kmvc.jeesite.modules.codesetmrg.dao.*;
import com.thinkgem.jeesite.common.utils.*;
import javax.servlet.http.*;
import org.springframework.ui.*;
import com.thinkgem.jeesite.common.persistence.*;
import org.springframework.web.servlet.mvc.support.*;
import com.thinkgem.jeesite.common.config.*;
import org.apache.shiro.authz.annotation.*;
import com.kmvc.jeesite.modules.codesetmrg.entity.*;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping({ "${adminPath}/codesetmrg/dataSetCategory" })
public class DataSetCategoryController extends BaseController
{
    @Autowired
    private DataSetCategoryService dataSetCategoryService;
    @Autowired
    private CodeSetDao codeSetDao;

    @ModelAttribute
    public DataSetCategory get(@RequestParam(required = false) final String id) {
        DataSetCategory entity = null;
        if (StringUtils.isNotBlank((CharSequence)id)) {
            entity = this.dataSetCategoryService.get(id);
        }
        if (entity == null) {
            entity = new DataSetCategory();
        }
        return entity;
    }

    @RequestMapping({ "list", "" })
    public String list(final DataSetCategory dataSetCategory, final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        final Page<DataSetCategory> page = (Page<DataSetCategory>)this.dataSetCategoryService.findPage(new Page(request, response), dataSetCategory);
        model.addAttribute("page", (Object)page);
        return "modules/codesetmrg/dataSetCategoryList";
    }

    @RequestMapping({ "form" })
    public String form(final DataSetCategory dataSetCategory, final Model model) {
        model.addAttribute("dataSetCategory", (Object)dataSetCategory);
        return "modules/codesetmrg/dataSetCategoryForm";
    }

    @RequiresPermissions({ "codesetmrg:dataSetCategory:edit" })
    @RequestMapping({ "save" })
    public String save(final DataSetCategory dataSetCategory, final Model model, final RedirectAttributes redirectAttributes) {
        if (!this.beanValidator(model, (Object)dataSetCategory, new Class[0])) {
            return this.form(dataSetCategory, model);
        }
        this.dataSetCategoryService.save(dataSetCategory);
        this.addMessage(redirectAttributes, new String[] { "\u4fdd\u5b58\u4ee3\u7801\u96c6\u5206\u7ec4\u6210\u529f" });
        return "redirect:" + Global.getAdminPath() + "/codesetmrg/dataSetCategory/?repage";
    }

    @RequiresPermissions({ "codesetmrg:dataSetCategory:edit" })
    @RequestMapping({ "delete" })
    public String delete(final DataSetCategory dataSetCategory, final RedirectAttributes redirectAttributes) {
        final CodeSet codeSet = new CodeSet();
        codeSet.setCodeGroupId(dataSetCategory.getId());
        final List<CodeSet> codeSetList = (List<CodeSet>)this.codeSetDao.findList(codeSet);
        if (codeSetList.size() == 0) {
            this.dataSetCategoryService.delete(dataSetCategory);
            this.addMessage(redirectAttributes, new String[] { "\u5220\u9664\u4ee3\u7801\u96c6\u5206\u7ec4\u6210\u529f" });
        }
        else {
            this.addMessage(redirectAttributes, new String[] { "\u62b1\u6b49\uff0c\u5220\u9664\u4ee3\u7801\u96c6\u5206\u7ec4\u5931\u8d25\uff0c\u539f\u56e0\uff1a\u8be5\u5206\u7ec4\u8fd8\u5b58\u5728\u4ee3\u7801\u96c6\u76ee\u5f55" });
        }
        return "redirect:" + Global.getAdminPath() + "/codesetmrg/dataSetCategory/?repage";
    }

    @RequestMapping({ "validExistCode" })
    @ResponseBody
    public boolean validExistCode(@RequestParam(required = false) final String id, final String code) {
        return this.dataSetCategoryService.validExistCode(id, code);
    }

    @RequestMapping({ "checkName" })
    @ResponseBody
    public boolean checkName(final DataSetCategory dataSetCategory) {
        return this.dataSetCategoryService.checkName(dataSetCategory);
    }
}
