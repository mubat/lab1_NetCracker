package Sergi.MVC.Viewer;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionListener;

import Sergi.MVC.Controller.ActionListenerTM;
import Sergi.MVC.Controller.Controller;

public class MainFrame extends JFrame  {

	private static final long serialVersionUID = 1L;

	private final String findFieldText = new String("������� �������� ������");

	private JButton jAddButton;
	private JButton jReplaceButton;
	private JButton jRemoveButton;
	private JButton jExitButton;
	private JList jList;// ������ �����
	private JButton jFindButton;
	private JTextField jtfFind;

	public MainFrame(String title) {
		super(title);
		initComponents();
	}

	public void initComponents() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new GridBagLayout());
		
		JPanel mainPanel = new JPanel();
		initMainPanel(mainPanel);

		JPanel buttonPanel = new JPanel();
		initButtonPanel(buttonPanel);
		
		JPanel findPanel = new JPanel();
		initFindPanel(findPanel);
		
		this.add(mainPanel, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,
						5, 0, 10), 0, 0));
		this.add(buttonPanel, new GridBagConstraints(1, 0, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
						5, 0, 5), 0, 0));
		this.add(findPanel, new GridBagConstraints(0, 1, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,
						5, 0, 5), 0, 0));
		addFocusListeners();
	}

	private void addWindowListener(WindowAdapter windowEventHandler) {
	    this.addWindowListener(windowEventHandler);
	           
    }

    private void initMainPanel(JPanel panel) {
		JLabel text = new JLabel("������ �����", JLabel.CENTER);
		Font font = new Font("Gabriola", Font.BOLD, 36);
		text.setFont(font);
		panel.setLayout(new GridBagLayout());
		panel.add(text, new GridBagConstraints(0, 0, 3, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,
						5, 0, 5), 0, 0));
		
		jList = new JList();
		panel.add(new javax.swing.JScrollPane(jList), 
		        new GridBagConstraints(0, 1, 3, 4, 0, 0, GridBagConstraints.CENTER, 
		        GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
	}

	private void initButtonPanel(JPanel panel) {
		jAddButton     = new JButton(ButtonNames.BUTTON_NAME_ADD_DIALOG.getTypeValue());
		jReplaceButton = new JButton(ButtonNames.BUTTON_NAME_REPALCE.getTypeValue());
		jRemoveButton  = new JButton(ButtonNames.BUTTON_NAME_REMOVE.getTypeValue());
		jRemoveButton.setEnabled(false);
		jExitButton    = new JButton(ButtonNames.BUTTON_NAME_EXIT.getTypeValue());
		
		panel.setLayout(new GridBagLayout());
		panel.add(jAddButton, new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5,
						5, 5, 5), 0, 0));
		panel.add(jReplaceButton, new GridBagConstraints(0, 1, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5,
						5, 5, 5), 0, 0));
		panel.add(jRemoveButton, new GridBagConstraints(0, 2, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5,
						5, 5, 5), 0, 0));
		panel.add(jExitButton, new GridBagConstraints(0, 3, 1, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5,
						5, 5, 5), 0, 0));

	}

	private void initFindPanel(JPanel panel) {
		jtfFind = new JTextField(findFieldText);
		jtfFind.setName("TextField");
		jtfFind.setColumns(20);
		jFindButton = new JButton(ButtonNames.BUTTON_NAME_FIND.getTypeValue());
		
		panel.setLayout(new GridBagLayout());
		panel.add(jtfFind, new GridBagConstraints(0, 1, 2, 1, 0, 0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5,
						5, 5, 5), 0, 0));
		panel.add(jFindButton, new GridBagConstraints(2, 1, 1, 1, 0, 0,
				GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5,
						5, 5, 5), 0, 0));
	}

	/**
	 * 
	 * @param listener ActionListener & ListSelectionListener
	 */
	public void addActionListener(ActionListener listener) {
		jAddButton.addActionListener	(new ActionListenerTM(this, 0, listener));
		jReplaceButton.addActionListener(new ActionListenerTM(this, 0, listener));
		jRemoveButton.addActionListener (new ActionListenerTM(this, 0, listener));
		jExitButton.addActionListener	(new ActionListenerTM(this, 0, listener));
//		jtfFind.addActionListener		(new ActionListenerTM(this, 0, listener));
		jFindButton.addActionListener	(new ActionListenerTM(this, 0, listener));
		
		jList.addListSelectionListener((ListSelectionListener) listener);
	}
	
	public String getFindString() {
		return jtfFind.getText();
	}
	
	private void addFocusListeners() {
		jtfFind.addFocusListener(new FocusListener() {

			String enteredData = null;

			@Override
			public void focusLost(FocusEvent arg0) {
				enteredData = jtfFind.getText();
				if (jtfFind.isShowing())
					jtfFind.setText(enteredData.isEmpty() ? findFieldText
							: enteredData);
			}

			@Override
			public void focusGained(FocusEvent arg0) {
				jtfFind.setText(enteredData);
				
			}
		});
	}

	public void enableInList(int index) {
		jList.setSelectedIndex(index);

	}
	
	public void updateList(Object[] listData) {
		jList.clearSelection();
		jList.setListData(listData);
		jRemoveButton.setEnabled(false);
		 
	}
	
	public static void showErrorMessage(Component component,String errorString) {
		JOptionPane.showMessageDialog(component,
				errorString, "������!", JOptionPane.ERROR_MESSAGE);
	}

	public int[] getSelectedIndicies() {
		return jList.getSelectedIndices();
	}

	public int getSelectedIndex() {
		return jList.getSelectedIndex();
	}

	public List<Object> getSelectasValuesList() {
		return Arrays.asList(jList.getSelectedValues());
	}

	public void setButtonEnabled(String buttonName, boolean b) {
		if(jAddButton.getText().equals(buttonName)) {
			jAddButton.setEnabled(b);
		}
		else if(jReplaceButton.getText().equals(buttonName)) {
			jReplaceButton.setEnabled(b);
		}
		else if(jRemoveButton.getText().equals(buttonName)) {
			jRemoveButton.setEnabled(b);
		}
		else if(jExitButton.getText().equals(buttonName)) {
			jExitButton.setEnabled(b);
		}
	}

	public void addListSelections(ListSelectionListener listener) {
		jList.addListSelectionListener(listener);
		
	}

	/** (non-Javadoc)
	 * @see java.awt.Window#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean arg0) {
		pack();
		setLocationRelativeTo(null);
		super.setVisible(arg0);
	}
	
}
