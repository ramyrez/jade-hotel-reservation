package com.cooperative.hotelreservation.main;

import com.cooperative.hotelreservation.rent.RoomRentAgent;
import com.cooperative.hotelreservation.rent.RoomRentGuiImpl;
import com.cooperative.hotelreservation.seller.BookSellerAgent;
import com.cooperative.hotelreservation.seller.BookSellerGuiImpl;

public class GuiTest {

	public static void main(String[] args) {
		BookSellerGuiImpl bsgi = new BookSellerGuiImpl();
//		BookSellerAgent bsa = new BookSellerAgent();
//		bsgi.setAgent(bsa);
		bsgi.setVisible(true);
		
		RoomRentGuiImpl roomRent = new RoomRentGuiImpl();
		roomRent.show();
	}

}
