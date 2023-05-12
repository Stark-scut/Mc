package utils;

import Pojo.Operate;
import Pojo.User;
import service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class operateLogUtils {
    public static void addOperateLog(HttpServletRequest request, String description) {
        UserServiceImpl userService = new UserServiceImpl();
        Operate operate = new Operate();
        User user = (User)request.getSession().getAttribute("user");
        System.out.println("user11");
        operate.setUser(user);
        System.out.println(user);
        operate.setDescription(description);
        ipUtils ipUtils = new ipUtils();
        String ipAddr = ipUtils.getIpAddr(request);
        operate.setIp(ipAddr);
        operate.setTime(new Date());
        userService.addOperateLog(operate);
    }
}
