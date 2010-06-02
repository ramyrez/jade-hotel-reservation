package com.cooperative.hotelreservation.seller;

import jade.content.ContentElementList;
import jade.content.ContentManager;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;

import com.cooperative.hotelreservation.ontology.Costs;
import com.cooperative.hotelreservation.ontology.Sell;
import com.cooperative.hotelreservation.rent.RoomInfo;

public class CallForOfferServer extends ContractNetResponder
{

	int price;
	private RoomSellerAgent rsa;

	CallForOfferServer(RoomSellerAgent rsa, Ontology ontology)
	{
		super(rsa, MessageTemplate.and(MessageTemplate.MatchOntology(ontology.getName()), MessageTemplate
				.MatchPerformative(ACLMessage.CFP)));

		this.rsa = rsa;
	}

	protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept)
			throws FailureException
	{
		ACLMessage inform = accept.createReply();
		inform.setPerformative(ACLMessage.INFORM);
		inform.setContent(Integer.toString(price));
		rsa.notifyUser("Sent inform at price " + price);
		return inform;
	}

	protected ACLMessage handleCfp(ACLMessage cfp) throws RefuseException, FailureException, NotUnderstoodException
	{
		// CFP Message received. Process it
		ACLMessage reply = cfp.createReply();
		try
		{
			ContentManager cm = myAgent.getContentManager();
			Action act = (Action) cm.extractContent(cfp);
			Sell sellAction = (Sell) act.getAction();
			RoomInfo roomInfo = sellAction.getRoomInfo();
			// Book book = sellAction.getItem();
			rsa.notifyUser("received proposal for a room " + roomInfo);

			// pick on free room for given room requirements, and create a
			// room seller price manager
			RoomSellerPriceManager pm = null;
			if (pm != null)
			{
				// The requested book is available for sale
				reply.setPerformative(ACLMessage.PROPOSE);
				ContentElementList cel = new ContentElementList();
				cel.add(act);
				Costs costs = new Costs();
				costs.setRoomInfo(roomInfo);
				price = pm.getCurrentPrice();
				costs.setPrice(price);
				cel.add(costs);
				cm.fillContent(reply, cel);
			}
			else
			{
				// The requested book is NOT available for sale.
				reply.setPerformative(ACLMessage.REFUSE);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
		}
		rsa.notifyUser(reply.getPerformative() == ACLMessage.PROPOSE ? "Sent proposal to sell room at " + price
				: "refused proposal, as there is no room for sale");
		return reply;
	}

}
