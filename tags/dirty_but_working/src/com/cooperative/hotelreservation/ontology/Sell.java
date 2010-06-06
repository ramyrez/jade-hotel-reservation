/**
 * Class associated to the SELL schema
 **/
package com.cooperative.hotelreservation.ontology;

import jade.content.AgentAction;

public class Sell implements AgentAction
{
	private Room item;

	public void setItem(Room item)
	{
		this.item = item;
	}

	public Room getItem()
	{
		return item;
	}

}