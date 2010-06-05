/**
 * Section 4.2.5.2, Page 63
 **/
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.cooperative.hotelreservation.ontology.Room;
import com.cooperative.hotelreservation.ontology.RoomReservationOntology;
import com.cooperative.hotelreservation.rent.RoomInfo;

public class RoomSellerAgent extends Agent
{

	public static final String TYPE = "Room-Seller";

	// all available rooms
	private List<RoomInfo> rooms;

	private Map<Room, RoomSellerPriceManager> priceManagers;

	// The GUI to interact with the user
	private RoomSellerGui roomSellerGui;

	private Codec codec = new SLCodec();
	private Ontology ontology = RoomReservationOntology.getInstance();

	public void addNewRoomForRent(Room room, int initPrice, int minPrice, Date deadline)
	{
		addBehaviour(new RoomSellerPriceManager(this, room, initPrice, minPrice, deadline));
	}

	public void notifyUser(String string)
	{
		roomSellerGui.notifyUser(string);
	}

	/**
	 * Agent initializations
	 **/
	protected void setup()
	{
		// Printout a welcome message
		System.out.println("Seller-agent " + getAID().getName() + " is ready.");

		rooms = new LinkedList<RoomInfo>();
		priceManagers = new HashMap<Room, RoomSellerPriceManager>();

		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);

		// Create and show the GUI
		roomSellerGui = new RoomSellerGui(this);
		roomSellerGui.setVisible(true);

		// Add the behaviour serving calls for price from buyer agents
		addBehaviour(new CallForOfferServer(this, ontology));

		// Add the behaviour serving purchase requests from buyer agents
		// addBehaviour(new PurchaseOrderServer());

		/**
		 * This piece of code, to register services with the DF, is explained in
		 * the book in section 4.4.2.1, page 73
		 **/
		// Register the book-selling service in the yellow pages
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

	/**
	 * Agent clean-up
	 **/
	protected void takeDown()
	{
		// Dispose the GUI if it is there
		if (roomSellerGui != null)
		{
			roomSellerGui.setVisible(false);
		}

		// Printout a dismissal message
		System.out.println("Seller-agent " + getAID().getName() + "terminating.");

		/**
		 * This piece of code, to deregister with the DF, is explained in the
		 * book in section 4.4.2.1, page 73
		 **/
		// Deregister from the yellow pages
		try
		{
			DFService.deregister(this);
		} catch (FIPAException fe)
		{
			fe.printStackTrace();
		}
	}

	public RoomSellerPriceManager getRoomSellerPriceManagerForRoom(Room room)
	{
		return priceManagers.get(room);
	}

	public void addRoomSellerPriceManager(Room room, RoomSellerPriceManager roomSellerPriceManager)
	{
		priceManagers.put(room, roomSellerPriceManager);
	}

}