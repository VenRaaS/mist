package com.venraas.cloudstorage.azure.blob.block;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;


public class Download {

    final static String StorageConnectionFormat =
		"DefaultEndpointsProtocol=http;"
	    + "AccountName=%s;"
	    + "AccountKey=%s";
    final static Logger logger = LoggerFactory.getLogger(Download.class);
    
    String accName_; 
	String accKey_;    			
    String storageConnection_str_;    
    String containerName_;    
    String fullFilePath_;
    
    
	public Download (String an, String ak, String cn, String ffp) {
    	accName_ = an;
    	accKey_  = ak;    			        
        storageConnection_str_ = String.format(StorageConnectionFormat, accName_, accKey_);        
        containerName_ = cn;        
        fullFilePath_ = ffp;
	}
	
	public void start() {
	   try {
            CloudStorageAccount account = CloudStorageAccount.parse(storageConnection_str_);
            CloudBlobClient serviceClient = account.createCloudBlobClient();            

            // Container name must be lower case.
            CloudBlobContainer container = serviceClient.getContainerReference(containerName_);
            container.createIfNotExists();

            File file = new File(fullFilePath_);        	
        	String fname = file.getName();
        	            
            CloudBlockBlob blob = container.getBlockBlobReference(fname);
            
            long startTime = System.currentTimeMillis();
            
            logger.info("Download from cloud storage ...");
            //-- Download the file.            
            File destinationFile = new File(file.getParentFile(), fname);
            blob.downloadToFile(destinationFile.getAbsolutePath());
            
            long duration = System.currentTimeMillis() - startTime;
            logger.info(String.format("Download from cloud storage completely in %d secs", TimeUnit.MILLISECONDS.toSeconds(duration)));
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
