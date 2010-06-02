/**
 * Class associated to the SELL schema
 **/
package com.cooperative.hotelreservation.ontology;

import com.cooperative.hotelreservation.rent.RoomInfo;

import jade.content.AgentAction;
import jade.core.AID;

public class Sell implements AgentAction
{
	private RoomInfo roomInfo;
	private Book item;

	public void setRoomInfo(RoomInfo roomInfo)
	{
		this.roomInfo = roomInfo;
	}

	public RoomInfo getRoomInfo()
	{
		return roomInfo;
	}

	public Book getItem()
	{
		return item;
	}

	public void setItem(Book item)
	{
		this.item = item;
	}

}