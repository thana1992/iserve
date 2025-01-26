package com.fs.serve.demo003;

import com.fs.api.BaseServicer;
import com.fs.spi.DataSet;
import com.fs.spi.DataTable;
import com.fs.spi.RecordSet;
import io.datatree.Tree;
import services.moleculer.context.Context;

@SuppressWarnings({"unchecked","rawtypes"})
public class Demo003Service extends BaseServicer {

	public Demo003Service() {
		super("demo003");
	}

	@Override
	public DataTable doCategories(Context context, Tree params) throws Exception {
		info("categories:: context: "+context);
		Demo003Handler handler = new Demo003Handler();
		handler.init(this,params);
		return handler.categories(params);
	}

	@Override
	public RecordSet<Jebo> doCollect(Context context, Tree params) throws Exception {
		info("collect:: context: "+context);
		Demo003Handler handler = new Demo003Handler();
		handler.init(this,params);
		return handler.collect(params);
	}

	@Override
	public RecordSet<Jebo> doDelete(Context context, Tree params) throws Exception {
		info("delete:: context: "+context);
		Demo003Handler handler = new Demo003Handler();
		handler.init(this,params);
		return handler.delete(params);
	}

	@Override
	public RecordSet<Jebo> doInsert(Context context, Tree params) throws Exception {
		info("insert:: context: "+context);
		Demo003Handler handler = new Demo003Handler();
		handler.init(this,params);
		return handler.insert(params);
	}

	@Override
	public DataSet doRetrieve(Context context, Tree params) throws Exception {
		info("retrieve:: context: "+context);
		Demo003Handler handler = new Demo003Handler();
		handler.init(this,params);
		return handler.retrieve(params);
	}

	@Override
	public RecordSet<Jebo> doUpdate(Context context, Tree params) throws Exception {
		info("update:: context: "+context);
		Demo003Handler handler = new Demo003Handler();
		handler.init(this,params);
		return handler.update(params);
	}

}
