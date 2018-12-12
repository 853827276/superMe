package com.hengzhang.springboot.config;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * json序列化配置
 * @author weiliu36
 * @date 2018-08-31上午08:51:12
 */
@Configuration
public class JsonConfig extends WebMvcConfigurerAdapter {

	//JackSon 配置
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		ObjectMapper objectMapper = builder.build();
		// 字段和值都加引号
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		// 数字加引号
		objectMapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
		//日期格式化
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		// 空值处理为空串  
		objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {  
			@Override  
			public void serialize(Object value, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {  
				jg.writeString("");  
			}  
		});
		converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
		super.configureMessageConverters(converters);
	}
}
