package com.sloera.person.user.dao;

import com.sloera.mng.core.ibatis.MapperBaseDao;
import com.sloera.person.user.po.UserBean;
import org.springframework.stereotype.Repository;

@Repository("userDao")
public class UserDao extends MapperBaseDao<UserBean> {
}
