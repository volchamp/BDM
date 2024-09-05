package com.kmvc.jeesite.modules.syncPortal.user.bo;

import org.springframework.transaction.annotation.*;
import org.springframework.stereotype.*;
import org.apache.shiro.authc.*;

@Transactional(readOnly = true)
@Service
public interface PortalUserSynBO
{
    @Transactional(readOnly = false, rollbackFor = { Throwable.class })
    void synDel(final Integer p0);

    @Transactional(readOnly = false, rollbackFor = { Throwable.class })
    void synUser(final String p0, final String[] p1);

    Account findByPortalLoginName(final String p0, final boolean p1);
}
