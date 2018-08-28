package ai.wo.dev.controller;

/**
 * Created by wu on 13/03/2018.
 */

import ai.wo.dev.model.APIResponse;
import ai.wo.dev.util.NetUtil;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Aaron
 */
@RestController
public class NetworkController extends BaseController {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(NetworkController.class);

    /**
     * 地理信息缓存
     */
    private static Map<String,Map<String,String>> ipInfo = new ConcurrentHashMap<>();

    @GetMapping("ip")
    ResponseEntity<APIResponse> showIP(HttpServletRequest request) {

        String ip = NetUtil.getRemoteHost(request);
        Map<String, String> attr = new HashMap<>();
        if (ip != null) {
            if(null != ipInfo.get(ip) && 0 != ipInfo.get(ip).size()) {
                attr = ipInfo.get(ip);
            }else {
                attr = NetUtil.ip2GeoInfo(ip);
                ipInfo.put(ip,attr);
            }
        }
        APIResponse apiResponse = new APIResponse();
        apiResponse.setMeta(attr);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
