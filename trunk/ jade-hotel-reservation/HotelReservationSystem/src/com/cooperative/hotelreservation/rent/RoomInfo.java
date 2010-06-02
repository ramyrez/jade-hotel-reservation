/**
 * This is a simple support class that allows to keep aggregated some pieces of
 * information related to a Hotel Room
 **/
package com.cooperative.hotelreservation.rent;

import java.util.Date;

public class RoomInfo
{

	// TODO remove title and maxPrice --> price is defined in the proposal
	@Deprecated
	private String title;
	@Deprecated
	private int maxPrice;

	// remove deadline? since a room offered by a seller can be sold anytime and
	// not until a given date??
	private Date deadline;
	private int bedCount;
	private boolean hasShower;

	// TODO maybe add boolean hasTV or more of such things !?!

	public boolean getHasShower()
	{
		return hasShower;
	}

	public void setHasShower(boolean hasShower)
	{
		this.hasShower = hasShower;
	}

	public int getBedCount()
	{
		return bedCount;
	}

	public void setBedCount(int bedCount)
	{
		this.bedCount = bedCount;
	}

	@Deprecated
	public String getTitle()
	{
		return title;
	}

	@Deprecated
	public void setTitle(String title)
	{
		this.title = title;
	}

	@Deprecated
	public int getMaxPrice()
	{
		return maxPrice;
	}

	@Deprecated
	public void setMaxPrice(int maxPrice)
	{
		this.maxPrice = maxPrice;
	}

	public Date getDeadline()
	{
		return deadline;
	}

	public void setDeadline(Date deadline)
	{
		this.deadline = deadline;
	}
}