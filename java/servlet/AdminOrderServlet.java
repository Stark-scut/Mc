package servlet;

import Pojo.*;
import com.alibaba.fastjson.JSON;
import service.impl.OrderServiceImpl;
import service.impl.TypeServiceImpl;
import service.impl.UserServiceImpl;
import utils.ipUtils;
import utils.operateLogUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@WebServlet("/adminOrder/*")
public class AdminOrderServlet extends baseServlet {
    private OrderServiceImpl orderService = new OrderServiceImpl();
    private TypeServiceImpl typeService = new TypeServiceImpl();
    private UserServiceImpl userService = new UserServiceImpl();

    public void orderList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int status = 0;
        if(request.getParameter("status") != null) {
            status=Integer.parseInt(request.getParameter("status") ) ;
        }
        request.setAttribute("status", status);
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
        Page p = orderService.getOrderPage(status,pageNumber);
        if(p.getTotalPage()==0)
        {
            p.setTotalPage(1);
            p.setPageNumber(1);

        }
        else {
            if(pageNumber>=p.getTotalPage()+1)
            {
                p = orderService.getOrderPage(status,pageNumber);
            }
        }
        User user = (User)request.getSession().getAttribute("user");
        operateLogUtils.addOperateLog(request,"查询了订单管理页面");
        request.setAttribute("p", p);
        request.getRequestDispatcher("/admin/order_list.jsp").forward(request, response);
    }

    public void orderStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        int status = Integer.parseInt(request.getParameter("status"));
        orderService.updateStatus(id, status);
        response.sendRedirect("http://119.91.22.246:8080/Mc/adminOrder/orderList");
    }

    public void orderDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        orderService.delete(id);
        operateLogUtils.addOperateLog(request,"删除了订单id为"+id+"的订单");

        response.sendRedirect("http://119.91.22.246:8080/Mc/adminOrder/orderList");
    }

    public void purchaseLog(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int typeId;
        int currentPage = Integer.parseInt(request.getParameter("currentPage"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        // 根据用户类别确定typeId
        User user = (User) request.getSession().getAttribute("user");
        if(user.getIdentity()==2){
            typeId = typeService.getTypeIdByUserId(user.getId());
        }else{
            typeId = Integer.parseInt(request.getParameter("typeId"));
        }
        // 记录查询用户购买日志的操作
//        Operate operate = new Operate();
//        operate.setUser(user);
//        operate.setDescription("查询了用户购买日志");
//        ipUtils ipUtils = new ipUtils();
//        String ipAddr = ipUtils.getIpAddr(request);
//        operate.setIp(ipAddr);
//        operate.setTime(new Date());
//        userService.addOperateLog(operate);
        operateLogUtils.addOperateLog(request,"查询了用户购买日志");

        PageBean<PurchaseLog> page = orderService.getPurchaseLog(typeId, currentPage, pageSize);
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        for(PurchaseLog p : page.getRows()){
            p.setFormatTime(ft.format(p.getTime()));
//            System.out.println(p);
        }
        // 转为json
        String jsonString = JSON.toJSONString(page);
//        System.out.println(jsonString);
        // 写数据
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(jsonString);
    }

    public void salemanTurnover(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int typeId;
        typeId = Integer.parseInt(request.getParameter("typeId"));
        int turnover = orderService.getTurnover(typeId);
//        System.out.println("营业额"+turnover);
        String jsonString = JSON.toJSONString(turnover);

        User user = (User)request.getSession().getAttribute("user");
        operateLogUtils.addOperateLog(request,"查询了销售员业绩");
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(jsonString);
//        System.out.println(jsonString);

    }

}
