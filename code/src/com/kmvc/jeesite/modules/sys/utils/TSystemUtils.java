package com.kmvc.jeesite.modules.sys.utils;

import com.kmvc.jeesite.modules.sys.dao.TSystemDao;
import com.kmvc.jeesite.modules.sys.entity.TSystem;

import java.util.*;

import com.thinkgem.jeesite.common.utils.*;

public class TSystemUtils
{
    private static TSystemDao systemDao;

    public static List<TSystem> getAllSystem() {
        final TSystem system = new TSystem();
        final List<TSystem> list = (List<TSystem>)TSystemUtils.systemDao.findAllList(system);
        return list;
    }

    static {
        TSystemUtils.systemDao = (TSystemDao)SpringContextHolder.getBean((Class)TSystemDao.class);
    }
}
