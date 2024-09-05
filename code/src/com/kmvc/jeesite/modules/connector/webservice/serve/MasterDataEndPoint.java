package com.kmvc.jeesite.modules.connector.webservice.serve;

import javax.jws.*;

@WebService(serviceName = "mdmSrv", targetNamespace = "http://www.mdmSrv.com.cn")
public interface MasterDataEndPoint
{
    @WebMethod
    @WebResult(name = "String")
    String codeSetQuerySrv(@WebParam(name = "codeSetQueryReqJson") final String p0);

    @WebMethod
    @WebResult(name = "String")
    String codeSetDetailQuerySrv(@WebParam(name = "codeSetQueryReqJson") final String p0);
}
