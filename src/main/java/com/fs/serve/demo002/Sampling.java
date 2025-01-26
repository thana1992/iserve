package com.fs.serve.demo002;

import com.fs.db.interfaces.FieldSet;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fs.db.interfaces.DbColumn;
import com.fs.db.interfaces.DbTable;
import lombok.Data;

/**
 * Sampling class implement for handle data table sampling
 * 
 * @author tassan_oro@freewillsolutions.com
 */

@SuppressWarnings("serial")
@Data
@DbTable(name="sampling")
public class Sampling implements FieldSet, java.io.Serializable {
	
	@DbColumn(name="account", type=java.sql.Types.VARCHAR, key=true)
	private String account = null;
	
	@DbColumn(name="amount", type=java.sql.Types.DECIMAL)
	private java.math.BigDecimal amount = null;
	
	@DbColumn(name="age", type=java.sql.Types.INTEGER)
	private int age;
	
	@DbColumn(name="gender", type=java.sql.Types.VARCHAR)
	private String gender;
	
	@DbColumn(name="domestic", type=java.sql.Types.VARCHAR)
	private String domestic;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
	@DbColumn(name="effectdate", type=java.sql.Types.DATE)
	private java.sql.Date effectdate;
	
	@DbColumn(name="effecttime", type=java.sql.Types.TIME)
	private java.sql.Time effecttime;
	
	@DbColumn(name="pincode", type=java.sql.Types.VARCHAR)
	private String pincode;
	
	@DbColumn(name="marrystatus", type=java.sql.Types.VARCHAR)
	private String marrystatus;
	
	@DbColumn(name="licenses", type=java.sql.Types.VARCHAR)
	private String licenses;
	
	@DbColumn(name="languages", type=java.sql.Types.VARCHAR)
	private String languages;
	
	@DbColumn(name="remark", type=java.sql.Types.VARCHAR)
	private String remark;
	
	@DbColumn(name="title", type=java.sql.Types.VARCHAR)
	private String title;
	
	@DbColumn(name="caption", type=java.sql.Types.VARCHAR)
	private String caption;
	
	@DbColumn(name="assets", type=java.sql.Types.INTEGER)
	private int assets;
	
	@DbColumn(name="credit", type=java.sql.Types.DECIMAL)
	private java.math.BigDecimal credit;
	
	@DbColumn(name="passcode", type=java.sql.Types.VARCHAR)
	private String passcode;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
	@DbColumn(name="createdate", type=java.sql.Types.DATE, selected=false, updated=false)
	private java.sql.Date createdate;
	
	@DbColumn(name="createtime", type=java.sql.Types.TIME, selected=false, updated=false)
	private java.sql.Time createtime;
	
	@DbColumn(name="createuser", type=java.sql.Types.VARCHAR, selected=false, updated=false)
	private String createuser;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
	@DbColumn(name="editdate", type=java.sql.Types.DATE, selected=false)
	private java.sql.Date editdate;
	
	@DbColumn(name="edittime", type=java.sql.Types.TIME, selected=false)
	private java.sql.Time edittime;
	
	@DbColumn(name="edituser", type=java.sql.Types.VARCHAR, selected=false)
	private String edituser;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
	@DbColumn(name="curtime", type=java.sql.Types.TIMESTAMP, selected=false)
	private java.sql.Timestamp curtime;
	
	public void fetchResult(java.sql.ResultSet rs) throws Exception {
		//for calculate field
	}
	
}
