/**
 * Section 5.1.3.2 Page 85 Java class representing a Costs
 **/

package com.cooperative.hotelreservation.ontology;

import jade.content.Predicate;

import com.cooperative.hotelreservation.rent.RoomInfo;

public class Costs implements Predicate
{

	private RoomInfo roomInfo;
	private int price;

	public void setRoomInfo(RoomInfo roomInfo)
	{
		this.roomInfo = roomInfo;
	}

	public RoomInfo getRoomInfo()
	{
		return roomInfo;
	}

	public int getPrice()
	{
		return price;
	}

	public void setPrice(int price)
	{
		this.price = price;
	}
}