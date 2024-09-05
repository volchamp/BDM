package com.kmvc.jeesite.modules.connector.webservice.client;

import com.kmvc.jeesite.modules.sys.entity.TSystem;

public interface SysWebService
{
    void collectSysCodeSets(final TSystem p0);

    void pushCodeSet2Systems(final String p0, final TSystem p1);
}
