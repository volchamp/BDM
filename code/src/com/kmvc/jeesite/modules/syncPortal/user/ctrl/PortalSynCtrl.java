package com.kmvc.jeesite.modules.syncPortal.user.ctrl;

import com.kmvc.jeesite.modules.syncPortal.ctrl.SynBaseCtrl;
import com.kmvc.jeesite.modules.syncPortal.user.bo.PortalOrgBOImpl;
import com.kmvc.jeesite.modules.syncPortal.user.bo.PortalUserBOImpl;
import com.kmvc.jeesite.modules.syncPortal.user.vo.ResultVO;
import com.kmvc.jeesite.modules.syncPortal.utils.DateUtil;
import com.kmvc.jeesite.modules.syncPortal.utils.JacksonUtil;
import com.kmvc.jeesite.modules.syncPortal.ctrl.*;
import org.springframework.stereotype.*;
import org.apache.log4j.*;
import org.springframework.beans.factory.annotation.*;
import com.kmvc.jeesite.modules.syncPortal.user.bo.*;
import com.thinkgem.jeesite.modules.sys.dao.*;
import javax.servlet.http.*;
import com.kmvc.jeesite.modules.syncPortal.user.vo.*;
import com.thinkgem.jeesite.modules.sys.entity.*;
import com.kmvc.jeesite.modules.syncPortal.utils.*;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping({ "${frontPath}/" })
public class PortalSynCtrl extends SynBaseCtrl
{
    Logger log;
    @Autowired
    PortalOrgBOImpl portalOrgBO;
    @Autowired
    PortalUserBOImpl portalUserBO;
    @Autowired
    UserDao userDao;

    public PortalSynCtrl() {
        this.log = Logger.getLogger((Class)PortalSynCtrl.class);
    }

    @RequestMapping(value = { "synchronizeUser" }, method = { RequestMethod.POST })
    @ResponseBody
    public String syn(final HttpServletResponse response, final HttpServletRequest request) throws Exception {
        this.log.debug((Object)("syn:" + DateUtil.getFormatterValue(new Date(), "yyyy-MM-dd HH:mm:ss")));
        final ResultVO resultVO = new ResultVO();
        try {
            final User user = new User();
            final Long countUser = this.userDao.findAllCount(user);
            this.log.debug((Object)("account count:" + DateUtil.getFormatterValue(new Date(), "yyyy-MM-dd HH:mm:ss") + ":" + countUser));
            if (countUser > 0L) {
                final List<User> findAllList = (List<User>)this.userDao.findAllList();
                final User user2 = findAllList.get(0);
                this.portalUserBO.synChange(user2.getUpdateDate());
            }
            else {
                final Date synPortalTime = new Date();
                this.portalUserBO.synAll(synPortalTime);
            }
        }
        catch (Exception e) {
            resultVO.setResultType("0");
            resultVO.setDescription("\u540c\u6b65\u5931\u8d25\uff0c\u672c\u7cfb\u7edf\u63a5\u53d7\u6570\u636e\u540e\u5904\u7406\u5f02\u5e38");
            e.printStackTrace();
        }
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setCharacterEncoding("utf-8");
        final String json = JacksonUtil.fromObject(resultVO);
        this.log.debug((Object)("\u540c\u6b65\u8fd4\u56de\u7ed3\u679c:" + json));
        return json;
    }
}
