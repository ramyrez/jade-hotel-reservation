package com.cooperative.hotelreservation.rent;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.ArrayList;
import java.util.List;

import com.cooperative.hotelreservation.seller.RoomSellerAgent;

public class UpdateSellerAgentBehavior extends TickerBehaviour
{

	private static final long serialVersionUID = 5697513870403308961L;
	private final RoomRentAgent roomRentAgent;

	public UpdateSellerAgentBehavior(RoomRentAgent roomRentAgent, long period)
	{
		super(roomRentAgent, period);
		this.roomRentAgent = roomRentAgent;
	}

	protected void onTick()
	{
		// get all available seller agents
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(RoomSellerAgent.TYPE);
		template.addServices(sd);

		// create search constraint so we get all and not just one agent
		SearchConstraints sc = new SearchConstraints();
		sc.setMaxResults(Long.valueOf(-1));
		try
		{
			DFAgentDescription[] result = DFService.search(myAgent, template);
			List<AID> sellerAgents = new ArrayList<AID>(result.length);
			for (DFAgentDescription agentDescription : result)
			{
				sellerAgents.add(agentDescription.getName());
			}

			// update the list of available seller agents in the roomRentAgent
			roomRentAgent.setAvailableSellerAgents(sellerAgents);
		} catch (FIPAException fe)
		{
			fe.printStackTrace();
		}
	}
}
