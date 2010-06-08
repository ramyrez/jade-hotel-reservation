package com.cooperative.hotelreservation.seller;

import jade.core.behaviours.TickerBehaviour;

import java.util.Date;

import com.cooperative.hotelreservation.Delays;
import com.cooperative.hotelreservation.ontology.Room;

public class RoomSellerPriceManager extends TickerBehaviour
{
	private static final long serialVersionUID = 5144870849225835501L;

	private int currentPrice, initPrice, deltaP;
	private long initTime, deltaT;
	private final Room room;
	private final RoomSellerAgent roomSellerAgent;
	private final Date deadline;
	private long deadlineTime;

	public RoomSellerPriceManager(RoomSellerAgent roomSellerAgent, Room room, int startPrice, int minimumPrice,
			Date deadline)
	{
		// delay of 1 second
		super(roomSellerAgent, Delays.ONE_SECOND);

		this.roomSellerAgent = roomSellerAgent;
		this.room = room;
		this.initPrice = startPrice;
		this.deadline = deadline;
		this.deadlineTime = deadline.getTime();
		currentPrice = initPrice;
		deltaP = initPrice - minimumPrice;
		initTime = System.currentTimeMillis();
		deltaT = ((deadlineTime - initTime) > 0 ? (deadlineTime - initTime) : 60000);
	}

	public int getCurrentPrice()
	{
		return currentPrice;
	}

	public void onStart()
	{
		super.onStart();
		roomSellerAgent.addRoomSellerPriceManager(room, this);
	}

	public void onTick()
	{
		long currentTime = System.currentTimeMillis();
		if (currentTime > deadline.getTime())
		{
			// deadline over
			roomSellerAgent.addLogMsg("room not sold " + room);
			roomSellerAgent.removeRoomSellerPriceManager(room);
			stop();
		}
		else
		{
			// calculate current price
			long elapsedTime = currentTime - initTime;
			currentPrice = (int) Math.round(initPrice - 1.0 * deltaP * (1.0 * elapsedTime / deltaT));
			roomSellerAgent.updatePriceForRoom(room, currentPrice);
		}
	}
}
