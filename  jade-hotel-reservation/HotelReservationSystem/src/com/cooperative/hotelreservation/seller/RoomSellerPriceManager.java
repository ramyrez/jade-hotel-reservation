package com.cooperative.hotelreservation.seller;

import jade.core.behaviours.TickerBehaviour;

import java.util.Date;

import com.cooperative.hotelreservation.ontology.Room;

public class RoomSellerPriceManager extends TickerBehaviour
{
	private int minPrice, currentPrice, initPrice, deltaP;
	private long initTime, deltaT;
	private final Room room;
	private final RoomSellerAgent roomSellerAgent;
	private final Date deadline;
	private long deadlineTime;

	public RoomSellerPriceManager(RoomSellerAgent a, Room room, int ip, int mp, Date d)
	{
		super(a, 10000);
		this.roomSellerAgent = a;
		this.room = room;
		this.initPrice = ip;
		this.deadline = d;
		this.deadlineTime = d.getTime();
		currentPrice = initPrice;
		deltaP = initPrice - mp;
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
			roomSellerAgent.notifyUser("room not sold " + room);
			stop();
		}
		else
		{
			// Compute the current price
			long elapsedTime = currentTime - initTime;
			currentPrice = (int) Math.round(initPrice - 1.0 * deltaP * (1.0 * elapsedTime / deltaT));
			roomSellerAgent.notifyUser("current price for room " + room + " == " + currentPrice);
		}
	}
}
