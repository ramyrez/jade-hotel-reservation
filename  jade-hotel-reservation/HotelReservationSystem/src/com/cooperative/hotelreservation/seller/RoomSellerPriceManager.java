package com.cooperative.hotelreservation.seller;

import jade.core.behaviours.TickerBehaviour;

import java.util.Date;

import com.cooperative.hotelreservation.rent.RoomInfo;

public class RoomSellerPriceManager extends TickerBehaviour
{
	private int minPrice, currentPrice, initPrice, deltaP;
	private long initTime, deltaT;
	private final RoomInfo roomInfo;
	private final RoomSellerAgent roomSellerAgent;
	private final Date deadline;
	private long deadlineTime;

	RoomSellerPriceManager(RoomSellerAgent a, RoomInfo roomInfo, int ip, int mp, Date d)
	{
		super(a, 500);
		this.roomSellerAgent = a;
		this.roomInfo = roomInfo;
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
		roomSellerAgent.addNewRoom(roomInfo, initPrice, minPrice, deadline);
		super.onStart();
	}

	public void onTick()
	{
		long currentTime = System.currentTimeMillis();
		if (currentTime > deadline.getTime())
		{
			// deadline over
			roomSellerAgent.notifyUser("room not sold " + roomInfo);
			stop();
		}
		else
		{
			// Compute the current price
			long elapsedTime = currentTime - initTime;
			currentPrice = (int) Math.round(initPrice - 1.0 * deltaP * (1.0 * elapsedTime / deltaT));
		}
	}
}
