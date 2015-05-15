package com.venraas.cloudstorage.azure.blob.block;

public class Block {
		
    public String id;
    
    public byte[] bytes;
    
    //-- the actual data size, i.e. $bytes, in bytes
    public int size;
    
    public boolean uploaded = false;
    
    
    public Block(String id, byte[] bytes, int size) {
    	this.id = id;
    	this.bytes = bytes;
    	this.size = size;
    }    
    
    
}
