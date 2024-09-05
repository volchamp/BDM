package com.kmvc.jeesite.modules.scheduler;

import com.kmvc.jeesite.modules.common.dao.CommonDao;
import com.kmvc.jeesite.modules.common.entity.CommonEntity;
import org.slf4j.*;
import com.kmvc.jeesite.modules.common.dao.*;
import com.thinkgem.jeesite.common.utils.*;
import com.kmvc.jeesite.modules.common.entity.*;
import com.thinkgem.jeesite.modules.sys.utils.*;
import com.thinkgem.jeesite.common.config.*;
import java.util.*;
import org.quartz.*;

public class UserCheckJob extends BaseJob
{
    private final Logger logger;

    public UserCheckJob() {
        this.logger = LoggerFactory.getLogger((Class)this.getClass());
    }

    @Override
    public void executeJob(final JobExecutionContext context) throws JobExecutionException {
        try {
            this.logger.info("=======\u5f00\u59cb\u6267\u884c\u4efb\u52a1, UserCheckJob->executeJob()");
            final long curTimeMillis = System.currentTimeMillis();
            final CommonDao commonDao = (CommonDao)SpringContextHolder.getBean((Class)CommonDao.class);
            final CommonEntity comEntCriteria = new CommonEntity();
            final String yes = DictUtils.getDictValue("\u662f", "yes_no", "1");
            final String no = DictUtils.getDictValue("\u5426", "yes_no", "0");
            comEntCriteria.setLoginFlag(yes);
            final List<CommonEntity> userList = (List<CommonEntity>)commonDao.findUserList(comEntCriteria);
            final String pswExpireDay = Global.getConfig("pswExpireDay");
            double pswExpDayDoub = 90.0;
            try {
                pswExpDayDoub = Double.parseDouble(pswExpireDay);
            }
            catch (Exception e2) {
                pswExpDayDoub = 90.0;
            }
            for (final CommonEntity user : userList) {
                final long lgDateMilSec = user.getLoginDate().getTime();
                if (curTimeMillis - lgDateMilSec > pswExpDayDoub * 8.64E7) {
                    user.setLoginFlag(no);
                    user.setUpdateDate(new Date(curTimeMillis));
                    if (user.getCurrentUser() != null) {
                        user.setUpdateBy(user.getCurrentUser());
                    }
                    commonDao.updUserById(user);
                }
            }
            this.logger.info("=======\u4efb\u52a1\u7ed3\u675f, UserCheckJob->executeJob()");
        }
        catch (Exception e) {
            this.logger.error(e.getMessage(), (Throwable)e);
        }
    }

    public static void main(final String[] args) {
    }
}
