<%--
  Created by IntelliJ IDEA.
  User: 19767
  Date: 2018/11/26
  Time: 16:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<%--    <%--%>
<%--        String path = request.getContextPath();--%>
<%--        String basePath = request.getScheme() + "://" + request.getServerName() + ":" +--%>
<%--                request.getServerPort() + path + "/";--%>
<%--    %>--%>
<%--    <base href="<%=basePath%>">--%>
    <title>首页</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link type="text/css" rel="stylesheet" href="http://119.91.22.246:8080/McDonalds_web/css/bootstrap.css">
    <link type="text/css" rel="stylesheet" href="http://119.91.22.246:8080/McDonalds_web/css/style.css">
    <script type="text/javascript" src="http://119.91.22.246:8080/McDonalds_web/js/jquery.min.js"></script>
    <script type="text/javascript" src="http://119.91.22.246:8080/McDonalds_web/js/bootstrap.min.js"></script>
<%--    <script type="text/javascript" src="<%=basePath%>js/simpleCart.min.js"></script>--%>
    <script type="text/javascript" src="http://119.91.22.246:8080/McDonalds_web/layer/layer.js"></script>
    <script type="text/javascript" src="http://119.91.22.246:8080/McDonalds_web/js/cart.js"></script>
</head>
<body>







<!--header-->
<jsp:include page="header.jsp">
    <jsp:param name="flag" value="2"></jsp:param>
</jsp:include>
<!--//header-->



<!--products-->
<div class="products">
    <div class="container">
        <h2><c:choose><c:when test="${empty t}">全部系列</c:when><c:otherwise>${t.name}</c:otherwise> </c:choose></h2>

        <div class="col-md-12 product-model-sec">
<%--            <script>console.log("typeList")</script>--%>
<%--            <script>console.log(${typeList})</script>--%>

            <c:forEach items="${p.list}" var="g">
                <div class="product-grid">
                    <a href="http://119.91.22.246:8080/McDonalds/goods/goodsDetail?id=${g.id}">
                        <div class="more-product"><span> </span></div>
                        <div class="product-img b-link-stripe b-animate-go  thickbox">
                            <img src="http://119.91.22.246:8080/McDonalds_web/${g.cover}" class="img-responsive" alt="${g.name}" width="240" height="240">
                            <div class="b-wrapper">
                                <h4 class="b-animate b-from-left  b-delay03">
                                    <button href="http://119.91.22.246:8080/McDonalds/goods/goodsDetail?id=${g.id}">查看详情</button>
                                </h4>
                            </div>
                        </div>
                    </a>
                    <div class="product-info simpleCart_shelfItem">
                        <div class="product-info-cust prt_name">
                            <h4>${g.name}</h4>
                            <span class="item_price">¥ ${g.price}</span>
                            <input type="button" class="item_add items" value="加入购物车" onclick="buy(${g.id})">
                            <div class="clearfix"> </div>
                        </div>
                    </div>
                </div>
            </c:forEach>

        </div>

        <jsp:include page="page.jsp">
            <jsp:param name="url" value="http://119.91.22.246:8080/McDonalds/goods/GoodsList"></jsp:param>
            <jsp:param name="param" value="&typeid=${id}"></jsp:param>
        </jsp:include>
        </div>
    </div>
</div>
<!--//products-->






<!--footer-->
<jsp:include page="footer.jsp"></jsp:include>
<!--//footer-->


</body>
</html>
