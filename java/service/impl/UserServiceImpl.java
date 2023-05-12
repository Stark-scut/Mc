package service.impl;

import Pojo.*;
import Pojo.Record;
import mapper.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import service.UserService;
import utils.SqlSessionFactoryUtils;

import java.text.SimpleDateFormat;
import java.util.List;

public class UserServiceImpl implements UserService {

    SqlSessionFactory factory = SqlSessionFactoryUtils.getSqlSessionFactory();
    @Override
    public boolean register(String username, String email) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User register = mapper.register(username, email);
        if(register==null) return true;
        sqlSession.close();
        return false;
    }

    @Override
    public void add(User user) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.add(user);
        sqlSession.commit();
        sqlSession.close();
    }

    @Override
    public User login(String username, String email) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = null;
        user = mapper.login(username, email);
        sqlSession.close();

        return user;
    }

    @Override
    public void updateInfo(String name, String phone, String address, Integer id) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.updateInfo(name, phone, address, id);
        sqlSession.commit();
        sqlSession.close();
    }

    @Override
    public void changePwd(String pwd, Integer id) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.changePwd(pwd, id);
        sqlSession.commit();
        sqlSession.close();
    }

    @Override
    public void addBrowseLog(Browse browse) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.addBrowseLog(browse);
        sqlSession.commit();
        sqlSession.close();
    }

    @Override
    public int getUserCount() { //user表中查询用户总数
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        int count = mapper.getUserCount();
        sqlSession.close();
        return count;
    }


    @Override
    public Page getUserPage(int pageNumber) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        Page p = new Page();
        p.setPageNumber(pageNumber);
        int pageSize = 7;
        int begin = (pageNumber-1)*pageSize;
        int totalCount = 0;
        try {
            totalCount = getUserCount();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        p.SetPageSizeAndTotalCount(pageSize,totalCount);
        List<User> list = null;
        try {
            list = mapper.selectUserList(begin,pageSize);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        for(User u : list){
            Integer expend = mapper.getExpendByUserId(u.getId());
            u.setExpenditure(expend);
        }
        p.setList(list);
        return p;
    }

    @Override
    public void editUser(User user) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.update(user);
        sqlSession.commit();
        sqlSession.close();
    }

    @Override
    public User getUserById(int id) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.selectUserById(id);
        sqlSession.close();
        return user;
    }

    @Override
    public boolean deleteUser(int id) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        try {
            mapper.delete(id);
            sqlSession.commit();
            sqlSession.close();
            return true;
        } catch (Exception e) {
            sqlSession.close();
            return false;
        }
    }

    @Override
    public void addRecord(Record record) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        try {
            mapper.addRecord(record);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        sqlSession.commit();
        sqlSession.close();
        return;
    }

    @Override
    public Page getRecordPage(int pageNumber) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        Page p = new Page();
        p.setPageNumber(pageNumber);
        int pageSize = 10;
        int totalCount = 0;
        int begin = (pageNumber-1)*pageSize;
        try {
            totalCount = mapper.getRecordCount();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        p.SetPageSizeAndTotalCount(pageSize, totalCount);

        List list = null;
        list = mapper.getRecordList(begin,pageSize);
        for(Record record : (List<Record>)list){
//            System.out.println("userId"+record.getUserId());
            String username = mapper.getUserNameByRecordId(record.getUserId());
            record.setUserName(username);  // 设置username
            int identity = mapper.getIdentityById(record.getUserId());
            record.getUser().setIdentity(identity);
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            record.setFormatStartTime(ft.format(record.getStartTime()));
            record.setFormatEndTime(ft.format(record.getEndTime()));
//            System.out.println("ip: "+record.getIp());
//            System.out.println("username: "+record.getUser().getUserName() + " "+username);
        }
        p.setList(list);
        sqlSession.close();
        return p;

    }

    @Override
    public PageBean<Browse> getBrowseLog(int typeId, int currentPage, int pageSize) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        int begin = (currentPage-1)*pageSize;
        List<Browse> browseLog = mapper.getBrowseLogByTypeId(typeId, begin, pageSize);
        int count = mapper.getBrowseCountByTypeId(typeId);
        PageBean<Browse> page = new PageBean<Browse>();
        page.setTotalCount(count);
        page.setRows(browseLog);
        sqlSession.close();
        return page;
    }

    @Override
    public void addOperateLog(Operate operate) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        mapper.addOperateLog(operate);
        sqlSession.commit();
        sqlSession.close();
        return;
    }

    @Override
    public PageBean<Operate> getOperateLog(int currentPage, int pageSize) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        int begin = (currentPage-1)*pageSize;
        List<Operate> operateLog = mapper.getOperateLog(begin, pageSize);
        int count = mapper.getOperateCount();
//        System.out.println("-----------------");
        for(Operate op : operateLog){
            User user = mapper.selectUserById(op.getUserId());
            op.setUsername(user.getUserName());
//            System.out.println(user);
            op.setUser(user);
            op.setIdentity();
            op.set_format_time();
        }
        PageBean<Operate> page= new PageBean<Operate>();
        page.setRows(operateLog);
        page.setTotalCount(count);
        return page;
    }

    @Override
    public Integer getExpendByUserId(int userId) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        Integer expend = mapper.getExpendByUserId(userId);
        sqlSession.close();
        return expend;
    }

    @Override
    public Integer getLastBoughtTypeId(int userId) {
        SqlSession sqlSession = factory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        Integer typeId = mapper.getLastBoughtTypeId(userId);
        sqlSession.close();
        return typeId;
    }


}
