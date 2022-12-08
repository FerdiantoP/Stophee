package Form;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import model.ProductType;
import util.MySQLConnect;

public class ManageProductTypeForm extends JInternalFrame implements ActionListener, MouseListener{
	
	JLabel lblTitle, lblProductType;
	JTable productTypeTbl;
	JScrollPane scrollProductType;
	DefaultTableModel dtmProductType;
	Vector<Object> tHeader, tContent;
	ArrayList<ProductType> listProductType;
	JTextField txtProductType;
	JButton btnAdd, btnUpdate, btnDelete;
	JPanel pnlTable, pnlNorth, pnlFill, pnlButton, pnlCenter;
	MainForm mainForm;
	
	public ManageProductTypeForm(MainForm mainForm) {
		this.mainForm = mainForm;
		initComponent();
		
		this.add(pnlNorth, BorderLayout.NORTH);
		this.add(pnlCenter, BorderLayout.CENTER);
		
		initFrame();
	}
	
	void initFrame () {
		this.setSize(mainForm.getContentPane().size());
		this.setLocation(0,0);
		this.setClosable(true);
		this.setMaximizable(true);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	void initComponent () {
		lblTitle = new JLabel("MANAGE PRODUCT TYPE");
		lblTitle.setFont(new Font("SansSerif",1,24));
		lblTitle.setHorizontalAlignment((int)CENTER_ALIGNMENT);
		
		lblProductType = new JLabel("Type :");
		
		txtProductType = new JTextField();
		
		productTypeTbl = new JTable() {
			@Override
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		
		productTypeTbl.getTableHeader().setResizingAllowed(false);
		productTypeTbl.getTableHeader().setReorderingAllowed(false);
		
		scrollProductType = new JScrollPane(productTypeTbl);
		
		btnAdd = new JButton("Add");
		btnUpdate = new JButton("Update");
		btnDelete = new JButton("Delete");
		
		pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
		pnlButton.add(btnAdd);
		pnlButton.add(btnUpdate);
		pnlButton.add(btnDelete);
		pnlButton.setPreferredSize(new Dimension(0, 150));
		
		pnlTable = new JPanel(new GridLayout(1,1));
		pnlTable.add(changeToJPanel(scrollProductType, Toolkit.getDefaultToolkit().getScreenSize().width-100, 250));
		pnlTable.setPreferredSize(new Dimension(0, 300));
		
		pnlFill = new JPanel(new FlowLayout(FlowLayout.CENTER));
		pnlFill.add(changeToJPanel(lblProductType, 150, 40));
		pnlFill.add(changeToJPanel(txtProductType, 300, 40));
	
		pnlNorth = new JPanel(new BorderLayout());
		pnlNorth.add(lblTitle, BorderLayout.NORTH);
		pnlNorth.add(pnlTable, BorderLayout.SOUTH);
		
		pnlCenter = new JPanel(new BorderLayout());
		pnlCenter.add(pnlFill, BorderLayout.NORTH);
		pnlCenter.add(pnlButton, BorderLayout.SOUTH);
		
		btnAdd.addActionListener(this);
		btnUpdate.addActionListener(this);
		btnDelete.addActionListener(this);
		productTypeTbl.addMouseListener(this);
		
		setTableData();
	}
	
	void setTableData () {
		tHeader = new Vector<>();
		tHeader.add("ProductTypeID");
		tHeader.add("ProductTypeName");
		dtmProductType = new DefaultTableModel(tHeader,0);
		ResultSet rs = new MySQLConnect().selectAllProductType();
		
		try {
			listProductType = new ArrayList<>();
			while (rs.next()) {
				ProductType pt = new ProductType();
				pt.setId(rs.getInt("ID"));
				pt.setProductTypeName(rs.getString("ProductTypeName"));
				listProductType.add(pt);
			}
			
			for (ProductType allProductType : listProductType) {
				tContent = new Vector<>();
				tContent.add(allProductType.getId());
				tContent.add(allProductType.getProductTypeName());
				dtmProductType.addRow(tContent);
			}
			productTypeTbl.setModel(dtmProductType);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void setFieldToDefault () {
		txtProductType.setText("");
	}
	
	public JPanel changeToJPanel(Component component, int width, int height) {
		JPanel panel = new JPanel();
		
		panel.add(component);
		
		component.setPreferredSize(new Dimension(width, height));
		
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnAdd) {
			if (txtProductType.getText().length() <= 3) {
				JOptionPane.showMessageDialog(this, "Product Type length must be more than 3");
			}else {
				ProductType newProductType = new ProductType();
				newProductType.setProductTypeName(txtProductType.getText());
				new MySQLConnect().insertProductTypeData(newProductType);
				
				JOptionPane.showMessageDialog(this, "Product type added");
				setFieldToDefault();
				setTableData();
			}
		}else if (e.getSource() == btnUpdate) {
			if (productTypeTbl.getSelectedRow() == -1) {
				JOptionPane.showMessageDialog(this, "You must choose a type to update first");
			}else {
				if (txtProductType.getText().length() <= 3) {
					JOptionPane.showMessageDialog(this, "Product Type length must be more than 3");
				}else {
					int selectedTypeId = listProductType.get(productTypeTbl.getSelectedRow()).getId();
					ProductType updateProductType = new ProductType();
					updateProductType.setId(selectedTypeId);
					updateProductType.setProductTypeName(txtProductType.getText());
					new MySQLConnect().updateProductTypeData(updateProductType);
					
					JOptionPane.showMessageDialog(this, "Product type updated");
					setFieldToDefault();
					setTableData();
				}
			}
		}else if (e.getSource() == btnDelete) {
			if (productTypeTbl.getSelectedRow() == -1) {
				JOptionPane.showMessageDialog(this, "Please choose a product to delete first");
			}else {
				int selectedTypeId = listProductType.get(productTypeTbl.getSelectedRow()).getId();
				ProductType selectedProductType = new ProductType();
				selectedProductType.setId(selectedTypeId);
				new MySQLConnect().DeleteProductTypeData(selectedProductType);
				
				JOptionPane.showMessageDialog(this, "Product type deleted");
				setFieldToDefault();
				setTableData();
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.getSource() == productTypeTbl) {
			String productType = listProductType.get(productTypeTbl.getSelectedRow()).getProductTypeName();
			txtProductType.setText(productType);
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
