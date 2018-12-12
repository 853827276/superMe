package com.hengzhang.springboot.common;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hengzhang.springboot.anno.CheckLogin;
import com.hengzhang.springboot.anno.CheckParam;
import com.hengzhang.springboot.anno.NotEmpty;
import com.hengzhang.springboot.anno.Validate;
import com.hengzhang.springboot.anno.validate.Add;
import com.hengzhang.springboot.anno.validate.Update;
import com.hengzhang.springboot.util.ResultWrapper;
import com.hengzhang.springboot.util.UUIDUtils;

/**
 * 公共controller 这个类包含的是通用的方法 个别的业务逻辑写在自己的controller
 * @author zhangh
 * @date 2018年8月27日下午5:46:49
 * @param <Domain>
 */
public class BaseController<Domain extends BaseEntity> {

	@SuppressWarnings("unchecked")
	protected Class<Domain> entityClass = (Class<Domain>)((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

	
	/**
	 * 添加数据
	 * @author zhangh
	 * @date 2018年8月28日上午9:04:46
	 * @param newDomain
	 * @param baseService
	 * @return
	 */
	@CheckLogin
	@CheckParam
	@RequestMapping("/add")
	public Map<?, ?> add(@Validate(required = true,value={Add.class})Domain newDomain, Object baseService) {
		newDomain.setId(UUIDUtils.getId());
		((BaseService)baseService).add(newDomain);
		return ResultWrapper.wrapSuccess("保存成功");
	}
	
	/**
	 * 更新数据
	 * @author zhangh
	 * @date 2018年8月28日上午9:05:05
	 * @param newDomain
	 * @param baseService
	 * @return
	 */
	@CheckLogin
	@CheckParam
	@RequestMapping("/update")
	public Map<?, ?> update(@Validate(required = true,value={Update.class})Domain newDomain, Object baseService) {
		((BaseService)baseService).update(newDomain);
		return ResultWrapper.wrapSuccess("保存成功");
	}

	/**
	 * 修改状态
	 * @author zhangh
	 * @date 2018年8月28日上午9:05:17
	 * @param id
	 * @param status
	 * @param baseService
	 * @return
	 */
	@CheckLogin
	@RequestMapping("/updateStatus/{id}/{status}")
	public Map<?, ?> updateStatus(@PathVariable String id, @PathVariable String status, Object baseService) {
		((BaseService)baseService).updateStatus(id, status);
		return ResultWrapper.wrapSuccess("更新成功");
	}
	
	/**
	 * 根据主键查询
	 * @author zhangh
	 * @date 2018年8月28日上午9:05:26
	 * @param id
	 * @param baseService
	 * @return
	 */
	@CheckLogin
	@RequestMapping("/findById/{id}")
	public Map<?, ?> findById(@PathVariable String id, Object baseService){
		return ResultWrapper.wrapObject(((BaseService)baseService).findById(id), "success","查询成功");
	}
	
	/**
	 * 根据主键删除数据
	 * @author zhangh
	 * @date 2018年8月28日上午9:05:35
	 * @param id
	 * @param baseService
	 * @return
	 */
	@CheckLogin
	@RequestMapping("/deleteById/{id}")
	public Map<?, ?> deleteById(@PathVariable String id, Object baseService){
		((BaseService)baseService).deleteById(id);
		return ResultWrapper.wrapSuccess("删除成功");
	}
	
	/**
	 * 根据主键批量删除数据
	 * @author zhangh
	 * @date 2018年8月28日下午6:57:06
	 * @param ids
	 * @param baseService
	 * @return
	 */
	@CheckLogin
	@CheckParam
	@RequestMapping("/deleteByIds")
	public Map<?, ?> deleteByIds(@NotEmpty(value="ids 不能为空") String ids, Object baseService){
		String[] ArrIds = ids.replaceAll("\\[", "").replaceAll("\\]", "").split(",");
		((BaseService)baseService).deleteByIds(Arrays.asList(ArrIds));
		return ResultWrapper.wrapSuccess("删除成功");
	}
	
	/**
	 * 多条件非分页查询
	 * @author zhangh
	 * @date 2018年8月28日上午9:05:53
	 * @param queryDomain
	 * @param baseService
	 * @return
	 */
	@CheckLogin
	@RequestMapping("/queryForList")
	public Map<?, ?> queryForList(Domain queryDomain, Object baseService) {
		return ResultWrapper.wrapListNoPage(((BaseService)baseService).queryForList(queryDomain), "success", "查询成功");
	}

	/**
	 * 多条件分页查询
	 * @author zhangh
	 * @date 2018年8月28日上午9:06:06
	 * @param queryDomain
	 * @param baseService
	 * @return
	 * @throws Exception
	 */
/*	@CheckLogin
	@SuppressWarnings("unchecked")
	@RequestMapping("/queryForPage")
	public Map<?, ?> queryForPage(Domain queryDomain, Object baseService) throws Exception {
		if(queryDomain == null){
			queryDomain = (Domain)Class.forName(entityClass.getName()).newInstance();
		}
		return ResultWrapper.wrapPage(((BaseService)baseService).queryForPage(queryDomain), "success", "查询成功");
	}*/

}
