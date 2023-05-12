package filter;


import Pojo.Browse;
import Pojo.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebFilter(urlPatterns = "/*")
public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        User user = (User) request.getSession().getAttribute("user");
        String url = request.getRequestURL().toString();
        String param = request.getQueryString();
//        System.out.println("url:"+url);
//        System.out.println("param"+param);
        String s = "admin";
        if(url.contains(s)){  // 判断是否访问后台界面
            if(user==null || user.getIdentity()==1){   //判断是否为管理员用户
            response.sendRedirect("../index.jsp"); //重定向到index界面
            }else{
                chain.doFilter(request,response);
            }
        }else{
            chain.doFilter(request,response);
        }
    }

    @Override
    public void destroy() {

    }
}
