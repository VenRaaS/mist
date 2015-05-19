package com.venraas.cloudstorage.azure.blob.block;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlockEntry;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import org.apache.commons.codec.binary.Base64;


public class Download {    
    final static Logger logger = LoggerFactory.getLogger(Download.class);
        
    String storageConnection_str_;    
    String containerName_;
    String fullFilePath_;
    
    
	public Download (String connStr, String ctnName, String ffp) {    	    	    			        
        storageConnection_str_ = connStr;        
        containerName_ = ctnName;        
        fullFilePath_ = ffp;
	}
	
	public void start() {
	   try {
	        logger.info(String.format("start to download file: %s", fullFilePath_));  
	        
            CloudStorageAccount account = CloudStorageAccount.parse(storageConnection_str_);
            CloudBlobClient serviceClient = account.createCloudBlobClient();            

            logger.info("check container");
            // Container name must be lower case.
            CloudBlobContainer container = serviceClient.getContainerReference(containerName_);
            container.createIfNotExists();

            long startTime = System.currentTimeMillis();
            logger.info("download from cloud storage ...");
            
            File file = new File(fullFilePath_);        	
        	String fname = file.getName();        	
            CloudBlockBlob blob = container.getBlockBlobReference(fname);                                                     
            
            //-- get blocks profile
            List<Block> block_list = new LinkedList<Block>();            
            List<BlockEntry> be_list = blob.downloadBlockList();                                                
            for (BlockEntry be : be_list) {
            	Block b = new Block();
            	String id_b64 = be.getId();            	
                String id = new String(Base64.decodeBase64(id_b64), "UTF-8");
                b.id_base64 = id_b64;
                b.id = Integer.parseInt(id);
                b.size = be.getSize();                
                block_list.add(b);
            }
            
            List<FutureTask<Block>> ft_list = new LinkedList<FutureTask<Block>>();
            //-- thread pool
            ExecutorService executor = Executors.newFixedThreadPool(Constants.SIZE_THREAD_POOL);
            //-- sort by blockID with ascending order
            Collections.sort(block_list);            
            long srcOffset = 0;
            for (Block b : block_list) {
                b.offsetInCS = srcOffset;                                
                srcOffset += b.size;
                
                FutureTask<Block> t = new FutureTask<Block>(new DownloaderThread(blob, b));
                executor.submit(t);
                ft_list.add(t);
            }                                    
            executor.shutdown();            
            
            HashSet<String> downloadedBlock_set = new HashSet<String>();
            long bytesDownloaded = 0;
            int downloadedBlock = 0;
                        
//            while (! executor.isTerminated()) {
            int blockNumber = be_list.size();
            while (downloadedBlock <= blockNumber) {
	            for (FutureTask<Block> t : ft_list) {
	            	if (t.isDone()) {
	            		Block b = t.get();
	            		if (b.downloaded && !downloadedBlock_set.contains(b.id_base64)) {	            			
	            			bytesDownloaded += b.size;
	            			downloadedBlock_set.add(b.id_base64);
	            			++downloadedBlock;	            			
	            		}
	            	}
	            }
	            	            	            
	            Thread.sleep(3000);
	            
//	            logger.info( String.format("Download %.1f %%", (float)bytesDownloaded/(float)srcOffset*100) );	            
	            System.out.print( String.format("download %.1f %% \r", (float)bytesDownloaded/(float)srcOffset*100) );
	            if (downloadedBlock == blockNumber) break;	            
            }
            System.out.println();

            FileOutputStream fos = new FileOutputStream(fullFilePath_);
            for (Block b : block_list) {
            	fos.write(b.bytes);
            }
            fos.close();
            
            long duration = System.currentTimeMillis() - startTime;
            logger.info(String.format("download from cloud storage completely in %d secs \n", TimeUnit.MILLISECONDS.toSeconds(duration)));            
        }
        catch (FileNotFoundException fileNotFoundException) {
        	logger.error( String.format("FileNotFoundException encountered: %s", fileNotFoundException.getMessage()) );        	
        }
        catch (StorageException storageException) {
        	logger.error( String.format("StorageException encountered: %s", storageException.getMessage()) );
        }
        catch (Exception e) {        	
        	logger.error( String.format("Exception encountered: %s", e.getMessage()) );            
        }
	    
		
	}

}
