package com.hengzhang.springboot.config;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam; 
import com.alibaba.druid.support.http.WebStatFilter; 

/**
 * 自定义druid过滤器
 * @author zhangh
 * @date 2018年8月31日下午5:26:27
 */
@WebFilter(filterName="druidWebStatFilter",urlPatterns="/*",initParams={@WebInitParam(name="exclusions",value="*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*")})
public class DruidStatFilter extends WebStatFilter{
  
}