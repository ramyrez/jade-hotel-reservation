/**
 * skeleton of the Book-BuyerAgent class.
 **/
package com.cooperative.hotelreservation.rent;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.cooperative.hotelreservation.Delays;
import com.cooperative.hotelreservation.ontology.Room;
import com.cooperative.hotelreservation.ontology.RoomReservationOntology;

public class RoomRentAgent extends Agent
{

	private static final long serialVersionUID = -1633994238851510422L;

	// The list of known seller agents
	private List<AID> sellerAgents = new LinkedList<AID>();

	// The GUI to interact with the user
	private RoomRentGui roomRentGui;

	private Codec codec = new SLCodec();
	private RoomReservationOntology ontology = RoomReservationOntology.getInstance();

	public Codec getCodec()
	{
		return codec;
	}

	public Ontology getOntology()
	{
		return ontology;
	}

	public List<AID> getSellerAgents()
	{
		return sellerAgents;
	}

	public void notifyUser(String message)
	{
		roomRentGui.notifyUser(message);
	}

	public void purchase(Room room, int maxPrice, Date deadline)
	{
		addBehaviour(new RentRoomTask(this, room, maxPrice, deadline));
	}

	public void setAvailableSellerAgents(List<AID> sellerAgents)
	{
		this.sellerAgents.clear();
		this.sellerAgents.addAll(sellerAgents);
	}

	public void updateCurrentMaxPrice(int acceptablePrice)
	{
		roomRentGui.updateCurrentMaxPrice(acceptablePrice);
	}

	/**
	 * Agent initializations
	 **/
	protected void setup()
	{

		/**
		 * The following piece of code is explained in section 5.6.1 pag. 113 of
		 * the book. It processes notifications from the external buying system
		 * (other modifications also need to be introduced to handle the
		 * successful purchase or deadline expiration).
		 **/
		// Enable O2A Communication
		setEnabledO2ACommunication(true, 0);
		// Add the behaviour serving notifications from the external system

		// Printout a welcome message
		System.out.println("Rent-agent " + getAID().getName() + " is ready.");

		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);

		// Show the GUI to interact with the user
		roomRentGui = new RoomRentGui();
		roomRentGui.setAgent(this);
		roomRentGui.setVisible(true);

		// update the list of seller agents every ten seconds
		addBehaviour(new UpdateSellerAgentBehavior(this, Delays.TEN_SECONDS));
	}

	/**
	 * Agent clean-up
	 **/
	protected void takeDown()
	{
		// Dispose the GUI if it is there
		if (roomRentGui != null)
		{
			roomRentGui.setVisible(false);
		}

		// Printout a dismissal message
		System.out.println("Buyer-agent " + getAID().getName() + "terminated.");
	}
}
