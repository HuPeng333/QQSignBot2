package priv.xds.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import priv.xds.exception.NoSuchUserException;
import priv.xds.pojo.AutoSign;
import priv.xds.service.AutoSignService;

import java.util.List;

/**
 * @author DeSen Xu
 * @date 2021-10-14 14:32
 */
@SpringBootTest
public class AutoSignMapperTest {

    @Autowired
    AutoSignService autoSignService;

    @Test
    public void testSelect() {
        System.out.println(autoSignService.getSavedInfo("2237803016"));
    }

    @Test
    public void testUpdate() throws NoSuchUserException {
        AutoSign autoSign = new AutoSign();
        autoSign.setQq("2237803016");
        autoSign.setActive(true);
        autoSignService.updateUserInfo(autoSign);
    }
}
