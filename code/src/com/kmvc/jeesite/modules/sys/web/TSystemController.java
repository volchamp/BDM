package com.kmvc.jeesite.modules.sys.web;

import com.kmvc.jeesite.modules.sys.entity.TSystem;
import com.kmvc.jeesite.modules.sys.service.TSystemService;
import com.kmvc.jeesite.modules.sys.utils.PinyinUtils;
import com.thinkgem.jeesite.common.web.*;
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import com.thinkgem.jeesite.modules.sys.service.*;
import javax.servlet.http.*;
import org.springframework.ui.*;
import org.apache.shiro.authz.annotation.*;
import org.springframework.web.servlet.mvc.support.*;
import com.thinkgem.jeesite.common.config.*;
import com.thinkgem.jeesite.common.utils.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;
import com.thinkgem.jeesite.common.utils.excel.*;
import com.thinkgem.jeesite.common.persistence.*;
import com.thinkgem.jeesite.common.beanvalidator.*;
import com.thinkgem.jeesite.modules.sys.entity.*;
import javax.validation.*;
import java.util.regex.*;
import java.util.*;
import com.google.common.collect.*;
import java.text.*;

@Controller
@RequestMapping({ "${adminPath}/sys/tSystem" })
public class TSystemController extends BaseController
{
    @Autowired
    private TSystemService tSystemService;
    @Autowired
    AreaService areas;

    @ModelAttribute
    public TSystem get(@RequestParam(required = false) final String id) {
        TSystem entity = null;
        if (StringUtils.isNotBlank((CharSequence)id)) {
            entity = this.tSystemService.get(id);
        }
        if (entity == null) {
            entity = new TSystem();
        }
        return entity;
    }

    @RequiresPermissions({ "sys:tSystem:view" })
    @RequestMapping({ "list", "" })
    public String list(final TSystem tSystem, final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        final Page<TSystem> page = (Page<TSystem>)this.tSystemService.findPage(new Page(request, response), tSystem);
        model.addAttribute("page", (Object)page);
        return "modules/sys/tSystemList";
    }

    @RequiresPermissions({ "sys:tSystem:view" })
    @RequestMapping({ "form" })
    public String form(final TSystem tSystem, final Model model) {
        model.addAttribute("tSystem", (Object)tSystem);
        return "modules/sys/tSystemForm";
    }

    @RequiresPermissions({ "sys:tSystem:edit" })
    @RequestMapping({ "save" })
    public String save(final TSystem tSystem, final Model model, final RedirectAttributes redirectAttributes) {
        if (!this.beanValidator(model, (Object)tSystem, new Class[0])) {
            return this.form(tSystem, model);
        }
        this.tSystemService.save(tSystem);
        this.addMessage(redirectAttributes, new String[] { "\u4fdd\u5b58\u4e1a\u52a1\u7cfb\u7edf\u6210\u529f" });
        return "redirect:" + Global.getAdminPath() + "/sys/tSystem/?repage";
    }

    @RequiresPermissions({ "sys:tSystem:edit" })
    @RequestMapping({ "delete" })
    public String delete(final TSystem tSystem, final RedirectAttributes redirectAttributes) {
        this.tSystemService.delete(tSystem);
        this.addMessage(redirectAttributes, new String[] { "\u7981\u7528\u4e1a\u52a1\u7cfb\u7edf\u6210\u529f" });
        return "redirect:" + Global.getAdminPath() + "/sys/tSystem/?repage";
    }

    @RequiresPermissions({ "sys:tSystem:edit" })
    @RequestMapping({ "enable" })
    public String enable(final TSystem tSystem, final RedirectAttributes redirectAttributes) {
        this.tSystemService.enable(tSystem);
        this.addMessage(redirectAttributes, new String[] { "\u542f\u7528\u4e1a\u52a1\u7cfb\u7edf\u6210\u529f" });
        return "redirect:" + Global.getAdminPath() + "/sys/tSystem/?repage";
    }

    @RequestMapping({ "isSystemNameExist" })
    @ResponseBody
    public boolean isSystemNameExist(final TSystem tSystem) {
        return this.tSystemService.isSystemNameExist(tSystem);
    }

    @RequestMapping({ "isSystemCodeExist" })
    @ResponseBody
    public boolean isSystemCodeExist(final TSystem tSystem) {
        return this.tSystemService.isSystemCodeExist(tSystem);
    }

    @RequestMapping({ "isSystemShortExist" })
    @ResponseBody
    public boolean isSystemShortExist(final TSystem tSystem) {
        return this.tSystemService.isSystemShortExist(tSystem);
    }

    @RequestMapping({ "validExist" })
    @ResponseBody
    public boolean validExist(final TSystem tSystem) {
        return this.tSystemService.validExist(tSystem);
    }

    @RequestMapping({ "getPingyin" })
    @ResponseBody
    public Map<String, Object> getPingyin(@RequestParam(required = false) final String input) throws Exception {
        final Map<String, Object> map = new HashMap<String, Object>();
        final String output = PinyinUtils.getPinYinHeadCharFilter(input);
        map.put("output", output);
        return map;
    }

    @RequiresPermissions({ "sys:tSystem:view" })
    @RequestMapping(value = { "export" }, method = { RequestMethod.POST })
    public String exportFile(final TSystem user, final HttpServletRequest request, final HttpServletResponse response, final RedirectAttributes redirectAttributes) {
        try {
            final String fileName = "\u4e1a\u52a1\u6570\u636e" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            final Page<TSystem> page = (Page<TSystem>)this.tSystemService.findPage(new Page(request, response, -1), user);
            new ExportExcel("\u4e1a\u52a1\u6570\u636e", (Class)TSystem.class).setDataList(page.getList()).write(response, fileName).dispose();
            return null;
        }
        catch (Exception e) {
            this.addMessage(redirectAttributes, new String[] { "\u4e1a\u52a1\u7cfb\u7edf\u6570\u636e\u5bfc\u51fa\u5931\u8d25" + e.getMessage() });
            return "redirect:" + this.adminPath + "/sys/tSystem/list?repage";
        }
    }

    @RequiresPermissions({ "sys:tSystem:edit" })
    @RequestMapping(value = { "import" }, method = { RequestMethod.POST })
    public String importFile(final MultipartFile file, final RedirectAttributes redirectAttributes, final HttpServletRequest request) throws Exception {
        if (Global.isDemoMode()) {
            this.addMessage(redirectAttributes, new String[] { "\u6f14\u793a\u6a21\u5f0f\uff0c\u4e0d\u5141\u8bb8\u64cd\u4f5c\uff01" });
            return "redirect:" + this.adminPath + "/sys/tSystem/list?repage";
        }
        try {
            int successNum = 0;
            int failureNum = 0;
            final StringBuilder failureMsg = new StringBuilder();
            final ImportExcel ei = new ImportExcel(file, 1, 0);
            final List<TSystem> list = (List<TSystem>)ei.getDataList((Class)TSystem.class, new int[0]);
            for (final TSystem tSystem : list) {
                try {
                    final Pattern pattern = Pattern.compile("http://(([a-zA-z0-9]|-){1,}\\.){1,}[a-zA-z0-9]{1,}-*");
                    final Matcher matcher = pattern.matcher(tSystem.getServiceAddr());
                    final Area area = tSystem.getArea();
                    final Area area2 = (Area)this.areas.get(area);
                    final TSystem t = new TSystem();
                    t.setSystemCode(tSystem.getSystemCode());
                    t.setSystemName(tSystem.getSystemName());
                    t.setArea(area);
                    if (this.tSystemService.validExist(t)) {
                        ++failureNum;
                    }
                    else if (this.tSystemService.findBySystemCode(tSystem.getSystemCode()) == null) {
                        if (matcher.find()) {
                            if (area2 == null) {
                                ++failureNum;
                            }
                            else {
                                BeanValidators.validateWithException(this.validator, (Object)tSystem, new Class[0]);
                                tSystem.setSystemSoprt(Long.valueOf(1L));
                                final User u = new User();
                                tSystem.setCreateBy(u);
                                final String date = DateUtils.getDate("yyyy-MM-dd:HH.mm.ss");
                                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd:HH.mm.ss");
                                final Date parse = sdf.parse(date);
                                tSystem.setCreateDate(parse);
                                this.tSystemService.save(tSystem);
                                ++successNum;
                            }
                        }
                        else {
                            ++failureNum;
                        }
                    }
                    else {
                        ++failureNum;
                    }
                }
                catch (ConstraintViolationException ex) {
                    failureMsg.append(" \u5bfc\u5165\u5931\u8d25\uff1a");
                    final List<String> messageList = (List<String>)BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
                    for (final String message : messageList) {
                        failureMsg.append(message + "; ");
                        ++failureNum;
                    }
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "\uff0c\u5931\u8d25 " + failureNum + " \u6761\u7528\u6237\uff0c\u5bfc\u5165\u4fe1\u606f\u5982\u4e0b\uff1a");
            }
            this.addMessage(redirectAttributes, new String[] { "\u5df2\u6210\u529f\u5bfc\u5165 " + successNum + " \u6761\u7528\u6237" + (Object)failureMsg });
        }
        catch (Exception e) {
            this.addMessage(redirectAttributes, new String[] { "\u5bfc\u5165\u4e1a\u52a1\u6570\u636e\u5931\u8d25\uff01\u5931\u8d25\u4fe1\u606f:" + e.getMessage() });
        }
        return "redirect:" + this.adminPath + "/sys/tSystem/list?repage";
    }

    @RequiresPermissions({ "sys:tSystem:view" })
    @RequestMapping({ "import/template" })
    public String importFileTemplate(final HttpServletRequest request, final HttpServletResponse response, final RedirectAttributes redirectAttributes) {
        try {
            final String fileName = "\u4e1a\u52a1\u6570\u636e\u5bfc\u5165\u6a21\u677f.xlsx";
            final List<TSystem> list = Lists.newArrayList();
            this.listAddObj(list);
            new ExportExcel("\u4e1a\u52a1\u6570\u636e", (Class)TSystem.class, 2, new int[0]).setDataList((List)list).write(response, fileName).dispose();
            return null;
        }
        catch (Exception e) {
            this.addMessage(redirectAttributes, new String[] { "\u5bfc\u5165\u6a21\u677f\u4e0b\u8f7d\u5931\u8d25\uff01\u5931\u8d25\u4fe1\u606f\uff1a" + e.getMessage() });
            return null;
        }
    }

    private void listAddObj(final List<TSystem> list) throws ParseException {
        final TSystem t = new TSystem();
        final Area area = new Area();
        area.setName("\u90d1\u5dde\u5e02");
        t.setSystemCode("cs");
        t.setSystemName("\u6d4b\u8bd5");
        t.setSystemShort("cs");
        t.setServiceAddr("http://127.0.0.1:8080/");
        t.setRemarks("\u6d4b\u8bd5");
        t.setArea(area);
        list.add(t);
    }
}
