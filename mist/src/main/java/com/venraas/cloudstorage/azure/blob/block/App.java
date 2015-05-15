package com.venraas.cloudstorage.azure.blob.block;

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
        
//        Option fun = Option.builder("f")
//        		.required()
//        		.hasArg()
//        		.argName("functionality")                
//                .desc("specify functionality [upload|...]")
//                .build();       
//        
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
        
        Option inputfile = Option.builder("inf")        		
                .hasArg()
                .required()
                .argName("input file")
                .desc("use given file as input")
                .build();                       
        
        Option help = new Option("help", "print help message");
        
//        options.addOption(fun);
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
            
            if (line.hasOption("an") && line.hasOption("ak") && line.hasOption("c") && line.hasOption("inf")) {
            	String an = line.getOptionValue("an");
            	String ak = line.getOptionValue("ak");
            	String c = line.getOptionValue("c");
            	String inf = line.getOptionValue("inf");
            
            	Upload u = new Upload(an, ak, c, inf);
            	u.start();
            }
        }
        catch (org.apache.commons.cli.ParseException exp) {
            // oops, something went wrong        	
        	logger.error("Parsing failed.");
        	logger.error("Reason: " + exp.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("mist", options );        			
		}
    }
}
