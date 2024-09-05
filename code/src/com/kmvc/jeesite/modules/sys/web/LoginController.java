package com.kmvc.jeesite.modules.sys.web;

import com.kmvc.jeesite.modules.common.dao.CommonDao;
import com.kmvc.jeesite.modules.common.entity.CommonEntity;
import com.thinkgem.jeesite.common.web.*;
import org.springframework.stereotype.*;
import com.thinkgem.jeesite.common.security.shiro.session.*;
import org.springframework.beans.factory.annotation.*;
import com.kmvc.jeesite.modules.common.dao.*;
import com.thinkgem.jeesite.modules.sys.dao.*;
import javax.servlet.http.*;
import org.springframework.ui.*;
import com.thinkgem.jeesite.common.config.*;
import com.thinkgem.jeesite.modules.sys.security.*;
import org.apache.shiro.web.util.*;
import com.kmvc.jeesite.modules.common.entity.*;
import com.thinkgem.jeesite.modules.sys.utils.*;
import com.thinkgem.jeesite.modules.sys.entity.*;
import org.apache.shiro.authz.*;
import org.apache.shiro.*;
import org.apache.shiro.subject.*;
import org.apache.shiro.authz.annotation.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import com.thinkgem.jeesite.common.utils.*;
import com.google.common.collect.*;

@Controller
public class LoginController extends BaseController
{
    @Autowired
    private SessionDAO sessionDAO;
    @Autowired
    private CommonDao commonDao;
    @Autowired
    private LogDao logDao;

    @RequestMapping(value = { "${adminPath}/login" }, method = { RequestMethod.GET })
    public String login(final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        final SystemAuthorizingRealm.Principal principal = UserUtils.getPrincipal();
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("login, active session size: {}", this.sessionDAO.getActiveSessions(false).size());
        }
        if ("true".equals(Global.getConfig("notAllowRefreshIndex"))) {
            CookieUtils.setCookie(response, "LOGINED", "false");
        }
        return (principal != null && !principal.isMobileLogin()) ? ("redirect:" + this.adminPath) : "modules/sys/sysLogin";
    }

    @RequestMapping(value = { "${adminPath}/login" }, method = { RequestMethod.POST })
    public String loginFail(final HttpServletRequest request, final HttpServletResponse response, final Model model) {
        SystemAuthorizingRealm.Principal principal = UserUtils.getPrincipal();
        final String username = WebUtils.getCleanParam(request, "username");
        final CommonEntity ceCriteria = new CommonEntity();
        ceCriteria.setLoginName(username);
        final CommonEntity comEnt = this.commonDao.getUserByLgName(ceCriteria);
        if (comEnt == null) {
            request.setAttribute("message", "\u7528\u6237\u6216\u5bc6\u7801\u9519\u8bef, \u8bf7\u91cd\u8bd5.");
            return "modules/sys/sysLogin";
        }
        final String no = DictUtils.getDictValue("\u5426", "yes_no", "0");
        if (comEnt != null && no.equals(comEnt.getLoginFlag())) {
            request.setAttribute("message", "\u7528\u6237\u6216\u5bc6\u7801\u9519\u8bef, \u8bf7\u91cd\u8bd5.");
            return "modules/sys/sysLogin";
        }
        boolean isLocked = false;
        final long curTimeMillis = System.currentTimeMillis();
        final Log log = new Log();
        log.setId(IdGen.uuid());
        log.setTitle("\u7cfb\u7edf\u767b\u5f55\u5931\u8d25");
        log.setCreateBy(new User(comEnt.getId()));
        log.setCreateDate(new Date(curTimeMillis));
        log.setRemoteAddr(request.getRemoteAddr());
        log.setUserAgent(request.getHeader("User-Agent"));
        log.setRequestUri(request.getRequestURI());
        log.setMethod(request.getMethod());
        log.setParams(request.getParameterMap());
        this.logDao.insert(log);
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
        long lgRecordTimeMilSec = curTimeMillis;
        if (comEnt.getLgRecordTime() != null) {
            lgRecordTimeMilSec = comEnt.getLgRecordTime().getTime();
        }
        if (comEnt.getIllegalLgCount() == illegalLgCountMaxInt && curTimeMillis - lgRecordTimeMilSec < accountLockedTimeDo * 60000.0) {
            isLocked = true;
        }
        if (curTimeMillis - lgRecordTimeMilSec > accountLockedTimeDo * 60000.0) {
            comEnt.setIllegalLgCount(Integer.valueOf(0));
        }


        if (principal != null && !isLocked) {
            comEnt.setIllegalLgCount(Integer.valueOf(0));
            this.commonDao.updIllegalLgById(comEnt);
            return "redirect:" + this.adminPath;
        }
        final boolean rememberMe = WebUtils.isTrue(request, "rememberMe");
        final boolean mobile = WebUtils.isTrue(request, "mobileLogin");
        final String exception = (String)request.getAttribute("shiroLoginFailure");
        String message = (String)request.getAttribute("message");
        model.addAttribute("username", username);
        model.addAttribute("rememberMe",rememberMe);
        model.addAttribute("mobileLogin", mobile);
        model.addAttribute("shiroLoginFailure", exception);
        if (!UnauthorizedException.class.getName().equals(exception)) {
            model.addAttribute("isValidateCodeLogin", isValidateCodeLogin(username, true, false));
        }
        request.getSession().setAttribute("validateCode", IdGen.uuid());
        if (comEnt.getIllegalLgCount() < illegalLgCountMaxInt) {
            comEnt.setIllegalLgCount(Integer.valueOf(comEnt.getIllegalLgCount() + 1));
            message = "\u7528\u6237\u6216\u5bc6\u7801\u9519\u8bef, \u8bf7\u91cd\u8bd5.\u6e29\u99a8\u63d0\u793a\uff1a\u60a8\u8fd8\u6709" + (illegalLgCountMaxInt - comEnt.getIllegalLgCount()) + "\u6b21\u767b\u5f55\u673a\u4f1a.";
            if (illegalLgCountMaxInt == comEnt.getIllegalLgCount()) {
                message = "\u62b1\u6b49, \u7528\u6237\u6216\u5bc6\u7801\u9519\u8bef\uff0c\u5f53\u524d\u8d26\u6237\u5904\u4e8e\u9501\u5b9a\u72b6\u6001\uff0c\u8bf7" + accountLockedTime + "\u5206\u949f\u540e\u518d\u767b\u5f55";
            }
        }
        else if (comEnt.getIllegalLgCount() == illegalLgCountMaxInt && isLocked) {
            message = "\u62b1\u6b49\uff0c\u5f53\u524d\u8d26\u6237\u5904\u4e8e\u9501\u5b9a\u72b6\u6001\uff0c\u8bf7" + accountLockedTime + "\u5206\u949f\u540e\u518d\u767b\u5f55";
        }
        model.addAttribute("message", message);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("login fail, active session size: {}, message: {}, exception: {}", new Object[] { this.sessionDAO.getActiveSessions(false).size(), message, exception });
        }
        this.commonDao.updIllegalLgById(comEnt);
        return mobile ? this.renderString(response,model) : "modules/sys/sysLogin";
        //return this.renderString(response,model);
    }

    @RequiresPermissions({ "user" })
    @RequestMapping({ "${adminPath}" })
    public String index(final HttpServletRequest request, final HttpServletResponse response) {
        final SystemAuthorizingRealm.Principal principal = UserUtils.getPrincipal();
        isValidateCodeLogin(principal.getLoginName(), false, true);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("show index, active session size: {}", this.sessionDAO.getActiveSessions(false).size());
        }
        if ("true".equals(Global.getConfig("notAllowRefreshIndex"))) {
            final String logined = CookieUtils.getCookie(request, "LOGINED");
            if (!StringUtils.isBlank((CharSequence)logined) && !"false".equals(logined)) {
                if (StringUtils.equals((CharSequence)logined, (CharSequence)"true")) {
                    UserUtils.getSubject().logout();
                    return "redirect:" + this.adminPath + "/login";
                }
            }
            else {
                CookieUtils.setCookie(response, "LOGINED", "true");
            }
        }
        final CommonEntity ceCriteria = new CommonEntity();
        ceCriteria.setLoginName(principal.getLoginName());
        final CommonEntity comEnt = this.commonDao.getUserByLgName(ceCriteria);
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
        final Subject subject = SecurityUtils.getSubject();
        if (comEnt == null) {
            request.setAttribute("message", (Object)"\u7528\u6237\u6216\u5bc6\u7801\u9519\u8bef, \u8bf7\u91cd\u8bd5\u3002");
            if (subject != null) {
                subject.logout();
            }
            return "modules/sys/sysLogin";
        }
        final String no = DictUtils.getDictValue("\u5426", "yes_no", "0");
        if (comEnt != null && no.equals(comEnt.getLoginFlag())) {
            request.setAttribute("message", (Object)"\u7528\u6237\u6216\u5bc6\u7801\u9519\u8bef, \u8bf7\u91cd\u8bd5\u3002");
            if (subject != null) {
                subject.logout();
            }
            return "modules/sys/sysLogin";
        }
        final Date lgRecordTime = comEnt.getLgRecordTime();
        if (lgRecordTime != null) {
            final long lgRecordTimeMilSec = lgRecordTime.getTime();
            if (comEnt.getIllegalLgCount() == illegalLgCountMaxInt && curTimeMillis - lgRecordTimeMilSec < accountLockedTimeDo * 60000.0) {
                isLocked = true;
            }
        }
        if (isLocked) {
            request.setAttribute("message", (Object)("\u62b1\u6b49\uff0c\u5f53\u524d\u8d26\u6237\u5904\u4e8e\u9501\u5b9a\u72b6\u6001\uff0c\u8bf7" + accountLockedTime + "\u5206\u949f\u540e\u518d\u767b\u5f55\u3002"));
            request.setAttribute("username", (Object)principal.getLoginName());
            return "modules/sys/sysLogin";
        }
        if (!principal.isMobileLogin()) {
            comEnt.setIllegalLgCount(Integer.valueOf(0));
            this.commonDao.updIllegalLgById(comEnt);
            return "modules/sys/sysIndex";
        }
        if (request.getParameter("login") != null) {
            return this.renderString(response, (Object)principal);
        }
        return (request.getParameter("index") != null) ? "modules/sys/sysIndex" : ("redirect:" + this.adminPath + "/login");
    }

    @RequestMapping({ "/theme/{theme}" })
    public String getThemeInCookie(@PathVariable String theme, final HttpServletRequest request, final HttpServletResponse response) {
        if (StringUtils.isNotBlank((CharSequence)theme)) {
            CookieUtils.setCookie(response, "theme", theme);
        }
        else {
            theme = CookieUtils.getCookie(request, "theme");
        }
        return "redirect:" + request.getParameter("url");
    }

    public static boolean isValidateCodeLogin(final String useruame, final boolean isFail, final boolean clean) {
        Map<String, Integer> loginFailMap = (Map<String, Integer>)CacheUtils.get("loginFailMap");
        if (loginFailMap == null) {
            loginFailMap = Maps.newHashMap();
            CacheUtils.put("loginFailMap", (Object)loginFailMap);
        }
        Integer loginFailNum = loginFailMap.get(useruame);
        if (loginFailNum == null) {
            loginFailNum = 0;
        }
        if (isFail) {
            ++loginFailNum;
            loginFailMap.put(useruame, loginFailNum);
        }
        if (clean) {
            loginFailMap.remove(useruame);
        }
        return loginFailNum >= 3;
    }
}
