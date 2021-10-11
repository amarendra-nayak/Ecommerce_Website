package test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class login {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		String URL = "jdbc:mysql://localhost:3306/ecommerce";
		String UserName = "root";
		String Password = "root";
		
		DBConnection dbobj = new DBConnection(URL,UserName,Password);
		Statement statement = dbobj.getConnection().createStatement();
		
		System.setProperty("webdriver.chrome.driver", "chromedriver");
		WebDriver driver = new ChromeDriver();
		driver.get("https://www.saucedemo.com/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(4000, TimeUnit.MILLISECONDS);
		
		ResultSet rst = statement.executeQuery("select * from login_details");
		
		String Uname="";
		String Pass="";
		
		while(rst.next()) {
			Uname = rst.getString("username");
			Pass = rst.getString("password");
		}
		
		WebElement email = driver.findElement(By.cssSelector("input[id=user-name]"));
		email.sendKeys(Uname);
		WebElement password = driver.findElement(By.cssSelector("input[id=password]"));
		password.sendKeys(Pass);
		WebElement lgn_btn = driver.findElement(By.cssSelector("input[id=login-button]"));
		lgn_btn.click();
		
		WebElement check_status = driver.findElement(By.xpath("//span[text()='Products']"));
		
		if(check_status.equals("Products")) {
			System.out.println("Failed");
		}else {
			System.out.println("Success!");
		}
		
		ResultSet rst1 = statement.executeQuery("select * from eproduct");
		String Name="";
		Double Price=0.0;
		
		while(rst1.next()) {
			Name = rst1.getString("name");
			Price = rst1.getDouble("price");
		}
		
		WebElement Add = driver.findElement(By.xpath("//div[@class='inventory_item_name' and text()='" + Name + "']/following::button[1]"));
		Add.click();
		
		WebElement cart = driver.findElement(By.cssSelector("a[class=shopping_cart_link]"));
		cart.click();
		
		WebElement price_cart = driver.findElement(By.xpath("//div[@class='inventory_item_price']"));
		
		if(price_cart.equals(Price)) {
			System.out.println("Price Changed from databas price i.e. "+Price);
		}else {
			System.out.println("Price is Same  as of database price i.e. "+Price);
		}
		
	

	}

}