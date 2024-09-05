package com.kmvc.jeesite.modules.syncPortal.user.rep;

import org.springframework.stereotype.*;
import java.util.*;
import java.text.*;

@Repository
public class PortalOrg
{
    public Integer getCount() {
        final Integer count = 0;
        return count;
    }

    public Date getLastSynTime() throws Exception {
        final String string = "2016-10-24 21:59:06";
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final Date parse = sdf.parse(string);
        return parse;
    }
}
