package com.cooperative.hotelreservation.rent;

import jade.core.behaviours.TickerBehaviour;

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
			// deadline has expired
			roomRentAgent.addLogMsg("There is no " + room + " available with maximum price of " + maxPrice);
			roomRentAgent.removeBehaviour(this);
			stop();
		}
		else
		{
			// Compute the currently acceptable price and start room negotiation
			long elapsedTime = currentTime - initTime;
			int acceptablePrice = (int) Math.round(1.0 * maxPrice * (1.0 * elapsedTime / deltaT));

			NegotiateRoomPriceTask task = new NegotiateRoomPriceTask(roomRentAgent, room, this, acceptablePrice);
			this.roomRentAgent.addBehaviour(task);

			// inform the agent of the current maximum price
			this.roomRentAgent.updateCurrentMaxPrice(acceptablePrice);
		}
	}
}
