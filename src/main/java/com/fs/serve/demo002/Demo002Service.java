package com.fs.serve.demo002;

import com.fs.api.BaseServicer;
import com.fs.bean.misc.Trace;
import com.fs.db.handler.PermitHandler;
import com.fs.spi.DataSet;
import com.fs.spi.DataTable;
import com.fs.spi.RecordSet;
import io.datatree.Tree;
import services.moleculer.context.Context;
import services.moleculer.service.Action;

/**
 * @author tassan_oro@freewillsolutions.com
 */
@SuppressWarnings({"unchecked","rawtypes","unused"})
public class Demo002Service extends BaseServicer {

	//example add more user defined action
	Action findby = ctx -> {
		if(isOptional(ctx)) return RecordSet.emptyMap();
		time("findby");
        try {
			info("@@findby");
			log(ctx,"findby");
	        validate(ctx);
	        Object info = findby(ctx);
	        if(info != null) {
	            return response(ctx,info);
	        } else {
	        	return response(ctx,notfound(ctx,"findby"));
	        }
        } catch(Exception ex) {
            Trace.error(this,ex);
            return handleException(ex, ctx);
        } finally {
        	timeEnd("findby");
        }
	};
	
	public Demo002Service() {
		super("demo002");
	}

	@Override
	protected java.util.Map<String,java.util.Map<String,Boolean>> doExecute(Context context, Tree params) throws Exception {
		String progid = "demo002";
		params.put("progid",progid).put("userid", "tso");
		//PermitHandler handler = new PermitHandler();
		//handler.init(this, params);
		//return handler.get(params);
		java.util.Map<String,java.util.Map<String,Boolean>> result = new java.util.LinkedHashMap<>();
		Tree response = this.call("permit.get", params).waitFor();
		debug("reponse: "+response);
		//ex. response = { "demo002" : { "all" : true, "delete" : true, "export" : true, "import" : true, "insert" : true, "retrieve" : true, "update" : true, "launch" : true } }
		if(response != null) {
			Tree prog = response.get(progid);
			if(prog != null) {
				java.util.Map<String,Boolean> permit = new java.util.LinkedHashMap<>();
				prog.forEach((Tree node) -> { permit.put(node.getName(), prog.get(node.getName(),false)); });
				result.put(progid, permit);
			}
		}
		return result; 
	}
	
	protected RecordSet<Sampling> doFindby(Context context, Tree params) throws Exception {
		info("findby:: context: "+context);
		Demo002Handler handler = new Demo002Handler();
		handler.init(this,params);
		return handler.findBy(params);
	}	
	
	@Override
	public DataTable doCategories(Context context, Tree params) throws Exception {
		//call /demo002/categories
		info("categories:: context: "+context);
		Demo002Handler handler = new Demo002Handler();
		handler.init(this, params);
		return handler.categories(params);
	}	

	@Override
	public RecordSet<Sampling> doCollect(Context context, Tree params) throws Exception {
		//call /demo002/collect with filter criteria
		info("collect:: context: "+context);
		Demo002Handler handler = new Demo002Handler();
		handler.init(this, params);
		return handler.collect(params);
	}
		
	@Override
	public RecordSet<Sampling> doDelete(Context context, Tree params) throws Exception {
		//call /demo002/delete?account=x
		info("delete:: context: "+context);
		Demo002Handler handler = new Demo002Handler();
		handler.init(this, params);
		return handler.delete(params);
	}
		
	@Override
	public RecordSet<java.util.Map<String,Object>> doFind(Context context, Tree params) throws Exception {
		//call /demo002/find with filter criteria
		info("find:: context: "+context);
		Demo002Handler handler = new Demo002Handler();
		handler.init(this, params);
		return handler.find(params);
	}

	public RecordSet<Sampling> doGet(Context context, Tree params) throws Exception {
		//call /demo002/get?account=x with data contents
		info("get:: context: "+context);
		Demo002Handler handler = new Demo002Handler();
		handler.init(this, params);
		return handler.get(params);	
	}

	@Override
	public RecordSet<Sampling> doInsert(Context context, Tree params) throws Exception {
		//call /demo002/insert with data contents
		info("insert:: context:"+context);
		Demo002Handler handler = new Demo002Handler();
		handler.init(this, params);
		return handler.insert(params);
	}
	
	@Override
	public java.util.List<Sampling> doList(Context context, Tree params) throws Exception {
		//return doCollect(context, params);
		//call /demo002/list with filter criteria
		info("list:: context: "+context);
		Demo002Handler handler = new Demo002Handler();
		handler.init(this, params);
		return handler.list(params,Sampling.class);
	}
	
	@Override
	public DataSet<java.util.Map<String,Object>> doRetrieve(Context context, Tree params) throws Exception {
		//call /demo002/retrieve?account=x with data contents
		info("retrieve:: context: "+context);
		Demo002Handler handler = new Demo002Handler();
		handler.init(this, params);
		return handler.retrieve(params);
	}
	
	@Override
	public RecordSet<Sampling> doUpdate(Context context, Tree params) throws Exception {
		//call /demo002/update with data contents
		info("update:: context: "+context);
		Demo002Handler handler = new Demo002Handler();
		handler.init(this, params);
		return handler.update(params);
	}
	
	public RecordSet<Sampling> findby(Context context) throws Exception {
		Tree params = exposeData(context);
		return doFindby(context,params);
	}
	
}

/* 

curl -v -X POST http://localhost:8080/iserve/service/api/demo002/insert -d ""
curl -v -X POST http://localhost:8080/iserve/service/api/demo002/retrieve -d "account=3-3-33333-3"
curl -v -X POST http://localhost:8080/iserve/service/api/demo002/update -d ""
curl -v -X POST http://localhost:8080/iserve/service/api/demo002/delete -d "account=3-3-33333-3"
curl -v -X POST http://localhost:8080/iserve/service/api/demo002/collect -d "account=3-3"
curl -v -X POST http://localhost:8080/iserve/service/api/demo002/find
curl -v -X POST http://localhost:8080/iserve/service/api/demo002/list

*/