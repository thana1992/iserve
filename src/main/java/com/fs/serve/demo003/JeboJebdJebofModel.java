package com.fs.serve.demo003;

import com.fs.db.DbModel;

@SuppressWarnings("serial")
public class JeboJebdJebofModel extends DbModel {
	public JeboJebdJebofModel() {
		super("jebo");
		getAlias().privateAlias("SBA"); // select connection by SBA alias
	}
	@Override
	public Class<JeboJebdJebof> getFields() { 
		return JeboJebdJebof.class;
	}
}
