package com.fs.serve.render;

import io.datatree.Tree;
import services.moleculer.service.Action;
import services.moleculer.service.Service;

/**
 * This is demo of using render engine from template
 * 
 * @author tassan_oro@freewillsolutions.com
 */
public class RenderService extends Service {

	public RenderService() {
		this("render");
	}

	public RenderService(String name) {
		super(name);
	}

	Action test = ctx -> {
		Tree data = new Tree();
		data.put("a", 1);
	    data.put("b", true);
	    data.put("c", "xyz");

	    Tree table = data.putList("table");
	    for (int i = 0; i < 10; i++) {
	        Tree row = table.addMap();
	        row.put("first", "some text");
	        row.put("second", false);
	        row.put("third", i);
	    }
	    
	    //this notify for raw data
	    //and point to test.html under resources/templates folder
	    Tree meta = data.getMeta();
	    meta.put("$raw", true);
	    meta.put("$template", "test");
	    
		return data;
	};
	
}
