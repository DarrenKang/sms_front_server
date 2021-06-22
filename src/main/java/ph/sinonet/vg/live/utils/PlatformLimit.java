package ph.sinonet.vg.live.utils;

import java.util.Date;

public class PlatformLimit {
	private String phoneNum;
	private Date startTime;
	private Integer count;
	public PlatformLimit() {
		super();
	}
	public PlatformLimit(String phoneNum, Date startTime, Integer count) {
		super();
		this.phoneNum = phoneNum;
		this.startTime = startTime;
		this.count = count;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	@Override
	public String toString() {
		return "PlatformLimit [phoneNum=" + phoneNum + ", startTime=" + startTime + ", count=" + count + "]";
	}
	
	
	
	
	
	
}
