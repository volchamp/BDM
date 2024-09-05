package com.kmvc.jeesite.modules.syncPortal.help;

import com.kmvc.jeesite.modules.syncPortal.user.model.PortalOrgPO;
import com.kmvc.jeesite.modules.syncPortal.user.model.PortalUserPO;
import com.kmvc.jeesite.modules.syncPortal.user.vo.PortalOrgChangeVO;
import com.kmvc.jeesite.modules.syncPortal.user.vo.PortalUserChangeVO;
import com.kmvc.jeesite.modules.syncPortal.utils.DateUtil;
import com.kmvc.jeesite.modules.syncPortal.utils.HttpUtil;
import com.kmvc.jeesite.modules.syncPortal.user.model.*;
import java.util.*;
import com.kmvc.jeesite.modules.syncPortal.utils.*;
import com.kmvc.jeesite.modules.syncPortal.user.vo.*;

public class SynHelper
{
    public static final String ORGALLPATH = "/sync/syncDatas/listOrganizations";
    public static final String ORGSYNPATH = "/sync/syncDatas/getOrganization";
    public static final String ORGCHAPATH = "/sync/syncDatas/listOrganizationChangeLog";
    public static final String USERALLPATH = "/sync/syncDatas/listUsers";
    public static final String USERSYNPATH = "/sync/syncDatas/GetUsers";
    public static final String USERCHAPATH = "/sync/syncDatas/listUserChangeLog";
    public static final String YTH_CODE = "007";
    public static final String YTH_AUTHTOKEN = "2400277864A0489A95E8E3E050F12A8F";
    public static final String YTH_PATH = "http://127.0.0.1:8080/portal/f";

    public static Map<String, String> getCommonParm() {
        final Map<String, String> urlVariables = new HashMap<String, String>();
        urlVariables.put("code", "007");
        urlVariables.put("authToken", "2400277864A0489A95E8E3E050F12A8F");
        return urlVariables;
    }

    public static String getCommonParamI() {
        return "?code=007&authToken=2400277864A0489A95E8E3E050F12A8F";
    }

    public static List<PortalOrgPO> sendOrgAll() {
        final String url = "http://127.0.0.1:8080/portal/f/sync/syncDatas/listOrganizations" + getCommonParamI();
        final List<PortalOrgPO> idList = HttpUtil.doGetAndToListBean(url, null, PortalOrgPO.class);
        return idList;
    }

    public static PortalOrgPO sendOrg(final String tyId) {
        final String url = "http://127.0.0.1:8080/portal/f/sync/syncDatas/getOrganization" + getCommonParamI() + "&orgId=" + tyId;
        final PortalOrgPO vo = HttpUtil.doGetAndToBean(url, null, PortalOrgPO.class);
        return vo;
    }

    public static List<PortalOrgChangeVO> sendOrgChange(final String time) {
        final String url = "http://127.0.0.1:8080/portal/f/sync/syncDatas/listOrganizationChangeLog" + getCommonParamI() + "&startTime=" + time;
        final List<PortalOrgChangeVO> changeList = HttpUtil.doGetAndToListBean(url, null, PortalOrgChangeVO.class);
        return changeList;
    }

    public static List<PortalUserPO> sendUserAll() {
        final String url = "http://127.0.0.1:8080/portal/f/sync/syncDatas/listUsers" + getCommonParamI();
        System.out.println("SynHelper sendUserAll:" + DateUtil.getFormatterValue(new Date(), "yyyy-MM-dd HH:mm:ss") + "URL:" + url);
        final List<PortalUserPO> idList = HttpUtil.doGetAndToListBean(url, null, PortalUserPO.class);
        return idList;
    }

    public static PortalUserPO sendUser(final String tyId) {
        final String url = "http://127.0.0.1:8080/portal/f/sync/syncDatas/GetUsers" + getCommonParamI() + "&Uid=" + tyId;
        final PortalUserPO vo = HttpUtil.doGetAndToBean(url, null, PortalUserPO.class);
        return vo;
    }

    public static List<PortalUserChangeVO> sendUserChange(final String time) {
        final String url = "http://127.0.0.1:8080/portal/f/sync/syncDatas/listUserChangeLog" + getCommonParamI() + "&startTime=" + time;
        final List<PortalUserChangeVO> changeList = HttpUtil.doGetAndToListBean(url, null, PortalUserChangeVO.class);
        return changeList;
    }
}
