package com.kmvc.jeesite.modules.sys.dao;

/**
 * @author lsm
 * @description
 * @date 2024/9/5 15:34
 */
import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.User;
import java.util.List;

@MyBatisDao
public interface UserDao extends CrudDao<User> {
    User getByLoginName(User var1);

    List<User> findUserByOfficeId(User var1);

    long findAllCount(User var1);

    int updatePasswordById(User var1);

    int updateLoginInfo(User var1);

    int deleteUserRole(User var1);

    int insertUserRole(User var1);

    int updateUserInfo(User var1);

    int insertUserOffice(User var1);

    int deleteUserOffice(User var1);
}