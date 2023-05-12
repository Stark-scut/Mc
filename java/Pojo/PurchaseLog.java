package Pojo;

import java.util.Date;

public class PurchaseLog {
    private String goodsName;
    private Integer price;
    private Integer amount;
    private String username;
    private Date time;

    private String formatTime;

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getTime() {
        return time;
    }

    public String getFormatTime() {
        return formatTime;
    }

    public void setFormatTime(String formatTime) {
        this.formatTime = formatTime;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "PurchaseLog{" +
                "goodsName='" + goodsName + '\'' +
                ", price='" + price + '\'' +
                ", amount=" + amount +
                ", username='" + username + '\'' +
                ", time=" + time +
                ", formatTime='" + formatTime + '\'' +
                '}';
    }
}
