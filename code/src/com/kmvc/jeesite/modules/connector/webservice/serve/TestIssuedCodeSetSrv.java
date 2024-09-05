package com.kmvc.jeesite.modules.connector.webservice.serve;

import javax.jws.*;

@WebService(serviceName = "mdmSrv", targetNamespace = "http://www.mdmSrv.com.cn")
public interface TestIssuedCodeSetSrv
{
    @WebMethod
    @WebResult(name = "String")
    String codeSetDistSrv(@WebParam(name = "codeSetDataReqJson") final String p0);

    @WebMethod
    @WebResult(name = "String")
    String codeSetCollSrv(@WebParam(name = "codeSetDataReqJson") final String p0);
}
