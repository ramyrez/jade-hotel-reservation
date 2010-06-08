package com.cooperative.hotelreservation.ontology;

import jade.content.Predicate;

/**
 * This predicate represents the costs for a given room.
 */
public class CostsPredicate implements Predicate
{

	private static final long serialVersionUID = -4903812029640824150L;

	// price of this room
	private int price;

	// room
	private Room room;

	public Room getRoom()
	{
		return room;
	}

	public int getPrice()
	{
		return price;
	}

	public void setRoom(Room item)
	{
		this.room = item;
	}

	public void setPrice(int price)
	{
		this.price = price;
	}
}