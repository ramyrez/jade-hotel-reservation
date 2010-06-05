package com.cooperative.hotelreservation.seller;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.cooperative.hotelreservation.ontology.Room;

public class RoomsTableModel extends AbstractTableModel
{

	private final String[] columns = new String[] { "# of beds in the room", "has shower?", "minimum price",
			"ready for sale", "current price" };

	private final List<Boolean> readyForSaleList;
	private final List<Room> rooms;
	private final List<Double> prices;
	private final List<Double> currentPrices;

	private final RoomSellerAgent roomSellerAgent;

	public RoomsTableModel(RoomSellerAgent roomSellerAgent)
	{
		super();

		this.roomSellerAgent = roomSellerAgent;

		rooms = new LinkedList<Room>();
		prices = new LinkedList<Double>();
		currentPrices = new LinkedList<Double>();
		readyForSaleList = new LinkedList<Boolean>();
	}

	public void addNewRoom()
	{
		rooms.add(new Room());
		prices.add(new Double(0.0D));
		currentPrices.add(new Double(0.0D));
		readyForSaleList.add(Boolean.FALSE);
		fireTableRowsInserted(rooms.size() - 1, rooms.size() - 1);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		switch (columnIndex)
		{
			case 0:
				return Integer.class;
			case 1:
				return Boolean.class;
			case 2:
				return Double.class;
			case 3:
				return Boolean.class;
			case 4:
				return Double.class;
		}
		return Object.class;
	}

	@Override
	public int getColumnCount()
	{
		return columns.length;
	}

	@Override
	public String getColumnName(int column)
	{
		return columns[column];
	}

	@Override
	public int getRowCount()
	{
		return rooms.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		switch (columnIndex)
		{
			case 0:
				return rooms.get(rowIndex).getBedCount();
			case 1:
				return rooms.get(rowIndex).getHasShower();
			case 2:
				return prices.get(rowIndex);
			case 3:
				return readyForSaleList.get(rowIndex);
			case 4:
				return currentPrices.get(rowIndex);
		}
		return null;
	};

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		Boolean readyForSale = readyForSaleList.get(rowIndex);
		if (readyForSale)
			return false;

		if (columnIndex == 4)
			return false;

		return true;
	}

	public void removeRow(int row)
	{
		if (row < 0)
			return;

		rooms.remove(row);
		prices.remove(row);
		readyForSaleList.remove(row);
		currentPrices.remove(row);
		fireTableRowsDeleted(row, row);

		// TODO inform agent for deleted room
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		switch (columnIndex)
		{
			case 0:
				int val = ((Integer) aValue).intValue();
				rooms.get(rowIndex).setBedCount(val);
				break;
			case 1:
				Boolean bool = (Boolean) aValue;
				rooms.get(rowIndex).setHasShower(bool.booleanValue());
				break;
			case 2:
				Double doubleVal = (Double) aValue;
				prices.set(rowIndex, doubleVal);
				break;
			case 3:
				readyForSaleList.set(rowIndex, Boolean.TRUE);

				Room room = rooms.get(rowIndex);
				Double price = prices.get(rowIndex);
				long now = new Date().getTime() + (5 * 60 * 1000);
				roomSellerAgent.addNewRoomForRent(room, price.intValue() * 2, price.intValue(), new Date(now));
				break;
		}
	}

	public void updatePriceForRoom(Room room, int currentPrice)
	{
		int index = rooms.indexOf(room);
		if (index >= 0)
		{
			currentPrices.set(index, Double.valueOf(currentPrice));
			fireTableCellUpdated(index, 4);
		}
	}

}
