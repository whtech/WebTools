package ai.wo.dev.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by cyk on 2018/5/7.
 */
public class SimpleHttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(SimpleHttpUtils.class);

    /**
     * 建立GET请求
     *
     * @param URL   请求IP地址
     * @param param 请求参数，如： name1=value1&name2=value2
     * @return 请求响应值
     */
    public static String doGet(String URL, String param) {
        String httpURL = (org.apache.commons.lang3.StringUtils.isNotBlank(param)) ? URL.concat("?" + param) : URL;
        StringBuffer result = new StringBuffer("");
        try {
            java.net.URL url = new URL(httpURL);
            URLConnection urlCon = url.openConnection();
            //最大响应时间7秒
            urlCon.setConnectTimeout(20000);
            urlCon.setReadTimeout(20000);
            urlCon.connect();
            InputStream is = urlCon.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader reader = new BufferedReader(isr);
            String temp = "";
            while ((temp = reader.readLine()) != null) {
                result.append(temp);
            }
            is.close();
            isr.close();
            reader.close();
        } catch (Exception e) {
            logger.error("", e);
        }
        return result.toString();
    }

    /**
     * 建立POST请求
     *
     * @param URL    请求IP地址
     * @param params 请求参数列表（注：必须是JSON串，否则会报出415或者400错误），如：'{"name1":"value1","name2":"value2"}'
     * @return 响应值
     */
    public static String doPost(String URL, String params) {
        StringBuffer result = new StringBuffer("");
        try {
            URL url = new URL(URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("encoding", "UTF-8");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(20000);
            connection.connect();
            //获取输出流
            OutputStream os = connection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            //POST请求传入参数
            bw.write(params);
            bw.flush();
            //获取输入流
            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            String temp = "";
            while ((temp = reader.readLine()) != null) {
                result.append(temp);
            }
            //关闭所有IO流
            bw.close();
            osw.close();
            os.close();
            is.close();
            isr.close();
            reader.close();
        } catch (Exception e) {
            logger.error("", e);
        }
        return result.toString();
    }

}
