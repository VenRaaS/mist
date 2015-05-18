package com.venraas.cloudstorage.azure.blob.block;

import java.util.LinkedList;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Hello world!
 *
 */
public class App 
{
	final static Logger logger = LoggerFactory.getLogger(App.class);
	
    public static void main( String[] args )
    {
        Options options = new Options();                
        
        Option func = Option.builder("fn")
        		.required()
        		.hasArg()
        		.argName("functionality")                
                .desc("specify functionality of Upload or Download, i.e. [up|down]")
                .build();       
        
//        Option paasName = Option.builder("p")
//        		.required()
//                .hasArg()
//                .argName("PaaS name")
//                .desc("name of PaaS service [azure|...]")
//                .build();
        
        Option accName = Option.builder("an")
        		.required()
                .hasArg()
                .argName("account name")
                .desc("account name in PaaS service")
                .build();
        
        Option accKey = Option.builder("ak")
                .required()
        		.hasArg()
                .argName("account key")
                .desc("account key in PaaS service")
                .build();
        
        Option container = Option.builder("c")
                .hasArg()
                .required()
                .argName("container")
                .desc("the container name of the storage")
                .build();
        
        Option inputfile = Option.builder("ffp")        		
                .hasArg()
                .required()
                .argName("full file name")
                .desc("use given full file path")
                .build();                       
        
        Option help = new Option("help", "print help message");
        
        options.addOption(func);
//        options.addOption(paasName);
        options.addOption(accName);
        options.addOption(accName);
        options.addOption(accKey);
        options.addOption(container);
        options.addOption(inputfile);
        options.addOption(help);                
                
        try {
        	CommandLineParser parser = new DefaultParser();            // 
            CommandLine line = parser.parse(options, args);
            
            if (line.hasOption("fn") && line.hasOption("an") && line.hasOption("ak") && line.hasOption("c") && line.hasOption("ffp")) {
            	String fn = line.getOptionValue("fn");
            	String an = line.getOptionValue("an");
            	String ak = line.getOptionValue("ak");
            	String c = line.getOptionValue("c");
            	String ffp = line.getOptionValue("ffp");
            	String connStr = String.format(Constants.STORAGE_CONNECTION_FORMAT, an, ak);
            	
            	//-- prepare file list to process 
            	List<String> fname_list = new LinkedList<String>();            	
            	if ('*' == ffp.charAt(ffp.length() - 1) ) {
            		MiniGlob glob = new MiniGlob(fn, connStr, c, ffp);
            		fname_list = glob.getFileList();
            	}
            	else {
            		fname_list.add(ffp);
            	}
            	
            	if (0 == fn.compareToIgnoreCase("up")) {
            		for (String fname : fname_list) {
            			Upload u = new Upload(an, ak, c, fname);
            			u.start();
            		}
            	}
            	else if (0 == fn.compareToIgnoreCase("down")) {
            		for (String fname : fname_list) {
            			Download d = new Download(an, ak, c, fname); 
            			d.start();
            		}
            	}
            	else {
            		throw new org.apache.commons.cli.ParseException(String.format("unavailable -fn input \"%s\"", fn));            		
            	}
                        	

            }
        }
        catch (org.apache.commons.cli.ParseException exp) {
            //-- oops, something went wrong        	
        	logger.error("Parsing failed from org.apache.commons.cli");
        	logger.error("Reason: " + exp.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("mist", options );        			
		}
    }
}
