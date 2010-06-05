/**
 * This is a simple support class that allows to keep aggregated some pieces of
 * information related to a Hotel Room
 **/
package com.cooperative.hotelreservation.rent;

import java.util.Date;

import com.cooperative.hotelreservation.ontology.Room;

public class RoomInfo extends Room
{

	private int maxPrice;
	private Date deadline;

	public int getMaxPrice()
	{
		return maxPrice;
	}

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