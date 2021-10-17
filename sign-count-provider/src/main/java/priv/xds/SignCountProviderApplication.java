package priv.xds;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author DeSen Xu
 * @date 2021-10-17 15:06
 */
@SpringBootApplication
@MapperScan("priv.xds.mapper")
public class SignCountProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(SignCountProviderApplication.class, args);
    }
}
