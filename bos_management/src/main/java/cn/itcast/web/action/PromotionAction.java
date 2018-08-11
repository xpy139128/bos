package cn.itcast.web.action;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;

import cn.itcast.domain.take_delivery.Promotion;
import cn.itcast.service.PromotionService;
import cn.itcast.web.action.base.BaseAction;

@Namespace("/")
@ParentPackage("json-default")
@Controller
@Scope("prototype")
public class PromotionAction extends BaseAction<Promotion> {

	private File titleImgFile;

	private String titleImgFileFileName;
	private String titleImgFileContentType;

	public void setTitleImgFile(File titleImgFile) {
		this.titleImgFile = titleImgFile;
	}

	public void setTitleImgFileFileName(String titleImgFileFileName) {
		this.titleImgFileFileName = titleImgFileFileName;
	}

	public void setTitleImgFileContentType(String titleImgFileContentType) {
		this.titleImgFileContentType = titleImgFileContentType;
	}

	@Autowired
	private PromotionService promotionService;

	@Action(value = "promotion_save", results = {
			@Result(name = "success", type = "redirect", location = "/pages/take_delivery/promotion.html") })
	public String promotionSave() throws IOException {

		String savePath = ServletActionContext.getServletContext().getRealPath("/upload/");
		String saveUrl = ServletActionContext.getRequest().getContextPath() + "/upload/";

		UUID uuid = UUID.randomUUID();
		String ext = titleImgFileFileName.substring(titleImgFileFileName.lastIndexOf("."));
		String randomFileName = uuid + ext;
		FileCopyUtils.copy(titleImgFile, new File(savePath + "/" + randomFileName));

		t.setTitleImg(saveUrl + randomFileName);
		promotionService.save(t);

		return SUCCESS;
	}

	@Action(value = "promotion_queryPage",results= {@Result(name="success",type="json")})
	public String promotionQueryPage() {
		
		Pageable pageable= new PageRequest(page-1, rows);
		
		Page<Promotion> page = promotionService.findAll(pageable);
		
		pushPageDataToValueStack(page);
		
		return SUCCESS;
	}
}
