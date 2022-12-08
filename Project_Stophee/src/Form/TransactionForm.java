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
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.table.DefaultTableModel;

import Form.MainForm;
import model.DetailTransaction;
import model.HeaderTransaction;
import model.Product;
import model.ProductType;
import util.MySQLConnect;

public class TransactionForm extends JInternalFrame implements ActionListener {
	
	JLabel lblTitle, lblHeaderTblTitle, lblDetailTblTitle, lblTotal;
	JTable headerTransactionTbl, detailTransactionTbl;
	JScrollPane scrollHeader, scrollDetail;
	JTextField txttotalPrice;
	JButton btnCheck;
	JPanel pnlTitle, pnlCenter, pnlTbl, pnlTotal;
	DefaultTableModel dtmHeader, dtmDetail;
	Vector<Object> tHeader;
	Vector<Object> tDetailHeader;
	Vector <Object> tHeaderContent;
	Vector<Object> tDetailContent;
	ArrayList<HeaderTransaction> listHeaderData;
	ArrayList<DetailTransaction> listDt;
	int userId;
	MainForm mainForm;
	
	public TransactionForm(MainForm mainForm, int userId) {
		this.mainForm = mainForm;
		this.userId = userId;
		
		initComponent();
		setHeaderData();
		
		this.add(pnlTitle, BorderLayout.NORTH);
		this.add(pnlCenter, BorderLayout.CENTER);
		this.add(pnlTotal, BorderLayout.SOUTH);
			
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
		lblTitle = new JLabel("TRANSACTION", JLabel.CENTER);
		lblTitle.setFont(new Font("Sans Serif", 1, 24));
		
		lblHeaderTblTitle = new JLabel("Header Transaction");
		lblDetailTblTitle = new JLabel("Detail Transaction");
		lblTotal = new JLabel("Total");
	
		headerTransactionTbl = new JTable() {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		headerTransactionTbl.getTableHeader().setResizingAllowed(false);
		headerTransactionTbl.getTableHeader().setReorderingAllowed(false);
		
		scrollHeader = new JScrollPane(headerTransactionTbl);
		scrollHeader.setPreferredSize(new Dimension(500, 100));
		
		tDetailHeader = new Vector<>();
		tDetailHeader.add("TransactionID");
		tDetailHeader.add("ProductName");
		tDetailHeader.add("ProductType");
		tDetailHeader.add("Quantity");
	
		dtmDetail = new DefaultTableModel(tDetailHeader, 0);
		detailTransactionTbl = new JTable(dtmDetail) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		detailTransactionTbl.getTableHeader().setResizingAllowed(false);
		detailTransactionTbl.getTableHeader().setReorderingAllowed(false);
		
		scrollDetail = new JScrollPane(detailTransactionTbl);
		scrollDetail.setPreferredSize(new Dimension(500, 100));
		txttotalPrice = new JTextField("0");
		txttotalPrice.setPreferredSize(new Dimension(100, 30));
		txttotalPrice.setEditable(false);
		
		btnCheck = new JButton("Check");
		
		pnlTitle = new JPanel(new BorderLayout());
		pnlTitle.add(lblTitle, BorderLayout.NORTH);
		pnlTitle.setPreferredSize(new Dimension(100, 100));
				
		pnlTbl = new JPanel(new GridLayout(4,1));
		pnlTbl.add(changeToJPanel(lblHeaderTblTitle, Toolkit.getDefaultToolkit().getScreenSize().width-100, 80));
		pnlTbl.add(changeToJPanel(scrollHeader, Toolkit.getDefaultToolkit().getScreenSize().width-100, 80));
		pnlTbl.add(changeToJPanel(lblDetailTblTitle, Toolkit.getDefaultToolkit().getScreenSize().width-100, 80));
		pnlTbl.add(changeToJPanel(scrollDetail, Toolkit.getDefaultToolkit().getScreenSize().width-100, 80));
		
		pnlTotal = new JPanel(new FlowLayout(FlowLayout.CENTER));
		pnlTotal.add(lblTotal);
		pnlTotal.add(txttotalPrice);
		pnlTotal.add(btnCheck);
		pnlTotal.setPreferredSize(new Dimension(300, 100));
		
		pnlCenter = new JPanel(new BorderLayout());
		pnlCenter.add(pnlTbl, BorderLayout.NORTH);
		
		btnCheck.addActionListener(this);
	}

	void setHeaderData() {
		tHeader = new Vector<>();
		tHeader.add("Transaction ID");
		tHeader.add("Date of Transactions");
		tHeader.add("Payment Type");
		
		dtmHeader = new DefaultTableModel(tHeader, 0);
		
		ResultSet rs = new MySQLConnect().selectAllHeaderTransaction(userId);
		
		try {
			listHeaderData = new ArrayList<HeaderTransaction>();
			while (rs.next()) {
				HeaderTransaction ht = new HeaderTransaction();
				ht.setId(rs.getInt("ID"));
				ht.setTransactionDate(rs.getString("TransactionDate"));
				ht.setPaymentType(rs.getString("PaymentType"));
				listHeaderData.add(ht);
			}
			for (HeaderTransaction headerT : listHeaderData) {
				tHeaderContent = new Vector<>();
				tHeaderContent.add(headerT.getId());
				tHeaderContent.add(headerT.getTransactionDate());
				tHeaderContent.add(headerT.getPaymentType());
				dtmHeader.addRow(tHeaderContent);
			}
			headerTransactionTbl.setModel(dtmHeader);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	void setDetailData () {
		dtmDetail = new DefaultTableModel(tDetailHeader,0);
		ResultSet rs = new MySQLConnect().selectAllDetailTransaction(listHeaderData.get(headerTransactionTbl.getSelectedRow()).getId());
		int grandTotal = 0;
		try {
			listDt = new ArrayList<>();
			while (rs.next()) {
				DetailTransaction dt = new DetailTransaction();
				dt.setTransactionId(rs.getInt("TransactionID"));
				dt.setProductId(rs.getInt("ProductID"));
				dt.setQuantity(rs.getInt("Quantity"));
				listDt.add(dt);
			}
			for (DetailTransaction detail : listDt) {
				Product product = new MySQLConnect().selectProductByid(detail.getProductId());
				ProductType productType = new MySQLConnect().selectProductTypeByid(product.getProductTypeId());
				tDetailContent = new Vector<>();
				tDetailContent.add(detail.getTransactionId());
				tDetailContent.add(product.getProductName());
				tDetailContent.add(productType.getProductTypeName());
				tDetailContent.add(detail.getQuantity());
				grandTotal += product.getProductPrice() * detail.getQuantity();
				dtmDetail.addRow(tDetailContent);
			}
			txttotalPrice.setText(String.valueOf(grandTotal));
			detailTransactionTbl.setModel(dtmDetail);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public JPanel changeToJPanel(Component component, int width, int height) {
		JPanel panel = new JPanel();
		
		panel.add(component);
		
		component.setPreferredSize(new Dimension(width, height));
		
		return panel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnCheck) {
			if (headerTransactionTbl.getSelectedColumnCount() == 0) {
				JOptionPane.showMessageDialog(this, "Please Choose a row to check first");
			}else {				
				setDetailData();
			}
		}
	}
}
