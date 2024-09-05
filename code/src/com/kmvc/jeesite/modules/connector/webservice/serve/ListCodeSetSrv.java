package com.kmvc.jeesite.modules.connector.webservice.serve;

import com.kmvc.jeesite.modules.connector.vo.serve.inquire.ListCodeSetReq;
import com.kmvc.jeesite.modules.connector.vo.serve.inquire.ListCodeSetResp;
import org.springframework.transaction.annotation.*;
import org.springframework.stereotype.*;
import com.kmvc.jeesite.modules.connector.vo.serve.inquire.*;

@Transactional(readOnly = true)
@Service
public interface ListCodeSetSrv
{
    ListCodeSetResp listCodeSet(final ListCodeSetReq p0);
}
