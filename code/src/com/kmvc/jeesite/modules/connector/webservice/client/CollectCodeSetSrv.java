package com.kmvc.jeesite.modules.connector.webservice.client;

import com.kmvc.jeesite.modules.sys.entity.TSystem;
import org.springframework.transaction.annotation.*;
import org.springframework.stereotype.*;

@Transactional(readOnly = true)
@Service
public interface CollectCodeSetSrv
{
    void collectSysCodeSets(final TSystem p0, final int p1);
}
