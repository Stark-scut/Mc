package service;

import Pojo.*;
import Pojo.Record;
import mapper.UserMapper;

import java.util.List;

public interface UserService {

    boolean register(String username,String email);

    void add(User user);

    User login(String username, String email);

    void updateInfo(String name, String phone, String address, Integer id);
    void changePwd(String pwd, Integer id);

    void addBrowseLog(Browse browse);

    int getUserCount();


    Page getUserPage(int pageNumber);
    void editUser(User user);

    User getUserById(int id);

    boolean deleteUser(int id);

    void addRecord(Record record);

    Page getRecordPage(int pageNumber);

    PageBean<Browse> getBrowseLog(int typeId, int currentPage, int pageSize);

    void addOperateLog(Operate operate);

    PageBean<Operate> getOperateLog(int currentPage, int pageSize);

    Integer getExpendByUserId(int userId);

    Integer getLastBoughtTypeId(int userId);
}
