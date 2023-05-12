package Pojo;

public class User {
    Integer id;
    String userName;     // 用户名
    String email;        // 邮件
    String password;     // 密码
    String name;         // 用户姓名
    String phone;        // 电话
    String address;      // 地址
    int identity;     // 1普通用户 2销售人员 3管理员
    Integer expenditure;   // 用户消费总额
    boolean validate;  // 账户是否有效

    public Integer getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(Integer expenditure) {
        this.expenditure = expenditure;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public boolean getValidate() {
        return validate;
    }


    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", identity=" + identity +
                ", validate=" + validate +
                '}';
    }
}
