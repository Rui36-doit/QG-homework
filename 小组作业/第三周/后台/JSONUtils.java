package Scoer;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;


public class JSONUtils {
    //获取JSON字符串
    public static String getJSONstr(HttpServletRequest req) throws IOException {
        req.setCharacterEncoding("UTF-8");
        String str = null;
        BufferedReader bfr = req.getReader();
        StringBuilder strb = new StringBuilder();
        while ((str = bfr.readLine())!= null){
            strb.append(str);
        }
        return strb.toString();
    }
}
