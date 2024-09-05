package com.kmvc.jeesite.modules.syncPortal.user.bo;

import com.kmvc.jeesite.modules.common.dao.CommonDao;
import com.kmvc.jeesite.modules.syncPortal.help.SynHelper;
import com.kmvc.jeesite.modules.syncPortal.user.model.PortalRelationPO;
import com.kmvc.jeesite.modules.syncPortal.user.model.PortalUserPO;
import com.kmvc.jeesite.modules.syncPortal.user.rep.PortalUser;
import com.kmvc.jeesite.modules.syncPortal.user.vo.PortalUserChangeVO;
import com.kmvc.jeesite.modules.syncPortal.utils.DateUtil;
import org.springframework.stereotype.*;
import com.kmvc.jeesite.modules.syncPortal.user.rep.*;
import org.springframework.beans.factory.annotation.*;
import com.thinkgem.jeesite.modules.sys.dao.*;
import com.kmvc.jeesite.modules.common.dao.*;
import com.kmvc.jeesite.modules.syncPortal.utils.*;
import com.kmvc.jeesite.modules.syncPortal.help.*;
import com.kmvc.jeesite.modules.syncPortal.user.vo.*;
import java.util.*;
import com.google.common.collect.*;
import com.kmvc.jeesite.modules.syncPortal.user.model.*;
import com.thinkgem.jeesite.modules.sys.entity.*;
import org.apache.shiro.authc.*;

@Service
public class PortalUserBOImpl implements PortalUserBO
{
    @Autowired
    PortalUser portalUserDao;
    @Autowired
    UserDao userDao;
    @Autowired
    OfficeDao officeDao;
    @Autowired
    CommonDao commonDao;

    @Override
    public void synChange(Date synPortalTime) {
        final String time = DateUtil.getFormatterValue(synPortalTime, "yyyy-MM-dd HH:mm:ss");
        synPortalTime = new Date();
        final List<PortalUserChangeVO> changeList = (List<PortalUserChangeVO>) SynHelper.sendUserChange(time);
        for (final PortalUserChangeVO changevo : changeList) {
            try {
                final PortalUserPO po = SynHelper.sendUser(changevo.getUid());
                int changeType = 0;
                if ("add".equals(po.getChangeTyep())) {
                    changeType = 1;
                }
                else if ("update".equals(po.getChangeTyep())) {
                    changeType = 2;
                }
                else if ("delete".equals(po.getChangeTyep())) {
                    changeType = 3;
                }
                this.syn1(po, changeType, synPortalTime);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void synAll(final Date synPortalTime) {
        System.out.println("PortalUserBOImpl synAll:" + DateUtil.getFormatterValue(new Date(), "yyyy-MM-dd HH:mm:ss"));
        final List<PortalUserPO> idList = (List<PortalUserPO>)SynHelper.sendUserAll();
        System.out.println("PortalUserBOImpl synAll:" + DateUtil.getFormatterValue(new Date(), "yyyy-MM-dd HH:mm:ss") + "accounts size:" + idList.size());
        for (final PortalUserPO id : idList) {
            final PortalUserPO po = SynHelper.sendUser(id.getUid());
            int changeType = 0;
            if ("add".equals(po.getChangeTyep())) {
                changeType = 1;
                po.setChangeTyep(changeType + "");
            }
            else if ("update".equals(po.getChangeTyep())) {
                changeType = 2;
                po.setChangeTyep(changeType + "");
            }
            else if ("delete".equals(po.getChangeTyep())) {
                changeType = 3;
                po.setChangeTyep(changeType + "");
            }
            this.syn1(po, changeType, synPortalTime);
        }
    }

    public void syn1(final PortalUserPO po, final int type, final Date synPortalTime) {
        if (type == 1) {
            System.out.println("=======syn1\u5f00\u59cb\u65b0\u589e\u7528\u6237" + po.getUserName());
            final User user = this.getUserByPo(po);
            this.userDao.insert(user);
            this.userDao.insertUserOffice(user);
            System.out.println("=======syn1\u5b8c\u6210\u65b0\u589e\u7528\u6237" + po.getUserName());
        }
        else if (type == 2) {
            System.out.println("=======syn1\u5f00\u59cb\u4fee\u6539\u7528\u6237" + po.getUserName());
            final User user = this.getUserByPo(po);
            final User dbUser = (User)this.userDao.get(po.getUid());
            if (dbUser == null) {
                this.userDao.insert(user);
                this.userDao.insertUserOffice(user);
            }
            else {
                this.userDao.update(user);
                this.userDao.deleteUserOffice(user);
                this.userDao.insertUserOffice(user);
            }
            System.out.println("=======syn1\u5b8c\u6210\u4fee\u6539\u7528\u6237" + po.getUserName());
        }
        else if (type == 3) {
            System.out.println("=======syn1\u5f00\u59cb\u5220\u9664\u7528\u6237" + po.getUserName());
            final User user = new User(po.getUid());
            this.userDao.delete(user);
            this.userDao.deleteUserOffice(user);
            System.out.println("=======syn1\u5b8c\u6210\u5220\u9664\u7528\u6237" + po.getUserName());
        }
    }

    private User getUserByPo(final PortalUserPO po) {
        final User user = new User();
        final StringBuffer remarkSb = new StringBuffer();
        user.setId(po.getUid());
        user.setNo(po.getUserCode());
        user.setLoginName(po.getUserName());
        user.setPassword(po.getPassword());
        user.setName(po.getName());
        remarkSb.append("\"sex\":\"" + po.getSex() + "\"");
        remarkSb.append(",\"idCard\":\"" + po.getIdCard() + "\"");
        if ("0".equals(po.getStatus())) {
            user.setDelFlag("1");
        }
        else {
            user.setDelFlag("0");
        }
        user.setUserType(po.getType());
        final Date d = new Date();
        user.setCreateDate(d);
        user.setUpdateDate(d);
        user.setCreateBy(new User("1"));
        user.setUpdateBy(new User("1"));
        remarkSb.append(",\"endTime\":\"" + po.getEndTime() + "\"");
        user.setRemarks(remarkSb.toString());
        final List<Office> officeList = Lists.newArrayList();
        final List<PortalRelationPO> relation = (List<PortalRelationPO>)po.getRelation();
        for (final PortalRelationPO relPo : relation) {
            final Office offCrit = new Office();
            offCrit.setId(relPo.getOid());
            offCrit.setCode(relPo.getOrgCode());
            offCrit.setName(relPo.getOrgName());
            Office office = this.commonDao.getOffByIdCodeName(offCrit);
            if (office == null) {
                office = new Office();
                office.setId(relPo.getOid());
                office.setName(relPo.getOrgName());
                office.setCode(relPo.getOrgCode());
                office.setSort(relPo.getDisplayOrder());
                office.setParent(new Office(" "));
                office.setParentIds(" ");
                office.setArea(new Area(" "));
                office.setGrade(" ");
                office.setCreateBy(new User(" "));
                office.setCreateDate(new Date());
                office.setUpdateBy(new User(" "));
                office.setUpdateDate(new Date());
                this.officeDao.insert(office);
            }
            officeList.add(office);
        }
        user.setOfficeList((List)officeList);
        return user;
    }

    @Override
    public Integer getCount() {
        return this.portalUserDao.getCount();
    }

    @Override
    public Date getLastSynTime() {
        try {
            return this.portalUserDao.getLastSynTime();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void validatePrivilege(final Account account) {
        final Account loginAccount = this.getLoginAccount();
        if (loginAccount.equals(account)) {}
    }

    private void publishAccountModifyEvent(final Integer accountId, final int eventType) {
    }

    public Account getLoginAccount() {
        return null;
    }

    public Account getByAccountCode(final String accountCode) {
        final Account account = null;
        return account;
    }

    public Account getByAccountId(final Integer accountId) {
        final Account account = null;
        return account;
    }

    @Override
    public void syn(final PortalUserPO po, final int type, final Date synPortalTime) {
    }
}
