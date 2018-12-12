package com.hengzhang.springboot;

import java.util.Properties;

import javax.servlet.MultipartConfigElement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.github.pagehelper.PageHelper;

/**
 * 程序启动入口
 * 
 * @author zhangh
 * @date 2018年11月27日上午10:16:27
 */
@ServletComponentScan
@SpringBootApplication
@EnableAspectJAutoProxy
@MapperScan("com.hengzhang.springboot.dao")
public class Application {

	/**
	 * @author zhangh
	 * @date 2018年11月28日下午5:18:38
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	

    /**
     * 文件上传配置
     * @author zhangh
     * @date 2018年11月29日下午5:58:35
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("100MB"); 
        factory.setMaxRequestSize("100MB");
        return factory.createMultipartConfig();
    }
    
    @Bean
	public PageHelper pageHelper() {
		PageHelper pageHelper = new PageHelper();
		Properties properties = new Properties();
		properties.setProperty("rowBoundsWithCount", "true");
		properties.setProperty("reasonable", "true");
		properties.setProperty("dialect", "mysql");
		pageHelper.setProperties(properties);
		return pageHelper;
	}
}