package com.kmvc.jeesite.modules.syncPortal.user.rep;

import com.kmvc.jeesite.modules.syncPortal.user.model.PortalUserSynPO;
import org.springframework.stereotype.*;
import com.kmvc.jeesite.modules.syncPortal.user.model.*;
import java.util.*;
import org.apache.shiro.authc.*;

@Repository
public class PortalUserSyn
{
    public List<PortalUserSynPO> findByMap(final Map<String, Object> map, final boolean like) {
        String hql = "from PortalUserSynPO o where 1=1 ";
        final List<Object> args = new ArrayList<Object>();
        for (final String key : map.keySet()) {
            if (like) {
                hql = hql + " and o." + key + " like ? ";
                args.add("%" + map.get(key) + "%");
            }
            else {
                hql = hql + " and o." + key + " = ? ";
                args.add(map.get(key));
            }
        }
        return null;
    }

    public List<Account> findByPortalLoginName(final String pln) {
        return null;
    }

    public PortalUserSynPO findBy2LoginName(final String pln, final String sln) {
        return null;
    }
}
