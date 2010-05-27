/**
 *
 * definition of the RoomRentGui interface 
 **/

package com.cooperative.hotelreservation.rent;

public interface RoomRentGui {
	void setAgent(RoomRentAgent a);

	void show();

	void hide();

	void notifyUser(String message);

	void dispose();
}