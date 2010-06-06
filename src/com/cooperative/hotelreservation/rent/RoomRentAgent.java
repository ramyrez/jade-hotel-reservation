/**
 * skeleton of the Book-BuyerAgent class.
 **/
package com.cooperative.hotelreservation.rent;

import jade.content.ContentElementList;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;

import java.util.Date;
import java.util.Vector;

import com.cooperative.hotelreservation.ontology.Costs;
import com.cooperative.hotelreservation.ontology.Room;
import com.cooperative.hotelreservation.ontology.RoomReservationOntology;
import com.cooperative.hotelreservation.ontology.Sell;
import com.cooperative.hotelreservation.seller.RoomSellerAgent;

public class RoomRentAgent extends Agent
{

	// The list of known seller agents
	private Vector sellerAgents = new Vector();

	// The GUI to interact with the user
	private RoomRentGui myGui;

	/**
	 * The following parts, where the SLCodec and RoomTradingOntology are
	 * registered, are explained in section 5.1.3.4 page 88 of the book.
	 **/
	private Codec codec = new SLCodec();
	private Ontology ontology = RoomReservationOntology.getInstance();

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
		
		/*
		addBehaviour(new CyclicBehaviour(this)
		{
			public void action()
			{
				RoomInfo info = (RoomInfo) myAgent.getO2AObject();
				if (info != null)
				{
					Room room = new Room();
					room.setBedCount(info.getBedCount());
					room.setHasShower(info.getHasShower());
					purchase(room, info.getMaxPrice(), info.getDeadline());
				}
				else
				{
					block();
				}
			}
		});
		*/

		// Printout a welcome message
		System.out.println("Rent-agent " + getAID().getName() + " is ready.");

		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);

		// Get names of seller agents as arguments
		Object[] args = getArguments();
		if (args != null && args.length > 0)
		{
			for (int i = 0; i < args.length; ++i)
			{
				AID seller = new AID((String) args[i], AID.ISLOCALNAME);
				sellerAgents.addElement(seller);
			}
		}

		// Show the GUI to interact with the user
		myGui = new RoomRentGui();
		myGui.setAgent(this);
		myGui.setVisible(true);

		/**
		 * This piece of code, to search services with the DF, is explained in
		 * the book in section 4.4.3, page 74
		 **/
		// Update the list of seller agents every 10 seconds
		addBehaviour(new TickerBehaviour(this, 10000)
		{
			protected void onTick()
			{
				// Update the list of seller agents
				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType(RoomSellerAgent.TYPE);
				template.addServices(sd);
				try
				{
					DFAgentDescription[] result = DFService.search(myAgent, template);
					sellerAgents.clear();
					for (int i = 0; i < result.length; ++i)
					{
						sellerAgents.addElement(result[i].getName());
					}
				} catch (FIPAException fe)
				{
					fe.printStackTrace();
				}
			}
		});
	}

	/**
	 * Agent clean-up
	 **/
	protected void takeDown()
	{
		// Dispose the GUI if it is there
		if (myGui != null)
		{
			myGui.dispose();
		}

		// Printout a dismissal message
		System.out.println("Buyer-agent " + getAID().getName() + "terminated.");
	}

	public void purchase(Room room, int maxPrice, Date deadline)
	{
		addBehaviour(new PurchaseManager(this, room, maxPrice, deadline));
	}

	private class PurchaseManager extends TickerBehaviour
	{
		private int maxPrice;
		private long deadline, initTime, deltaT;
		private final RoomRentAgent roomRentAgent;
		private final Room room;

		public PurchaseManager(RoomRentAgent roomRentAgent, Room room, int maxPrice, Date deadline)
		{
			// tick every 10 seconds
			super(roomRentAgent, 1000);

			this.roomRentAgent = roomRentAgent;
			this.room = room;
			this.maxPrice = maxPrice;
			this.deadline = deadline.getTime();
			initTime = System.currentTimeMillis();
			deltaT = this.deadline - initTime;
		}

		public void onTick()
		{
			long currentTime = System.currentTimeMillis();
			if (currentTime > deadline)
			{
				// Deadline expired
				myGui.notifyUser("Cannot rent room " + room);
				stop();
			}
			else
			{
				// Compute the currently acceptable price and start a
				// negotiation
				long elapsedTime = currentTime - initTime;
				int acceptablePrice = (int) Math.round(1.0 * maxPrice * (1.0 * elapsedTime / deltaT));
				myAgent.addBehaviour(new RoomNegotiator(room, acceptablePrice, this));
				myGui.updateCurrentMaxPrice(acceptablePrice);
			}
		}
	}

	public ACLMessage cfp = new ACLMessage(ACLMessage.CFP); // variable needed

	// to the
	// ContractNetInitiator
	// constructor

	// TODO: Anpassen an Rooms
	/**
	 * Section 5.4.2 of the book, page 104 Inner class BookNegotiator. This is
	 * the behaviour reimplemented by using the ContractNetInitiator
	 **/
	public class RoomNegotiator extends ContractNetInitiator
	{
		private int maxPrice;
		private PurchaseManager purchaseManager;
		private Room room;

		public RoomNegotiator(Room room, int p, PurchaseManager m)
		{
			super(RoomRentAgent.this, cfp);

			this.room = room;
			maxPrice = p;
			purchaseManager = m;
			Sell sellAction = new Sell();
			sellAction.setItem(room);
			Action act = new Action(RoomRentAgent.this.getAID(), sellAction);
			try
			{
				cfp.setLanguage(codec.getName());
				cfp.setOntology(ontology.getName());
				RoomRentAgent.this.getContentManager().fillContent(cfp, act);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		protected Vector prepareCfps(ACLMessage cfp)
		{
			cfp.clearAllReceiver();
			for (int i = 0; i < sellerAgents.size(); ++i)
			{
				cfp.addReceiver((AID) sellerAgents.get(i));
			}
			Vector v = new Vector();
			v.add(cfp);
			if (sellerAgents.size() > 0)
				myGui.notifyUser("Sent Call for Proposal to " + sellerAgents.size() + " sellers.");
			return v;
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
						int price = ((Costs) cel.get(1)).getPrice();
						myGui.notifyUser("Received Proposal at " + price + " when maximum acceptable price was "
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
					accept.setContent(room.toString());
					myGui.notifyUser(acceptedProposal ? "sent Accept Proposal" : "sent Reject Proposal");
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
			myGui.notifyUser(room + " successfully purchased. Price =" + price);
			purchaseManager.stop();
		}

	} // End of inner class BookNegotiator
}
