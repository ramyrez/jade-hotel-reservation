package com.cooperative.hotelreservation.main;

import jade.Boot;

import com.cooperative.hotelreservation.rent.RoomRentAgent;
import com.cooperative.hotelreservation.seller.RoomSellerAgent;

public class GuiTest
{

	public static void main(String[] args)
	{
		String rentAgent = RoomRentAgent.class.getName();
		String sellAgent = RoomSellerAgent.class.getName();
		Boot.main(new String[] { "-gui", "rsa:" + sellAgent + ";rra:" + rentAgent });
	}

}