package com.cooperative.hotelreservation.rent;

import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
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
		// Update the list of seller agents
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(RoomSellerAgent.TYPE);
		template.addServices(sd);
		try
		{
			DFAgentDescription[] result = DFService.search(myAgent, template);
			List<AID> sellerAgents = new ArrayList<AID>(result.length);
			for (DFAgentDescription agentDescription : result)
			{
				sellerAgents.add(agentDescription.getName());
			}
			roomRentAgent.setAvailableSellerAgents(sellerAgents);
		} catch (FIPAException fe)
		{
			fe.printStackTrace();
		}
	}

}
