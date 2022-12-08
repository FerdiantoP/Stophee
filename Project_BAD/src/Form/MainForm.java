package Form;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import model.Users;
import util.MySQLConnect;

public class MainForm extends JFrame implements ActionListener, MouseListener{
	
	JMenu menuAccount, menuBuy, menuTransaction, menuManage;
	JMenuItem menuLogout, menuManageProduct, menuManageType;
	JMenuBar menuBar;
	JDesktopPane dekstopPane = new JDesktopPane();
	int userId;
	String userRole;
	
	public MainForm(String email) {
		Users user;
		ResultSet rs = new MySQLConnect().selectByEmail(email);
		
		try {
			rs.next();
			user = new Users();
			user.setId(rs.getInt("id"));
			user.setRole(rs.getString("Role"));
			userId = user.getId();
			userRole = user.getRole();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		initComponent();
		
		if (userRole.equals("User")) {
			menuManage.setVisible(false);
		}else {
			menuBuy.setVisible(false);
			menuTransaction.setVisible(false);
		}
	
		setContentPane(dekstopPane);
		setJMenuBar(menuBar);
		
		initFrame();
	}

	void initFrame() {
		this.setTitle("Stophee");
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	void initComponent() {
		menuAccount = new JMenu("Account");
		menuBuy = new JMenu("Buy");
		menuTransaction = new JMenu("Transaction");
		menuManage = new JMenu("Manage");
		
		menuLogout = new JMenuItem("Logout");
		menuManageProduct = new JMenuItem("Product");
		menuManageType = new JMenuItem("Product Type");
		
		menuAccount.add(menuLogout);
		menuManage.add(menuManageProduct);
		menuManage.add(menuManageType);
		
		menuBar = new JMenuBar();
		
		menuBar.add(menuAccount);
		menuBar.add(menuBuy);
		menuBar.add(menuTransaction);
		menuBar.add(menuManage);
		
		menuLogout.addActionListener(this);
		menuManageProduct.addActionListener(this);
		menuManageType.addActionListener(this);
		menuBuy.addMouseListener(this);
		menuTransaction.addMouseListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == menuLogout) {
			this.dispose();
			JFrame loginForm = new LoginForm();
			loginForm.setVisible(true);
		}else if (e.getSource() == menuManageProduct) {
			JInternalFrame manageProductForm = new ManageProductForm(MainForm.this);
			dekstopPane.removeAll();
			manageProductForm.setVisible(true);
			dekstopPane.add(manageProductForm);
		}else if (e.getSource() == menuManageType) {
			JInternalFrame manageProductTypeForm = new ManageProductTypeForm(MainForm.this);
			dekstopPane.removeAll();
			manageProductTypeForm.setVisible(true);
			dekstopPane.add(manageProductTypeForm);
		}
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
			if (e.getClickCount() == 1 && e.getSource() == menuBuy) {
				JInternalFrame buyForm = new BuyForm(MainForm.this, userId);
				dekstopPane.removeAll();
				buyForm.setVisible(true);
				dekstopPane.add(buyForm);
			}else if (e.getClickCount() == 1 && e.getSource() == menuTransaction) {
				JInternalFrame transactionForm = new TransactionForm(MainForm.this, userId);
				dekstopPane.removeAll();
				transactionForm.setVisible(true);
				dekstopPane.add(transactionForm);
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
