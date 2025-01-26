package com.fs.serve.demo002;

import java.util.Arrays;
import java.util.stream.Collectors;
import com.fs.spi.DataMapSetting;
import com.fs.spi.DataSet;
import com.fs.spi.DataTable;
import com.fs.spi.DataTableSetting;
import com.fs.spi.HTTP;
import com.fs.spi.RecordSet;
import com.fs.spi.ValidateInfo;
import com.fs.api.VerifyException;
import com.fs.bean.misc.ExecuteStatement;
import com.fs.bean.misc.KnSQL;
import com.fs.bean.util.BeanUtility;
import com.fs.db.OperateHandler;
import com.fs.db.DbOperation;
import com.fs.db.handler.DataTableHandler;
import com.fs.db.util.DbColumnSetting;
import io.datatree.Tree;

/**
 * @author tassan_oro@freewillsolutions.com
 */
@SuppressWarnings({"serial","unchecked","rawtypes"})
public class Demo002Handler extends OperateHandler {
	BeanUtility util = new BeanUtility();
	SamplingModel model = new SamplingModel();

	public Demo002Handler() {
		super();
	}
	
	@Override
    protected void assignParameters(Tree params, ExecuteStatement sql, String action, String mode) {
        debug("assignParameters: action="+action+", mode="+mode);
        if(!DbOperation.COLLECT.equalsIgnoreCase(action)) {
        	java.sql.Date curdate = BeanUtility.getCurrentDate();
        	java.sql.Time curtime = BeanUtility.getCurrentTime();
        	String curuser = getToken() != null ? getToken().getUserid() : null;
            java.sql.Date createdate = util.parseSQLDate(params.get("createdate",(String)null));
            java.sql.Time createtime = util.parseTime(params.get("createtime",(String)null));
            java.sql.Date editdate = util.parseSQLDate(params.get("editdate", (String)null));
            java.sql.Time edittime = util.parseTime(params.get("edittime", (String)null)); 
            String licenses = getParameterArrayValue(params,"licenses");
            String languages = getParameterArrayValue(params,"languages");
            sql.setParameter("licenses",licenses);
            sql.setParameter("languages",languages);
            sql.setParameter("amount",BeanUtility.parseDecimal(params.get("amount",(String)null)));
            sql.setParameter("age",BeanUtility.parseInt(params.get("age",(String)null)));
            sql.setParameter("effectdate",util.parseSQLDate(params.get("effectdate",(String)null)));
            sql.setParameter("effecttime",util.parseTime(params.get("effecttime",(String)null)));
            sql.setParameter("createdate",createdate == null ? curdate : createdate);
            sql.setParameter("createtime",createtime == null ? curtime : createtime);
            sql.setParameter("createuser", curuser);
            sql.setParameter("editdate",editdate == null ? curdate : editdate);
            sql.setParameter("edittime",edittime == null ? curtime : edittime);
            sql.setParameter("edituser", curuser);
            sql.setParameter("assets",BeanUtility.parseInt(params.get("assets",(String)null)));
            sql.setParameter("credit",BeanUtility.parseDecimal(params.get("credit",(String)null)));
        }        
    }
	
	@Override
	protected ExecuteStatement buildFilterQuery(Tree params, ExecuteStatement sql, String selector, String action, String subaction, java.util.List<DbColumnSetting> settings) throws Exception {
        if(isCollectMode(action)) {
            sql.append(selector);
            sql.append(" from ");
            sql.append(getModel().getName());
            String filter = " where ";
            String account = params.get("account", (String)null);
            if(account != null && account.trim().length() > 0) {
                sql.append(filter);
                sql.append(getModel().getName());
                sql.append(".account LIKE ?account ");
                sql.setParameter("account","%"+account+"%");
                filter = " and ";
            }
            String effectdate = params.get("effectdate", (String)null);
            if(effectdate != null && effectdate.trim().length() > 0) {
                java.sql.Date date = util.parseSQLDate(effectdate);
                if(date != null) {
                    sql.append(filter);
                    sql.append(getModel().getName());
                    sql.append(".effectdate = ?effectdate ");
                    sql.setParameter("effectdate",date);
                    filter = " and ";
                }
            }
            java.util.List<String> marrystatus = getParameterList(params,"marrystatus");
            if(marrystatus != null && !marrystatus.isEmpty()) {
            	String status = marrystatus.stream().map(item -> item.trim().length() > 0 ? "'"+item+"'" : "").collect(Collectors.joining(","));
            	if(status.trim().length() > 0) {
	                sql.append(filter);
	                sql.append(getModel().getName());
	                sql.append(".marrystatus IN (");
	                sql.append(status);
	                sql.append(")");
	                filter = " and ";
            	}
            }
            String title = params.get("title", (String)null);
            if(title != null && title.trim().length() > 0) {
                sql.append(filter);
                sql.append(getModel().getName());
                sql.append(".title LIKE ?title ");
                sql.setParameter("title","%"+title+"%");
                filter = " and ";
            }
            return sql;    
        }
        return super.buildFilterQuery(params, sql, selector, action, subaction, settings);
    }
	
	protected RecordSet<Sampling> performRetrieving(java.sql.Connection connection, String account) throws Exception {
    	if(connection == null || account == null || account.trim().length() == 0) return null;
        KnSQL knsql = new KnSQL(this);
        knsql.append("select * from ");
        knsql.append(getModel().getName());
        knsql.append(" where account = ?account ");
        knsql.setParameter("account",account);
        try(java.sql.ResultSet rs = knsql.executeQuery(connection)) {
        	return createRecordSet(rs,Sampling.class);
        }
    }

	@Override
    protected ValidateInfo validateRequireFields(Tree params, String action) throws Exception {
        ValidateInfo vi = validateParameters(params,"account");
        if(!vi.isValid()) {
        	throw new VerifyException("Parameter not found ("+vi.info+")",HTTP.NOT_ACCEPTABLE,-16061);
        }
        return vi;
    }
	
	@Override
	public DataTable doCategories(Tree params) throws Exception {
		DataTableSetting setting1 = new DataTableSetting("kt_marrystatus","statusid");
		setting1.setSetting(new DataMapSetting("statusid","nameen"));
		DataTableSetting setting2 = new DataTableSetting("kt_languages","langid");
		setting2.setSetting(new DataMapSetting("langid","nameen"));	
		DataTableHandler handler = new DataTableHandler();
		handler.init(this);
		//try get settings from setting_config.json
		/*
		java.util.List<DataTableSetting> settings = com.fs.db.util.DataUtils.getSettings(params, getToken(), "tkactive","tkprogtype");
		settings.add(setting1);
		settings.add(setting2);
		java.util.List<com.fs.spi.DataTableResultSet> results = handler.getDataLists(settings);
		*/
		//output entity as map
		/*
		java.util.Map<String,java.util.Map<String,Object>> results = handler.getDataCategories(setting1,setting2);
		if(results != null) {
			return createDataTable("categories",null,new java.util.LinkedHashMap<>(results));
		}
		return createDataTable("categories",null,new java.util.LinkedHashMap<>());
		*/
		//output entity as list		
		java.util.List<com.fs.spi.DataTableResultSet> results = handler.getDataLists(setting1,setting2);
		if(results != null) {
			return createDataTable("categories",null,results);
		}
		return createDataTable("categories",null,new java.util.ArrayList<>());			
	}

	@Override
	public RecordSet<Sampling> doCollecting(Tree params) throws Exception {
		return super.doFinding(params, Sampling.class);
	}

	@Override
	public RecordSet<Sampling> doDeleting(Tree params) throws Exception {		
		return super.doClearing(params);
	}
	
	@Override
	public RecordSet<Sampling> doGetting(Tree params) throws Exception {
		try(java.sql.Connection connection = getPrivateConnection()) {
			String account = params.get("account", (String)null);
			RecordSet<Sampling> result = performRetrieving(connection, account);
			if(result != null && result.hasRows()) {
				return result;
			}
		}
		return recordNotFound();
	}
	
    @Override
	public RecordSet<Sampling> doInserting(Tree params) throws Exception {		
		return super.doCreating(params);
	}

    @Override
	public DataSet<java.util.Map<String,Object>> doRetrieving(Tree params) throws Exception {
		try(java.sql.Connection connection = getPrivateConnection()) {
			String account = params.get("account", (String)null);
			RecordSet<Sampling> result = performRetrieving(connection, account);
			if(result != null && result.hasRows()) {
				//java.util.Map<String,Object> map = objectToMap(result.rows().get(0));
				//if need to convert data then using transformData method
				java.util.Map<String,Object> map = transformData(result.rows().get(0));
				String licenses = (String)map.get("licenses");
				String languages = (String)map.get("languages");
				java.util.List<String> licens = new java.util.ArrayList<>();
				java.util.List<String> langs = new java.util.ArrayList<>();
				if(licenses != null && licenses.trim().length() > 0) {
					licens = Arrays.asList(licenses.split(","));
				}
				if(languages != null && languages.trim().length() > 0) {
					langs = Arrays.asList(languages.split(","));
				}
				map.put("licenses", licens);
				map.put("languages", langs);
				return createDataSet(DbOperation.RETRIEVE,map);
			}
		}
		return recordNotFound();
	}
    
	@Override
	public RecordSet<Sampling> doUpdating(Tree params) throws Exception {		
		return super.doUpdating(params);
	}
	
	public RecordSet<Sampling> findBy(Tree params) throws Exception {
		try(java.sql.Connection connection = getPrivateConnection()) {
			String account = params.get("account", (String)null);
			return performRetrieving(connection, account);
		}
	}
	
	@Override
	public SamplingModel getModel() { return model; }
	
}
