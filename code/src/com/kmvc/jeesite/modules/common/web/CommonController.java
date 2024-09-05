package com.kmvc.jeesite.modules.common.web;

import com.kmvc.jeesite.modules.common.dao.CommonDao;
import com.kmvc.jeesite.modules.common.entity.CommonEntity;
import com.kmvc.jeesite.modules.sys.web.LoginController;
import com.thinkgem.jeesite.common.web.*;
import org.springframework.stereotype.*;
import com.kmvc.jeesite.modules.common.dao.*;
import org.springframework.beans.factory.annotation.*;
import com.thinkgem.jeesite.common.security.shiro.session.*;
import com.thinkgem.jeesite.modules.sys.dao.*;
import com.thinkgem.jeesite.modules.sys.utils.*;
import com.thinkgem.jeesite.common.config.*;
import com.kmvc.jeesite.modules.common.entity.*;
import com.thinkgem.jeesite.modules.sys.entity.*;
import java.util.*;
import org.springframework.ui.*;
import javax.servlet.http.*;
import org.apache.shiro.web.util.*;
import javax.servlet.*;
import org.apache.shiro.authz.*;
import com.thinkgem.jeesite.common.utils.*;
import com.thinkgem.jeesite.modules.sys.security.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping({ "${adminPath}/common/comCon" })
public class CommonController extends BaseController
{
    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SessionDAO sessionDAO;
    @Autowired
    private LogDao logDao;

    @RequestMapping({ "isPswExpire" })
    @ResponseBody
    public boolean isPswExpire(final HttpServletRequest reuqest) {
        final CommonEntity comEnt = this.commonDao.getUserById(UserUtils.getUser().getId());
        final Date pswUpdateDate = comEnt.getPswUpdateDate();
        if (pswUpdateDate == null) {
            return false;
        }
        final long pswUpdateMilSec = pswUpdateDate.getTime();
        final long curTimeMillis = System.currentTimeMillis();
        final String pswExpireDay = Global.getConfig("pswExpireDay");
        double pswExpDayDoub = 30.0;
        try {
            pswExpDayDoub = Double.parseDouble(pswExpireDay);
        }
        catch (Exception e) {
            pswExpDayDoub = 30.0;
        }
        return curTimeMillis - pswUpdateMilSec > pswExpDayDoub * 8.64E7;
    }

    @RequestMapping({ "isPswUseTimeEnough" })
    @ResponseBody
    public boolean isPswUseTimeEnough(final HttpServletRequest reuqest) {
        final long curTimeMillis = System.currentTimeMillis();
        final CommonEntity comEnt = this.commonDao.getUserById(UserUtils.getUser().getId());
        final Date pswUpdateDate = comEnt.getPswUpdateDate();
        if (pswUpdateDate == null) {
            return true;
        }
        final long pswUpdateMilSec = pswUpdateDate.getTime();
        return curTimeMillis - pswUpdateMilSec >= 86400000L;
    }

    @RequestMapping({ "isExistLastLgLog" })
    @ResponseBody
    public boolean isExistLastLgLog(final HttpServletRequest reuqest) {
        final Log log = new Log();
        log.setTitle("\u7cfb\u7edf\u767b\u5f55");
        log.setCreateBy(UserUtils.getUser());
        final List<Log> logList = (List<Log>)this.logDao.findList(log);
        return logList.size() >= 2;
    }

    @RequestMapping({ "logForm" })
    public String logForm(final Model model) {
        final Log log = new Log();
        log.setTitle("\u7cfb\u7edf\u767b\u5f55");
        log.setCreateBy(UserUtils.getUser());
        final List<Log> logList = (List<Log>)this.logDao.findList(log);
        model.addAttribute("log", (Object)logList.get(1));
        return "modules/sys/logForm";
    }

    @RequestMapping(value = { "login" }, method = { RequestMethod.POST })
    public String loginFail(final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        final SystemAuthorizingRealm.Principal principal = UserUtils.getPrincipal();
        final CommonEntity comEnt = this.commonDao.getUserById(UserUtils.getUser().getId());
        boolean isLocked = false;
        final long curTimeMillis = System.currentTimeMillis();
        final String illegalLgCountMax = Global.getConfig("illegalLgCountMax");
        final String accountLockedTime = Global.getConfig("accountLockedTime");
        int illegalLgCountMaxInt = 3;
        try {
            illegalLgCountMaxInt = Integer.parseInt(illegalLgCountMax);
        }
        catch (Exception e) {
            illegalLgCountMaxInt = 3;
        }
        double accountLockedTimeDo = 10.0;
        try {
            accountLockedTimeDo = Double.parseDouble(accountLockedTime);
        }
        catch (Exception e2) {
            accountLockedTimeDo = 10.0;
        }
        final Date loginDate = comEnt.getLgRecordTime();
        if (loginDate != null) {
            final long loginDateMilSec = loginDate.getTime();
            if (comEnt.getIllegalLgCount() == illegalLgCountMaxInt && curTimeMillis - loginDateMilSec < accountLockedTimeDo * 60000.0) {
                isLocked = true;
            }
        }
        if (principal != null && !isLocked) {
            comEnt.setIllegalLgCount(0);
            this.commonDao.updIllegalLgById(comEnt);
            return "redirect:" + this.adminPath;
        }
        final String username = WebUtils.getCleanParam((ServletRequest)request, "username");
        final boolean rememberMe = WebUtils.isTrue((ServletRequest)request, "rememberMe");
        final boolean mobile = WebUtils.isTrue((ServletRequest)request, "mobileLogin");
        final String exception = (String)request.getAttribute("shiroLoginFailure");
        String message = (String)request.getAttribute("message");
        if (StringUtils.isBlank((CharSequence)message) || StringUtils.equals((CharSequence)message, (CharSequence)"null")) {
            message = "\u7528\u6237\u6216\u5bc6\u7801\u9519\u8bef, \u8bf7\u91cd\u8bd5.";
        }
        model.addAttribute("username", (Object)username);
        model.addAttribute("rememberMe", (Object)rememberMe);
        model.addAttribute("mobileLogin", (Object)mobile);
        model.addAttribute("shiroLoginFailure", (Object)exception);
        if (!UnauthorizedException.class.getName().equals(exception)) {
            model.addAttribute("isValidateCodeLogin", (Object) LoginController.isValidateCodeLogin(username, true, false));
        }
        request.getSession().setAttribute("validateCode", (Object)IdGen.uuid());
        if (comEnt.getIllegalLgCount() == null) {
            comEnt.setIllegalLgCount(1);
        }
        else if (comEnt.getIllegalLgCount() < illegalLgCountMaxInt) {
            comEnt.setIllegalLgCount(comEnt.getIllegalLgCount() + 1);
        }
        else if (comEnt.getIllegalLgCount() == illegalLgCountMaxInt) {
            message = "\u62b1\u6b49, \u767b\u5f55\u5931\u8d25\u6b21\u6570\u5df2\u8fbe\u6700\u5927\u503c\uff0c\u5f53\u524d\u8d26\u6237\u5904\u4e8e\u9501\u5b9a\u72b6\u6001\uff0c\u8bf7" + accountLockedTime + "\u5206\u949f\u540e\u518d\u767b\u5f55";
        }
        model.addAttribute("message", (Object)message);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("login fail, active session size: {}, message: {}, exception: {}", new Object[] { this.sessionDAO.getActiveSessions(false).size(), message, exception });
        }
        this.commonDao.updIllegalLgById(comEnt);
        return mobile ? this.renderString(response, (Object)model) : "modules/sys/sysLogin";
    }
}
