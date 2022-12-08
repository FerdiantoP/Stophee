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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.Flow;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;

import model.DetailTransaction;
import model.HeaderTransaction;
import model.Product;
import model.ProductType;
import util.MySQLConnect;

public class BuyForm extends JInternalFrame implements ActionListener {
	
	JLabel lblTitle, lblQty, lblPaymentType, lblProduct, lblCart;
	JSpinner spinnerQty;
	SpinnerModel value;
	JRadioButton radioCash, radioDebitCredit;
	ButtonGroup groupType;
	JButton btnAdd, btnCheckOut;
	JPanel pnlTitle, pnlType, pnlFill, pnlNorth, pnlProductTbl, pnlSouth, pnlCartTbl, pnlWest, pnlEast, pnlLeft, 
	pnlRight, pnlProductTitle, pnlCartTitle, pnlBtnAdd, pnlBtnCo;
	
	Vector<Object> tHeader, productContent, cartContent;
	Vector<Integer> vProductID = new Vector<>();
	ArrayList<Product> listProduct;
	ArrayList<Product>listSelectedProducts;
	ArrayList<Product> listCart = new ArrayList<>();
	
	DefaultTableModel dtmProduct, dtmCart;
	JTable productTable, cartTable;
	JScrollPane scrollProduct, scrollCart;
	String transactionDate;
	int userId, qty;
	int transactionId;
	MainForm mainForm;
		
	public BuyForm(MainForm mainForm, int userId) {
		this.mainForm = mainForm;	
		this.userId = userId;
		
		initComponent();	
		
		this.add(pnlNorth, BorderLayout.NORTH);
		this.add(pnlSouth, BorderLayout.SOUTH);
		
		initFrame();
	}

	private void initFrame() {
		this.setSize(mainForm.getContentPane().size());
		this.setLocation(0,0);
		this.setClosable(true);
		this.setResizable(false);
		this.setVisible(true);
		this.setMaximizable(true);
	}

	private void initComponent() {
		lblTitle = new JLabel("BUY PRODUCT", JLabel.CENTER);
		lblTitle.setFont(new Font("Sans Serif",1,24));
		
		lblQty = new JLabel("Quantity :");
		lblPaymentType = new JLabel("Payment Type :");
		lblProduct = new JLabel("Product");
		lblCart = new JLabel("Cart");
		
		value = new SpinnerNumberModel(1, 1, null, 1);
		
		spinnerQty = new JSpinner(value);
		
		radioCash = new JRadioButton("Cash");
		radioDebitCredit = new JRadioButton("Debit / Credit");
		
		radioCash.setActionCommand("Cash");
		radioDebitCredit.setActionCommand("Debit / Credit");
		
		groupType = new ButtonGroup();
		groupType.add(radioCash);
		groupType.add(radioDebitCredit);
		
		btnAdd = new JButton("Add to cart");
		btnCheckOut = new JButton("Check out");
		
		pnlTitle = new JPanel(new GridLayout(1,1));
		pnlTitle.add(lblTitle);
		pnlTitle.setPreferredSize(new Dimension(100, 100));
		
		pnlType = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pnlType.add(radioCash);
		pnlType.add(radioDebitCredit);
		
		pnlFill = new JPanel(new GridLayout(2,2));
		pnlFill.add(changeToJPanel(lblQty, 550, 30));
		pnlFill.add(changeToJPanel(spinnerQty, 550, 40));
		pnlFill.add(changeToJPanel(lblPaymentType, 550, 30));
		pnlFill.add(changeToJPanel(pnlType, 200, 30));
		
		pnlNorth = new JPanel(new BorderLayout());
		pnlNorth.add(pnlTitle, BorderLayout.NORTH);
		pnlNorth.add(pnlFill, BorderLayout.SOUTH);
		
		tHeader = new Vector<>();
		tHeader.add("ProductID");
		tHeader.add("ProductName");
		tHeader.add("ProductType");
		tHeader.add("ProductPrice");
		tHeader.add("ProductQty");
	
		productTable = new JTable() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		productTable.getTableHeader().setResizingAllowed(false);
		productTable.getTableHeader().setReorderingAllowed(false);
		scrollProduct = new JScrollPane(productTable);
		scrollProduct.setPreferredSize(new Dimension(620, 120));
		
		setTableData();
		
		dtmCart = new DefaultTableModel(tHeader,0);
		cartTable = new JTable() {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		cartTable.setModel(dtmCart);
		
		cartTable.getTableHeader().setResizingAllowed(false);
		cartTable.getTableHeader().setReorderingAllowed(false);
		scrollCart = new JScrollPane(cartTable);
		scrollCart.setPreferredSize(new Dimension(620, 120));
	
		pnlProductTbl = new JPanel(new BorderLayout());
		pnlProductTbl.add(scrollProduct, BorderLayout.EAST);
		
		pnlBtnAdd = new JPanel(new BorderLayout());
		pnlBtnAdd.add(changeToJPanel(btnAdd, 400, 30), BorderLayout.CENTER);
		pnlBtnAdd.setPreferredSize(new Dimension(400, 70));
		
		pnlCartTbl = new JPanel(new BorderLayout());
		pnlCartTbl.add(scrollCart, BorderLayout.WEST);
		
		pnlBtnCo = new JPanel(new BorderLayout());
		pnlBtnCo.add(changeToJPanel(btnCheckOut, 400, 30), BorderLayout.CENTER);
		pnlBtnCo.setPreferredSize(new Dimension(400, 70));
		
		pnlLeft = new JPanel(new BorderLayout());
		pnlLeft.add(pnlProductTbl, BorderLayout.NORTH);
		pnlLeft.add(pnlBtnAdd, BorderLayout.SOUTH);
		
		pnlRight = new JPanel(new BorderLayout());
		pnlRight.add(pnlCartTbl, BorderLayout.NORTH);
		pnlRight.add(pnlBtnCo, BorderLayout.SOUTH);
		
		pnlWest = new JPanel(new BorderLayout());
		pnlWest.add(changeToJPanel(lblProduct, 550, 60), BorderLayout.NORTH);
		pnlWest.add(pnlLeft, BorderLayout.CENTER);
		pnlWest.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/2-12, 300));
		
		pnlEast = new JPanel(new BorderLayout());
		pnlEast.add(changeToJPanel(lblCart, 650, 60), BorderLayout.NORTH);
		pnlEast.add(pnlRight, BorderLayout.CENTER);
		pnlEast.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/2-12, 300));
		
		pnlSouth = new JPanel(new BorderLayout());
		pnlSouth.add(pnlWest, BorderLayout.WEST);
		pnlSouth.add(pnlEast, BorderLayout.EAST);
		
		btnAdd.addActionListener(this);
		btnCheckOut.addActionListener(this);
	}
	
	void setTableData () {
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for (Product product : listProduct) {
			ProductType	productType = new MySQLConnect().selectProductTypeByid(product.getProductTypeId());
			productContent = new Vector<>();
			productContent.add(product.getId());
			productContent.add(product.getProductName());
			productContent.add(productType.getProductTypeName());
			productContent.add(product.getProductPrice());
			productContent.add(product.getProductQuantity());
			dtmProduct.addRow(productContent);
		}
		productTable.setModel(dtmProduct);
	}
	
	
	public JPanel changeToJPanel(Component component, int width, int height) {
		JPanel panel = new JPanel();
		
		panel.add(component);
		
		component.setPreferredSize(new Dimension(width, height));
		
		return panel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int selectedRow = productTable.getSelectedRow();
		int spinnerValue = (int) spinnerQty.getValue();
		
		if (e.getSource() == btnAdd) {
			listSelectedProducts = new ArrayList<Product>();
			if (productTable.getSelectedRow() == -1) {
				JOptionPane.showMessageDialog(this, "You must choose a row first");
			}else {
				int quantity = listProduct.get(productTable.getSelectedRow()).getProductQuantity();
				if (spinnerValue > quantity) {
					JOptionPane.showMessageDialog(this, "Not enought item to be bought");
				}else {
					int id = listProduct.get(productTable.getSelectedRow()).getId();
					Product selectedProduct = new MySQLConnect().selectProductByid(id);
					
					listSelectedProducts.add(selectedProduct);
					
					if (listSelectedProducts.contains(selectedProduct.getId())) {
					
					}
					
					for (Product pr : listSelectedProducts) {
						ProductType productType = new MySQLConnect().selectProductTypeByid(selectedProduct.getProductTypeId());					
						cartContent = new Vector<>();
						cartContent.add(pr.getId());
						cartContent.add(pr.getProductName());
						cartContent.add(productType.getProductTypeName());
						cartContent.add(pr.getProductPrice());
						cartContent.add(spinnerValue);
						
						Product cart = new Product();
						cart.setId(pr.getId());
						cart.setProductTypeId(pr.getProductTypeId());
						cart.setProductName(pr.getProductName());
						cart.setProductPrice(pr.getProductPrice());
						cart.setProductQuantity(spinnerValue);
						listCart.add(cart);
						
						dtmCart.addRow(cartContent);	
					}
					cartTable.setModel(dtmCart);
				}
			}	
		}else if (e.getSource() == btnCheckOut) {			
			if (cartTable.getRowCount() == 0) {
				JOptionPane.showMessageDialog(this, "You must add a product first");
			}else if (groupType.getSelection() == null) {
				JOptionPane.showMessageDialog(this, "Payment type must be chosen");
			}else {				
					int input = JOptionPane.showConfirmDialog(this, "Are you sure want to check out ?", "Select an Option", JOptionPane.YES_NO_CANCEL_OPTION);
		
					if (input == JOptionPane.YES_OPTION) {	
						DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
						LocalDateTime ldt = LocalDateTime.now();
						
						String transactionDate = dtf.format(ldt);
						String paymentType = groupType.getSelection().getActionCommand();
						
						HeaderTransaction newHeaderTransaction = new HeaderTransaction();
						newHeaderTransaction.setId(transactionId);
						newHeaderTransaction.setUserId(userId);
						newHeaderTransaction.setTransactionDate(transactionDate);
						newHeaderTransaction.setPaymentType(paymentType);
						new MySQLConnect().insertIntoHeaderTransaction(newHeaderTransaction);
						
						for (Product cartProduct : listCart) {
							ResultSet rs = new MySQLConnect().selectNewHeaderTransaction(userId);
							HeaderTransaction getID = new HeaderTransaction();
							try {
								rs.next();
								getID.setId(rs.getInt("ID"));
								DetailTransaction newDt = new DetailTransaction();
								newDt.setTransactionId(getID.getId());
								newDt.setProductId(cartProduct.getId());
								newDt.setQuantity(cartProduct.getProductQuantity());
								new MySQLConnect().insertIntoDetailTransaction(newDt);
								
								Product coProduct = new MySQLConnect().selectProductByid(newDt.getProductId());
								int afterCoQty = coProduct.getProductQuantity() - newDt.getQuantity();
								coProduct.setId(coProduct.getId());
								coProduct.setProductQuantity(afterCoQty);
								new MySQLConnect().updateProductQty(coProduct);
							} catch (SQLException e1) {
								e1.printStackTrace();
							}
						}
						setTableData();
						dtmCart = new DefaultTableModel(tHeader, 0);
						cartTable.setModel(dtmCart);
					}
				}
			}
		}
	}
