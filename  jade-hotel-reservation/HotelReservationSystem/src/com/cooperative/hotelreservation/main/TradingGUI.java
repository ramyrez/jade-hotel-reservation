package com.cooperative.hotelreservation.main;

import com.cooperative.hotelreservation.buyer.BookBuyerAgent;
import com.cooperative.hotelreservation.buyer.BookBuyerGuiImpl;
import com.cooperative.hotelreservation.seller.BookSellerAgent;
import com.cooperative.hotelreservation.seller.BookSellerGuiImpl;

public class TradingGUI {

	public static void main(String[] args) {
		BookSellerGuiImpl bsgi = new BookSellerGuiImpl();
//		BookSellerAgent bsa = new BookSellerAgent();
//		bsgi.setAgent(bsa);
		bsgi.setVisible(true);
		
		BookBuyerGuiImpl bbgi = new BookBuyerGuiImpl();
//		BookBuyerAgent bba = new BookBuyerAgent();
//		bbgi.setAgent(bba);
		bbgi.setVisible(true);
	}

}
