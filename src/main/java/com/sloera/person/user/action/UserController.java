package com.sloera.person.user.action;

import com.sloera.mng.core.action.BaseController;
import com.sloera.mng.core.utils.CTools;
import com.sloera.person.user.dao.UserDao;
import com.sloera.person.user.po.UserBean;
import com.sloera.person.user.service.UserService;
import com.sloera.utils.CCalendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping(value = "person/user")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void register(HttpServletRequest request, HttpServletResponse response){
        String userName = this.getPara("userName");
        String account = this.getPara("account");
        String password = this.getPara("password");
        String sex = this.getPara("sex");
        String email = this.getPara("email");
        String birthday = this.getPara("birthday");
        SimpleDateFormat df = new SimpleDateFormat(CCalendar.day);
        Date birth = null;
        try {
            birth= df.parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
            logger.error(e);
        }
        try {
            UserBean oldUserBean = userService.findByAccount(account);
            if(null != oldUserBean){
                this.renderText(response,"已有账号为："+account+"的用户存在");
                return;
            }
        } catch (Exception e){
            System.err.println(e);
            logger.error(e);
            this.renderText(response,e.getMessage());
        }
        UserBean userBean = new UserBean();
        userBean.setId(CTools.getUUID());
        userBean.setUserName(userName);
        userBean.setAccount(account);
        userBean.setPassword(password);
        userBean.setSex(sex);
        userBean.setAge(CCalendar.getAge(birth));
        userBean.setEmail(email);
        userBean.setActive("1");
        userBean.setVersion(1);
        userBean.setBirthday(birth);
        try {
            int res = userDao.save("com.sloera.person.user.userInfo.insert",userBean);
            this.renderText(response,String.valueOf(res));
        }catch (Exception e){
            e.printStackTrace();
            logger.error(e);
            this.renderText(response,e.getMessage());
        }

    }

}
