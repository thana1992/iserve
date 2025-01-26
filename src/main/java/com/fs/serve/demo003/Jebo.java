package com.fs.serve.demo003;

import com.fs.db.interfaces.FieldSet;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fs.db.interfaces.DbColumn;
import com.fs.db.interfaces.DbTable;
import lombok.Data;

@SuppressWarnings("serial")
@Data
@DbTable(name="jebo")
public class Jebo implements FieldSet, java.io.Serializable {

	@DbColumn(name="userid", type=java.sql.Types.VARCHAR)
	private String userid;

	@DbColumn(name="userteamid", type=java.sql.Types.VARCHAR)
	private String userteamid;

	@DbColumn(name="userbranch", type=java.sql.Types.VARCHAR)
	private String userbranch;

	@DbColumn(name="suborderno", type=java.sql.Types.DECIMAL)
	private java.math.BigDecimal suborderno;

	@DbColumn(name="orderseqno", type=java.sql.Types.INTEGER)
	private int orderseqno;

	@DbColumn(name="side", type=java.sql.Types.VARCHAR)
	private String side;

	@DbColumn(name="brokerno", type=java.sql.Types.VARCHAR)
	private String brokerno;

	@DbColumn(name="brokerorderno", type=java.sql.Types.INTEGER)
	private int brokerorderno;

	@DbColumn(name="custacct", type=java.sql.Types.VARCHAR)
	private String custacct;

	@DbColumn(name="custcode", type=java.sql.Types.VARCHAR)
	private String custcode;

	@DbColumn(name="account", type=java.sql.Types.VARCHAR)
	private String account;

	@DbColumn(name="sharecode", type=java.sql.Types.VARCHAR)
	private String sharecode;

	@DbColumn(name="price", type=java.sql.Types.DECIMAL)
	private java.math.BigDecimal price;

	@DbColumn(name="openunit", type=java.sql.Types.DECIMAL)
	private java.math.BigDecimal openunit;

	@DbColumn(name="confirmunit", type=java.sql.Types.DECIMAL)
	private java.math.BigDecimal confirmunit;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
	@DbColumn(name="orderdate", type=java.sql.Types.DATE)
	private java.sql.Date orderdate;

	@DbColumn(name="ordertime", type=java.sql.Types.TIME)
	private java.sql.Time ordertime;

	@DbColumn(name="orderstatus", type=java.sql.Types.VARCHAR)
	private String orderstatus;

	@DbColumn(name="life", type=java.sql.Types.VARCHAR)
	private String life;

	@DbColumn(name="s_condition", type=java.sql.Types.VARCHAR)
	private String scondition;

	@DbColumn(name="brokerport", type=java.sql.Types.VARCHAR)
	private String brokerport;

	@DbColumn(name="updflag", type=java.sql.Types.VARCHAR)
	private String updflag;

	@DbColumn(name="delflag", type=java.sql.Types.VARCHAR)
	private String delflag;

	@DbColumn(name="entrytrader", type=java.sql.Types.VARCHAR)
	private String entrytrader;

	@DbColumn(name="appr_userid", type=java.sql.Types.VARCHAR)
	private String appruserid;

	@DbColumn(name="chg_userid", type=java.sql.Types.VARCHAR)
	private String chguserid;

	@DbColumn(name="xchgmkt", type=java.sql.Types.VARCHAR)
	private String xchgmkt;

	@DbColumn(name="putthrough", type=java.sql.Types.VARCHAR)
	private String putthrough;

	@DbColumn(name="trxbranch", type=java.sql.Types.VARCHAR)
	private String trxbranch;

	@DbColumn(name="trxmktid", type=java.sql.Types.VARCHAR)
	private String trxmktid;

	@DbColumn(name="trxteamid", type=java.sql.Types.VARCHAR)
	private String trxteamid;

	@DbColumn(name="shortsale", type=java.sql.Types.VARCHAR)
	private String shortsale;

	@DbColumn(name="servicetype", type=java.sql.Types.VARCHAR)
	private String servicetype;

	@DbColumn(name="invalidcause", type=java.sql.Types.VARCHAR)
	private String invalidcause;

	public void fetchResult(java.sql.ResultSet rs) throws Exception {
		//for calculate field
	}

}
