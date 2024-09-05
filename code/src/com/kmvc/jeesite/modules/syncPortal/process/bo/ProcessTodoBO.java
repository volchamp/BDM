package com.kmvc.jeesite.modules.syncPortal.process.bo;

import com.kmvc.jeesite.modules.syncPortal.process.vo.ProcessCountVO;
import com.kmvc.jeesite.modules.syncPortal.process.vo.ProcessVO;
import org.springframework.transaction.annotation.*;
import java.util.*;
import org.apache.shiro.authc.*;
import com.kmvc.jeesite.modules.syncPortal.process.vo.*;

@Transactional(readOnly = true)
public interface ProcessTodoBO
{
    List<ProcessCountVO> getTodoCount(final String... p0);

    List<ProcessVO> getTodoList(final Account p0);
}
