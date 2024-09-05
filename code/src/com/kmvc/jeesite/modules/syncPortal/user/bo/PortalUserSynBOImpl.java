package com.kmvc.jeesite.modules.syncPortal.user.bo;

import com.kmvc.jeesite.modules.syncPortal.user.model.PortalUserPO;
import com.kmvc.jeesite.modules.syncPortal.user.model.PortalUserSynPO;
import com.kmvc.jeesite.modules.syncPortal.user.rep.PortalUser;
import com.kmvc.jeesite.modules.syncPortal.user.rep.PortalUserSyn;
import org.springframework.transaction.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import com.kmvc.jeesite.modules.syncPortal.user.rep.*;
import org.springframework.util.*;
import com.kmvc.jeesite.modules.syncPortal.user.model.*;
import org.apache.shiro.authc.*;

@Transactional(readOnly = true)
@Service("portalUserSynBO")
public class PortalUserSynBOImpl implements PortalUserSynBO
{
    @Autowired
    PortalUser portalUserDao;
    @Autowired
    PortalUserSyn portalUserSynDao;

    @Transactional(readOnly = false, rollbackFor = { Throwable.class })
    @Override
    public void synDel(final Integer id) {
    }

    @Transactional(readOnly = false, rollbackFor = { Throwable.class })
    @Override
    public void synUser(final String pln, final String[] slns) {
        Assert.hasText(pln);
        Assert.notEmpty((Object[])slns);
        for (final String sln : slns) {
            final PortalUserPO puser = this.portalUserDao.getByLoginName(pln);
            final PortalUserSynPO po = new PortalUserSynPO();
            po.setPorLoginName(pln);
            po.setSysLoginName(sln);
        }
    }

    @Override
    public Account findByPortalLoginName(final String pln, final boolean simple) {
        return null;
    }
}
