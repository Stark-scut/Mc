package servlet;

import Pojo.*;
import com.alibaba.fastjson.JSON;
import service.UserService;
import service.impl.OrderServiceImpl;
import service.impl.UserServiceImpl;
import service.impl.TypeServiceImpl;
import utils.operateLogUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;

@WebServlet("/adminUser/*")
public class AdminUserServlet extends baseServlet {

    private UserServiceImpl userService = new UserServiceImpl();
    private TypeServiceImpl typeService = new TypeServiceImpl();


    public void userList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int pageNumber = 1;
        if(request.getParameter("pageNumber") != null) {
            try {
                pageNumber=Integer.parseInt(request.getParameter("pageNumber") ) ;
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }

        }
        if(pageNumber<=0)
            pageNumber=1;
        Page p = userService.getUserPage(pageNumber);
        if(p.getTotalPage()==0)
        {
            p.setTotalPage(1);
            p.setPageNumber(1);
        }
        else {
            if(pageNumber>=p.getTotalPage()+1)
            {
                p = userService.getUserPage(pageNumber);
            }
        }
        User user = (User)request.getSession().getAttribute("user");
        operateLogUtils.addOperateLog(request,"查询了用户列表");
        request.setAttribute("p", p);
        request.getRequestDispatcher("/admin/user_list.jsp").forward(request, response);
    }

    public void userAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = new User();
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        user.setUserName(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setName(name);
        user.setPhone(phone);
        user.setAddress(address);
        user.setIdentity(1);
        user.setValidate(true);
        if(userService.register(username, email)){
            request.setAttribute("msg","客户添加成功！");
            userService.add(user);
            operateLogUtils.addOperateLog(request,"添加了用户名为"+username+"的用户");
            request.getRequestDispatcher("http://119.91.22.246:8080/Mc/adminUser/userList").forward(request,response);
        }else{
            request.setAttribute("failMsg","用户名或邮箱重复，请重新填写！");
            request.setAttribute("u",user);
            request.getRequestDispatcher("/admin/user_add.jsp").forward(request,response);
        }
    }

    public void userEditShow(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        User user = userService.getUserById(id);
        request.setAttribute("u",user);
        request.getRequestDispatcher("/admin/user_edit.jsp").forward(request,response);
    }

    public void userEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = new User();

        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        user.setId(id);
        user.setName(name);
        user.setPhone(phone);
        user.setAddress(address);
        operateLogUtils.addOperateLog(request,"修改了id为"+id+"的用户的信息");
        userService.editUser(user);
        request.getRequestDispatcher("http://119.91.22.246:8080/Mc/adminUser/userList").forward(request,response);
    }

    public void userDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean isSuccess = userService.deleteUser(id);
        if(isSuccess){
            request.setAttribute("msg","客户删除成功");
            operateLogUtils.addOperateLog(request,"删除了id为"+id+"的用户");
        }else{
            request.setAttribute("failMsg","客户下有订单，请先删除该客户下的订单，再删除该客户！");
        }
        request.getRequestDispatcher("http://119.91.22.246:8080/Mc/adminUser/userList").forward(request,response);
    }

    public void userReset(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String password = request.getParameter("password");
        userService.changePwd(password,id);
        operateLogUtils.addOperateLog(request,"重置了id为"+id+"的用户的密码");
        request.getRequestDispatcher("http://119.91.22.246:8080/Mc/adminUser/userList").forward(request,response);
    }

    public void recordList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int pageNumber = 1;
        if(request.getParameter("pageNumber") != null) {
            try {
                pageNumber=Integer.parseInt(request.getParameter("pageNumber") ) ;
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
        }
        if(pageNumber<=0)
            pageNumber=1;
        Page p = userService.getRecordPage(pageNumber);
        if(p.getTotalPage()==0)
        {
            p.setTotalPage(1);
            p.setPageNumber(1);
        }
        else {
            if(pageNumber>=p.getTotalPage()+1)
            {
                p = userService.getRecordPage(pageNumber);
            }
        }
        operateLogUtils.addOperateLog(request,"查询了用户浏览记录");
        request.setAttribute("p", p);
        request.getRequestDispatcher("/admin/record_list.jsp").forward(request, response);
    }

    public void browseLog(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int typeId;
        int currentPage = Integer.parseInt(request.getParameter("currentPage"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        // 根据用户类别确定typeId
        User user = (User) request.getSession().getAttribute("user");

        operateLogUtils.addOperateLog(request,"查询了用户浏览日志");

        if(user.getIdentity()==2){
            typeId = typeService.getTypeIdByUserId(user.getId());
        }else{
            typeId = Integer.parseInt(request.getParameter("typeId"));
        }
        PageBean<Browse> page = userService.getBrowseLog(typeId, currentPage, pageSize);
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        for(Browse b : page.getRows()){
            b.setFormatTime(ft.format(b.getTime()));
        }
        String jsonString = JSON.toJSONString(page);
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(jsonString);
    }

    public void operateLog(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int currentPage = Integer.parseInt(request.getParameter("currentPage"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        PageBean<Operate> page = userService.getOperateLog(currentPage, pageSize);
        String jsonString = JSON.toJSONString(page);
        User user = (User)request.getSession().getAttribute("user");
        System.out.println("page");
        System.out.println(page);
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(jsonString);
    }

    public void userRecommend(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");

    }



}
