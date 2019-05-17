package com.atguigu.gmall.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/*
* @ComponentScan：会自动扫描包路径下面的
* 		所有@Controller、@Service、@Repository、@Component 的类
* */
@SpringBootApplication
@ComponentScan(basePackages = "com.atguigu.gmall")
public class GmallManageWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(GmallManageWebApplication.class, args);
	}
}
