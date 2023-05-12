package Pojo;

import java.util.Date;

public class Browse {
    private Integer id;
    private String userName;
    private String goodsName;
    private float price;
    private Date time;
    private Integer duration;
    private String formatTime;
    private User user;
    private Goods goods;

    public void setGoodsId(int goodsId){
        if(this.goods==null){
            this.goods = new Goods();
        }
        this.goods.setId(goodsId);
    }

    public int getGoodsId(){
        if(this.goods==null) return 0;
        return this.goods.getId();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getFormatTime() {
        return formatTime;
    }

    public void setFormatTime(String formatTime) {
        this.formatTime = formatTime;
    }

    @Override
    public String toString() {
        return "Browse{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", price=" + price +
                ", time=" + time +
                ", duration=" + duration +
                ", formatTime='" + formatTime + '\'' +
                ", user=" + user +
                ", goods=" + goods +
                '}';
    }
}
