package com.cooperative.hotelreservation.ontology;

public class Room
{

	private int bedCount;
	private boolean hasShower;

	public int getBedCount()
	{
		return bedCount;
	}

	public void setBedCount(int bedCount)
	{
		this.bedCount = bedCount;
	}

	public void setHasShower(boolean hasShower)
	{
		this.hasShower = hasShower;
	}

	public boolean getHasShower()
	{
		return hasShower;
	}

	@Override
	public String toString()
	{
		StringBuilder str = new StringBuilder();
		str.append("[bedCount=").append(bedCount);
		str.append(",hasShower=").append(hasShower).append("]");
		return str.toString();
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

	@Override
	public int hashCode()
	{
		int result = 42;
		int prime = 37;
		result = (result * prime) + (hasShower ? 0 : 1);
		result = (result * prime) + bedCount;
		return result;
	}

}
