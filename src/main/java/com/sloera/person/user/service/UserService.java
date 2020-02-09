package com.sloera.person.user.service;

import com.sloera.person.user.dao.UserDao;
import com.sloera.person.user.po.UserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service("userService")
@Transactional
public class UserService {
    @Autowired
    private UserDao userDao;
    public Boolean isLegality(String account, String password){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("account", account);
        map.put("password", password);
        UserBean userBean = userDao.selectOne("com.sloera.person.user.userInfo.findUser",map);
        if(null != userBean)
            return true;
        else
            return false;

    }
    public UserBean findByAccount(String account){
        return this.userDao.selectOne("com.sloera.person.user.userInfo.findByAccount",account);
    }
}
