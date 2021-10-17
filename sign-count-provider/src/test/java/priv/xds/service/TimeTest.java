package priv.xds.service;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;

/**
 * @author DeSen Xu
 * @date 2021-10-17 15:22
 */
public class TimeTest {

    @Test
    public void test() {
        Instant instant = Instant.ofEpochMilli(new Date().getTime());
        System.out.println(instant.toString());
    }
}
