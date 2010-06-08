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

	private List<AID> sellerAgents = new LinkedList<AID>();
	private RoomRentGui roomRentGui;
	private Codec codec = new SLCodec();
	private RoomReservationOntology ontology = RoomReservationOntology.getInstance();

	public void addLogMsg(String message)
	{
		roomRentGui.addLogMsg(message);
	}

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
		// register codec and ontology
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);

		// create a new rent gui and show it
		roomRentGui = new RoomRentGui();
		roomRentGui.setAgent(this);
		roomRentGui.setVisible(true);

		// update the list of seller agents every ten seconds
		addBehaviour(new UpdateSellerAgentBehavior(this, Delays.TEN_SECONDS));
	}

	protected void takeDown()
	{
		if (roomRentGui != null)
		{
			roomRentGui.setVisible(false);
		}
	}
}
