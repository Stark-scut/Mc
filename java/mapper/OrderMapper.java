package mapper;

import Pojo.Order;
import Pojo.OrderItem;
import Pojo.PurchaseLog;
import Pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface OrderMapper {

    @Insert("insert into orders values (null, #{order.total},#{order.amount},#{order.status},#{order.paytype},#{order.name},#{order.phone},#{order.address},#{order.datetime},#{order.user.id})")
    void addOrder(@Param("order") Order order);

    @Select("select max(id) from orders")
    Integer selectLastId();

    @Insert("insert into orderitem values (null, #{orderItem.price}, #{orderItem.amount}, #{orderItem.goods.id},#{orderId})")
    void addOrderItem(@Param("orderItem")OrderItem orderItem,@Param("orderId")int orderId);

    @Select("select * from orders where user_id=#{userId}")
    List<Order> selectOrdersByUserId(int userId);

    @Select("select t.id, t.price, t.amount,g.name goodsName from orderitem t,goods g where t.order_id=#{orderId} and t.goods_id=g.id")
//    @Select("select * from orderitem where order_id=#{orderId}")
    List<OrderItem> selectOrderItemByOrderId(int orderId);
//    @Select("select * from orders where ")
//    List<Order>selectAll(int userId);
    // 获取order总数
    @Select("select count(*) from orders")
    Integer getOrderCount();
    // 获取对应status的order数量
    @Select("select count(*) from orders where status=#{status}")
    Integer getOrderCountByStatus(int status);

    @Select("select o.id,o.total,o.amount,o.status,o.paytype,o.name,o.phone,o.address,o.datetime,u.name user from orders o,user u where o.user_id=u.id order by o.datetime limit #{begin}, #{pageSize}")
    List<Order> getAllOrderList(@Param("begin") int begin, @Param("pageSize") int pageSize);

    @Select("select o.id,o.total,o.amount,o.status,o.paytype,o.name,o.phone,o.address,o.datetime,u.name user from orders o,user u where o.user_id=u.id and o.status=#{status} order by o.datetime limit #{begin}, #{pageSize}")
    List<Order> getOrderListByStatus(@Param("status")int status,@Param("begin")int begin,@Param("pageSize")int pageSize);

    @Select("select u.name from orders o, user u where o.user_id=u.id and o.id=#{orderId}")
    String getUserNameByOrderId(int orderId);

    // 更改订单状态
    @Select("update orders set status=#{status} where id=#{id}")
    void updateStatus(@Param("id")int id, @Param("status")int status);

    @Delete("delete from orders where id=#{id}")
    void delete(int id);

    // 根据typeId 获取用户的购买日志
    @Select("select g.name goodsName,ot.price price,ot.amount amount,u.username username,o.datetime time from orderitem ot,orders o,user u,goods g where ot.goods_id=g.id and g.type_id=#{typeId} and ot.order_id=o.id and o.user_id=u.id order by o.datetime limit #{begin},#{pageSize}")
    List<PurchaseLog> getPurchaseLog(@Param("typeId") int typeId,@Param("begin")int begin,@Param("pageSize")int pageSize);

    @Select("select ot.price price,ot.amount amount from orderitem ot,orders o,user u,goods g where ot.goods_id=g.id and g.type_id=#{typeId} and ot.order_id=o.id and o.user_id=u.id ")
    List<PurchaseLog> getAllPurchaseLog(@Param("typeId") int typeId);

    // 根据typeId 获取购买日志总数
    @Select("select count(*) from orderitem ot,orders o,user u,goods g where ot.goods_id=g.id and g.type_id=#{typeId} and ot.order_id=o.id and o.user_id=u.id")
    int getPurchaseLogCount(int typeId);


}
