package com.venraas.cloudstorage.azure.blob.block;

public class Block implements Comparable<Block> {
		
    public String id_base64;
    
    public long id;
    
    //-- offset in cloud storage
    public long offsetInCS;
    
    public byte[] bytes;
    
    //-- the actual data size, i.e. $bytes, in bytes
    public long size;
    
    public boolean uploaded = false;
    
    public boolean downloaded = false;
    
    
    public Block () {    	
    }
    
    public Block(String id, byte[] bytes, int size) {
    	this.id_base64 = id;
    	this.bytes = bytes;
    	this.size = size;
    }

    //-- compare function for sorting
	public int compareTo(Block o) {
		//-- ascending order
		return (int) (this.id - o.id);
	}    
    
    
}
