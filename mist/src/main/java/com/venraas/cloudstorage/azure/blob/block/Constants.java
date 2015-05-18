package com.venraas.cloudstorage.azure.blob.block;

public class Constants {	
	//-- concurrent thread size
	public final static int SIZE_THREAD_POOL = 100;	
	
	public final static String STORAGE_CONNECTION_FORMAT =
			"DefaultEndpointsProtocol=http;"
		    + "AccountName=%s;"
		    + "AccountKey=%s";
	
	
	//-- block size unit in bytes 
	public final static int BLOCK_SIZE_UNIT = 1024 * 1024;	
	public final static float FACTOR_BLOCK_SIZE_UNIT = 0.5f;	

	
}
