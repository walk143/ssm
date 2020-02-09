package com.sloera.sso;

import com.sloera.mng.core.action.BaseController;
import com.sloera.mng.core.db.Record;
import com.sloera.person.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;

@Controller
public class MainController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/main", method = RequestMethod.POST)
    public String home(HttpServletResponse response) {
        try {
            String type = this.getPara("type");
            String account = this.getPara("userName");
            account = URLDecoder.decode(account,"UTF-8");
            String password = this.getPara("password");
            password = URLDecoder.decode(password,"UTF-8");
            Boolean flag = userService.isLegality(account,password);
            if(!flag)
                return login(response);
            String query = request.getQueryString();
        } catch (Exception e) {
            logger.error(e);
        }
//        return login(response);
        return defaultHtmlUrl();
    }

    /*
     * @Description 用户请求登录
     * @Return 页面
     * @Author liuwangyang
     * @Date 2019-12-14 11:38
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpServletResponse response) {
        Record record = (Record) WebUtils.getSessionAttribute(request, BaseController.USER_TOCKEN);
        String root = this.getCdnURL(request);
        request.setAttribute("command", root + "/main");
        request.setAttribute("root", root );
        return "login";
    }
    private String defaultHtmlUrl(){
        String defaultHtmlUrl = "list";
        return defaultHtmlUrl;
    }
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(HttpServletResponse response) {
        Record record = (Record) WebUtils.getSessionAttribute(request, BaseController.USER_TOCKEN);
        this.setAttr("root",getContextPath(request));
        return "register";
    }
}
