package com.kmvc.jeesite.modules.syncPortal.user.rep;

import com.kmvc.jeesite.modules.syncPortal.user.model.PortalUserPO;
import org.springframework.stereotype.*;
import java.util.*;
import java.text.*;
import com.kmvc.jeesite.modules.syncPortal.user.model.*;
import org.apache.shiro.authc.*;

@Repository
public class PortalUser
{
    public Integer getCount() {
        return 0;
    }

    public Date getLastSynTime() throws Exception {
        final String string = "2016-10-24 21:59:06";
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final Date parse = sdf.parse(string);
        return parse;
    }

    public PortalUserPO getByLoginName(final String pln) {
        return null;
    }

    public void save(final Account account) {
    }
}
