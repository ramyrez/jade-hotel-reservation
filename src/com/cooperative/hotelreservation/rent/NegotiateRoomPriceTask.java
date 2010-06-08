package com.cooperative.hotelreservation.rent;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.List;

import com.cooperative.hotelreservation.ontology.CostsPredicate;
import com.cooperative.hotelreservation.ontology.RentAgentAction;
import com.cooperative.hotelreservation.ontology.Room;

/**
 * This Behaviour handles the contact of all available seller agents, call them
 * for a proposal, handle the returned proposals and choosing the best/cheapest
 * offer of an agent - if available.
 **/
public class NegotiateRoomPriceTask extends Behaviour
{

	private static final long serialVersionUID = -4144630640730144544L;
	private int maxPrice;
	private RentRoomTask rentRoomTask;
	private Room room;
	private final RoomRentAgent roomRentAgent;

	public NegotiateRoomPriceTask(RoomRentAgent roomRentAgent, Room room, RentRoomTask rentRoomTask, int maxPrice)
	{
		super(roomRentAgent);

		this.roomRentAgent = roomRentAgent;
		this.room = room;
		this.maxPrice = maxPrice;
		this.rentRoomTask = rentRoomTask;
	}

	/**
	 * the following attributes are needed for the negotiation
	 */
	// current step of negotiation
	private int step = 0;

	// template which shall be used for receiving messages we are interested in
	private MessageTemplate mt;

	// available seller agents
	private AID[] sellerAgents;

	// best seller
	private AID bestSeller;

	// current price
	private int currentPrice;

	// best available price
	private int bestPrice;

	// count of replies we have got
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
		boolean noSellerAgents = (step == 2 && bestSeller == null);
		boolean finished = (step == 4);

		// we are done if there are no seller agents or we have successfully
		// rent a room
		return (noSellerAgents || finished);
	}

	private void handleReceiveRoomReservationAck()
	{
		// receive the room reservation inform reply
		ACLMessage reply = myAgent.receive(mt);
		if (reply != null)
		{
			// has the room been successfully rent?
			if (reply.getPerformative() == ACLMessage.INFORM)
			{
				roomRentAgent.addLogMsg(room + " successfully reserved at a price of " + bestPrice);

				// remove the rent room task from the agent
				roomRentAgent.removeBehaviour(rentRoomTask);
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
		// send accept proposal to the best room offerer
		ACLMessage acceptProposal = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
		acceptProposal.addReceiver(bestSeller);
		acceptProposal.setReplyWith("rent-room" + System.currentTimeMillis());
		try
		{
			acceptProposal.setContentObject(room);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		myAgent.send(acceptProposal);
		System.out.println("sending out accept proposal to " + bestSeller.getName());

		// change message template so we only listen on replies to this accept
		// proposal message
		mt = MessageTemplate.MatchInReplyTo(acceptProposal.getReplyWith());
		step = 3;
	}

	private void handleSellerAgentReply() throws UnreadableException
	{
		// receive all proposals/refusals from seller agents
		ACLMessage reply = myAgent.receive(mt);
		if (reply != null)
		{
			// proposal received
			if (reply.getPerformative() == ACLMessage.PROPOSE)
			{
				// this is an offer for a room
				CostsPredicate cP = (CostsPredicate) reply.getContentObject();
				currentPrice = cP.getPrice();
				if ((bestSeller == null || currentPrice < bestPrice) && currentPrice < maxPrice)
				{
					// change the best seller to this one
					bestPrice = currentPrice;
					bestSeller = reply.getSender();
				}

				System.out.println("received proposal from seller " + reply.getSender().getLocalName());
			}

			// update the counter of agents who had replied
			repliesCnt++;

			// check if all replies have been received
			if (repliesCnt >= sellerAgents.length)
				step = 2;
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

		// create call for proposal message to all seller agents
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
		System.out.println("send CFP to " + sellerAgents.length + " sellers");
		roomRentAgent.addLogMsg("send CFP to " + sellerAgents.length + " sellers");

		// switch to step where we wait for all proposals / refuses
		step = 1;
	}

}
