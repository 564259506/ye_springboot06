package com.yeahsir;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubboConfiguration
public class SpringbootSsmProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootSsmProviderApplication.class, args);
	}

}
