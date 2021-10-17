//Author: Joey Paschke
//Date: 9/28/21
//Class: CS 457
import java.util.Formatter;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.DriverManager;


/////////////////
public class read
{
	public static String url = "C:/Users/jojop/eclipse-workspace/Database2/";
	public static String dbNameGlobal = " ";
	
	/////////////////////////
	//////////READ///////////
	/////////////////////////
    public static void read()
    {
    	//this is the scanner to scan in the users input
    	Scanner scanner = new Scanner(System.in);
    	System.out.print("-->");
    	String input = scanner.nextLine();
    	String [] num = new String[10];
    	Scanner parse = new Scanner(input);
    	//this is how i did the parsing
    	parse.useDelimiter("\s((?=[a-z][a-z]))");
    	
    	int i = 0;
    	//parses the input into an array and does this until there isnt anything left
    	while (parse.hasNext())
    	{
    		num[i] = parse.next();
    		i++;
    	}
    	
    	i = 0;
    	if(num[i].contains("CREATE DATABASE"))
    	{
    		String dbName = num[i + 1];
    		//Cuts off semicolon
    		String name = dbName.substring(0, dbName.length() - 1);
    		createDirect(name);
    	}
    	if(num[i].contains("DROP DATABASE"))
    	{
    		String dbName = num[i + 1];
    		//Cuts off semicolon
    		String name = dbName.substring(0, dbName.length() - 1);
			deleteDirect(name);
    	}
    	else if(num[i].contains("USE"))
    	{
    		String dbName = num[i + 1];
    		//Cuts off semicolon
    		String name = dbName.substring(0, dbName.length() - 1);
    		use(name);
    	}
    	else if(num[i].contains("DROP TABLE"))
    	{
    		String dbName = num[i + 1];
    		//Cuts off semicolon
    		String name = dbName.substring(0, dbName.length() - 1);
    		deleteTable(name);
    	}
    	else if(num[i].contains("CREATE TABLE"))
    	{
    		String dbName = num[i + 1];
    		String dbName2 = num[i + 2];
    		String dbName3 = num[i + 3];
    		//Makes the parsing correct
    		String tableName = dbName.substring(0, dbName.length() - 4);
    		String firstEntry = dbName.substring(7, dbName.length());
    		String secondEntry = dbName2.substring(0, dbName2.length() - 4);
    		String thirdEntry = dbName2.substring(5, dbName2.length());
    		String fourthEntry = dbName3.substring(0, dbName3.length() - 2);
    		if(tableName.contains("tbl_2"))
    		{
    			thirdEntry = dbName2.substring(7, dbName2.length());
    		}
    		createTable(tableName, firstEntry, secondEntry, thirdEntry, fourthEntry);
    	}
    	else if(num[i].contains("ALTER TABLE"))
    	{
    		String dbName = num[i + 1];
    		String dbName2 = num[i + 2];
    		//Helps with parsing
    		String name = dbName.substring(0, dbName.length() - 7);
    		String add = dbName.substring(6, dbName.length() - 3);
    		String firstEntry = dbName.substring(10, dbName.length());
    		String secondEntry = dbName2.substring(0, dbName2.length() - 1);
    		alterTable(name, firstEntry, secondEntry);
    		
    	}
    	else if(num[i].contains("SELECT * FROM"))
    	{
    		String dbName = num[i + 1];
    		//Cuts off semicolon
    		String name = dbName.substring(0, dbName.length() - 1);
    		selectAll(name);
    	}
    	else if(num[i].contains(".EXIT"))
    	{
    		System.out.print("All Done.");
    	}
    }
    
    //////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////ALTER TABLE////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////
    public static void alterTable(String tableName, String firstEntry, String secondEntry) 
    {
    	File file = new File(url + dbNameGlobal + "/" + tableName + ".db");
    	try 
    	{
			if(file.exists())
			{
				FileWriter myWriter = new FileWriter(url + dbNameGlobal + "/" + tableName + ".db", true);
			    myWriter.write(firstEntry + " " + secondEntry);
			    myWriter.close();
				System.out.println("Table " + tableName + " modified.");
				read();
			}
			else
			{
				System.out.println("!Failed to query table " + tableName + " because it does not exist.");
				read();
			}
		} 
    	catch (IOException e)
    	{
			e.printStackTrace();
		}
    }
    
    //////////////////////////////////////////////
    /////////////////SELECT ALL///////////////////
    //////////////////////////////////////////////
    public static void selectAll(String tableName) 
    {
    	try 
    	{
			Scanner scanner = new Scanner(new File(url + dbNameGlobal + "/" + tableName + ".db"));
			while (scanner.hasNextLine()) 
			{
				System.out.print(scanner.nextLine());
				System.out.print(" | ");
			}
			scanner.close();
			System.out.print("\n");
			read();
		} 
    	catch (FileNotFoundException e) 
    	{
    		System.out.print("!Failed to query table " + tableName + " because it does not exist.");
			read();
		}
    }
    
    //////////////////////////////////////////////
    /////////////DELETE DIRECTORY/////////////////
    //////////////////////////////////////////////
    public static void deleteDirect(String dbName) 
    {
    	File dir = new File(url + dbName);
		if (!dir.delete())
		{
			System.out.println("!Failed to delete " +  dbName + " because it does not exist.");
			read();
		} 
		else 
		{
			System.out.println("Database " + dbName + " deleted.");
		    read();
		}
    }
    
    ////////////////////////////////////////////////
    ////////////////DELETE TABLE////////////////////
    ////////////////////////////////////////////////
    public static void deleteTable(String tableName) 
    {
    	File dir = new File(url + dbNameGlobal + "/" + tableName + ".db");
		if (!dir.delete())
		{
			System.out.println("!Failed to delete " +  tableName + " because it does not exist.");
			read();
		} 
		else 
		{
			System.out.println("Table " + tableName + " deleted.");
		    read();
		}
    }
    
    //////////////////////////////////////////////
    /////////////CREATE DIRECTORY/////////////////
    //////////////////////////////////////////////
    public static File createDirect(String dbName)
    {
    	File dir = new File(url + dbName);
		if (!dir.exists())
		{
		    dir.mkdirs();
			System.out.println("Database " + dbName + " created.");
			read();
		} 
		else 
		{
		    System.out.println("!Failed to create database " +  dbName + " because it already exists.");
		    read();
		}
		return dir;
    }
    
    /////////////////////////////////////
    ////////////////USE//////////////////
    /////////////////////////////////////
    public static void use(String dbName) 
    {
    	File dir = new File(url + dbName);
		if (dir.exists())
		{
	    	dbNameGlobal =  dbName;
	    	System.out.println("Using database " + dbName + ".");
	    	read();
		}
		else 
		{
		    System.out.println("!Failed to use database " +  dbName + " because it doesnt exist.");
		    read();
		}
    }
   
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////CREATE TABLE//////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void createTable(String tableName, String firstEntry, String secondEntry, String thirdEntry, String fourthEntry)
    {
    	File file = new File(url + dbNameGlobal + "/" + tableName + ".db");
    	try 
    	{
			if(file.createNewFile()) 
			{
				FileWriter myWriter = new FileWriter(url + dbNameGlobal + "/" + tableName + ".db");
			    myWriter.write(firstEntry + " " + secondEntry + "\n" + thirdEntry + " " + fourthEntry + "\n");
			    myWriter.close();
				System.out.println("Table " + tableName + " created.");
				read();
			}
			else
			{
				System.out.println("!Failed to create table " + tableName + " because it already exists.");
				read();
			}
		} 
    	catch (IOException e) 
    	{

			e.printStackTrace();
		}
    }
    
	//////////////////////////////////////
    /////////////////MAIN/////////////////
    //////////////////////////////////////
	public static void main(String[] args) 
	{
		read();
	}

}
