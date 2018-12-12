package com.hengzhang.springboot.config;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;

/**
 * Druid 数据源配置
 * @author zhangh
 * @date 2018年8月30日下午3:49:38
 */
@Configuration
public class DruidConfiguration {
	
	/**
	 * dataSource
	 * @author zhangh
	 * @date 2018年8月30日下午3:49:55
	 * @return
	 */
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
	    DruidDataSource druidDataSource = new DruidDataSource();
	    List<Filter> filterList=new ArrayList<>();
	    filterList.add(wallFilter());
	    druidDataSource.setProxyFilters(filterList);
	    return druidDataSource;
	}
	
	@Bean
	public WallFilter wallFilter(){
	    WallFilter wallFilter=new WallFilter();
	    wallFilter.setConfig(wallConfig());
	    return  wallFilter;
	}
	
	@Bean
	public WallConfig wallConfig(){
	    WallConfig config =new WallConfig();
	    config.setMultiStatementAllow(true);//允许一次执行多条语句
	    config.setNoneBaseStatementAllow(true);//允许非基本语句的其他语句
	    return config;
	}
}