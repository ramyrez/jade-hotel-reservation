package com.cooperative.hotelreservation.seller;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.cooperative.hotelreservation.ontology.Room;
import com.cooperative.hotelreservation.ontology.RoomReservationOntology;

public class RoomSellerAgent extends Agent
{

	public static final String TYPE = "room-seller";

	private static final long serialVersionUID = 5748395638208816754L;

	private Map<Room, RoomSellerPriceManager> priceManagers;
	private RoomSellerGui roomSellerGui;
	private Codec codec = new SLCodec();
	private Ontology ontology = RoomReservationOntology.getInstance();

	public void addLogMsg(String string)
	{
		roomSellerGui.addLogMsg(string);
	}

	public void addNewRoomForRent(Room room, int initPrice, int minPrice, Date deadline)
	{
		addBehaviour(new RoomSellerPriceManager(this, room, initPrice, minPrice, deadline));
	}

	public void addRoomSellerPriceManager(Room room, RoomSellerPriceManager roomSellerPriceManager)
	{
		priceManagers.put(room, roomSellerPriceManager);
	}

	public RoomSellerPriceManager getRoomSellerPriceManagerForRoom(Room room)
	{
		return priceManagers.get(room);
	}

	public void removeRoomSellerPriceManager(Room room)
	{
		roomSellerGui.removeRoom(room);
		RoomSellerPriceManager priceManager = priceManagers.remove(room);
		if (priceManager != null)
			removeBehaviour(priceManager);
	}

	public void updatePriceForRoom(Room room, int currentPrice)
	{
		roomSellerGui.updatePriceForRoom(room, currentPrice);
	}

	protected void setup()
	{
		priceManagers = new HashMap<Room, RoomSellerPriceManager>();

		// register codec and ontology
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);

		roomSellerGui = new RoomSellerGui(this);
		roomSellerGui.setVisible(true);

		// add behavior which listens on call for proposals
		addBehaviour(new CallForRoomOfferServer(this));

		// register this agent as a service which sells rooms
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(TYPE);
		sd.setName(getLocalName() + "-" + TYPE);
		dfd.addServices(sd);
		try
		{
			DFService.register(this, dfd);
		} catch (FIPAException fe)
		{
			fe.printStackTrace();
		}
	}

	protected void takeDown()
	{
		if (roomSellerGui != null)
		{
			roomSellerGui.setVisible(false);
		}

		// remove this agents service from the directory facilitator
		try
		{
			DFService.deregister(this);
		} catch (FIPAException e)
		{
			e.printStackTrace();
		}
	}

}