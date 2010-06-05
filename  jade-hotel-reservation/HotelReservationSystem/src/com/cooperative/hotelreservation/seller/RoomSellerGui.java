package com.cooperative.hotelreservation.seller;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

public class RoomSellerGui extends JFrame
{
	private RoomsTableModel roomsTableModel;
	private RoomSellerAgent roomSellerAgent;

	public RoomSellerGui(RoomSellerAgent rsa)
	{
		super();
		roomSellerAgent = rsa;

		initComponents();

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// add window listener so we can inform the agent to shut down
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				roomSellerAgent.doDelete();
			}
		});

		// init the table
		roomsTableModel = new RoomsTableModel(roomSellerAgent);
		roomsTable.setModel(roomsTableModel);
		roomsTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// set action for the add room button
		addRoomButton.setText("+");
		addRoomButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				roomsTableModel.addNewRoom();
			}
		});

		// set action for the remove room button
		removeRoomButton.setText("-");
		removeRoomButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int row = roomsTable.getSelectedRow();
				roomsTableModel.removeRow(row);
			}
		});

		setTitle("Offer rooms to rent");
	}

	private void initComponents()
	{
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Open Source Project license - unknown
		JScrollPane roomsScrollPane = new JScrollPane();
		roomsTable = new JTable();
		JPanel buttonPanel = new JPanel();
		removeRoomButton = new JButton();
		addRoomButton = new JButton();
		JSeparator separator = new JSeparator();
		JScrollPane logScrollPane = new JScrollPane();
		logTextArea = new JTextArea();

		// ======== this ========
		setName("this");
		Container contentPane = getContentPane();
		contentPane.setLayout(new GridBagLayout());
		((GridBagLayout) contentPane.getLayout()).columnWidths = new int[] { 0, 0 };
		((GridBagLayout) contentPane.getLayout()).rowHeights = new int[] { 145, 0, 0, 0, 0 };
		((GridBagLayout) contentPane.getLayout()).columnWeights = new double[] { 1.0, 1.0E-4 };
		((GridBagLayout) contentPane.getLayout()).rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 1.0E-4 };

		// ======== roomsScrollPane ========
		{
			roomsScrollPane.setName("roomsScrollPane");

			// ---- roomsTable ----
			roomsTable.setName("roomsTable");
			roomsScrollPane.setViewportView(roomsTable);
		}
		contentPane.add(roomsScrollPane, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		// ======== buttonPanel ========
		{
			buttonPanel.setName("buttonPanel");
			buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

			// ---- removeRoomButton ----
			removeRoomButton.setText("text");
			removeRoomButton.setName("removeRoomButton");
			buttonPanel.add(removeRoomButton);

			// ---- addRoomButton ----
			addRoomButton.setText("text");
			addRoomButton.setName("addRoomButton");
			buttonPanel.add(addRoomButton);
		}
		contentPane.add(buttonPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		// ---- separator ----
		separator.setName("separator");
		contentPane.add(separator, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		// ======== logScrollPane ========
		{
			logScrollPane.setName("logScrollPane");

			// ---- logTextArea ----
			logTextArea.setName("logTextArea");
			logScrollPane.setViewportView(logTextArea);
		}
		contentPane.add(logScrollPane, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		pack();
		setLocationRelativeTo(getOwner());
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	// Generated using JFormDesigner Open Source Project license - unknown
	private JTable roomsTable;
	private JButton removeRoomButton;
	private JButton addRoomButton;
	private JTextArea logTextArea;

	// JFormDesigner - End of variables declaration //GEN-END:variables

	public void notifyUser(String message)
	{
		int length = logTextArea.getText().length();
		if (length != 0)
			logTextArea.append("\n");
		logTextArea.append(message);

		// scroll to last pos
		logTextArea.setCaretPosition(logTextArea.getText().length() - 1);
	}

}
