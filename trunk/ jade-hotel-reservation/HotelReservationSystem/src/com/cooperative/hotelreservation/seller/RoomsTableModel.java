package com.cooperative.hotelreservation.seller;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.cooperative.hotelreservation.ontology.Room;

public class RoomsTableModel extends AbstractTableModel
{

	private static final long serialVersionUID = 5983739885948030414L;

	private final String[] columns = new String[] { "# of beds in the room", "has shower?", "minimum price",
			"start price", "ready for sale", "current price" };

	private final List<Boolean> readyForSaleList;
	private final List<Room> rooms;
	private final List<Double> minimumPrices;
	private final List<Double> startPrices;
	private final List<Double> currentPrices;

	private final RoomSellerAgent roomSellerAgent;

	public RoomsTableModel(RoomSellerAgent roomSellerAgent)
	{
		super();

		this.roomSellerAgent = roomSellerAgent;

		rooms = new LinkedList<Room>();
		minimumPrices = new LinkedList<Double>();
		startPrices = new LinkedList<Double>();
		currentPrices = new LinkedList<Double>();
		readyForSaleList = new LinkedList<Boolean>();
	}

	public void addNewRoom()
	{
		rooms.add(new Room());
		minimumPrices.add(new Double(0.0D));
		startPrices.add(new Double(0.0D));
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
			case 3:
				return Double.class;
			case 4:
				return Boolean.class;
			case 5:
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
				return minimumPrices.get(rowIndex);
			case 3:
				return startPrices.get(rowIndex);
			case 4:
				return readyForSaleList.get(rowIndex);
			case 5:
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

		if (columnIndex == 5)
			return false;

		return true;
	}

	public void removeRow(int row)
	{
		if (row < 0)
			return;

		Room room = rooms.remove(row);
		startPrices.remove(row);
		minimumPrices.remove(row);
		readyForSaleList.remove(row);
		currentPrices.remove(row);
		fireTableRowsDeleted(row, row);

		// inform agent for deleted room
		roomSellerAgent.removeRoomSellerPriceManager(room);
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
			case 3:
				Double doubleVal = (Double) aValue;
				if (columnIndex == 2)
					minimumPrices.set(rowIndex, doubleVal);
				else
					startPrices.set(rowIndex, doubleVal);
				break;
			case 4:
				readyForSaleList.set(rowIndex, Boolean.TRUE);

				Room room = rooms.get(rowIndex);
				Double minPrice = minimumPrices.get(rowIndex);
				Double startPrice = startPrices.get(rowIndex);
				long now = new Date().getTime() + (4 * 60 * 1000);
				roomSellerAgent.addNewRoomForRent(room, startPrice.intValue(), minPrice.intValue(), new Date(now));
				break;
		}
	}

	public void updatePriceForRoom(Room room, int currentPrice)
	{
		int index = rooms.indexOf(room);
		if (index >= 0)
		{
			currentPrices.set(index, Double.valueOf(currentPrice));
			fireTableCellUpdated(index, 5);
		}
	}

	public void removeRoom(Room room)
	{
		if (room == null)
			return;

		int index = rooms.indexOf(room);
		if (index >= 0)
			removeRow(index);
	}

}
