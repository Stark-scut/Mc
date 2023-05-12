package Pojo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Operate {
    private Integer id;
    private Integer userId;
    private String description;
    private String ip;
    private Date time;
    private String formatTime;
    private String identity;
    private User user;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public void setIdentity(){
        int t = this.getUser().getIdentity();
        if(t==2) this.setIdentity("销售员");
        else this.setIdentity("管理员");
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getFormatTime() {
        return formatTime;
    }

    public void setFormatTime(String formatTime) {
        this.formatTime = formatTime;
    }
    public void set_format_time(){
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        this.setFormatTime(ft.format(this.getTime()));
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Operate{" +
                "id=" + id +
                ", userId=" + userId +
                ", description='" + description + '\'' +
                ", ip='" + ip + '\'' +
                ", time=" + time +
                ", formatTime='" + formatTime + '\'' +
                ", user=" + user +
                '}';
    }
}
