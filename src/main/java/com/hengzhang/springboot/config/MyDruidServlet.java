package com.hengzhang.springboot.config;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import com.alibaba.druid.support.http.StatViewServlet;

/**
 * druid数据源监控
 * @author zhangh
 * @date 2018年12月12日上午10:58:04
 */
@WebServlet(urlPatterns="/druid/*", initParams={
                    @WebInitParam(name="allow",value=""),
                    @WebInitParam(name="deny",value=""),
                    @WebInitParam(name="loginUsername",value="admin"),
                    @WebInitParam(name="loginPassword",value="iflytek2018"),
                    @WebInitParam(name="resetEnable",value="false") } )
public class MyDruidServlet extends StatViewServlet{
	private static final long serialVersionUID = 4132404047969678064L;  
}