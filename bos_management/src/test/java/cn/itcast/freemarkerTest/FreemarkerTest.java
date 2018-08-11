package cn.itcast.freemarkerTest;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreemarkerTest {
	@Test
	public void testOutPut() throws Exception {
		//配置对象,配置模板位置
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_21);
		configuration.setDirectoryForTemplateLoading(new File("src/main/webapp/WEB-INF/templates"));
		//创建模板
		Template template = configuration.getTemplate("hello.ftl");
		//动态数据对象
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("title", "dddd");
		map.put("msg", "嘿嘿");
		//合并输出
		
		template.process(map, new PrintWriter(System.out));
	}
}
