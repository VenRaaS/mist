package com.venraas.cloudstorage.azure.blob.block;

import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlockBlob;


public class DownloaderThread implements Callable<Block> {	
    final static Logger logger = LoggerFactory.getLogger(DownloaderThread.class);
    
    private CloudBlockBlob blob_;
    private Block b_;        
    
    public DownloaderThread(CloudBlockBlob blob, Block b) {
    	this.blob_ = blob;    	
    	this.b_ = b;    	       
    }
    
	public Block call() throws Exception {
		Block b = processCommand();
		return b;
	}    
 
    private Block processCommand() {    	
        try {
        	byte[] buf = new byte[(int) b_.size];        	
        	blob_.downloadRangeToByteArray(b_.offsetInCS, b_.size, buf, 0);
        	b_.bytes = buf;
        	b_.downloaded = true;        	
//        	logger.info(String.format("Block: %s downloaded", this.blockID_));
        } catch (StorageException e) {			
        	logger.error(e.getMessage());        	
		} catch (Exception e) {
			logger.error(e.getMessage());			
		}
        
        return b_;
    }


 

}
