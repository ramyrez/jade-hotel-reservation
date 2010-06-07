package com.cooperative.hotelreservation.seller;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import com.cooperative.hotelreservation.ontology.CostsPredicate;
import com.cooperative.hotelreservation.ontology.RentAgentAction;
import com.cooperative.hotelreservation.ontology.Room;

public class NewCallForOfferServer extends CyclicBehaviour
{

	private static final long serialVersionUID = 147495886799182189L;
	private int price;
	private RoomSellerAgent rsa;

	public NewCallForOfferServer(RoomSellerAgent rsa)
	{
		super(rsa);
		this.rsa = rsa;
	}

	@Override
	public void action()
	{
		// MessageTemplate matchOntology = MessageTemplate.MatchOntology(RoomReservationOntology.getInstance().getName());
		ACLMessage message = myAgent.receive();
		if (message != null)
		{
			// check the type of message
			int performative = message.getPerformative();
			ACLMessage reply = null;
			if (performative == ACLMessage.CFP)
			{
				// received a message where we have to create a proposal for an
				// room
				reply = handleCFPMessage(message);
			}
			else if (performative == ACLMessage.ACCEPT_PROPOSAL)
			{
				// received a message where a proposal has been accepted
				reply = handleAcceptMessage(message);
			}

			if (reply != null)
				myAgent.send(reply);
		}
		else
		{
			block();
		}
	}

	private ACLMessage handleAcceptMessage(ACLMessage message)
	{
		ACLMessage inform = message.createReply();

		inform.setPerformative(ACLMessage.INFORM);
		inform.setContent(Integer.toString(price));

		rsa.notifyUser("Sent inform at price " + price);

		try
		{
			// extract room
			Room room = (Room) message.getContentObject();
			rsa.removeRoomSellerPriceManager(room);
		} catch (UnreadableException e)
		{
			e.printStackTrace();
		}

		return inform;
	}

	private ACLMessage handleCFPMessage(ACLMessage message)
	{
		// CFP Message received. Process it
		ACLMessage reply = message.createReply();
		try
		{
			RentAgentAction rAA = (RentAgentAction) message.getContentObject();
			Room room = rAA.getRoom();
			rsa.notifyUser("received proposal for a room " + room);

			// pick on free room for given room requirements, and create a
			// room seller price manager
			RoomSellerPriceManager pm = rsa.getRoomSellerPriceManagerForRoom(room);
			if (pm != null)
			{
				// The requested book is available for sale
				reply.setPerformative(ACLMessage.PROPOSE);
				price = pm.getCurrentPrice();

				CostsPredicate costs = new CostsPredicate();
				costs.setPrice(price);
				costs.setRoom(room);

				reply.setContentObject(costs);

				rsa.notifyUser("sent proposal to offer the room at a rent of " + price);
			}
			else
			{
				reply.setPerformative(ACLMessage.REFUSE);
				rsa.notifyUser("sent refuse as there is no room for rent");
			}
		} catch (Exception e)
		{
			reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
			rsa.notifyUser("did not understood the message");
		}
		return reply;
	}

}
