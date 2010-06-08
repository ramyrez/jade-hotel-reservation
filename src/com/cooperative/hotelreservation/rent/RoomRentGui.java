/**
 * definition of the RoomRentGui interface
 **/

package com.cooperative.hotelreservation.rent;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import com.cooperative.hotelreservation.ontology.Room;

public class RoomRentGui extends JFrame
{

	private static final long serialVersionUID = -1268662637017707167L;

	public static void main(String[] args)
	{
		new RoomRentGui().setVisible(true);
	}

	private RoomRentAgent roomRentAgent;

	public RoomRentGui()
	{
		initComponents();

		deadlineTextField.setToolTipText("example: 23:59 01.09.2010");
		long deadline = new Date().getTime() + 2 * 60 * 1000;
		deadlineTextField.setValue(new Date(deadline));

		lookForOfferButton.setText("Look for offers");
		lookForOfferButton.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				// get all room properties
				int numOfBeds = (Integer) nrOfBedsSpinner.getValue();
				boolean hasShower = hasShowerCheckBox.isSelected();
				double maxPrice = (Double) maximumPriceSpinner.getValue();
				Date deadline = (Date) deadlineTextField.getValue();

				Room room = new Room();
				room.setBedCount(numOfBeds);
				room.setHasShower(hasShower);

				roomRentAgent.purchase(room, (int) maxPrice, deadline);
			}
		});

		setTitle("Search for rooms");
		setSize(350, 320);
	}

	private void initComponents()
	{
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Open Source Project license - unknown
		JPanel roomPropertiesPanel = new JPanel();
		JLabel nrOfBedsLabel = new JLabel();
		nrOfBedsSpinner = new JSpinner();
		JLabel hasShowerLabel = new JLabel();
		hasShowerCheckBox = new JCheckBox();
		JLabel maximumPriceLabel = new JLabel();
		JPanel pricePanel = new JPanel();
		maximumPriceSpinner = new JSpinner();
		JLabel currentPriceLabel = new JLabel();
		currentPriceTextField = new JTextField();
		JLabel deadlineLabel = new JLabel();
		deadlineTextField = new JFormattedTextField(new SimpleDateFormat("HH:mm dd.MM.yyyy"));
		lookForOfferButton = new JButton();
		JSeparator separator = new JSeparator();
		JScrollPane logScrollPane = new JScrollPane();
		logTextArea = new JTextArea();

		// ======== this ========
		setName("this");
		Container contentPane = getContentPane();
		contentPane.setLayout(new GridBagLayout());
		((GridBagLayout) contentPane.getLayout()).columnWidths = new int[] { 0, 0 };
		((GridBagLayout) contentPane.getLayout()).rowHeights = new int[] { 0, 0, 0, 0 };
		((GridBagLayout) contentPane.getLayout()).columnWeights = new double[] { 1.0, 1.0E-4 };
		((GridBagLayout) contentPane.getLayout()).rowWeights = new double[] { 0.0, 0.0, 1.0, 1.0E-4 };

		// ======== roomPropertiesPanel ========
		{
			roomPropertiesPanel.setName("roomPropertiesPanel");
			roomPropertiesPanel.setLayout(new GridBagLayout());
			((GridBagLayout) roomPropertiesPanel.getLayout()).columnWidths = new int[] { 0, 0, 0 };
			((GridBagLayout) roomPropertiesPanel.getLayout()).rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
			((GridBagLayout) roomPropertiesPanel.getLayout()).columnWeights = new double[] { 0.0, 1.0, 1.0E-4 };
			((GridBagLayout) roomPropertiesPanel.getLayout()).rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
					1.0E-4 };

			// ---- nrOfBedsLabel ----
			nrOfBedsLabel.setText("# of beds:");
			nrOfBedsLabel.setName("nrOfBedsLabel");
			roomPropertiesPanel.add(nrOfBedsLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(0, 0, 5, 5), 0, 0));

			// ---- nrOfBedsSpinner ----
			nrOfBedsSpinner.setModel(new SpinnerNumberModel(1, 1, 10, 1));
			nrOfBedsSpinner.setName("nrOfBedsSpinner");
			roomPropertiesPanel.add(nrOfBedsSpinner, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 5, 0), 0, 0));

			// ---- hasShowerLabel ----
			hasShowerLabel.setText("Has shower?");
			hasShowerLabel.setName("hasShowerLabel");
			roomPropertiesPanel.add(hasShowerLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(0, 0, 5, 5), 0, 0));

			// ---- hasShowerCheckBox ----
			hasShowerCheckBox.setName("hasShowerCheckBox");
			roomPropertiesPanel.add(hasShowerCheckBox, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
					GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(0, 0, 5, 0), 0, 0));

			// ---- maximumPriceLabel ----
			maximumPriceLabel.setText("Maximum price:");
			maximumPriceLabel.setName("maximumPriceLabel");
			roomPropertiesPanel.add(maximumPriceLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
					GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(0, 0, 5, 5), 0, 0));

			// ======== pricePanel ========
			{
				pricePanel.setName("pricePanel");
				pricePanel.setLayout(new GridBagLayout());
				((GridBagLayout) pricePanel.getLayout()).columnWidths = new int[] { 0, 0, 65, 60, 0 };
				((GridBagLayout) pricePanel.getLayout()).rowHeights = new int[] { 0, 0 };
				((GridBagLayout) pricePanel.getLayout()).columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0, 1.0E-4 };
				((GridBagLayout) pricePanel.getLayout()).rowWeights = new double[] { 0.0, 1.0E-4 };

				// ---- maximumPriceSpinner ----
				maximumPriceSpinner.setModel(new SpinnerNumberModel(0.0, 0.0, 1000.0, 0.1));
				maximumPriceSpinner.setName("maximumPriceSpinner");
				pricePanel.add(maximumPriceSpinner, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 5), 0, 0));

				// ---- currentPriceLabel ----
				currentPriceLabel.setText("Current price:");
				currentPriceLabel.setName("currentPriceLabel");
				pricePanel.add(currentPriceLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 5), 0, 0));

				// ---- currentPriceTextField ----
				currentPriceTextField.setEditable(false);
				currentPriceTextField.setName("currentPriceTextField");
				pricePanel.add(currentPriceTextField, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
			}
			roomPropertiesPanel.add(pricePanel, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
					GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

			// ---- deadlineLabel ----
			deadlineLabel.setText("Deadline:");
			deadlineLabel.setName("deadlineLabel");
			roomPropertiesPanel.add(deadlineLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
					GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(0, 0, 5, 5), 0, 0));

			// ---- deadlineTextField ----
			deadlineTextField.setName("deadlineTextField");
			roomPropertiesPanel.add(deadlineTextField, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

			// ---- lookForOfferButton ----
			lookForOfferButton.setText("look for offers");
			lookForOfferButton.setName("lookForOfferButton");
			roomPropertiesPanel.add(lookForOfferButton, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
					GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
		}
		contentPane.add(roomPropertiesPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		// ---- separator ----
		separator.setName("separator");
		contentPane.add(separator, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 5, 0), 0, 0));

		// ======== logScrollPane ========
		{
			logScrollPane.setName("logScrollPane");

			// ---- logTextArea ----
			logTextArea.setName("logTextArea");
			logScrollPane.setViewportView(logTextArea);
		}
		contentPane.add(logScrollPane, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
		pack();
		setLocationRelativeTo(getOwner());
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	// Generated using JFormDesigner Open Source Project license - unknown
	private JSpinner nrOfBedsSpinner;
	private JCheckBox hasShowerCheckBox;
	private JSpinner maximumPriceSpinner;
	private JTextField currentPriceTextField;
	private JFormattedTextField deadlineTextField;
	private JButton lookForOfferButton;
	private JTextArea logTextArea;

	// JFormDesigner - End of variables declaration //GEN-END:variables

	public void setAgent(RoomRentAgent roomRentAgent)
	{
		this.roomRentAgent = roomRentAgent;
	}

	public void addLogMsg(String message)
	{
		int length = logTextArea.getText().length();
		if (length != 0)
			logTextArea.append("\n");
		logTextArea.append(message);

		// scroll to last position
		logTextArea.setCaretPosition(logTextArea.getText().length() - 1);
	}

	public void updateCurrentMaxPrice(int acceptablePrice)
	{
		currentPriceTextField.setText(Integer.toString(acceptablePrice));
	}
}
