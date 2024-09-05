package com.kmvc.jeesite.modules.syncPortal.user.bo;

import com.kmvc.jeesite.modules.syncPortal.help.SynHelper;
import com.kmvc.jeesite.modules.syncPortal.user.model.PortalOrgPO;
import com.kmvc.jeesite.modules.syncPortal.user.rep.PortalOrg;
import com.kmvc.jeesite.modules.syncPortal.user.vo.PortalOrgChangeVO;
import com.kmvc.jeesite.modules.syncPortal.utils.DateUtil;
import org.springframework.stereotype.*;
import com.kmvc.jeesite.modules.syncPortal.user.rep.*;
import org.springframework.beans.factory.annotation.*;
import com.kmvc.jeesite.modules.syncPortal.utils.*;
import com.kmvc.jeesite.modules.syncPortal.help.*;
import com.kmvc.jeesite.modules.syncPortal.user.vo.*;
import java.util.*;
import com.kmvc.jeesite.modules.syncPortal.user.model.*;

@Service
public class PortalOrgBOImpl implements PortalOrgBO
{
    @Autowired
    private PortalOrg portalOrgDao;

    @Override
    public void synChange(Date synPortalTime) {
        final String time = DateUtil.getFormatterValue(synPortalTime, "yyyy-MM-dd HH:mm:ss");
        synPortalTime = new Date();
        final List<PortalOrgChangeVO> changeList = (List<PortalOrgChangeVO>) SynHelper.sendOrgChange(time);
        if (changeList == null) {
            return;
        }
        //Collections.sort(changeList, (Comparator<? super PortalOrgChangeVO>)new PortalOrgBOImpl.PortalOrgBOImpl22(this));
        for (final PortalOrgChangeVO changevo : changeList) {
            try {
                if (changevo.getChangeType().equals("1")) {
                    changevo.setChangeType("add");
                }
                else if (changevo.getChangeType().equals("2")) {
                    changevo.setChangeType("modify");
                }
                else if (changevo.getChangeType().equals("3")) {
                    changevo.setChangeType("delete");
                }
                else {
                    changevo.setChangeType("");
                }
                int type = 0;
                final PortalOrgPO po = SynHelper.sendOrg(changevo.getOrgId());
                System.out.println(po.getOrgId() + "   aaaaa");
                if ("add".equalsIgnoreCase(changevo.getChangeType())) {
                    type = 1;
                }
                else if ("update".equalsIgnoreCase(changevo.getChangeType())) {
                    type = 2;
                }
                else if ("delete".equalsIgnoreCase(changevo.getChangeType())) {
                    type = 3;
                }
                this.syn(po, type, synPortalTime);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void synAll(final Date synPortalTime) {
        final List<PortalOrgPO> idList = (List<PortalOrgPO>)SynHelper.sendOrgAll();
        if (idList == null) {
            return;
        }
        Collections.sort(idList, new Comparator<PortalOrgPO>() {
            @Override
            public int compare(final PortalOrgPO o1, final PortalOrgPO o2) {
                final int flag = o1.getOrgId().compareTo(o2.getOrgId());
                return flag;
            }
        });
        for (final PortalOrgPO id : idList) {
            final PortalOrgPO po = SynHelper.sendOrg(id.getOrgId());
            this.syn(po, 0, synPortalTime);
        }
    }

    @Override
    public void syn(final PortalOrgPO po, final int type, final Date synPortalTime) {
        if (type == 0 || type == 1 || type == 2) {}
    }

    @Override
    public Integer getCount() {
        return this.portalOrgDao.getCount();
    }

    @Override
    public Date getLastSynTime() {
        try {
            return this.portalOrgDao.getLastSynTime();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    class PortalOrgBOImpl22 implements Comparator<PortalOrgPO> {
        @Override
        public int compare(final PortalOrgPO o1, final PortalOrgPO o2) {
            final int flag = o1.getOrgId().compareTo(o2.getOrgId());
            return flag;
        }
    }
}
