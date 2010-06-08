package com.cooperative.hotelreservation.ontology;

import jade.content.AgentAction;

/**
 * This AgentAction holds a description of the room which shall be rent.
 */
public class RentAgentAction implements AgentAction
{
	private static final long serialVersionUID = 4301907219616486436L;

	private Room room;

	public Room getRoom()
	{
		return room;
	}

	public void setRoom(Room room)
	{
		this.room = room;
	}

}