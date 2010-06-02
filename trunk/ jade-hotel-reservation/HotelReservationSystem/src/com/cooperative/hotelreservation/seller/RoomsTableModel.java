package com.cooperative.hotelreservation.seller;

import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.cooperative.hotelreservation.rent.RoomInfo;

public class RoomsTableModel extends AbstractTableModel
{

	private final String[] columns = new String[] { "# of beds in the room", "has shower?", "minimum price",
			"ready for sale" };

	private final List<Boolean> readyForSaleList;
	private final List<RoomInfo> roomInfos;
	private final List<Double> prices;

	private final RoomSellerAgent roomSellerAgent;

	public RoomsTableModel(RoomSellerAgent roomSellerAgent)
	{
		super();

		this.roomSellerAgent = roomSellerAgent;

		roomInfos = new LinkedList<RoomInfo>();
		prices = new LinkedList<Double>();
		readyForSaleList = new LinkedList<Boolean>();
	}

	public void addNewRoom()
	{
		roomInfos.add(new RoomInfo());
		prices.add(new Double(0.0D));
		readyForSaleList.add(Boolean.FALSE);
		fireTableRowsInserted(roomInfos.size() - 1, roomInfos.size() - 1);
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
		return roomInfos.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		switch (columnIndex)
		{
			case 0:
				return roomInfos.get(rowIndex).getBedCount();
			case 1:
				return roomInfos.get(rowIndex).getHasShower();
			case 2:
				return prices.get(rowIndex);
			case 3:
				return readyForSaleList.get(rowIndex);
		}
		return null;
	};

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		Boolean readyForSale = readyForSaleList.get(rowIndex);
		if (readyForSale)
			return false;

		return true;
	}

	public void removeRow(int row)
	{
		if (row <= 0)
			return;

		roomInfos.remove(row);
		prices.remove(row);
		readyForSaleList.remove(row);
		fireTableRowsDeleted(row, row);

		// TODO inform agent for deleted room
	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		switch (columnIndex)
		{
			case 0:
				int val = ((Integer) aValue).intValue();
				roomInfos.get(rowIndex).setBedCount(val);
				break;
			case 1:
				Boolean bool = (Boolean) aValue;
				roomInfos.get(rowIndex).setHasShower(bool.booleanValue());
				break;
			case 2:
				Double doubleVal = (Double) aValue;
				prices.set(rowIndex, doubleVal);
				break;
			case 3:
				readyForSaleList.set(rowIndex, Boolean.TRUE);
				// TODO inform agent for a new ready for sale room
				break;
		}
	}

}
