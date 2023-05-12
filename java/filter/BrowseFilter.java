package filter;


import Pojo.Browse;
import Pojo.User;
import service.impl.UserServiceImpl;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebFilter(urlPatterns = "/*")
public class BrowseFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Browse flag = (Browse)request.getSession().getAttribute("browse");
        User user = (User) request.getSession().getAttribute("user");
        if(flag==null)    // session 中还没有browse的记录
        {
            String param = request.getQueryString();
            if(param!=null && param.contains("goodsDetail?id=") && user!=null)  // 访问的是商品详情页需要在session中添加browse
            {
                Browse browse = new Browse();
                browse.setUser(user);
                String s = param.substring(param.indexOf('=')+1);
                Integer id = Integer.parseInt(s);  // 获取商品id
                browse.setGoodsId(id);
                Date date = new Date();
                browse.setTime(date);   // 设置访问的开始时间
                request.getSession().setAttribute("browse",browse);
                System.out.println(browse);
                chain.doFilter(request,response);
            }else{   // 访问的不是商品详情页
                chain.doFilter(request,response);
            }
        }
        else{  // 有browse记录
            Browse browse = (Browse) request.getSession().getAttribute("browse");
            Date t1 = browse.getTime();
            Date t2 = new Date();
            long start = t1.getTime();
            long end = t2.getTime();
            int duration = (int)((end-start)/1000);
            browse.setDuration(duration);
            UserServiceImpl userService = new UserServiceImpl();
            userService.addBrowseLog(browse);   // 将已有的记录加入数据库
            request.getSession().removeAttribute("browse");
            String param = request.getQueryString();

            if(param!=null && param.contains("goodsDetail?id=") && user!=null)  // 访问的是商品详情页需要在session中添加browse
            {

                Browse browse1 = new Browse();
                browse1.setUser(user);
                String s = param.substring(param.indexOf('=')+1);
                Integer id = Integer.parseInt(s);  // 获取商品id
                browse1.setGoodsId(id);
                Date date = new Date();
                browse1.setTime(date);   // 设置访问的开始时间
                request.getSession().setAttribute("browse",browse1);
                chain.doFilter(request,response);
            }else{   // 访问的不是商品详情页
                chain.doFilter(request,response);
            }

        }
    }

    @Override
    public void destroy() {

    }
}
