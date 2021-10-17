package priv.xds.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import priv.xds.exception.UnNecessaryInvokeException;
import priv.xds.pojo.Sign;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@SpringBootTest
public class SignServiceImplTest {

    @Autowired
    private SignService signService;

    @Test
    public void testSign() {
        try {
            signService.sign("123", "456");
        } catch (UnNecessaryInvokeException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetUserRole() {
    }

    @Test
    public void testIgnoreUser() {
    }

    @Test
    public void testReStatisticsUser() {
    }

    @Test
    public void testInitGroup() throws UnNecessaryInvokeException {
        List<Sign> users = new ArrayList<>(3);
        Sign sign = new Sign();
        sign.setQq("123");
        sign.setGroupCode("456");
        sign.setLastSign(new Date());

        Sign sign1 = new Sign();
        sign1.setQq("444444444214");
        sign1.setGroupCode("456");
        sign1.setLastSign(new Date());

        Sign sign2 = new Sign();
        sign2.setQq("2412442412");
        sign2.setGroupCode("456");
        sign2.setLastSign(new Date());


        users.add(sign);
        users.add(sign1);
        users.add(sign2);

        System.out.println(signService.initGroup("456", users));


    }
}