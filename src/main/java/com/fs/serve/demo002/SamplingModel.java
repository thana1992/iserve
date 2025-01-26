package com.fs.serve.demo002;

import com.fs.db.DbModel;

/**
 * SamplingModel class implement to handle table sampling
 * 
 * @author tassan_oro@freewillsolutions.com
 */

@SuppressWarnings("serial")
public class SamplingModel extends DbModel {
	public SamplingModel() {
		super("sampling");		
	}
	@Override
	public Class<Sampling> getFields() {
		return Sampling.class;
	}
}
