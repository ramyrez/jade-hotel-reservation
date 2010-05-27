/**
 * This is a simple support class that allows to keep aggregated some
 * pieces of information related to a Hotel Room
 **/
package com.cooperative.hotelreservation.rent;

import java.util.Date;

class RoomInfo {
	private String title;
	private int maxPrice;
	private Date deadline;
	private int bedCount;

	public int getBedCount() {
		return bedCount;
	}

	public void setBedCount(int bedCount) {
		this.bedCount = bedCount;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(int maxPrice) {
		this.maxPrice = maxPrice;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
}