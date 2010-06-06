/**
 * Section 5.1.3.1 Page 82 Ontology of the Book Trading Example.
 **/
package com.cooperative.hotelreservation.ontology;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ConceptSchema;
import jade.content.schema.PredicateSchema;
import jade.content.schema.PrimitiveSchema;

public class RoomReservationOntology extends Ontology implements RoomReservationVocabulary
{
	// The name identifying this ontology
	public static final String ONTOLOGY_NAME = "room-reservation-ontology";

	// The singleton instance of this ontology
	private static Ontology theInstance = new RoomReservationOntology();

	// Retrieve the singleton Book-trading ontology instance
	public static Ontology getInstance()
	{
		return theInstance;
	}

	// Private constructor
	private RoomReservationOntology()
	{
		// The Book-trading ontology extends the basic ontology
		super(ONTOLOGY_NAME, BasicOntology.getInstance());
		try
		{
			add(new ConceptSchema(ROOM), Room.class);
			add(new PredicateSchema(COSTS), Costs.class);
			add(new AgentActionSchema(SELL), Sell.class);

			// Structure of the schema for the Room concept
			ConceptSchema cs = (ConceptSchema) getSchema(ROOM);
			cs.add(ROOM_BED_COUNT, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
			cs.add(ROOM_HAS_SHOWER, (PrimitiveSchema) getSchema(BasicOntology.BOOLEAN));

			// Structure of the schema for the Costs predicate
			PredicateSchema ps = (PredicateSchema) getSchema(COSTS);
			ps.add(COSTS_ITEM, (ConceptSchema) cs);
			ps.add(COSTS_PRICE, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));

			// Structure of the schema for the Sell agent action
			AgentActionSchema as = (AgentActionSchema) getSchema(SELL);
			as.add(SELL_ITEM, (ConceptSchema) getSchema(ROOM));
		} catch (OntologyException oe)
		{
			oe.printStackTrace();
		}
	}
}