package com.kmvc.jeesite.modules.codesetstatistics.web;

import com.kmvc.jeesite.modules.sys.entity.TSystem;
import com.kmvc.jeesite.modules.sys.service.TSystemService;
import com.thinkgem.jeesite.common.web.*;
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.ui.*;
import java.util.*;
import javax.servlet.http.*;
import org.springframework.web.servlet.mvc.support.*;
import com.thinkgem.jeesite.common.utils.*;
import com.thinkgem.jeesite.common.utils.excel.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping({ "${adminPath}/codesetstatistics/statistics" })
public class StatisticsController extends BaseController
{
    @Autowired
    private TSystemService systemService;

    @RequestMapping({ "list", "" })
    public String list(final HttpServletRequest request, final Model model) {
        return "modules/codesetstatistics/statisticsList";
    }

    @RequestMapping({ "getData" })
    @ResponseBody
    public List<TSystem> getData(final HttpServletRequest reuqest) {
        final TSystem tSystem = new TSystem();
        final List<TSystem> list = this.systemService.codeSetStatistics(tSystem);
        return list;
    }

    @RequestMapping(value = { "export" }, method = { RequestMethod.POST })
    public String exportFile(final TSystem tSystem, final HttpServletRequest request, final HttpServletResponse response, final RedirectAttributes redirectAttributes) {
        try {
            final String fileName = "\u4ee3\u7801\u96c6\u7edf\u8ba1\u6570\u636e" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            final List<TSystem> list = this.systemService.codeSetStatistics(tSystem);
            for (int i = 0; i < list.size(); ++i) {
                list.get(i).setNum(i + 1);
            }
            new ExportExcel("\u4ee3\u7801\u96c6\u7edf\u8ba1\u6570\u636e", (Class)TSystem.class).setDataList((List)list).write(response, fileName).dispose();
            return null;
        }
        catch (Exception e) {
            this.addMessage(redirectAttributes, new String[] { "\u5bfc\u51fa\u4ee3\u7801\u96c6\u7edf\u8ba1\u6570\u636e\u5931\u8d25\uff01\u5931\u8d25\u4fe1\u606f\uff1a" + e.getMessage() });
            return "redirect:" + this.adminPath + "/codesetstatistics/statistics/list?repage";
        }
    }
}
