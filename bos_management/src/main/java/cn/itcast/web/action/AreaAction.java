package cn.itcast.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.domain.Area;
import cn.itcast.service.AreaService;
import cn.itcast.utils.PinYin4jUtils;
import cn.itcast.web.action.base.BaseAction;

@Namespace("/")
@ParentPackage("json-default")
@Controller
@Scope("prototype")
@Actions
public class AreaAction extends BaseAction<Area>{

	
	
	private File file;
	
	public void setFile(File file) {
		this.file = file;
	}
	
	@Autowired
	private AreaService areaService;

	@Action(value="area_import")
	public String batchImport() throws Exception {
		List<Area> list = new ArrayList<Area>();
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
		HSSFSheet sheetAt = hssfWorkbook.getSheetAt(0);
		for (Row row : sheetAt) {
			if(row.getRowNum()==0) {
				continue;
			}
			if(row.getCell(0)==null||StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
				continue;
			}
			Area area = new Area();
			area.setId(row.getCell(0).getStringCellValue());
			area.setProvince(row.getCell(1).getStringCellValue());
			area.setCity(row.getCell(2).getStringCellValue());
			area.setDistrict(row.getCell(3).getStringCellValue());
			area.setPostcode(row.getCell(4).getStringCellValue());
			
			String province = area.getProvince();
			String city = area.getCity();
			String district = area.getDistrict();
			province = province.substring(0, province.length()-1);
			city = city.substring(0, city.length()-1);
			district = district.substring(0, district.length()-1);
			
			String name = province+city+district;
			String[] headArray = PinYin4jUtils.getHeadByString(name);
			StringBuffer sb = new StringBuffer();
			for (String string : headArray) {
				sb.append(string);
			}
			String string = sb.toString();
			area.setShortcode(string);
			
			String citycode = PinYin4jUtils.hanziToPinyin(city, "");
			area.setCitycode(citycode);
			
			
			list.add(area);
			
			
		}
		areaService.save(list);
		
		return NONE;
	}
	
	
	

	@Action(value="area_query",results= {@Result(name="success",type="json")})
	public String areaQuery() {
		Pageable pageable = new PageRequest(page-1,rows);
		
		Specification<Area> specification = new Specification<Area>() {

			@Override
			public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				if(StringUtils.isNotBlank(t.getProvince())) {
					Predicate p1 = cb.like(root.get("province").as(String.class), "%"+t.getProvince()+"%");
					list.add(p1);
				}
				if(StringUtils.isNotBlank(t.getCity())) {
					Predicate p2 = cb.like(root.get("city").as(String.class),"%"+t.getCity()+"%");
					list.add(p2);
				}
				if(StringUtils.isNotBlank(t.getDistrict())) {
					Predicate p3 = cb.like(root.get("district").as(String.class), "%"+t.getDistrict()+"%");
					list.add(p3);
				}
				
				
				return cb.and(list.toArray(new Predicate[0]));
			}
			
		};
				
		Page<Area> page = areaService.findAll(specification,pageable);
		pushPageDataToValueStack(page);
		return SUCCESS;
	}

}
