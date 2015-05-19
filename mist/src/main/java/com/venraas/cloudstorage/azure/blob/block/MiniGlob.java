package com.venraas.cloudstorage.azure.blob.block;

import java.io.File;
import java.io.FileFilter;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.ListBlobItem;


public class MiniGlob {
	String fun_;
	String fullPrefix_;
	List<String> file_list_ = new LinkedList<String>();
	
	final static Logger logger = LoggerFactory.getLogger(MiniGlob.class);
	
	
	public MiniGlob (String fun, String connStr, String ctnName, String fullPrefix) {
		if (null == fun || fun.isEmpty() || 
				null == connStr || connStr.isEmpty() || 
				null == ctnName || ctnName.isEmpty() || 
				null == fullPrefix || fullPrefix.isEmpty())
			return;
		
		//-- validate pattern
		if (0 != "*".compareTo(fullPrefix.substring(fullPrefix.length() - 1)))
			return;
		
		//-- collect file list to download from cloud storage 
		if (0 == fun.compareToIgnoreCase("down")) {
			try {
				logger.info("get pattern-matched file list from cloud storage ...");
				//-- Retrieve storage account from connection-string
				CloudStorageAccount storageAccount = CloudStorageAccount.parse(connStr);

			    //-- Create the blob client.
			    CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

			    //-- Retrieve reference to a previously created container.
			    CloudBlobContainer container = blobClient.getContainerReference(ctnName);			    

			    //-- file pattern with regular expression 
			    String fPattern = FilenameUtils.getName(fullPrefix);
			    fPattern = fPattern.substring(0, fPattern.length() - 1);			    
			    fPattern = Pattern.quote(fPattern) + ".*";

			    //-- add into list if the blob name matches the pattern 
			    for (ListBlobItem blobItem : container.listBlobs()) {
			    	String fName =  FilenameUtils.getName(blobItem.getUri().toString());
			    	if (fName.matches(fPattern)) {
			    		file_list_.add(fName);
			    		logger.info(fName);
			    	}			    		 
			    }			    
			} catch (InvalidKeyException e) {
				logger.error(e.getMessage());
			} catch (URISyntaxException e) {
				logger.error(e.getMessage());
			}  catch (StorageException e) {
				logger.error(e.getMessage());
			}
		}
		//-- collect file list to upload to cloud storage
		else if (0 == fun.compareToIgnoreCase("up")) {
			logger.info("get pattern-matched file list from local ...");
			
			String path = FilenameUtils.getFullPath(fullPrefix);
			//-- apply current path as absolutely path
			if (0 == path.length()) {
				path = System.getProperty("user.dir") + System.getProperty("file.separator");				
			}			
			File dir = new File(path);			
			
			String fPrefix = FilenameUtils.getName(fullPrefix);			
			fPrefix = fPrefix.substring(0, fPrefix.length() - 1);
			FileFilter fFilter = new PrefixFileFilter(fPrefix);

			File[] files = dir.listFiles(fFilter);			
			for (File f : files) {
				if (f.isFile()) {
					file_list_.add(path + f.getName());
					logger.info(path + f.getName());
				}
			}
			
		}
	}

	public List<String> getFileList () {		
		return file_list_;
	}
	

}
