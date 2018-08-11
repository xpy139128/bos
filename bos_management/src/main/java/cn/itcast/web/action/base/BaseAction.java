package cn.itcast.web.action.base;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T>{
	
	protected T t;
	@Override
	public T getModel() {
		return t;
	}
	
	
	
	public BaseAction() {
		Type genericSuperclass = this.getClass().getGenericSuperclass();
		ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
		Class<T> modelClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
		try {
			t = modelClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("模型构造失败");
		}
	}


	protected int page;
	protected int rows;
	
	public void setPage(int page) {
		this.page = page;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public void pushPageDataToValueStack(Page<T> page) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("total", page.getTotalElements());
		map.put("rows", page.getContent());
		
		ActionContext.getContext().getValueStack().push(map);
	}
	
}
