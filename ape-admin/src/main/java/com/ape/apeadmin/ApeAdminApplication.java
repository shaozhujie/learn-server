package com.ape.apeadmin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

//SET GLOBAL sql_mode='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
/**
 * @author shaozhujie
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class },scanBasePackages = "com.ape.*")
@ComponentScan("com.ape")
@MapperScan("com.**.mapper")
public class ApeAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApeAdminApplication.class, args);
    }

}
