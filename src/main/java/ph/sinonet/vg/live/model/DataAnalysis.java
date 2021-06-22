package ph.sinonet.vg.live.model;

import java.util.Date;

public class DataAnalysis {
	private Integer count;
	private Date date;
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "DataAnalysis [count=" + count + ", date=" + date + "]";
	}
	
}
