package com.kmvc.jeesite.modules.syncPortal.user.bo;

import java.util.*;

import com.kmvc.jeesite.modules.syncPortal.user.model.PortalOrgPO;
import com.kmvc.jeesite.modules.syncPortal.user.model.*;

class PortalOrgBOImpl$2 implements Comparator<PortalOrgPO> {
    @Override
    public int compare(final PortalOrgPO o1, final PortalOrgPO o2) {
        final int flag = o1.getOrgId().compareTo(o2.getOrgId());
        return flag;
    }
}