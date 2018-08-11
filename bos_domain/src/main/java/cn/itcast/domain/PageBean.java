package cn.itcast.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import cn.itcast.domain.take_delivery.Promotion;

@XmlRootElement(name="PageBean")
@XmlSeeAlso({Promotion.class})
public class PageBean<T> {
	private long totalCount;
	private List<T> pageData;
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	public List<T> getPageData() {
		return pageData;
	}
	public void setPageData(List<T> pageData) {
		this.pageData = pageData;
	}
	@Override
	public String toString() {
		return "PageBean [totalCount=" + totalCount + ", pageData=" + pageData + "]";
	}
	
	
	
}
