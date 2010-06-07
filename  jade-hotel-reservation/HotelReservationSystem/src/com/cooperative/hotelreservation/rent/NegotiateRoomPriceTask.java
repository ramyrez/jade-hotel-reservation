package com.cooperative.hotelreservation.rent;

import jade.content.ContentElementList;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import com.cooperative.hotelreservation.ontology.CostsPredicate;
import com.cooperative.hotelreservation.ontology.Room;
import com.cooperative.hotelreservation.ontology.RentAgentAction;

/**
 * Section 5.4.2 of the book, page 104 Inner class BookNegotiator. This is the
 * behaviour reimplemented by using the ContractNetInitiator
 **/
public class NegotiateRoomPriceTask extends ContractNetInitiator
{

	private static final long serialVersionUID = -4144630640730144544L;
	private int maxPrice;
	private RentRoomTask rentRoomTask;
	private Room room;
	private final RoomRentAgent roomRentAgent;

	public NegotiateRoomPriceTask(RoomRentAgent roomRentAgent, ACLMessage callForProposal, Room room, int p,
			RentRoomTask m)
	{
		super(roomRentAgent, callForProposal);

		this.roomRentAgent = roomRentAgent;
		this.room = room;
		maxPrice = p;
		rentRoomTask = m;
		RentAgentAction rent = new RentAgentAction();
		rent.setRoom(room);
		Action act = new Action(roomRentAgent.getAID(), rent);
		try
		{
			callForProposal.setLanguage(roomRentAgent.getCodec().getName());
			callForProposal.setOntology(roomRentAgent.getOntology().getName());
			roomRentAgent.getContentManager().fillContent(callForProposal, act);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	protected void handleAllResponses(Vector responses, Vector acceptances)
	{
		ACLMessage bestOffer = null;
		int bestPrice = -1;
		for (int i = 0; i < responses.size(); i++)
		{
			ACLMessage rsp = (ACLMessage) responses.get(i);
			if (rsp.getPerformative() == ACLMessage.PROPOSE)
			{
				try
				{
					ContentElementList cel = (ContentElementList) myAgent.getContentManager().extractContent(rsp);
					int price = ((CostsPredicate) cel.get(1)).getPrice();
					roomRentAgent.notifyUser("Received Proposal at " + price + " when maximum acceptable price was "
							+ maxPrice);
					if (bestOffer == null || price < bestPrice)
					{
						bestOffer = rsp;
						bestPrice = price;
					}
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		for (int i = 0; i < responses.size(); i++)
		{
			ACLMessage rsp = (ACLMessage) responses.get(i);
			ACLMessage accept = rsp.createReply();
			if (rsp == bestOffer)
			{
				boolean acceptedProposal = (bestPrice <= maxPrice);
				accept.setPerformative(acceptedProposal ? ACLMessage.ACCEPT_PROPOSAL : ACLMessage.REJECT_PROPOSAL);
				try
				{
					accept.setContentObject(room);
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				roomRentAgent.notifyUser(acceptedProposal ? "sent Accept Proposal" : "sent Reject Proposal");
			}
			else
			{
				accept.setPerformative(ACLMessage.REJECT_PROPOSAL);
			}
			// System.out.println(myAgent.getLocalName()+" handleAllResponses.acceptances.add "+accept);
			acceptances.add(accept);
		}
	}

	protected void handleInform(ACLMessage inform)
	{
		// Book successfully purchased
		int price = Integer.parseInt(inform.getContent());
		roomRentAgent.notifyUser(room + " successfully purchased. Price =" + price);
		rentRoomTask.stop();
	}

	protected Vector prepareCfps(ACLMessage cfp)
	{
		cfp.clearAllReceiver();
		List<AID> sellerAgents = roomRentAgent.getSellerAgents();
		for (AID sellerAgent : sellerAgents)
		{
			cfp.addReceiver(sellerAgent);
		}
		Vector<ACLMessage> v = new Vector<ACLMessage>();
		v.add(cfp);
		if (sellerAgents.size() > 0)
			roomRentAgent.notifyUser("Sent Call for Proposal to " + sellerAgents.size() + " sellers.");
		return v;
	}

} // End of inner class BookNegotiator
