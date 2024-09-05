package com.kmvc.jeesite.modules.syncPortal.user.bo;

import java.util.*;

import com.kmvc.jeesite.modules.syncPortal.user.model.PortalUserPO;
import com.kmvc.jeesite.modules.syncPortal.user.model.*;

public interface PortalUserBO
{
    void synChange(final Date p0);

    void synAll(final Date p0);

    void syn(final PortalUserPO p0, final int p1, final Date p2);

    Integer getCount();

    Date getLastSynTime();
}
