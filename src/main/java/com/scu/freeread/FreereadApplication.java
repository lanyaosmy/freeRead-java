package com.scu.freeread;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.scu.freeread.mapper")
@SpringBootApplication
public class FreereadApplication {

	public static void main(String[] args) {
		SpringApplication.run(FreereadApplication.class, args);
	}

}
