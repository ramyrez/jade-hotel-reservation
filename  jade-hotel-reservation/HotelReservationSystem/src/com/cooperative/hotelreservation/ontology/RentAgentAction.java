/**
 * Class associated to the RENT schema
 **/
package com.cooperative.hotelreservation.ontology;

import jade.content.AgentAction;

public class RentAgentAction implements AgentAction
{
	private static final long serialVersionUID = 4301907219616486436L;

	private Room room;

	public void setRoom(Room room)
	{
		this.room = room;
	}

	public Room getRoom()
	{
		return room;
	}

}