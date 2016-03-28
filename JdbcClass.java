import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

public class JdbcClass implements DbInterface {
    // JDBC driver name and database URL
	  static Connection conn = null;

	static String json_string;
	
	public static void main(String[] args) throws Exception
	{
		char choice;
		Statement stmt = null;
		try{
            Class.forName("com.mysql.jdbc.Driver");
     System.out.println("Connecting to database...");
     conn = DriverManager.getConnection(DB_URL, USER, PASS);
     System.out.println("Creating database...");
     stmt = conn.createStatement(); 
     
     JdbcClass.createDatabase(stmt);
     System.out.println("Inserting Data...");
     JdbcClass.fileRead(stmt);
     System.out.println("Data Inserted Successfully");
   	for (;;)					//Infinite Loop.
	{ 
     choice= JOptionPane.showInputDialog("Enter \n\t1: To search by City \n\t2: To search by Latitude/Longitude  \n\t0: To exit\n").charAt(0);
		switch (choice)
		{
		case '1':	//To search by City
		     String city=JOptionPane.showInputDialog("Enter City you want to search");
		     Double[] list= JdbcClass.showSearchedCity(stmt, city);
		//     System.out.println(json_string);
		 	String result= JdbcClass.findCities(stmt, list[0].toString(),list[1].toString() );
			JOptionPane.showMessageDialog(null,result);
			break;
		case '2': //To search by Latitude/Longitude
	        String latitude=JOptionPane.showInputDialog("Enter Latitude");
	        String longitude=JOptionPane.showInputDialog("Enter Longitude");
			 result= JdbcClass.findCities(stmt, latitude,longitude );
			JOptionPane.showMessageDialog(null,result);
			break;
		case '0': //to exit
			return;
		default:
			JOptionPane.showMessageDialog(null,"Wrong number entered");	
		}//end switch
	}
		}
		catch(SQLException se){
            JOptionPane.showMessageDialog(null, "Sorry Run system again");
	}
	} //end main
    public static void createDatabase(Statement stmt){

        try {
            String sql = "CREATE DATABASE IF NOT EXISTS AP_Lab5";
            stmt.executeUpdate(sql);
            System.out.println("Database created successfully");
            
            String sql1_1 = "DROP TABLE IF EXISTS AP_Lab5.map";
            stmt.executeUpdate(sql1_1);
            String sql1 = "CREATE TABLE IF NOT EXISTS AP_Lab5.map" +
            		  "(locId INTEGER not NULL, " +
                      " country VARCHAR(50), " + 
                      " region VARCHAR(50), " + 
                      " city VARCHAR(50), " + 
                      " postalCode VARCHAR(50), " +
                      " latitude DOUBLE, " +
                      " longitude DOUBLE, " +
                      " metroCode INTEGER DEFAULT NULL, " +
                      " areaCode INTEGER DEFAULT NULL, " +
                      " PRIMARY KEY ( locId ))"; 
            stmt.executeUpdate(sql1);
            System.out.println("Table created successfully"); 
            
        } //End TRY
        catch (SQLException ex) {
            Logger.getLogger(JdbcClass.class.getName()).log(Level.SEVERE, null, ex);
        }
 } //End CreateDatabase

    public static void fileRead(Statement stmt) throws Exception {

        BufferedReader CSVFile = 
        new BufferedReader(new FileReader("C:\\Users\\shehz\\Desktop\\GeoLiteCity-Location.csv"));
        CSVFile.readLine();
        CSVFile.readLine();
        String dataRow = CSVFile.readLine(); //3rd line of file i.e 1st line of data
        String sql="";
        int i=0;
        
        while (dataRow != null){
            i++;
            if(i==300) break;
            dataRow=dataRow.replace(",,",",NULL,");
            dataRow=dataRow.replace(",)",",NULL)");
            sql="INSERT INTO AP_Lab5.map(locId,country,region,city,postalCode,latitude,longitude,metroCode,areaCode) VALUES("+dataRow+");";
            sql=sql.replace(",,",",NULL,");
            sql=sql.replace(",)",",NULL)");

            stmt.executeUpdate(sql);
            dataRow = CSVFile.readLine(); // Reading next line of data.
        }
        CSVFile.close();
    }//Read csv file and insert into db  
    
    public static Double[] showSearchedCity(Statement stmt,String city ){
        String allRecords=""; 
        Double latitude = null, longitude = null;
        
        try {   
             String sq3 = "SELECT * FROM AP_Lab5.map" +" WHERE city = " +"'" +city + "'";
             ResultSet rs = stmt.executeQuery(sq3);
             while(rs.next()){
                //Retrieve by column name
            	 latitude=rs.getDouble("latitude");
            	 longitude=rs.getDouble("longitude");
                 allRecords=allRecords+"City: "+city;
                 allRecords=allRecords+"\nLatitude: "+rs.getDouble("latitude");
                 allRecords=allRecords+"\nLongitude: "+rs.getDouble("longitude") +"\n";
             } //End While
             rs.close();
         } 
         catch (SQLException ex) {
             Logger.getLogger(JdbcClass.class.getName()).log(Level.SEVERE, null, ex);
         }
        
        Double[] myList=new Double[2];
        myList[0]=latitude;
        myList[1]=longitude;
        return myList;
  }//End showSearchedCity
    
    public static String findCities(Statement stmt, String latitude, String longitude) throws Exception {
    	String allRecords=""; 
        String sql="SELECT city FROM AP_Lab5.map where DEGREES(ACOS(COS(RADIANS("+latitude+")) * COS(RADIANS(latitude)) *\n" +
              " COS(RADIANS("+longitude+") - RADIANS(longitude)) +\n" +
              " SIN(RADIANS("+latitude+")) * SIN(RADIANS(latitude)))) < 2";
        
         ResultSet rs = stmt.executeQuery(sql);
          while(rs.next()){
           //Retrieve by column name
        	  allRecords=allRecords+"City: "+rs.getString("city")+"\n";
        }
        rs.close();    
		return allRecords;
     }    
}
