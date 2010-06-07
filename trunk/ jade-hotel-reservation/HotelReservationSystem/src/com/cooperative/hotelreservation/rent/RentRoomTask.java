package com.cooperative.hotelreservation.rent;

import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Date;

import com.cooperative.hotelreservation.Delays;
import com.cooperative.hotelreservation.ontology.Room;

public class RentRoomTask extends TickerBehaviour
{

	private static final long serialVersionUID = -4481981636485592783L;

	private int maxPrice;
	private long deadline, initTime, deltaT;
	private final Room room;
	private final RoomRentAgent roomRentAgent;

	public RentRoomTask(RoomRentAgent roomRentAgent, Room room, int maxPrice, Date deadline)
	{
		super(roomRentAgent, Delays.ONE_SECOND);

		this.roomRentAgent = roomRentAgent;
		this.room = room;
		this.maxPrice = maxPrice;
		this.deadline = deadline.getTime();
		initTime = System.currentTimeMillis();
		deltaT = this.deadline - initTime;
	}

	public void onTick()
	{
		long currentTime = System.currentTimeMillis();
		if (currentTime > deadline)
		{
			// Deadline expired
			roomRentAgent.notifyUser("Cannot rent room " + room);
			roomRentAgent.removeBehaviour(this);
			stop();
		}
		else
		{
			// Compute the currently acceptable price and start a
			// negotiation
			long elapsedTime = currentTime - initTime;
			int acceptablePrice = (int) Math.round(1.0 * maxPrice * (1.0 * elapsedTime / deltaT));

			ACLMessage callForProposal = new ACLMessage(ACLMessage.CFP);
			NegotiateRoomPriceTask task = new NegotiateRoomPriceTask(roomRentAgent, callForProposal, room,
					acceptablePrice, this);
			this.roomRentAgent.addBehaviour(task);
			this.roomRentAgent.updateCurrentMaxPrice(acceptablePrice);
		}
	}
}
