package com.feifan.bp.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by konta on 2016/1/18.
 */
public class NumberUtil {

    private NumberUtil(){

    }

    /**
     * 格式化金额，千分位
     * @param s 要格式化的金额
     * @param len 保留的小数位数
     * @return
     */
    public static String moneyFormat(String s,int len){
        if (s == null || s.length() < 1 || s.equals("0")) {
            return "0.00";
        }
        NumberFormat formater = null;
        double num = Double.parseDouble(s);
        if (len == 0) {
            formater = new DecimalFormat("###,##0");

        } else {
            StringBuffer buff = new StringBuffer();
            buff.append("#,###,##0.");
            for (int i = 0; i < len; i++) {
                buff.append("0");
            }
            formater = new DecimalFormat(buff.toString());
        }

        return formater.format(num);
    }

}
