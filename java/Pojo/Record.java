package Pojo;

import java.util.Date;

public class Record {
    private Integer id;
    private Integer userId;
    private Date startTime;
    private Date endTime;
    private String formatStartTime;
    private String formatEndTime;
    private String ip;


    public String getFormatStartTime() {
        return formatStartTime;
    }

    public void setFormatStartTime(String formatStartTime) {
        this.formatStartTime = formatStartTime;
    }

    public String getFormatEndTime() {
        return formatEndTime;
    }

    public void setFormatEndTime(String formatEndTime) {
        this.formatEndTime = formatEndTime;
    }

    public String getIp() {
        return ip;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", userId=" + userId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", formatStartTime='" + formatStartTime + '\'' +
                ", formatEndTime='" + formatEndTime + '\'' +
                ", user=" + user +
                ", ip='" + ip + '\'' +
                '}';
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    private User user;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public void setUserName(String username) {
        user = new User();
        this.user.setUserName(username);
    }
}
