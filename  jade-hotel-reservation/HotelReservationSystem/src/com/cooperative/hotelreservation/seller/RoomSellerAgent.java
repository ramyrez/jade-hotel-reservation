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
import java.util.LinkedList;
import java.util.List;

import com.cooperative.hotelreservation.ontology.BookTradingOntology;
import com.cooperative.hotelreservation.rent.RoomInfo;

public class RoomSellerAgent extends Agent
{

	// all available rooms
	private List<RoomInfo> rooms;

	// The GUI to interact with the user
	private RoomSellerGui roomSellerGui;

	private Codec codec = new SLCodec();
	private Ontology ontology = BookTradingOntology.getInstance();

	public void addNewRoom(RoomInfo roomInfo, int initPrice, int minPrice, Date deadline)
	{
		addBehaviour(new RoomSellerPriceManager(this, roomInfo, initPrice, minPrice, deadline));
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

		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);

		// Create and show the GUI
		roomSellerGui = new RoomSellerGui();
		roomSellerGui.setAgent(this);
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
		sd.setType("Book-selling");
		sd.setName(getLocalName() + "-Book-selling");
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

}