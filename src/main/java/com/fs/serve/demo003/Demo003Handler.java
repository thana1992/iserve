package com.fs.serve.demo003;

import com.fs.db.OperateHandler;
import com.fs.db.util.DbColumnSetting;
import com.fs.spi.DataSet;
import com.fs.spi.DataTable;
import com.fs.spi.RecordSet;

import java.util.Date;

import com.fs.bean.misc.ExecuteStatement;
import com.fs.bean.misc.Trace;

import io.datatree.Tree;

@SuppressWarnings({"serial","unchecked","rawtypes",})
public class Demo003Handler extends OperateHandler {
	private JeboJebdJebofModel model = new JeboJebdJebofModel();

	public Demo003Handler() {
		super();
	}

	@Override
	protected void assignParameters(Tree params, ExecuteStatement sql, String action, String mode) {
		//for override
	}

	@Override
	protected ExecuteStatement buildFilterQuery(Tree params, ExecuteStatement sql, String selector, String action, String subaction, java.util.List<DbColumnSetting> settings) throws Exception {
		if(isCollectMode(action)) {
			Date orderdate = params.get("orderdate", (Date)null);
			String sharecode = params.get("sharecode", (String)null);
			String side = params.get("side", (String)null);
			String shortsale = params.get("shortsale", (String)null);
			Trace.info("subaction: "+subaction);
			if("count".equalsIgnoreCase(subaction)) {
				sql.append(selector);
				sql.append(" from jebo a,jebd b,jebof c \n");
				sql.append(" where a.suborderno = b.suborderno \n");
				sql.append(" and a.orderseqno = b.orderseqno \n");
				sql.append(" and a.suborderno = c.suborderno \n");
				sql.append(" and a.xchgmkt = '7' \n");
				sql.append(" and b.xchgmkt = '7' \n");
				sql.append(" and a.delflag = '0' \n");
				sql.append(" and b.delflag = '0' \n");
			} else if("find".equalsIgnoreCase(subaction)) {
				sql.clear();
				sql.append("select a.suborderno,b.confirmno,a.side,a.shortsale,a.account, \n"
						+ "a.sharecode,a.confirmunit,a.price,a.ordertime,b.confirmtime, \n"
						+ "a.servicetype,a.invalidcause,c.dealno,c.trdno,a.orderseqno, \n"
						+ "c.asofdate,c.instrumenttype,c.asoftime,c.invtype,c.orgaccount \n"
						+ ",c.trdparti, c.trdaccount \n");
				sql.append(" from jebo a,jebd b,jebof c \n");
				sql.append(" where a.suborderno = b.suborderno \n");
				sql.append(" and a.orderseqno = b.orderseqno \n");
				sql.append(" and a.suborderno = c.suborderno \n");
				sql.append(" and a.xchgmkt = '7' \n");
				sql.append(" and b.xchgmkt = '7' \n");
				sql.append(" and a.delflag = '0' \n");
				sql.append(" and b.delflag = '0' \n");
				if(orderdate!=null) {
					sql.append(" and a.orderdate = ?orderdate \n");
					sql.setParameter("orderdate", orderdate);
				}
				if(sharecode!=null && sharecode.trim().length() > 0) {
					sql.append(" and a.sharecode = ?sharecode \n");
					sql.setParameter("sharecode", sharecode);
				}
				if(side!=null && side.trim().length() > 0) {
					sql.append(" and a.side = ?side \n");
					sql.setParameter("side", side);
				}
				if(shortsale!=null && shortsale.trim().length() > 0) {
					sql.append(" and a.shortsale = ?shortsale \n");
					if("O".equalsIgnoreCase(shortsale)) {
						sql.setParameter("shortsale", "0");
					}else if("C".equalsIgnoreCase(shortsale)) {
						sql.setParameter("shortsale", "1");
					}
				}
			}
			return sql;
		}
		return super.buildFilterQuery(params, sql, selector, action, subaction, settings);
	}

	@Override
	public DataTable doCategories(Tree params) throws Exception {
		return createDataTable("categories",null,new java.util.LinkedHashMap<>());
	}

	@Override
	public RecordSet<JeboJebdJebof> doCollecting(Tree params) throws Exception {
		return super.doFinding(params, JeboJebdJebof.class);
	}

	@Override
	public RecordSet<JeboJebdJebof> doDeleting(Tree params) throws Exception {
		return super.doClearing(params);
	}

	@Override
	public RecordSet<JeboJebdJebof> doInserting(Tree params) throws Exception {
		return super.doCreating(params);
	}

	@Override
	public DataSet doRetrieving(Tree params) throws Exception {
		return recordNotFound();
	}

	@Override
	public RecordSet<Jebo> doUpdating(Tree params) throws Exception {
		return super.doUpdating(params);
	}

	@Override
	public JeboJebdJebofModel getModel() { 
		return model;
	}

}
