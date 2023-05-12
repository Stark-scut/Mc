package mapper;

import Pojo.Browse;
import Pojo.Operate;
import Pojo.User;
import org.apache.ibatis.annotations.*;
import Pojo.Record;
import java.util.List;

public interface UserMapper {

    // 用户注册
    @ResultMap("userResultMap")
    @Select("select * from user where username=#{username} or email=#{email}")
    User register(@Param("username") String username, @Param("email") String email);

    // 注册成功后添加用户数据
    @Insert("insert into user values(null,#{userName},#{email},#{password},#{name},#{phone},#{address},#{admin},#{validate})")
    void add(User user);

    // 用户登录
    @ResultMap("userResultMap")
    @Select("select * from user where username=#{username} and password=#{password}")
    User login(@Param("username")String username,@Param("password")String password);

    @Update("update user set name=#{name},phone=#{phone},address=#{address} where id = #{id}")
    void updateInfo(@Param("name")String name, @Param("phone")String phone, @Param("address")String address,@Param("id")Integer id);

    @Update("update user set password=#{pwd} where id=#{id}")
    void changePwd(@Param("pwd") String pwd, @Param("id") Integer id);

    @Select("select count(*) from user")
    int getUserCount();

    @ResultMap("userResultMap")
    @Select("select * from user limit #{begin},#{pageSize}")
    List<User> selectUserList(@Param("begin")int begin,@Param("pageSize")int pageSize);

    @Update("update user set name=#{u.name},phone=#{u.phone},address=#{u.address} where id=#{u.id}")
    void update(@Param("u")User u);

    @ResultMap("userResultMap")
    @Select("select * from user where id = #{id}")
    User selectUserById(int id);

    @Delete("delete from user where id=#{id}")
    void delete(int id);

    @Insert("insert into record values (null,#{r.startTime},#{r.endTime},#{r.user.id},#{r.ip})")
    void addRecord(@Param("r")Record record);

    @Select("select count(*) from record")
    int getRecordCount();

    @Select("select user_id userId,start_time startTime,end_time endTime,ip ip,id id from record limit #{begin},#{pageSize}")
    List<Record>getRecordList(@Param("begin")int begin,@Param("pageSize")int pageSize);

    @Select("select username from user where id=#{userId} ")
    String getUserNameByRecordId(int userId);

    // 添加用户浏览商品的日志
    @Insert("insert into browse values(null, #{browse.user.id},#{browse.goods.id},#{browse.time},#{browse.duration})")
    void addBrowseLog(@Param("browse") Browse browse);

    // 根据typeID查询用户浏览商品的日志
    @Select("select u.name userName,g.name goodsName,g.price price,b.time time,b.duration duration from browse b,user u,goods g where b.user_id=u.id and b.goods_id=g.id and g.type_id=#{typeId} order by b.time limit #{begin},#{pageSize}")
    List<Browse> getBrowseLogByTypeId(@Param("typeId")int typeId,@Param("begin")int begin,@Param("pageSize")int pageSize );
    // 根据typeID查询浏览日志总数
    @Select("select count(*) from browse b,user u,goods g where b.user_id=u.id and b.goods_id=g.id and g.type_id=#{typeId}")
    int getBrowseCountByTypeId(@Param("typeId")int typeId);

    @Select("select identity from user where id=#{id}")
    int getIdentityById(int id);

    @Insert("insert into operate values(null, #{op.userId},#{op.description},#{op.ip},#{op.time})")
    void addOperateLog(@Param("op") Operate op);

    @Select("select user_id userId,description description,ip ip,time time from operate limit #{begin},#{pageSize}")
    List<Operate> getOperateLog(@Param("begin")int begin,@Param("pageSize")int pageSize);

    @Select("select count(*) from operate")
    int getOperateCount();

    @Select("select sum(total) from orders where user_id=#{userId}")
    Integer getExpendByUserId(int userId);

    // 获取用户最后一次购买的商品类型id
    @Select("select g.type_id from orderitem ot,orders o,goods g where o.id=ot.order_id and ot.goods_id=g.id and o.user_id=#{userId} order by o.datetime desc limit 1 ")
    Integer getLastBoughtTypeId(int userId);
}
