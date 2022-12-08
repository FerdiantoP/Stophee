package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.cj.x.protobuf.MysqlxPrepare.Prepare;
import com.mysql.cj.xdevapi.Result;

import model.DetailTransaction;
import model.HeaderTransaction;
import model.Product;
import model.ProductType;
import model.Users;


public class MySQLConnect {
	
	private Connection con;
	private Statement st;
	private ResultSet rs;
	private ResultSetMetaData rsm;
	private PreparedStatement pStat;
	
	private String HOST = "localhost:3306";
	private String DATABASE = "stophee";
	private String USER = "root";
	private String PASS = "";
	private String URL = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);

	public MySQLConnect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			con = DriverManager.getConnection(URL, USER, PASS);
			st = con.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet executeQuery (String query) {
		try {
			rs = st.executeQuery(query);
			rsm = rs.getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	
	public ResultSet selectByUsernameAndPassword(Users users) {
		String query = String.format("SELECT * FROM users WHERE email = '%s' AND Password = '%s' LIMIT 1", users.getEmail(), users.getPassword());
		return executeQuery(query);
	}
	
	public ResultSet selectByEmail (String email) {
		String query = String.format("SELECT * FROM users WHERE email = '%s' LIMIT 1", email);
		return executeQuery(query);
	}
	
	public ResultSet selectAllProduct () {
		String query = "SELECT * FROM Product ORDER BY id";
		return executeQuery(query);
	}
	
	public ResultSet selectAllProductType () {
		String query = "SELECT * FROM ProductType ORDER BY id";
		return executeQuery(query);
	}
	
	public ResultSet selectAllHeaderTransaction (int userId) {
		String query = String.format("SELECT * FROM headertransaction WHERE UserID = '%d'ORDER BY id", userId);
		return executeQuery(query);
	}
	
	public ResultSet selectNewHeaderTransaction (int userId) {
		String query = String.format("SELECT * FROM headertransaction WHERE UserID = '%d' ORDER BY id DESC LIMIT 1", userId);
		return executeQuery(query);
	}
	
	public ResultSet selectAllDetailTransaction (int transactionId) {
		String query = String.format("SELECT * FROM detailtransaction WHERE TransactionID = '%d'", transactionId);
		return executeQuery(query);
	}
	
	public ProductType selectProductTypeIDByName (String productTypeName) {
		ProductType pt = null;
		String query = String.format("SELECT * FROM productType WHERE ProductTypeName = '%s'", productTypeName);
		ResultSet rs = new MySQLConnect().executeQuery(query);
		try {
			rs.next();
			pt = new ProductType();
			pt.setId(rs.getInt("ID"));
			pt.setProductTypeName(rs.getString("ProductTypeName"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return pt;
	}
	
	public Product selectProductByid (int productId) {
		Product product = null;
		String query = String.format("SELECT * FROM product WHERE id = '%s'", productId);
		ResultSet rs = new MySQLConnect().executeQuery(query);
		try {
			rs.next();
			product = new Product();
			product.setId(rs.getInt("ID"));
			product.setProductTypeId(rs.getInt("ProductTypeID"));
			product.setProductName(rs.getString("ProductName"));
			product.setProductPrice(rs.getInt("ProductPrice"));
			product.setProductQuantity(rs.getInt("ProductQuantity"));
		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return product;
		
	}
	
	public ProductType selectProductTypeByid (int productTypeId) {
		String query = String.format("SELECT * FROM productType WHERE id = '%s'", productTypeId);
		ResultSet rs = new MySQLConnect().executeQuery(query);
		ProductType productType = null;
		try {
			rs.next();
			productType = new ProductType();
			productType.setId(rs.getInt("ID"));
			productType.setProductTypeName(rs.getString("ProductTypeName"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return productType;
	}
	 
	public void executeUpdate (String query) {	
		try {
			st.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private PreparedStatement preparedStatement(String query) {
		try {
			pStat = con.prepareStatement(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pStat;
	}
	
	public void insertIntoUsers (Users user) {
		String query = "INSERT INTO users(name,email,password,phone,gender,role) VALUES (?,?,?,?,?,?)";
		PreparedStatement pStat = preparedStatement(query);
		try {
			pStat.setString(1, user.getName());
			pStat.setString(2, user.getEmail());
			pStat.setString(3, user.getPassword());
			pStat.setString(4, user.getPhone());
			pStat.setString(5, user.getGender());
			pStat.setString(6, user.getRole());
			pStat.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertIntoHeaderTransaction(HeaderTransaction ht) {
		String query = "INSERT INTO headerTransaction (UserID,TransactionDate,PaymentType) VALUES (?,?,?)";
		PreparedStatement pStat = preparedStatement(query);
		try {
			pStat.setInt(1, ht.getUserId());
			pStat.setString(2, ht.getTransactionDate());
			pStat.setString(3, ht.getPaymentType());
			pStat.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertIntoDetailTransaction(DetailTransaction dt) {
		String query = "INSERT INTO detailTransaction (TransactionID,ProductID,Quantity) VALUES (?,?,?)";
		PreparedStatement pStat = preparedStatement(query);
		try {
			pStat.setInt(1, dt.getTransactionId());
			pStat.setInt(2, dt.getProductId());
			pStat.setInt(3, dt.getQuantity());
			pStat.executeUpdate();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertDataProduct (Product product) {
		String query = "INSERT INTO product (ProductName, ProductTypeID, ProductPrice, ProductQuantity) VALUES (?,?,?,?) ";
		PreparedStatement pStat = preparedStatement(query);
		try {
			pStat.setString(1, product.getProductName());
			pStat.setInt(2, product.getProductTypeId());
			pStat.setInt(3, product.getProductPrice());
			pStat.setInt(4, product.getProductQuantity());
			pStat.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateDataProduct(Product product) {
		String query = "UPDATE product SET ProductTypeID = ?, ProductName = ?, ProductPrice = ?, ProductQuantity = ? WHERE ID = ?";
		PreparedStatement pStat = preparedStatement(query);
		try {
			pStat.setInt(1, product.getProductTypeId());
			pStat.setString(2, product.getProductName());
			pStat.setInt(3, product.getProductPrice());
			pStat.setInt(4, product.getProductQuantity());
			pStat.setInt(5, product.getId());
			pStat.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteDataProduct (Product product) {
		String query = "DELETE FROM product WHERE ID = ?";
		PreparedStatement pStat = preparedStatement(query);
		
		try {
			pStat.setInt(1, product.getId());
			pStat.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateProductQty (Product product) {
		String query = "UPDATE product SET productQuantity = ? WHERE ID = ?";
		PreparedStatement pStat = preparedStatement(query);
		
		try {
			pStat.setInt(1, product.getProductQuantity());
			pStat.setInt(2, product.getId());
			pStat.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void insertProductTypeData (ProductType productType) {
		String query = "INSERT INTO productType (productTypeName) VALUES (?)";
		PreparedStatement pStat = preparedStatement(query);
		try {
			pStat.setString(1, productType.getProductTypeName());
			pStat.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateProductTypeData (ProductType productType) {
		String query = "UPDATE productType SET productTypeName = ? WHERE ID = ?";
		PreparedStatement pStat = preparedStatement(query);
		try {
			pStat.setString(1, productType.getProductTypeName());
			pStat.setInt(2, productType.getId());
			pStat.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void DeleteProductTypeData (ProductType productType) {
		String query = "DELETE FROM producttype WHERE ID = ?";
		PreparedStatement pStat = preparedStatement(query);
		try {
			pStat.setInt(1, productType.getId());
			pStat.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
