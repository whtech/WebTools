package ai.wo.dev.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class NetUtil {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(NetUtil.class);

    public static String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        return ip;
    }


    public static Map<String, String> ip2GeoInfo(String ip) {

        Map<String, String> attr = new HashMap<>();
        try {
            Document doc = Jsoup.connect("http://geoipinfo.org/?ip=" + ip).get();
            logger.debug(doc.title());
            Elements tds = doc.select("table tbody tr td");

            if (tds.size() % 2 == 0) {

                for (int i = 0; i < tds.size(); i += 2) {
                    String key = tds.get(i).text();
                    if (key != null) {
                        key = key.replaceAll("[^\\w]", "");
                        key = key.toLowerCase();
                        //key = key.replace("\u00A0", "");

//                        key = key.trim().replace('\t',' ');
//                        key = key.trim().replace("&nbsp;","");
//                        key = key.trim().replace(" ","");

                    }

                    attr.put(key, tds.get(i + 1).text().trim());
                }

            }
        } catch (IOException e) {
            logger.error("", e);
        }

        return attr;
    }
}
