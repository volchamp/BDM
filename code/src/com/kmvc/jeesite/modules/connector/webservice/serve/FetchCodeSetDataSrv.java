package com.kmvc.jeesite.modules.connector.webservice.serve;

import com.kmvc.jeesite.modules.connector.vo.serve.getcodeset.CollectCodeSetReq;
import com.kmvc.jeesite.modules.connector.vo.serve.getcodeset.CollectCodeSetResp;
import com.kmvc.jeesite.modules.connector.vo.serve.getcodeset.*;

public interface FetchCodeSetDataSrv
{
    CollectCodeSetResp fetchCodeSetData(final CollectCodeSetReq p0);
}
