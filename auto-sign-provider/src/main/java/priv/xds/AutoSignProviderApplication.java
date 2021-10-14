package priv.xds;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author DeSen Xu
 * @date 2021-10-12 21:55
 */
@SpringBootApplication
@MapperScan("priv.xds.mapper")
public class AutoSignProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(AutoSignProviderApplication.class, args);
    }
}
