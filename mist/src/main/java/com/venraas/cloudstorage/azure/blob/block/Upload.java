package com.venraas.cloudstorage.azure.blob.block;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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
import com.microsoft.azure.storage.core.Base64;


public class Upload {	   
    final static Logger logger = LoggerFactory.getLogger(Upload.class);

    String storageConnection_str_;    
    String containerName_;    
    String fullFilePath_;    


	public Upload (String connStr, String ctnName, String ffp) {    	    	    			       
        storageConnection_str_ = connStr;        
        containerName_ = ctnName;        
        fullFilePath_ = ffp;
	}
	
	public boolean start() {
		boolean rt = false; 
				
        logger.info(String.format("start to upload file: %s", fullFilePath_));        
        
        FileInputStream fileStream = null;
                
        try {
        	long startTime = System.currentTimeMillis();
        	
        	//-- account
            CloudStorageAccount account = CloudStorageAccount.parse(storageConnection_str_);
            CloudBlobClient serviceClient = account.createCloudBlobClient();

            logger.info("check container");
            //-- container name
            CloudBlobContainer container = serviceClient.getContainerReference(containerName_);
            container.createIfNotExists();            
            
            //-- assign the blob name in azure storage
            File file = new File(fullFilePath_);        	
        	String fname = file.getName();
            CloudBlockBlob blob = container.getBlockBlobReference(fname);
            
            fileStream = new FileInputStream(fullFilePath_);
                 
            //-- set the upload block size 
            int blockSize = (int)(Constants.FACTOR_BLOCK_SIZE_UNIT * Constants.BLOCK_SIZE_UNIT);            
            List<BlockEntry> blockIDs = new LinkedList<BlockEntry>();            
            List<FutureTask<Block>> ft_list = new LinkedList<FutureTask<Block>>();                                                           
            
            //-- thread pool
            ExecutorService executor = Executors.newFixedThreadPool(Constants.SIZE_THREAD_POOL);
            
            int blockNumber = 0;
            
            //-- number of bytes left to read and upload
            long fileSize = file.length();
            long bytesLeft = fileSize;
            //-- do until all of the bytes are loaded into blocks
            logger.info("load the file");
            while (bytesLeft > 0) {
              blockNumber++;
         
              //-- create a blockID from the block number, add it to the block ID list
              //   the block ID is a base64 string
              String blkID = String.format("%08d", blockNumber);
              String blockId = Base64.encode(blkID.getBytes());
              blockIDs.add( new BlockEntry(blockId) );
              
              //   set up new buffer with the right size, and read that many bytes into it 
              byte[] bytes = new byte[blockSize];
              int actuallReadSize = fileStream.read(bytes);
              Block b = new Block(blockId, bytes, actuallReadSize);

              //-- upload the block
              FutureTask<Block> t = new FutureTask<Block>(new UploaderThread(blob, b));
              executor.submit(t);
              ft_list.add(t);

              bytesLeft -= actuallReadSize;                            
            }
            executor.shutdown();
            
            HashSet<String> uploadedBlock_set = new HashSet<String>();
            int bytesUploaded = 0;
            int uploadedBlock = 0;
            
            logger.info("upload to cloud storage ...");
//            while (! executor.isTerminated()) {            
            while (uploadedBlock <= blockNumber) {            
	            for (FutureTask<Block> t : ft_list) {
	            	if (t.isDone()) {
	            		Block b = t.get();
	            		if (b.uploaded && !uploadedBlock_set.contains(b.id_base64)){	            			
	            			bytesUploaded += b.size;
	            			uploadedBlock_set.add(b.id_base64);
	            			++uploadedBlock;	            			
	            		}
	            	}	            		            		            	            
	            }
	            	            	            
	            Thread.sleep(3000);
	            
	            System.out.print( String.format("upload %.1f %% \r", (float)bytesUploaded/(float)fileSize*100) );
//	            logger.info( String.format("Upload %.1f %%", (float)bytesUploaded/(float)fileSize*100) );	            
	            if (uploadedBlock == blockNumber) break;	            
            }
            System.out.println();
                                                                     
            //-- commit the blocks
            blob.commitBlockList(blockIDs);
            
            long duration = System.currentTimeMillis() - startTime;
            logger.info(String.format("upload to cloud storage completely in %d secs \n", TimeUnit.MILLISECONDS.toSeconds(duration)));
            
            rt = true;
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
        finally
        {
    		try {
    			if (null != fileStream)		
					fileStream.close();
    		} catch (IOException e) {
    			e.printStackTrace();
			}
        
        }
        
        return rt;
	}
	
	
	
}
