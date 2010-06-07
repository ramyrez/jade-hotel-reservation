package com.cooperative.hotelreservation.rent;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.List;

import com.cooperative.hotelreservation.Delays;
import com.cooperative.hotelreservation.ontology.CostsPredicate;
import com.cooperative.hotelreservation.ontology.RentAgentAction;
import com.cooperative.hotelreservation.ontology.Room;

/**
 * Section 5.4.2 of the book, page 104 Inner class BookNegotiator. This is the
 * behaviour reimplemented by using the ContractNetInitiator
 **/
public class NewNegotiateRoomPriceTask extends Behaviour
{

	private static final long serialVersionUID = -4144630640730144544L;
	private int maxPrice;
	private RentRoomTask rentRoomTask;
	private Room room;
	private final RoomRentAgent roomRentAgent;

	public NewNegotiateRoomPriceTask(RoomRentAgent roomRentAgent, Room room, int p, RentRoomTask m)
	{
		super(roomRentAgent);

		this.roomRentAgent = roomRentAgent;
		this.room = room;
		maxPrice = p;
		rentRoomTask = m;
	}

	private int step = 0;
	private MessageTemplate mt;
	private AID[] sellerAgents;
	private AID bestSeller;
	private int currentPrice;
	private int bestPrice;
	private int repliesCnt;

	@Override
	public void action()
	{
		try
		{
			switch (step)
			{
				case 0:
					// send CFP to all sellers
					sendCFPToAllSellers();
					break;
				case 1:
					// handle proposals/informs/refuses from the sellers
					handleSellerAgentReply();
					break;
				case 2:
					// accept proposal for best offer
					sendOutBestOffer();
					break;
				case 3:
					// receive the inform that room has been rent
					handleReceiveRoomReservationAck();
					break;

			}
		} catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	@Override
	public boolean done()
	{
		if (step == 2 && bestSeller == null)
		{
			// roomRentAgent.notifyUser(room +
			// " cannot be rent, because there are no sellers");
		}

		// we are done if there are no seller agents or we have succesfully rent
		// a room
		return ((step == 2 && bestSeller == null) || step == 4);
	}

	private void handleReceiveRoomReservationAck()
	{
		// Receive the purchase order reply
		ACLMessage reply = myAgent.receive(mt);
		if (reply != null)
		{
			// Purchase order reply received
			if (reply.getPerformative() == ACLMessage.INFORM)
			{
				// Purchase successful. We can terminate
				roomRentAgent.notifyUser(room + " successfully purchased. Price =" + bestPrice);
				
				// remove the rent room task from the agent
				roomRentAgent.removeBehaviour(rentRoomTask);
			}
			else
			{
				System.out.println("Attempt failed: requested book already sold.");
			}

			step = 4;
		}
		else
		{
			block();
		}
	}

	private void sendOutBestOffer()
	{
		// Send the purchase order to the seller that provided the best offer
		ACLMessage acceptProposal = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
		acceptProposal.addReceiver(bestSeller);
		acceptProposal.setReplyWith("rent-room" + System.currentTimeMillis());

		myAgent.send(acceptProposal);

		// Prepare the template to get the purchase order reply
		mt = MessageTemplate.MatchInReplyTo(acceptProposal.getReplyWith());
		step = 3;
	}

	private void handleSellerAgentReply() throws UnreadableException
	{
		// Receive all proposals/refusals from seller agents
		ACLMessage reply = myAgent.receive(mt);
		if (reply != null)
		{
			// Reply received
			if (reply.getPerformative() == ACLMessage.PROPOSE)
			{
				// This is an offer
				CostsPredicate cP = (CostsPredicate) reply.getContentObject();
				currentPrice = cP.getPrice();
				if ((bestSeller == null || currentPrice < bestPrice) && currentPrice < maxPrice)
				{
					// This is the best offer at present
					bestPrice = currentPrice;
					bestSeller = reply.getSender();
				}
			}
			repliesCnt++;
			if (repliesCnt >= sellerAgents.length)
			{
				// We received all replies
				step = 2;
			}
		}
		else
		{
			block();
		}
	}

	private void sendCFPToAllSellers() throws IOException
	{
		// get actual list of sellers
		List<AID> agents = roomRentAgent.getSellerAgents();
		sellerAgents = agents.toArray(new AID[] {});

		// create new Call for Proposal message
		ACLMessage cfpMsg = new ACLMessage(ACLMessage.CFP);
		for (AID seller : agents)
		{
			cfpMsg.addReceiver(seller);
		}

		RentAgentAction rra = new RentAgentAction();
		rra.setRoom(room);
		cfpMsg.setContentObject(rra);
		cfpMsg.setReplyWith("rent-room-" + System.currentTimeMillis());

		mt = MessageTemplate.MatchInReplyTo(cfpMsg.getReplyWith());

		myAgent.send(cfpMsg);

		// switch to step where we wait for all proposals / refuses
		step = 1;
	}

}
