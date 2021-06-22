package ph.sinonet.vg.live.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Page implements Serializable {

	public static final Integer PAGE_BEGIN_INDEX = 1;
	public static final Integer PAGE_DEFAULT_SIZE = 20;
	private static final long serialVersionUID = 1L;
	private Integer pageNumber;
	private Integer totalPages;
	private Integer size;
	private List pageContents;
	private Double statics1;
	private Double statics2;
	private Integer totalRecords;
	private Integer numberOfRecordsShown;
	private String jsPageCode;

	public Page() {
		pageNumber = 0;
		totalPages = 0;
		size = 0;
		pageContents = new ArrayList();
		statics1 = 0.0;
		statics2 = 0.0;
		totalRecords = 0;
		numberOfRecordsShown = 0;
	}

	public Page(List pageContents) {
		pageNumber = 0;
		totalPages = 0;
		size = 0;
		this.pageContents = pageContents;
		statics1 = 0.0;
		statics2 = 0.0;
		totalRecords = 0;
		numberOfRecordsShown = 0;
		this.pageContents = pageContents;
	}

	public String getJsPageCode() {
		String Str = "";
		try {
			Str = (new StringBuilder(String.valueOf(Str))).append("共").append(totalRecords).append("条,每页").append(size).append("条,当前").append(pageNumber).append("/").append(totalPages).append("&nbsp;")
					.toString();
			if (pageNumber.intValue() > 1)
				Str = (new StringBuilder(String.valueOf(Str))).append("<a style='color:yellow' href='javascript:gopage(1);'>首页</a>&nbsp;<a style='color:yellow' href='javascript:gopage(").append(
						pageNumber.intValue() - 1).append(")'>上一页</a>&nbsp;").toString();
			else
				Str = (new StringBuilder(String.valueOf(Str))).append("首页&nbsp;上一页&nbsp;").toString();
			if (pageNumber.intValue() < totalPages.intValue())
				Str = (new StringBuilder(String.valueOf(Str))).append("<a style='color:yellow' href='javascript:gopage(").append(pageNumber.intValue() + 1).append(
						")'>下一页</a>&nbsp;<a style='color:yellow' href='javascript:gopage(").append(totalPages).append(")'>末页</a>&nbsp;").toString();
			else
				Str = (new StringBuilder(String.valueOf(Str))).append("下一页&nbsp;末页&nbsp;").toString();
			Str = (new StringBuilder(String.valueOf(Str))).append("到第<select name='page' onchange='javascript:gopage(this.options[this.selectedIndex].value)'>").toString();
			for (int g1 = 1; g1 <= totalPages.intValue(); g1++)
				if (g1 == pageNumber.intValue())
					Str = (new StringBuilder(String.valueOf(Str))).append("<option value='").append(g1).append("' selected>").append(g1).append("</option>").toString();
				else
					Str = (new StringBuilder(String.valueOf(Str))).append("<option value='").append(g1).append("'>").append(g1).append("</option>").toString();

			Str = (new StringBuilder(String.valueOf(Str))).append("</select>页").toString();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		jsPageCode = Str;
		return jsPageCode;
	}

	public Integer getNumberOfRecordsShown() {
		return numberOfRecordsShown;
	}

	public List getPageContents() {
		return pageContents;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public Integer getSize() {
		return size;
	}

	public Double getStatics1() {
		return statics1;
	}

	public Double getStatics2() {
		return statics2;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setJsPageCode(String jsPageCode) {
		this.jsPageCode = jsPageCode;
	}

	public void setNumberOfRecordsShown(Integer numberOfRecordsShown) {
		this.numberOfRecordsShown = numberOfRecordsShown;
	}

	public void setPageContents(List pageContents) {
		this.pageContents = pageContents;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = totalPages == null ? pageNumber : pageNumber.intValue() <= totalPages.intValue() ? pageNumber : PAGE_BEGIN_INDEX;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public void setStatics1(Double statics1) {
		this.statics1 = statics1;
	}

	public void setStatics2(Double statics2) {
		this.statics2 = statics2;
	}

	public void setTotalPages(Integer totalPages) {
		if (pageNumber != null)
			pageNumber = totalPages.intValue() >= pageNumber.intValue() ? pageNumber : PAGE_BEGIN_INDEX;
		this.totalPages = totalPages;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}

	@Override
	public String toString() {
		return (new StringBuilder("Page[pageNumber:")).append(pageNumber).append(";size:").append(size).append(";totalPages:").append(totalPages).append(";pageContent:").append(pageContents.toString())
				.append("]").toString();
	}

}
