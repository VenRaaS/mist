package com.venraas.cloudstorage.azure.blob.block;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlockBlob;


public class UploaderThread implements Callable<Block> {	
    final static Logger logger = LoggerFactory.getLogger(UploaderThread.class);
    
    private CloudBlockBlob blob_;
    private Block b_;        
    
    public UploaderThread(CloudBlockBlob blob, Block b) {
    	this.blob_ = blob;    	
    	this.b_ = b;    	       
    }
    
	public Block call() throws Exception {
		Block b = processCommand();
		return b;
	}    
 
    private Block processCommand() {    	
        try {        	
        	blob_.uploadBlock(b_.id, new ByteArrayInputStream(b_.bytes), b_.size);
        	this.b_.uploaded = true;
//        	logger.info(String.format("Block: %s uploaded", this.blockID_));
        } catch (StorageException e) {			
        	logger.error(e.getMessage());        	
		} catch (IOException e) {
			logger.error(e.getMessage());			
		}
        
        return b_;
    }


 

}
