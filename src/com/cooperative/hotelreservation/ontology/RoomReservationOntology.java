package com.cooperative.hotelreservation.ontology;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ConceptSchema;
import jade.content.schema.PredicateSchema;
import jade.content.schema.PrimitiveSchema;

public class RoomReservationOntology extends Ontology implements RoomReservationVocabulary
{
	private static final long serialVersionUID = 3448981807861039724L;

	public static final String NAME = "room-reservation-ontology";
	private static final RoomReservationOntology INSTANCE = new RoomReservationOntology();

	/**
	 * Get the singleton instance of this ontology
	 * @return
	 */
	public static RoomReservationOntology getInstance()
	{
		return INSTANCE;
	}

	private RoomReservationOntology()
	{
		super(NAME, BasicOntology.getInstance());
		try
		{
			// add all schemas
			add(new ConceptSchema(ROOM), Room.class);
			add(new PredicateSchema(COSTS), CostsPredicate.class);
			add(new AgentActionSchema(RENT), RentAgentAction.class);

			// define the room schema
			ConceptSchema roomSchema = (ConceptSchema) getSchema(ROOM);
			roomSchema.add(ROOM_BED_COUNT, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
			roomSchema.add(ROOM_HAS_SHOWER, (PrimitiveSchema) getSchema(BasicOntology.BOOLEAN));

			// define the costs schema
			PredicateSchema costsSchema = (PredicateSchema) getSchema(COSTS);
			costsSchema.add(COSTS_ROOM, (ConceptSchema) roomSchema);
			costsSchema.add(COSTS_PRICE, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));

			// Structure of the schema for the RentAgentAction agent action
			AgentActionSchema sellSchema = (AgentActionSchema) getSchema(RENT);
			sellSchema.add(RENT_ROOM, (ConceptSchema) getSchema(ROOM));
		} catch (Exception e)
		{
			throw new RuntimeException("esception while specifying room reservation ontology", e);
		}
	}
}