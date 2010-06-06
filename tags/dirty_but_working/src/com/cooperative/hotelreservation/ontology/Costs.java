/**
 * Section 5.1.3.2 Page 85 Java class representing a Costs
 **/

package com.cooperative.hotelreservation.ontology;

import jade.content.Predicate;

public class Costs implements Predicate
{

	private int price;
	private Room item;

	public void setItem(Room item)
	{
		this.item = item;
	}

	public Room getItem()
	{
		return item;
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