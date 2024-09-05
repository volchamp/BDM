package com.kmvc.jeesite.modules.connector.webservice.client;

import com.kmvc.jeesite.modules.common.FixedThreadPool;
import com.kmvc.jeesite.modules.common.SyncThreadPool;
import com.kmvc.jeesite.modules.sys.entity.TSystem;
import org.springframework.stereotype.*;
import org.slf4j.*;

import java.util.concurrent.*;
import com.thinkgem.jeesite.common.utils.*;
import com.kmvc.jeesite.modules.common.*;

@Repository("sysWebServiceProxy")
public class SysWebServiceProxy
{
    private SyncThreadPool pool;
    Logger log;

    public SysWebServiceProxy() {
        this.pool = new SyncThreadPool();
        this.log = LoggerFactory.getLogger((Class)this.getClass());
    }

    public void collectSysCodeSets(final TSystem system) {
        this.pool.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                final SysWebService sysWebService = (SysWebService)SpringContextHolder.getBean("sysWebService");
                sysWebService.collectSysCodeSets(system);
                return true;
            }
        });
    }

    public void asyncCommit() {
        this.pool.finish();
    }

    public void pushCodeSet2Systems(final String recordId, final TSystem system) {
        FixedThreadPool.exec(new Runnable() {
            @Override
            public void run() {
                try {
                    final SysWebService sysWebService = (SysWebService)SpringContextHolder.getBean("sysWebService");
                    sysWebService.pushCodeSet2Systems(recordId, system);
                }
                catch (Exception e) {
                    SysWebServiceProxy.this.log.error(e.getMessage(), (Throwable)e);
                }
            }
        });
    }
}
