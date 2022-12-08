package Form;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import model.Users;
import util.MySQLConnect;

public class LoginForm extends JFrame implements ActionListener {
	
	JPanel pnlForm, pnlTitle, pnlIsi, pnlButton;
	JLabel lblTitle, lblEmail, lblPassword;
	JTextField txtEmail;
	JPasswordField password;
	JButton btnLogin, btnCreate;
	
	public LoginForm() {	
		initComponent();

		this.getContentPane().add(pnlTitle, BorderLayout.NORTH);
		pnlTitle.setPreferredSize(new Dimension(100, 150));
		this.getContentPane().add(pnlIsi, BorderLayout.CENTER);
		this.getContentPane().add(pnlButton, BorderLayout.SOUTH);
		pnlButton.setPreferredSize(new Dimension(100, 90));
		
		initFrame();
	}
	
	void initFrame () {
		this.setTitle("Stophee");
		this.setSize(370,400);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	void initComponent () {				
		lblTitle = new JLabel("LOGIN");
		lblTitle.setFont(new Font("SansSerif", 1, 24));
		lblTitle.setHorizontalAlignment((int)CENTER_ALIGNMENT);
	
		lblEmail = new JLabel("Email :");
		lblPassword = new JLabel("Password :");
		txtEmail = new JTextField();
		password = new JPasswordField();
		btnLogin = new JButton("Login");
		btnCreate = new JButton("Create Account");
		
		pnlTitle = new JPanel (new GridLayout(1,1));
		pnlTitle.add(lblTitle, "Centre");
		
		pnlIsi = new JPanel (new GridLayout(2, 2));
		pnlIsi.add(changeToJPanel(lblEmail));
		pnlIsi.add(changeToJPanel(txtEmail));
		pnlIsi.add(changeToJPanel(lblPassword));
		pnlIsi.add(changeToJPanel(password));
		
		pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
		pnlButton.add(btnLogin);
		pnlButton.add(btnCreate);
		
		btnLogin.addActionListener(this);
		btnCreate.addActionListener(this);
	}
	
	
	public static void main(String[] args) {
		new LoginForm();
	}
	
	public JPanel changeToJPanel(Component component) {
		JPanel panel = new JPanel();
		
		panel.add(component);
		component.setPreferredSize(new Dimension(120, 30));
		
		return panel;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == btnLogin) {
			Users user = new Users();
			user.setEmail(txtEmail.getText());
			user.setPassword(password.getText());
			ResultSet rs = new MySQLConnect().selectByUsernameAndPassword(user);
			try {	
				if (txtEmail.getText().isEmpty() || password.getText().isEmpty()) {
					JOptionPane.showMessageDialog(this, "Email & Password field must be filled!");
				}else if (!rs.next()) {
					JOptionPane.showMessageDialog(this, "Wrong Email or Password!");
				}else {
					this.dispose();
					JFrame formMain = new MainForm(user.getEmail());
					formMain.setVisible(true);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}			
		}else if (e.getSource() == btnCreate) {
			this.dispose();
			JFrame registerForm = new RegisterForm();
			registerForm.setVisible(true);
		}
	}
}
