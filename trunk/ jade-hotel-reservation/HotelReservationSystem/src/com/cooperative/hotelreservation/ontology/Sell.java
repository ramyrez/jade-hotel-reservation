/** Class associated to the SELL schema
 **/
package com.cooperative.hotelreservation.ontology;

import jade.content.AgentAction;
import jade.core.AID;

public class Sell implements AgentAction {
	private Book item;

	public Book getItem() {
		return item;
	}

	public void setItem(Book item) {
		this.item = item;
	}

}