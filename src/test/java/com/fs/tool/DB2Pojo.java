package com.fs.tool;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import com.fs.dev.Arguments;
import com.fs.dev.Console;

/**
 * DB2Pojo class implements for generate java class from database table
 * 
 * @author tassan_oro@freewillsolutions.com
 */
public class DB2Pojo extends DB2Prog {

	private ArrayList<DB2Column> flist = null;
	private StringBuilder outputBuffer;
	
	public DB2Pojo() {
		super();
		Console.out.println(getClass().getName()+" "+getVersion());
	}
	public static void main(String[] args) {
		//java com/fs/tool/DB2Pojo -d com.mysql.cj.jdbc.Driver -l jdbc:mysql://127.0.0.1:3306/servedb -u root -p root -o D:\workspace -t sampling
		try {
			if(args.length>0) {		
				if(Arguments.getBoolean(args, "-?", "-h")) {
					usage();
					System.exit(0);					
				}
		        String asfile = Arguments.getString(args, null, "-s","-save");
				DB2Pojo db2 = new DB2Pojo();
				db2.readArguments(args);
				db2.execute();
				if((asfile!=null) && asfile.trim().length() > 0) {
	        		db2.saveAs(db2.getDirectory()+asfile);
				} else {
					db2.save();
				}
			} else {
				usage();
			}
	    } catch (Exception ex) {
	        Console.out.print(ex);
	    }
	}
	public static void usage() {
		Console.out.println("USAGE: "+DB2Pojo.class);
		usageInfo();
		Console.out.println("\t-s,-save save as file name");
	}
	public void createClass() throws Exception {
		if(flist==null) process(getConnection());
		boolean foundFormat = false;
		outputBuffer = new StringBuilder();
		StringBuilder buf1 = new StringBuilder();
		StringBuilder buf2 = new StringBuilder();
		for(DB2Column dbc : flist) {
			if(dbc != null) {
				ColumnStruct cs = createColumnStruct(dbc);
				if(cs.foundFormat) foundFormat = true;
				buf2.append("\n").append(cs.dataContents).append("\n");
			}
		}
		buf1.append("package ").append(getPackageName()).append(";\n\n");
		buf1.append("import com.fs.db.interfaces.FieldSet;\n");
		if(foundFormat) buf1.append("import com.fasterxml.jackson.annotation.JsonFormat;\n");
		buf1.append("import com.fs.db.interfaces.DbColumn;\n");
		buf1.append("import com.fs.db.interfaces.DbTable;\n");
		buf1.append("import lombok.Data;\n\n");
		buf1.append("@SuppressWarnings(\"serial\")\n");
		buf1.append("@Data\n");
		buf1.append("@DbTable(name=\"").append(getTableName()).append("\")\n");
		buf1.append("public class ").append(getClassName()).append(" implements FieldSet, java.io.Serializable {\n");
		buf1.append(buf2.toString()).append("\n");
		buf1.append("\tpublic void fetchResult(java.sql.ResultSet rs) throws Exception {\n");
		buf1.append("\t\t//for calculate field\n");
		buf1.append("\t}\n\n");
		buf1.append("}\n");
		outputBuffer.append(buf1);
	}
	public ColumnStruct createColumnStruct(DB2Column dbc) {
		boolean found = false;
		StringBuilder buf = new StringBuilder();
		if(dbc.getColumnType() == java.sql.Types.DATE) {
			found = true;
			buf.append("\t@JsonFormat(shape = JsonFormat.Shape.STRING, pattern=\"").append(getDatePattern()).append("\")\n");
		}
		buf.append("\t@DbColumn(name=\"").append(dbc.getColumnName()).append("\", type=").append(getSQLType(dbc.getColumnType()));
		if(dbc.isKeyField()) {
			buf.append(", key=true)\n");
		} else {
			buf.append(")\n");
		}
		String colname = dbc.getColumnName();
		if(isRemoveDelimiter()) colname = clearingDelimiter(colname);
		buf.append("\tprivate ").append(getDataType(dbc.getColumnType())).append(" ").append(colname).append(";");
		return new ColumnStruct(buf.toString(),found);
	}
	public void execute() throws Exception {
		validate();
		setConnection(createConnection());
		Console.out.println("generating ...");
		generate();
	}
	public String fetchVersion() {
		return DB2Pojo.class+"="+getVersion()+"\n";
	}
	public void generate() throws Exception {
		process();
		createClass();
	}
	public StringBuilder getOutputBuffer() {
		return outputBuffer;
	}
	public String getVersion() {
		return "$Revision: FS-20240827-120700 $";
	}
	public void process() throws Exception {
		process(getConnection());
	}
	public void process(Connection connection) throws Exception {
		if(connection==null) return;
		flist = new ArrayList<>();
		java.util.Map<String,String> khat = new HashMap<>();	
		try {
			DatabaseMetaData dmet = connection.getMetaData();
			try(ResultSet krs = dmet.getPrimaryKeys(connection.getCatalog(),"",getTableName())) {
				while(krs.next()) {
					String colname = krs.getString("COLUMN_NAME");
					khat.put(colname,colname);
				}
			}
		} catch(Exception ex) {
			Console.out.print(ex);
		}
		java.util.Map<String,DB2Column> dbmap = new java.util.HashMap<>();
		java.util.Map<String,String> dbcmap = new java.util.LinkedHashMap<>();
		try {	
			DatabaseMetaData dmet = connection.getMetaData();
			try(ResultSet crs = dmet.getColumns(connection.getCatalog(),"",getTableName(),"%")) {
				ResultSetMetaData met = crs.getMetaData();
				int colcount = met.getColumnCount();
				for(int i=1;i<=colcount;i++) {
					String colname = met.getColumnName(i);
					colname = colname.toUpperCase();
					dbcmap.put(colname,colname);
				}
				while(crs.next()) {
					String colname = crs.getString("COLUMN_NAME");
					int coltype = crs.getInt("DATA_TYPE");
					DB2Column dbc = new DB2Column(colname,coltype);
					if(khat.get(colname)!=null) dbc.setKeyField(true);
					flist.add(dbc);
					String defaultValue = null;
					String auto = null;
					if(dbcmap.get("COLUMN_DEF")!=null) {
						defaultValue = crs.getString("COLUMN_DEF");
					}
					if(dbcmap.get("IS_AUTOINCREMENT")!=null) {
						auto = crs.getString("IS_AUTOINCREMENT");					
					}
					dbc.setDefaultValue(defaultValue);
					dbc.setAutoIncrement("YES".equalsIgnoreCase(auto) || "TRUE".equalsIgnoreCase(auto));
					//Console.out.println(colname+": default="+defaultValue+", auto="+auto+","+dbc.isAutoIncrement());
					dbmap.put(colname,dbc);
				}
				
			}
		} catch(Exception ex) {
			Console.out.print(ex);
		}
		if(flist.isEmpty()) {
			try(ResultSet rs = connection.createStatement().executeQuery("select * from "+getTableName())) {
				ResultSetMetaData met = rs.getMetaData();
				int colcount = met.getColumnCount();
				for(int i=1;i<=colcount;i++) {
					String colname = met.getColumnName(i);
					String label = met.getColumnLabel(i);
					if(label!=null && label.trim().length()>0 && !colname.equals(label)) colname = label;			
					int coltype = met.getColumnType(i);
					DB2Column dbc = new DB2Column(colname,coltype);
					if(khat.get(colname)!=null) dbc.setKeyField(true);
					dbc.setAutoIncrement(met.isAutoIncrement(i));
					flist.add(dbc);
				}	
			}
		} else {
			if(dbcmap.get("IS_AUTOINCREMENT")==null) {
				try(ResultSet rs = connection.createStatement().executeQuery("select * from "+getTableName())) {
					ResultSetMetaData met = rs.getMetaData();
					int colcount = met.getColumnCount();
					for(int i=1;i<=colcount;i++) {
						String colname = met.getColumnName(i);						
						DB2Column dbc = dbmap.get(colname);
						if(dbc!=null) {
							dbc.setAutoIncrement(met.isAutoIncrement(i));
						}
					}	
				}				
			}
		}
		if(flist.isEmpty()) {
			Console.out.println("Cannot extracting data base schemas ...");
		}
	}
	public void save() throws Exception {
		Console.out.println("package : "+getPackageName());
		String packing = getPackagePath();
		String filename = getDirectory()+java.io.File.separator+(packing==null?"":packing+java.io.File.separator)+getOutfileName();
		saveAs(filename);
	}
	public void saveAs(java.io.File file) throws Exception {
		saveAs(file,getOutputBuffer());
	}
	public void saveAs(String filename) throws Exception {
		saveAs(new java.io.File(filename));		
	}
	public void setOutputBuffer(StringBuilder outputBuffer) {
		this.outputBuffer = outputBuffer;
	}	
	public void validate() throws Exception {
		if(getTableName() == null || getTableName().trim().length() == 0) throw new java.sql.SQLException("Table is undefined","table");		
	}
}
