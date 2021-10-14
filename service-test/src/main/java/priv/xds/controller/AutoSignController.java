package priv.xds.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import priv.xds.pojo.AutoSign;
import priv.xds.service.AutoSignService;

/**
 * @author DeSen Xu
 * @date 2021-10-14 18:14
 */
@RestController
public class AutoSignController {

    @DubboReference
    private AutoSignService autoSignService;



    @PostMapping("/register")
    public String register(@RequestParam("qq") String qq) {
        AutoSign savedInfo = autoSignService.getSavedInfo(qq);
        return savedInfo == null ? "无法找到:" + qq : savedInfo.toString();
    }
}
