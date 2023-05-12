package servlet;

import Pojo.*;
import com.alibaba.fastjson.JSON;
import service.TypeService;
import service.impl.GoodsServiceImpl;
import service.impl.OrderServiceImpl;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import service.impl.TypeServiceImpl;
import service.impl.UserServiceImpl;
import utils.ipUtils;
import utils.operateLogUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@WebServlet("/adminGoods/*")
public class AdminGoodsServlet extends baseServlet {
    private GoodsServiceImpl goodsService = new GoodsServiceImpl();
    private TypeServiceImpl typeService = new TypeServiceImpl();
    private UserServiceImpl userService = new UserServiceImpl();
    public void goodsList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int type = 0;//推荐类型
        if(request.getParameter("type") != null) {
            type=Integer.parseInt(request.getParameter("type") ) ;
        }
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

        Page p=null;
        if(type==0){  // 当type==0 时查询所有商品
            p = goodsService.selectPageByTypeId(0, pageNumber);
        }else{
            p = goodsService.getGoodsRecommendPage(type, pageNumber);
        }

        if(p.getTotalPage()==0)
        {
            p.setTotalPage(1);
            p.setPageNumber(1);
        }
        else {
            if(pageNumber>=p.getTotalPage()+1)
            {
                p = goodsService.getGoodsRecommendPage(type, pageNumber);
            }
        }
        if(request.getSession().getAttribute("typeList")==null){
            List<Type> typeList = typeService.getAllTypes();
            request.getSession().setAttribute("typeList",typeList);
        }
        User user = (User) request.getSession().getAttribute("user");
        request.setAttribute("p", p);
        request.setAttribute("type", type);
        operateLogUtils.addOperateLog(request,"查询了商品列表");
        request.getRequestDispatcher("/admin/goods_list.jsp").forward(request, response);


    }

    // 添加商品
    public void goodsAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DiskFileItemFactory factory=new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            List<FileItem> list = upload.parseRequest(request);
            Goods g = new Goods();
            for(FileItem item:list) {
                if(item.isFormField()) { // 普通表单数据
                    switch(item.getFieldName()) { //获取参数并存入Goods对象
                        case "name":
                            g.setName(item.getString("utf-8"));
                            break;
                        case "price":
                            g.setPrice(Integer.parseInt(item.getString("utf-8")));
                            break;
                        case "intro":
                            g.setIntro(item.getString("utf-8"));
                            break;
                        case "stock":
                            g.setStock(Integer.parseInt(item.getString("utf-8")));
                            break;
                        case "typeid":
                            g.setTypeid(Integer.parseInt(item.getString("utf-8")));
                            break;
                    }
                }else {
                    if(item.getInputStream().available()<=0)continue;
                    String fileName = item.getName();  // 获取表单中文件名称和路径
                    fileName = fileName.substring(fileName.lastIndexOf("."));
                    fileName = "/"+new Date().getTime()+fileName;
                    String path = this.getServletContext().getRealPath("picture")+fileName;
                    InputStream in = item.getInputStream();
                    FileOutputStream out = new FileOutputStream(path);
                    byte[] buffer = new byte[1024];
                    int len=0;
                    while( (len=in.read(buffer))>0 ) { // 通过io流写文件到指定目录
                        out.write(buffer);
                    }
                    in.close();
                    out.close();
                    item.delete();
                    switch(item.getFieldName()) { // 将图片路径保存到Goods对象
                        case "cover":
                            g.setCover("picture"+fileName);
                            break;
                        case "image1":
                            g.setImage1("picture"+fileName);
                            break;
                        case "image2":
                            g.setImage2("picture"+fileName);
                            break;
                    }
                }
            }
            goodsService.addGoods(g);
            User user = (User) request.getSession().getAttribute("user");

            operateLogUtils.addOperateLog(request,"添加了商品名为"+g.getName()+"的商品");

            if(user.getIdentity()==2){
                request.getRequestDispatcher("http://119.91.22.246:8080/Mc/adminGoods/saleManGoodsList").forward(request, response);
            }else{
                request.getRequestDispatcher("http://119.91.22.246:8080/Mc/adminGoods/goodsList").forward(request, response);
            }
        } catch (FileUploadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    // 删除商品
    public void goodsDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean flag = goodsService.deleteGoods(id);
        User user = (User) request.getSession().getAttribute("user");

        if(flag){
            request.setAttribute("msg","删除成功");
            operateLogUtils.addOperateLog(request,"删除了id为"+id+"的商品");

        }else{
            request.setAttribute("failMsg","已有订单中包含该商品，无法直接删除该商品");
        }


        if(user.getIdentity()==2){
            request.getRequestDispatcher("http://119.91.22.246:8080/Mc/adminGoods/saleManGoodsList").forward(request, response);
        }else{
            request.getRequestDispatcher("http://119.91.22.246:8080/Mc/adminGoods/goodsList").forward(request, response);
        }
    }

    // 显示要编辑的商品信息
    public void goodsEditShow(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Goods g = goodsService.getGoodsById(id);
        request.setAttribute("g", g);
        request.getRequestDispatcher("/admin/goods_edit.jsp").forward(request, response);
    }

    // 编辑商品
    public void goodsEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DiskFileItemFactory factory=new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            List<FileItem> list = upload.parseRequest(request);
            Goods g = new Goods();
            int pageNumber =1;
            int type=0;
            for(FileItem item:list) {
                if(item.isFormField()) {
                    switch(item.getFieldName()) {
                        case "id":
                            g.setId(Integer.parseInt(item.getString("utf-8")));
                            break;
                        case "name":
                            g.setName(item.getString("utf-8"));
                            break;
                        case "price":
                            g.setPrice(Float.parseFloat(item.getString("utf-8")));
                            break;
                        case "intro":
                            g.setIntro(item.getString("utf-8"));
                            break;
                        case "cover":
                            g.setCover(item.getString("utf-8"));
                            break;
                        case "image1":
                            g.setImage1(item.getString("utf-8"));
                            break;
                        case "image2":
                            g.setImage2(item.getString("utf-8"));
                            break;
                        case "stock":
                            g.setStock(Integer.parseInt(item.getString("utf-8")));
                            break;
                        case "typeid":
                            g.setTypeid(Integer.parseInt(item.getString("utf-8")));
                            break;
                        case "pageNumber":
                            pageNumber=Integer.parseInt(item.getString("utf-8"));
                            break;
                        case "type":
                            type = Integer.parseInt(item.getString("utf-8"));
                            break;
                    }
                }else {
                    if(item.getInputStream().available()<=0)continue;
                    String fileName = item.getName();
                    fileName = fileName.substring(fileName.lastIndexOf("."));
                    fileName = "/"+new Date().getTime()+fileName;
                    String path = this.getServletContext().getRealPath("picture")+fileName;
                    InputStream in = item.getInputStream();
                    FileOutputStream out = new FileOutputStream(path);
                    byte[] buffer = new byte[1024];
                    int len=0;
                    while( (len=in.read(buffer))>0 ) {
                        out.write(buffer);
                    }
                    in.close();
                    out.close();
                    item.delete();
                    switch(item.getFieldName()) {
                        case "cover":
                            g.setCover("picture"+fileName);
                            break;
                        case "image1":
                            g.setImage1("picture"+fileName);
                            break;
                        case "image2":
                            g.setImage2("picture"+fileName);
                            break;
                    }
                }
            }
            operateLogUtils.addOperateLog(request,"编辑了商品id为"+g.getId()+"的商品");
            goodsService.editGoods(g);

            User user = (User)request.getSession().getAttribute("user");
            if(user.getIdentity()==2){
                request.getRequestDispatcher("http://119.91.22.246:8080/Mc/adminGoods/saleManGoodsList?pageNumber="+pageNumber+"&type="+type).forward(request, response);
            }else{
                request.getRequestDispatcher("http://119.91.22.246:8080/Mc/adminGoods/goodsList?pageNumber="+pageNumber+"&type="+type).forward(request, response);
            }

        } catch (FileUploadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void goodsRecommend(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String method = request.getParameter("method");
        int typeTarget = Integer.parseInt(request.getParameter("typeTarget"));
        if(method.equals("add")){  // 加入还是移出
            goodsService.addRecommend(id,typeTarget);
        }else{
            goodsService.removeRecommend(id, typeTarget);
        }
        User user = (User) request.getSession().getAttribute("user");

        request.getRequestDispatcher("http://119.91.22.246:8080/Mc/adminGoods/goodsList").forward(request,response);

    }


    public void saleManGoodsList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        int typeId=0;
//        if(request.getParameter("typeid")!=null)
//        {
//            typeId=Integer.parseInt(request.getParameter("typeid"));
//        }
//        System.out.println("typeid="+typeId);
        User user = (User)request.getSession().getAttribute("user");

        // 记录查询用户查询商品销售情况的操作
//        Operate operate = new Operate();
//        operate.setUser(user);
//        operate.setDescription("查询了商品销售情况");
//        ipUtils ipUtils = new ipUtils();
//        String ipAddr = ipUtils.getIpAddr(request);
//        operate.setIp(ipAddr);
//        operate.setTime(new Date());
//        userService.addOperateLog(operate);
        operateLogUtils.addOperateLog(request,"查询了商品销售情况");

        int typeId = typeService.getTypeIdByUserId(user.getId());
        int pageNumber=1;            // 商品页数
        if(request.getParameter("pageNumber")!=null) {
            try {
                pageNumber=Integer.parseInt(request.getParameter("pageNumber"));
            }
            catch (Exception e) {
            }
        }

        if(pageNumber<=0)
            pageNumber=1;
        // 根据typeId获取商品，并对获取到的商品集合进行分页
        Page p=goodsService.selectPageByTypeId(typeId,pageNumber);
        if(p.getTotalPage()==0) {
            p.setTotalPage(1);
            p.setPageNumber(1);
        }
        else {
            if(pageNumber>=p.getTotalPage()+1) {
                p=goodsService.selectPageByTypeId(typeId,p.getTotalPage());
            }
        }
//        System.out.println(p);
        if(request.getSession().getAttribute("typeList")==null){
            List<Type> typeList = typeService.getAllTypes();
            request.getSession().setAttribute("typeList",typeList);
        }

        operateLogUtils.addOperateLog(request,"查询了销售员的业绩");
        request.setAttribute("p",p);
        request.setAttribute("type",typeId);
        request.getRequestDispatcher("/admin/saleman_goods_list.jsp").forward(request,response);
        return;
    }

    public void abnormalGoods(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Integer currentPage = Integer.parseInt(request.getParameter("currentPage"));
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));

        PageBean<Goods> abnormalGoods = goodsService.getAbnormalGoods(currentPage,pageSize);
        String jsonString = JSON.toJSONString(abnormalGoods);
        response.setContentType("text/json;charset=utf-8");
        response.getWriter().write(jsonString);
    }
}
