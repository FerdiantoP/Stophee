package Form;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.management.StringValueExp;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import model.Users;
import util.MySQLConnect;

public class RegisterForm extends JFrame implements ActionListener {
	
	JLabel lblTitle, lblName, lblEmail, lblPassword, lblPhoneNumber, lblGender;
	JTextField txtName, txtEmail, txtPhoneNumber;
	JPasswordField password;
	JRadioButton radioMale, radioFemale;
	ButtonGroup groupGender;
	JPanel pnlRForm,pnlGender,pnlButton, pnlTitle;
	JButton btnRegister;
	
	public RegisterForm() {
		initComponent();

		this.getContentPane().add(pnlTitle, BorderLayout.NORTH);
		pnlTitle.setPreferredSize(new Dimension(100, 120));
		this.getContentPane().add(pnlRForm, BorderLayout.CENTER);
		this.getContentPane().add(pnlButton, BorderLayout.SOUTH);
		pnlButton.setPreferredSize(new Dimension(100, 70));
		
		initFrame();
	}

	void initFrame () {
		this.setTitle("Stophee");
		this.setSize(350,450);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	void initComponent () {
		lblTitle = new JLabel("REGISTER");
		lblTitle.setFont(new Font("SansSerif", 1, 24));
		lblTitle.setHorizontalAlignment((int)CENTER_ALIGNMENT);
		
		lblName = new JLabel("Name :");
		lblEmail = new JLabel("Email :");
		lblPassword = new JLabel("Password :");
		lblPhoneNumber = new JLabel("Phone Number :");
		lblGender = new JLabel("Gender :");
		
		txtName = new JTextField();
		txtEmail = new JTextField();
		txtPhoneNumber = new JTextField();
		password = new JPasswordField();
		
		radioMale = new JRadioButton("Male");
		radioFemale = new JRadioButton("Female");
		
		radioMale.setActionCommand("Male");
		radioFemale.setActionCommand("Female");
		
		groupGender = new ButtonGroup();
		groupGender.add(radioMale);
		groupGender.add(radioFemale);
		
		btnRegister = new JButton("Submit");
		
		pnlTitle = new JPanel(new GridLayout(1,1));
		pnlTitle.add(lblTitle);
		
		pnlGender = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pnlGender.add(radioMale);
		pnlGender.add(radioFemale);
		
		pnlRForm = new JPanel(new GridLayout(5, 2));
		pnlRForm.add(changeToJPanel(lblName,120,30));
		pnlRForm.add(changeToJPanel(txtName,120,30));
		pnlRForm.add(changeToJPanel(lblEmail,120,30));
		pnlRForm.add(changeToJPanel(txtEmail,120,30));
		pnlRForm.add(changeToJPanel(lblPassword,120,30));
		pnlRForm.add(changeToJPanel(password,120,30));
		pnlRForm.add(changeToJPanel(lblPhoneNumber,120,30));
		pnlRForm.add(changeToJPanel(txtPhoneNumber,120,30));
		pnlRForm.add(changeToJPanel(lblGender,120,30));
		pnlRForm.add(changeToJPanel(pnlGender, 135, 30));
				
		pnlButton = new JPanel();
		pnlButton.add(btnRegister);
		
		btnRegister.addActionListener(this);
	}
	
	public JPanel changeToJPanel(Component component, int width, int height) {
		JPanel panel = new JPanel();
		
		panel.add(component);
		
		component.setPreferredSize(new Dimension(width, height));
		
		return panel;
	}
	
	public static void main(String[] args) {
		new RegisterForm();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub	
		String email = txtEmail.getText();
		String phone = txtPhoneNumber.getText();
		
		if (e.getSource() == btnRegister) {
			if (txtName.getText().length() < 3 || txtName.getText().length() > 30) {
				JOptionPane.showMessageDialog(this, "Name length must be between 3 - 30");
			}else if (!validEmail(email)) {
				JOptionPane.showMessageDialog(this, "Email must contain @ and ends with .com");
			}else if (password.getText().length() < 5 || password.getText().length() > 20) {
				JOptionPane.showMessageDialog(this, "Password must be 5 - 20 characters");
			}else if (!validPhoneNumber(phone)) {
				JOptionPane.showMessageDialog(this, "Phone can't empty & Numeric");
			}else if (phone.length() < 12 || phone.length() > 15) {
				JOptionPane.showMessageDialog(this, "Phone length must be between 12 - 15 and digits only");
			}else if (groupGender.getSelection() == null) {
				JOptionPane.showMessageDialog(this, "Gender must be chosen");
			}else {
				ResultSet rs = new MySQLConnect().selectByEmail(email);
				try {
					if (rs.next()) {
						JOptionPane.showMessageDialog(this, "Email has been taken");
					}else {
						Users user = new Users();
						user.setName(txtName.getText());
						user.setEmail(txtEmail.getText());
						user.setPassword(password.getText());
						user.setPhone(txtPhoneNumber.getText());
						user.setGender(groupGender.getSelection().getActionCommand());
						user.setRole("User");
						new MySQLConnect().insertIntoUsers(user);
						
						JOptionPane.showMessageDialog(this, "Account created");
						this.dispose();
						JFrame formLogin = new LoginForm();
						formLogin.setVisible(true);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private boolean validPhoneNumber(String phone) {
		boolean validPhone = true;
			
		for (int i = 0; i < phone.length(); i++) {
			Character ch = phone.charAt(i);
			if (phone.isEmpty() || !Character.isDigit(ch)) {
				validPhone = false;
			}
		}
		return validPhone;
	}

	private boolean validEmail(String email) {
		boolean validEmail = true;
		
		if (email.isEmpty()) {
			validEmail = false;
		}else if (email.startsWith("@") || email.startsWith(".") || email.endsWith("@") || email.endsWith(".") || !email.endsWith(".com")) {
			validEmail = false;
		}else if (!email.contains("@") || !email.contains(".")) {
			validEmail = false;
		}else {
			int index = email.indexOf('@');
			if (email.charAt(index - 1) == '.') {
				validEmail = false;
			}else if (email.charAt(index + 1) == '.') {
				validEmail = false;
			}
			String temp = email.substring(email.indexOf("@"));
			for (int i = 1; i < temp.length(); i++) {
				char c = temp.charAt(i);
				if (c == '@') {
					validEmail =false;
				}
			}
			String temp2 = temp.substring(temp.indexOf("."));
			for (int i = 1; i < temp2.length(); i++) {
				char ch = temp2.charAt(i);
				if (ch == '.') {
					validEmail = false;
				}
			}
		}
		return validEmail;
	}
}
