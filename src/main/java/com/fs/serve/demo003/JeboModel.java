package com.fs.serve.demo003;

import com.fs.db.DbModel;

@SuppressWarnings("serial")
public class JeboModel extends DbModel {
	public JeboModel() {
		super("jebo");
		getAlias().privateAlias("SBA"); // select connection by SBA alias
	}
	@Override
	public Class<Jebo> getFields() { 
		return Jebo.class;
	}
}
