package com.kmvc.jeesite.modules.syncPortal.user.bo;

import java.util.*;

import com.kmvc.jeesite.modules.syncPortal.user.vo.PortalOrgChangeVO;
import com.kmvc.jeesite.modules.syncPortal.user.vo.*;

class PortalOrgBOImpl$1 implements Comparator<PortalOrgChangeVO> {
    @Override
    public int compare(final PortalOrgChangeVO o1, final PortalOrgChangeVO o2) {
        final int flag = o1.getOrgId().compareTo(o2.getOrgId());
        if (flag == 0) {
            return o1.getChangeTime().compareTo(o2.getChangeTime());
        }
        return flag;
    }
}