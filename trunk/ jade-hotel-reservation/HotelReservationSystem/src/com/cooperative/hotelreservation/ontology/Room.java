package com.cooperative.hotelreservation.ontology;

import java.io.Serializable;

/**
 * Representation of a room with all it's descriptive properties like number of
 * beds and has shower.
 */
public class Room implements Serializable
{

	private static final long serialVersionUID = -8647556717248630790L;

	private int bedCount;
	private boolean hasShower;

	public Room()
	{
		super();
		bedCount = 2;
		hasShower = true;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof Room))
			return false;

		Room that = (Room) obj;
		if (this.bedCount != that.bedCount)
			return false;
		if (this.hasShower != that.hasShower)
			return false;

		return true;
	}

	public int getBedCount()
	{
		return bedCount;
	}

	public boolean getHasShower()
	{
		return hasShower;
	}

	@Override
	public int hashCode()
	{
		int result = 42;
		int prime = 37;
		result = (result * prime) + (hasShower ? 0 : 1);
		result = (result * prime) + bedCount;
		return result;
	}

	public void setBedCount(int bedCount)
	{
		this.bedCount = bedCount;
	}

	public void setHasShower(boolean hasShower)
	{
		this.hasShower = hasShower;
	}

	@Override
	public String toString()
	{
		StringBuilder str = new StringBuilder();
		str.append("[bedCount=").append(bedCount);
		str.append(",hasShower=").append(hasShower).append("]");
		return str.toString();
	}

}
