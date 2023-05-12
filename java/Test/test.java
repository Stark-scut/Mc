package Test;
import service.impl.GoodsServiceImpl;
import service.impl.OrderServiceImpl;

import java.util.Date;

public class test {
    public static void main(String[] args) throws InterruptedException {
        String strLast;
        String strNow;
        Date date = new Date();
        Thread.sleep(2000);
        Date date1 = new Date();
        System.out.println(date1);
        System.out.println(date);
        long a = date.getTime();
        long b = date1.getTime();
        int c = (int)((b-a)/1000);
        System.out.println(c);
        String s = "id=255";
        boolean flag = s.contains("id=");
        if(flag){
            String s1 = s.substring(s.indexOf('=')+1);
            System.out.println(s1);
            Integer goodsId = Integer.parseInt(s1);

        }
    }
}
