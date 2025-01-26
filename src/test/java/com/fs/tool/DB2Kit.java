package com.fs.tool;

import com.fs.dev.Arguments;
import com.fs.dev.Console;

/**
 * DB2Kit class implements for generate service provider interface classes
 * 
 * @author tassan_oro@freewillsolutions.com
 */
public class DB2Kit extends DB2Prog {
	private String serviceName = null;
	private String serviceClass = null;
	
	public DB2Kit() {
		super();
		setPackageName(null);
		Console.out.println(getClass().getName()+" "+getVersion());		
	}

	public static void main(String[] args) {
		//java com/fs/tool/DB2Kit -d com.mysql.cj.jdbc.Driver -l jdbc:mysql://127.0.0.1:3306/servedb -u root -p root -o D:\workspace -t sampling -svc sampling
		//java com/fs/tool/DB2Kit -ms PROMPT -o D:\workspace -t sampling -svc sampling
		try {
			if(args.length>0) {		
				if(Arguments.getBoolean(args, "-?", "-h")) {
					usage();
					System.exit(0);					
				}
				DB2Kit kit = new DB2Kit();
				kit.readArguments(args);
				kit.execute();
			} else {
				usage();
			}
	    } catch (Exception ex) {
	        Console.out.print(ex);
	    }
	}

	public static void usage() {
		Console.out.println("USAGE: "+DB2Kit.class);
		usageInfo();
		Console.out.println("\t-svc,-service service name");
		Console.out.println("\t-sc,-sclass service class name");
	}

	public void createClass() throws Exception {
		setPackageName(parsePackageName());
		validate();
		DB2Pojo p = new DB2Pojo();
		p.obtain(this);
		p.validate();
		p.execute();
		p.save();
		String serviceClassName = getServiceClass();
		String entityClass = p.getClassName();
		String modelClass = entityClass+"Model";
		String handlerClass = serviceClassName+"Handler";
		String serviceClass = serviceClassName+"Service";
		StringBuilder mbuf = createModelClass(modelClass,entityClass);
		StringBuilder hbuf = createHandlerClass(handlerClass,modelClass,entityClass);
		StringBuilder sbuf = createServiceClass(serviceClass,modelClass,entityClass,handlerClass);
		String packing = getPackagePath();
		String modelfile = getDirectory()+java.io.File.separator+(packing==null?"":packing+java.io.File.separator)+modelClass+".java";
		String handlerfile = getDirectory()+java.io.File.separator+(packing==null?"":packing+java.io.File.separator)+handlerClass+".java";
		String servicefile = getDirectory()+java.io.File.separator+(packing==null?"":packing+java.io.File.separator)+serviceClass+".java";
		saveAs(modelfile,mbuf);
		saveAs(handlerfile,hbuf);
		saveAs(servicefile,sbuf);
	}
	
	public StringBuilder createHandlerClass(String className,String modelName,String entityName) throws Exception {
		StringBuilder buffer = new StringBuilder();
		buffer.append("package ").append(getPackageName()).append(";\n\n");
		buffer.append("import com.fs.db.OperateHandler;\n");
		buffer.append("import com.fs.db.util.DbColumnSetting;\n");
		buffer.append("import com.fs.spi.DataSet;\n");
		buffer.append("import com.fs.spi.DataTable;\n");
		buffer.append("import com.fs.spi.RecordSet;\n");
		buffer.append("import com.fs.bean.misc.ExecuteStatement;\n");
		buffer.append("import io.datatree.Tree;\n\n");
		buffer.append("@SuppressWarnings({\"serial\",\"unchecked\",\"rawtypes\",})\n");
		buffer.append("public class ").append(className).append(" extends OperateHandler {\n");
		buffer.append("\tprivate ").append(modelName).append(" model = new ").append(modelName).append("();\n\n");
		buffer.append("\tpublic ").append(className).append("() {\n");
		buffer.append("\t\tsuper();\n");
		buffer.append("\t}\n\n");

		buffer.append("\t@Override\n");
		buffer.append("\tprotected void assignParameters(Tree params, ExecuteStatement sql, String action, String mode) {\n");
		buffer.append("\t\t//for override\n");
		buffer.append("\t}\n\n");
		
		buffer.append("\t@Override\n");
		buffer.append("\tprotected ExecuteStatement buildFilterQuery(Tree params, ExecuteStatement sql, String selector, String action, String subaction, java.util.List<DbColumnSetting> settings) throws Exception {\n");
		buffer.append("\t\treturn super.buildFilterQuery(params, sql, selector, action, subaction, settings);\n");
		buffer.append("\t}\n\n");
		
		buffer.append("\t@Override\n");
		buffer.append("\tpublic DataTable doCategories(Tree params) throws Exception {\n");
		buffer.append("\t\treturn createDataTable(\"categories\",null,new java.util.LinkedHashMap<>());\n");
		buffer.append("\t}\n\n");

		buffer.append("\t@Override\n");
		buffer.append("\tpublic RecordSet<").append(entityName).append("> doCollecting(Tree params) throws Exception {\n");
		buffer.append("\t\treturn super.doFinding(params, ").append(entityName).append(".class);\n");
		buffer.append("\t}\n\n");

		buffer.append("\t@Override\n");
		buffer.append("\tpublic RecordSet<").append(entityName).append("> doDeleting(Tree params) throws Exception {\n");
		buffer.append("\t\treturn super.doClearing(params);\n");
		buffer.append("\t}\n\n");

		buffer.append("\t@Override\n");
		buffer.append("\tpublic RecordSet<").append(entityName).append("> doInserting(Tree params) throws Exception {\n");
		buffer.append("\t\treturn super.doCreating(params);\n");
		buffer.append("\t}\n\n");
		
		buffer.append("\t@Override\n");
		buffer.append("\tpublic DataSet doRetrieving(Tree params) throws Exception {\n");
		buffer.append("\t\treturn recordNotFound();\n");
		buffer.append("\t}\n\n");

		buffer.append("\t@Override\n");
		buffer.append("\tpublic RecordSet<").append(entityName).append("> doUpdating(Tree params) throws Exception {\n");
		buffer.append("\t\treturn super.doUpdating(params);\n");
		buffer.append("\t}\n\n");
		
		buffer.append("\t@Override\n");
		buffer.append("\tpublic ").append(modelName).append(" getModel() { \n");
		buffer.append("\t\treturn model;\n");
		buffer.append("\t}\n\n");				
		buffer.append("}\n");
		return buffer;		
	}

	public StringBuilder createModelClass(String className,String entityName) throws Exception {
		StringBuilder buffer = new StringBuilder();
		buffer.append("package ").append(getPackageName()).append(";\n\n");
		buffer.append("import com.fs.db.DbModel;\n\n");
		buffer.append("@SuppressWarnings(\"serial\")\n");
		buffer.append("public class ").append(className).append(" extends DbModel {\n");
		buffer.append("\tpublic ").append(className).append("() {\n");
		buffer.append("\t\tsuper(\"").append(getTableName()).append("\");\n");
		buffer.append("\t}\n");
		buffer.append("\t@Override\n");
		buffer.append("\tpublic Class<").append(entityName).append("> getFields() { \n");
		buffer.append("\t\treturn ").append(entityName).append(".class;\n");
		buffer.append("\t}\n");				
		buffer.append("}\n");
		return buffer;
	}
	
	public StringBuilder createServiceClass(String className,String modelName,String entityName,String handlerName) throws Exception {
		StringBuilder buffer = new StringBuilder();
		buffer.append("package ").append(getPackageName()).append(";\n\n");
		buffer.append("import com.fs.api.BaseServicer;\n");
		buffer.append("import com.fs.spi.DataSet;\n");
		buffer.append("import com.fs.spi.DataTable;\n");
		buffer.append("import com.fs.spi.RecordSet;\n");
		buffer.append("import io.datatree.Tree;\n");
		buffer.append("import services.moleculer.context.Context;\n\n");
		buffer.append("@SuppressWarnings({\"unchecked\",\"rawtypes\"})\n");
		buffer.append("public class ").append(className).append(" extends BaseServicer {\n\n");
		buffer.append("\tpublic ").append(className).append("() {\n");
		buffer.append("\t\tsuper(\"").append(getServiceName()).append("\");\n");
		buffer.append("\t}\n\n");
		
		buffer.append("\t@Override\n");
		buffer.append("\tpublic DataTable doCategories(Context context, Tree params) throws Exception {\n");
		buffer.append("\t\tinfo(\"categories:: context: \"+context);\n");
		buffer.append("\t\t").append(handlerName).append(" handler = new ").append(handlerName).append("();\n");
		buffer.append("\t\thandler.init(this,params);\n");
		buffer.append("\t\treturn handler.categories(params);\n");
		buffer.append("\t}\n\n");

		buffer.append("\t@Override\n");
		buffer.append("\tpublic RecordSet<").append(entityName).append("> doCollect(Context context, Tree params) throws Exception {\n");
		buffer.append("\t\tinfo(\"collect:: context: \"+context);\n");
		buffer.append("\t\t").append(handlerName).append(" handler = new ").append(handlerName).append("();\n");
		buffer.append("\t\thandler.init(this,params);\n");
		buffer.append("\t\treturn handler.collect(params);\n");
		buffer.append("\t}\n\n");

		buffer.append("\t@Override\n");
		buffer.append("\tpublic RecordSet<").append(entityName).append("> doDelete(Context context, Tree params) throws Exception {\n");
		buffer.append("\t\tinfo(\"delete:: context: \"+context);\n");
		buffer.append("\t\t").append(handlerName).append(" handler = new ").append(handlerName).append("();\n");
		buffer.append("\t\thandler.init(this,params);\n");
		buffer.append("\t\treturn handler.delete(params);\n");
		buffer.append("\t}\n\n");

		buffer.append("\t@Override\n");
		buffer.append("\tpublic RecordSet<").append(entityName).append("> doInsert(Context context, Tree params) throws Exception {\n");
		buffer.append("\t\tinfo(\"insert:: context: \"+context);\n");
		buffer.append("\t\t").append(handlerName).append(" handler = new ").append(handlerName).append("();\n");
		buffer.append("\t\thandler.init(this,params);\n");
		buffer.append("\t\treturn handler.insert(params);\n");
		buffer.append("\t}\n\n");
		
		buffer.append("\t@Override\n");
		buffer.append("\tpublic DataSet doRetrieve(Context context, Tree params) throws Exception {\n");
		buffer.append("\t\tinfo(\"retrieve:: context: \"+context);\n");
		buffer.append("\t\t").append(handlerName).append(" handler = new ").append(handlerName).append("();\n");
		buffer.append("\t\thandler.init(this,params);\n");
		buffer.append("\t\treturn handler.retrieve(params);\n");
		buffer.append("\t}\n\n");

		buffer.append("\t@Override\n");
		buffer.append("\tpublic RecordSet<").append(entityName).append("> doUpdate(Context context, Tree params) throws Exception {\n");
		buffer.append("\t\tinfo(\"update:: context: \"+context);\n");
		buffer.append("\t\t").append(handlerName).append(" handler = new ").append(handlerName).append("();\n");
		buffer.append("\t\thandler.init(this,params);\n");
		buffer.append("\t\treturn handler.update(params);\n");
		buffer.append("\t}\n\n");
		
		buffer.append("}\n");
		return buffer;		
	}
	
	public void execute() throws Exception {
		setConnection(createConnection());
		Console.out.println("generating ...");
		generate();
	}
		
	public String fetchVersion() {
		return DB2Kit.class+"="+getVersion()+"\n";
	}
	
	public void generate() throws Exception {
		createClass();
	}
	
	public String getServiceClass() {
		if(serviceClass==null || serviceClass.trim().length()==0) {
			return parseClassName(getServiceName());
		}
		return serviceClass;
	}
	
	public String getServiceName() {
		return serviceName;
	}
	
	public String getVersion() {
		return "$Revision: FS-20241002-084000 $";
	}
	
	public String parsePackageName() {
		String name = getPackageName();
		if(name != null && name.trim().length() > 0) return name;		
		return "com.fs.serve."+getServiceName().toLowerCase();		
	}

	@Override
	public void readArguments(String[] args) {
		if(args==null) return;
		super.readArguments(args);
		setServiceName(Arguments.getString(args, getServiceName(), "-svc","-service"));		
		setServiceClass(Arguments.getString(args, getServiceClass(), "-sc","-sclass"));		
	}
	
	public void setServiceClass(String serviceClass) {
		this.serviceClass = serviceClass;
	}
	
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void validate() throws Exception {
		if(getServiceName() == null || getServiceName().trim().length() == 0) throw new java.sql.SQLException("Service name is undefined","servicename");
	}
	
}
