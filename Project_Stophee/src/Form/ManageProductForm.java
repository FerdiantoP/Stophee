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

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import model.Product;
import model.ProductType;
import util.MySQLConnect;

public class ManageProductForm extends JInternalFrame implements ActionListener, MouseListener {
	
	JLabel lblTitle, lblProductName, lblProductType, lblProductPrice, lblProductQty;
	JTable productTable;
	JScrollPane scrollProduct;
	DefaultTableModel dtmProduct;
	JTextField txtProductName;
	JSpinner spinnerPrice, spinnerQty;
	JButton btnAdd, btnUpdate, btnDelete;
	Vector<Object> tHeader, tContent;
	ArrayList<Product> listProduct;
	Vector<String> vType = new Vector<>();
	JComboBox<String> cbProductType = new JComboBox<>(vType);
	SpinnerModel modelPrice, modelQty;
	JPanel pnlTitle, pnlTable,pnlFill,pnlNorth,pnlButton, pnlCenter;
	MainForm mainForm;
	
	public ManageProductForm(MainForm mainForm) {
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
		setProductTypeComboBox();
		
		lblTitle = new JLabel("MANAGE PRODUCT");
		lblTitle.setFont(new Font("SansSerif",1,24));
		lblTitle.setHorizontalAlignment((int)CENTER_ALIGNMENT);
		
		lblProductName = new JLabel("Product Name :");
		lblProductType = new JLabel("Product Type :");
		lblProductPrice = new JLabel("Product Price :");
		lblProductQty = new JLabel("Product Quantity");
		
		txtProductName = new JTextField();
		
		modelPrice = new SpinnerNumberModel(1000,1000,null,1000);
		spinnerPrice = new JSpinner(modelPrice);
		
		modelQty = new SpinnerNumberModel(1,1,null,1);
		spinnerQty = new JSpinner(modelQty);

		btnAdd = new JButton("Add");
		btnUpdate = new JButton("Update");
		btnDelete = new JButton("Delete");
		
		pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
		pnlButton.add(btnAdd);
		pnlButton.add(btnUpdate);
		pnlButton.add(btnDelete);
		pnlButton.setPreferredSize(new Dimension(0, 150));
		
		productTable = new JTable() {
			@Override	
			public boolean isCellEditable(int row, int column) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		
		productTable.getTableHeader().setResizingAllowed(false);
		productTable.getTableHeader().setReorderingAllowed(false);
		scrollProduct = new JScrollPane(productTable);
		
		pnlTitle = new JPanel(new BorderLayout());
		pnlTitle.add(lblTitle, BorderLayout.NORTH);
		pnlTitle.setPreferredSize(new Dimension(100,100));
		
		pnlTable = new JPanel(new GridLayout(1,1));
		pnlTable.add(changeToJPanel(scrollProduct, Toolkit.getDefaultToolkit().getScreenSize().width-200, 190));
		pnlTable.setPreferredSize(new Dimension(0,200));
		
		pnlFill = new JPanel(new GridLayout(4,2));
		pnlFill.add(changeToJPanel(lblProductName, (Toolkit.getDefaultToolkit().getScreenSize().width-370)/2, 30));
		pnlFill.add(changeToJPanel(txtProductName, (Toolkit.getDefaultToolkit().getScreenSize().width-370)/2, 30));
		pnlFill.add(changeToJPanel(lblProductType, (Toolkit.getDefaultToolkit().getScreenSize().width-370)/2, 30));
		pnlFill.add(changeToJPanel(cbProductType, (Toolkit.getDefaultToolkit().getScreenSize().width-370)/2, 30));
		pnlFill.add(changeToJPanel(lblProductPrice, (Toolkit.getDefaultToolkit().getScreenSize().width-370)/2, 30));
		pnlFill.add(changeToJPanel(spinnerPrice, (Toolkit.getDefaultToolkit().getScreenSize().width-370)/2, 30));
		pnlFill.add(changeToJPanel(lblProductQty, (Toolkit.getDefaultToolkit().getScreenSize().width-370)/2, 30));
		pnlFill.add(changeToJPanel(spinnerQty, (Toolkit.getDefaultToolkit().getScreenSize().width-370)/2, 30));
		
		pnlNorth = new JPanel(new BorderLayout());
		pnlNorth.add(pnlTitle, BorderLayout.NORTH);
		pnlNorth.add(pnlTable, BorderLayout.SOUTH);
		
		pnlCenter = new JPanel(new BorderLayout());
		pnlCenter.add(pnlFill, BorderLayout.NORTH);
		pnlCenter.add(pnlButton, BorderLayout.SOUTH);
		
		btnAdd.addActionListener(this);
		btnUpdate.addActionListener(this);
		btnDelete.addActionListener(this);
		productTable.addMouseListener(this);
		
		setTableData();
	}
	
	void setTableData () {
		tHeader = new Vector<>();
		tHeader.add("ProductID");
		tHeader.add("ProductName");
		tHeader.add("ProductTypeName");
		tHeader.add("ProductPrice");
		tHeader.add("ProductQty");
			
		dtmProduct = new DefaultTableModel(tHeader,0);		
		ResultSet rs = new MySQLConnect().selectAllProduct();
		try {
			listProduct = new ArrayList<Product>();
			while (rs.next()) {
				Product allProduct = new Product();
				allProduct.setId(rs.getInt("id"));
				allProduct.setProductTypeId(rs.getInt("ProductTypeID"));
				allProduct.setProductName(rs.getString("ProductName"));
				allProduct.setProductPrice(rs.getInt("ProductPrice"));
				allProduct.setProductQuantity(rs.getInt("ProductQuantity"));
				listProduct.add(allProduct);
			}
				
			for (Product product : listProduct) {
				ProductType	productType = new MySQLConnect().selectProductTypeByid(product.getProductTypeId());
				tContent = new Vector<>();
				tContent.add(product.getId());
				tContent.add(product.getProductName());
				tContent.add(productType.getProductTypeName());
				tContent.add(product.getProductPrice());
				tContent.add(product.getProductQuantity());
				dtmProduct.addRow(tContent);
			}
			productTable.setModel(dtmProduct);
		} catch (SQLException e) {
			e.printStackTrace();
		}			
	}

	void setProductTypeComboBox () {
		ResultSet rs = new MySQLConnect().selectAllProductType();
		try {
			while (rs.next()) {
				ProductType pt = new ProductType();
				pt.setId(rs.getInt("ID"));
				pt.setProductTypeName(rs.getString("ProductTypeName"));
				vType.add(pt.getProductTypeName());
				cbProductType.setActionCommand(String.valueOf(pt.getId()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	void setFieldToDefault () {
		txtProductName.setText("");
		cbProductType.setSelectedItem(null);
		spinnerPrice.setValue(1000);
		spinnerQty.setValue(1);
	}
	
	public JPanel changeToJPanel(Component component, int width, int height) {
		JPanel panel = new JPanel();
		
		panel.add(component);
		
		component.setPreferredSize(new Dimension(width, height));
		
		return panel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int price = (int) spinnerPrice.getValue();
		int quantity = (int) spinnerQty.getValue();
		
		if (e.getSource() == btnAdd) {
			if (txtProductName.getText().length() < 5 || txtProductName.getText().length() > 15) {
				JOptionPane.showMessageDialog(this, "Name Length must be between 5 and 15");
			}else if (cbProductType.getSelectedItem() == null) {
				JOptionPane.showMessageDialog(this, "Product Type must be chosen");
			}else if (price <= 0) {
				JOptionPane.showMessageDialog(this, "Price must be more than 0");
			}else if (quantity <= 0) {
				JOptionPane.showMessageDialog(this, "Quantity must be more than 0");
			}else {
				Product newProduct = new Product();
				ProductType productT = new MySQLConnect().selectProductTypeIDByName(String.valueOf(cbProductType.getSelectedItem()));
				newProduct.setProductTypeId(productT.getId());
				newProduct.setProductName(txtProductName.getText());
				newProduct.setProductPrice(price);
				newProduct.setProductQuantity(quantity);
				new MySQLConnect().insertDataProduct(newProduct);
				
				JOptionPane.showMessageDialog(this, "Product added");
				setFieldToDefault();
				setTableData();
			}
		}else if (e.getSource() == btnUpdate) {
			if (productTable.getSelectedRow() == -1) {
				JOptionPane.showMessageDialog(this, "You must choose a product to update first");
			}else {
				if (txtProductName.getText().length() < 5 || txtProductName.getText().length() > 15) {
					JOptionPane.showMessageDialog(this, "Name length must be between 5 and 15");
				}else {
					int selectedId = listProduct.get(productTable.getSelectedRow()).getId();
					Product updateProduct = new Product();
					ProductType productT = new MySQLConnect().selectProductTypeIDByName(String.valueOf(cbProductType.getSelectedItem()));
					
					updateProduct.setId(selectedId);
					updateProduct.setProductTypeId(productT.getId());
					updateProduct.setProductName(txtProductName.getText());
					updateProduct.setProductPrice(price);
					updateProduct.setProductQuantity(quantity);
					
					new MySQLConnect().updateDataProduct(updateProduct);
					
					JOptionPane.showMessageDialog(this, "Product Updated");	
					setTableData();
					setFieldToDefault();
				}
			}
		}else if (e.getSource() == btnDelete) {
			if (productTable.getSelectedRow() == -1) {
					JOptionPane.showMessageDialog(this, "You must choose a product to delete first");
			}else {
				int selectedId = listProduct.get(productTable.getSelectedRow()).getId();
				Product selectedProduct = new Product();
				selectedProduct.setId(selectedId);
				new MySQLConnect().deleteDataProduct(selectedProduct);
				
				JOptionPane.showMessageDialog(this, "Product deleted");
				setFieldToDefault();
				setTableData();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && e.getSource() == productTable) {
			ProductType selectedProductType = new MySQLConnect().selectProductTypeByid(listProduct.get(productTable.getSelectedRow()).getProductTypeId());
			
			String selectedName = listProduct.get(productTable.getSelectedRow()).getProductName();
			String selectedItem = selectedProductType.getProductTypeName();
			int selectedPrice = listProduct.get(productTable.getSelectedRow()).getProductPrice();
			int selectedQuantity = listProduct.get(productTable.getSelectedRow()).getProductQuantity();
			
			txtProductName.setText(selectedName);
			cbProductType.setSelectedItem(selectedItem);
			spinnerPrice.setValue(selectedPrice);
			spinnerQty.setValue(selectedQuantity);
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
