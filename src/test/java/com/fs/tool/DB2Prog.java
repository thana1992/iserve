package com.fs.tool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;

import com.fs.bean.util.GlobalVariable;
import com.fs.dev.Arguments;
import com.fs.dev.Console;

/**
 * DB2Prog class implements for attributes setting
 * 
 * @author tassan_oro@freewillsolutions.com
 */
public class DB2Prog {
	private String section = null;
	private String urlName;
	private String packageName = "com.fs.db.model";
	private String driverName;
	private String outfileName;
	private String tableName;
	private String user;
	private String password;
	private String className;
	private String directory;
	private String datePattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	private boolean removeDelimiter = true;
	private boolean autoPackagePath = true;
	private Connection connection;

	public DB2Prog() {
		super();
	}

	public static java.sql.Connection getNewConnection(String section) throws Exception {
		String user = (String)GlobalVariable.getVariable((section==null||section.length()<=0?"":section+"_")+"USER");
		String password = (String)GlobalVariable.getVariable((section==null||section.length()<=0?"":section+"_")+"PASSWORD");
		String url = (String)GlobalVariable.getVariable((section==null||section.length()<=0?"":section+"_")+"URL");
		String driver = (String)GlobalVariable.getVariable((section==null||section.length()<=0?"":section+"_")+"DRIVER");
		if(driver==null) throw new SQLException("Driver name is not specified ("+section+")");
		if(user==null) throw new SQLException("User is not specified ("+section+")");
		if(password==null) throw new SQLException("Password is not defined ("+section+")");
		if(url==null) throw new SQLException("URL is not specified ("+section+")");
		Class.forName(driver);
		return DriverManager.getConnection(url,user,password);
	}

	public static void usageInfo() {
		Console.out.println("\t-t,-table table name");
		Console.out.println("\t-d,-driver JDBC driver");
		Console.out.println("\t-l,-url URL to access database");
		Console.out.println("\t-u,-user user priviledge");
		Console.out.println("\t-p,-pwd password");
		Console.out.println("\t-k,-pck package");
		Console.out.println("\t-o,-output output directory");
		Console.out.println("\t-c,-class class name");
		Console.out.println("\t-ms	db section point to global configuration");
	}
	
	protected String clearingDelimiter(String colname) {
		int idx = colname.indexOf('_');
		while(idx>=0) {
			colname = colname.substring(0,idx)+colname.substring(idx+1);
			idx = colname.indexOf('_');
		}
		return colname;
	}

	protected java.sql.Connection createConnection() throws Exception {
		if(getDriverName()!=null && getUrlName()!=null && getUser()!=null) {
			Console.out.println("loading driver "+getDriverName());
			Class.forName(getDriverName());
			Console.out.println("create connection ...");
			return DriverManager.getConnection(getUrlName(),getUser(),getPassword());
		} else {
			return getNewConnection(getSection());
		}		
	}

	protected String getDataType(int type) {
		switch(type) {
			case Types.BIGINT: 
				return "long";
			case Types.DOUBLE: 
				return "double";
			case Types.REAL: 
			case Types.FLOAT: 
				return "float";
			case Types.INTEGER: 
			case Types.SMALLINT:
			case Types.TINYINT: 
				return "int";
			case Types.DECIMAL: 
			case Types.NUMERIC: 
				return "java.math.BigDecimal";
			case Types.VARCHAR:
			case Types.CHAR:
			case Types.BLOB:
			case Types.CLOB:
			case 2011 : //NCLOB
			case 2009 : //SQLXML
				return "String";
			case Types.DATE: 
				return "java.sql.Date";
			case Types.TIME: 
				return "java.sql.Time";
			case Types.TIMESTAMP: 
				return "java.sql.Timestamp";
			case Types.BIT : 
			case Types.BOOLEAN: 
				return "boolean";
			case -15 : //NCHAR
			case -9  : //NVARCHAR
			case -16 : //LONGNVARCHAR
				return "String";
			default: return "String";
		}
	}

	protected String getSQLType(int type) {
		switch(type) {
			case Types.BIGINT: 
				return "java.sql.Types.BIGINT";
			case Types.DOUBLE: 
				return "java.sql.Types.DOUBLE";
			case Types.REAL: 
			case Types.FLOAT: 
				return "java.sql.Types.FLOAT";
			case Types.INTEGER: 
			case Types.SMALLINT:
			case Types.TINYINT: 
				return "java.sql.Types.INTEGER";
			case Types.DECIMAL: 
			case Types.NUMERIC: 
				return "java.sql.Types.DECIMAL";
			case Types.VARCHAR:
			case Types.CHAR:
			case Types.BLOB:
			case Types.CLOB:
			case 2011 : //NCLOB
			case 2009 : //SQLXML
				return "java.sql.Types.VARCHAR";
			case Types.DATE: 
				return "java.sql.Types.DATE";
			case Types.TIME: 
				return "java.sql.Types.TIME";
			case Types.TIMESTAMP: 
				return "java.sql.Types.TIMESTAMP";
			case Types.BIT : 
			case Types.BOOLEAN: 
				return "java.sql.Types.BIT";
			case -15 : //NCHAR
			case -9  : //NVARCHAR
			case -16 : //LONGNVARCHAR
				return "java.sql.Types.VARCHAR";
			default: return "java.sql.Types.VARCHAR";
		}
	}

	public void ensureFilePath(java.io.File file) throws Exception {
		java.io.File pfile = file.getParentFile();
		if(!pfile.exists()) pfile.mkdirs();
	}

	public String getClassName() {
		if(className==null || className.trim().length()==0) {
			return parseClassName(getTableName());
		}
		return className;
	}
	
	public Connection getConnection() {
		return connection;
	}

	public String getDatePattern() {
		return datePattern;
	}

	public String getDirectory() {
		if(directory == null) return "";
		if(directory.trim().length() == 0) return directory;
		return directory+java.io.File.separator;
	}

	public String getDriverName() {
		return driverName;
	}

	public String getOutfileName() {
		if(outfileName==null || outfileName.trim().length()==0) {
			return getClassName()+".java";
		}
		return outfileName;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getPackagePath() {
		String packing = null;
		if(isAutoPackagePath()) {
			packing = getPackageName();
			if(packing!=null) {
				packing = packing.replaceAll("\\.", "\\"+java.io.File.separator);
			}
		}
		return packing;
	}

	public String getPassword() {
		return password;
	}

	public String getSection() {
		return section;
	}

	public String getTableName() {
		return tableName;
	}

	public String getUrlName() {
		return urlName;
	}

	public String getUser() {
		return user;
	}
	
	public boolean isAutoPackagePath() {
		return autoPackagePath;
	}

	public boolean isRemoveDelimiter() {
		return removeDelimiter;
	}

	public void obtain(DB2Prog prog) {
		if(prog == null) return;
		setTableName(prog.getTableName());
		setDriverName(prog.getDriverName());
		setUrlName(prog.getUrlName());
		setUser(prog.getUser());
		setPassword(prog.getPassword());
		setPackageName(prog.getPackageName());
		setDirectory(prog.getDirectory());
		setClassName(prog.getClassName());
		setSection(prog.getSection());
		setRemoveDelimiter(prog.isRemoveDelimiter());		
	}

	public String parseClassName(String naming) {
		if(naming==null) return null;
		String clsname = clearingDelimiter(naming);
		char firstChar = clsname.charAt(0);
		String table = clsname.substring(1);
		return Character.toUpperCase(firstChar)+table;		
	}

	public void readArguments(String[] args) {
		if(args==null) return;
		setTableName(Arguments.getString(args, getTableName(), "-t","-table"));
		setDriverName(Arguments.getString(args, getDriverName(), "-d","-driver"));
		setUrlName(Arguments.getString(args, getUrlName(), "-l", "-url"));
		setUser(Arguments.getString(args, getUser(), "-u","-user"));
		setPassword(Arguments.getString(args, getPassword(), "-p","-pwd"));
		setPackageName(Arguments.getString(args, getPackageName(), "-k","-pck"));
		setDirectory(Arguments.getString(args, getDirectory(), "-o","-output"));
		setClassName(Arguments.getString(args, getClassName(), "-c","-class"));
		setSection(Arguments.getString(args, getSection(), "-ms"));
		setRemoveDelimiter(Arguments.getBoolean(args, isRemoveDelimiter(), "-rem"));
		setAutoPackagePath(Arguments.getBoolean(args, isAutoPackagePath(), "-auto"));
	}

	public void saveAs(java.io.File file,StringBuilder buffer) throws Exception {
		if(file == null || buffer == null) return;
		ensureFilePath(file);
		try(java.io.FileOutputStream fileoutput = new java.io.FileOutputStream(file)) {
			java.io.PrintStream ps = new java.io.PrintStream(fileoutput);
			ps.print(buffer);
			Console.out.println("saving file as "+file.getAbsolutePath());
		}
	}

	public void saveAs(String filename,StringBuilder buffer) throws Exception {
		saveAs(new java.io.File(filename),buffer);		
	}

	public void setAutoPackagePath(boolean autoPackagePath) {
		this.autoPackagePath = autoPackagePath;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}
	
	public void setDriverName(String driver) {
		this.driverName = driver;
	}
	
	public void setOutfileName(String outfileName) {
		this.outfileName = outfileName;
	}
	
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setRemoveDelimiter(boolean removeDelimiter) {
		this.removeDelimiter = removeDelimiter;
	}
	
	public void setSection(String section) {
		this.section = section;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}	

	public void setUrlName(String url) {
		this.urlName = url;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
}
