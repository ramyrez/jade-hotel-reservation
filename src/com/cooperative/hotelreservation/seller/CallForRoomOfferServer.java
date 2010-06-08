package com.cooperative.hotelreservation.seller;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import com.cooperative.hotelreservation.ontology.CostsPredicate;
import com.cooperative.hotelreservation.ontology.RentAgentAction;
import com.cooperative.hotelreservation.ontology.Room;

public class CallForRoomOfferServer extends CyclicBehaviour
{

	private static final long serialVersionUID = 147495886799182189L;

	private int currentPrice;
	private final RoomSellerAgent rsa;

	public CallForRoomOfferServer(RoomSellerAgent rsa)
	{
		super(rsa);
		this.rsa = rsa;
	}

	@Override
	public void action()
	{
		// MessageTemplate matchOntology =
		// MessageTemplate.MatchOntology(RoomReservationOntology.getInstance().getName());
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
		inform.setContent(Integer.toString(currentPrice));

		try
		{
			// extract room
			Room room = (Room) message.getContentObject();
			rsa.addLogMsg("room " + room + " has been reserved by an user for " + currentPrice);
			rsa.removeRoomSellerPriceManager(room);
		} catch (UnreadableException e)
		{
			e.printStackTrace();
		}

		return inform;
	}

	private ACLMessage handleCFPMessage(ACLMessage message)
	{
		ACLMessage reply = message.createReply();
		try
		{

			// extract the room out of the CFP message
			RentAgentAction rAA = (RentAgentAction) message.getContentObject();
			Room room = rAA.getRoom();
			rsa.addLogMsg("received proposal for a room " + room);

			// get the room seller price manager for given room
			RoomSellerPriceManager pm = rsa.getRoomSellerPriceManagerForRoom(room);
			if (pm != null)
			{
				// create the propose as reply
				reply.setPerformative(ACLMessage.PROPOSE);
				currentPrice = pm.getCurrentPrice();

				CostsPredicate costs = new CostsPredicate();
				costs.setPrice(currentPrice);
				costs.setRoom(room);

				reply.setContentObject(costs);

				rsa.addLogMsg("sent proposal to offer the room at a rent of " + currentPrice);
			}
			else
			{
				// there is no room available
				reply.setPerformative(ACLMessage.REFUSE);
			}
		} catch (Exception e)
		{
			// message has not been understood
			reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
			rsa.addLogMsg("did not understood the message");
		}
		return reply;
	}

}
