/*
Copyright (c) 2012, Bernard Butler and Arif Fareed (Waterford Institute of Technology, Ireland), Project: FP7-ICT-257930 Aniketos
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

 -  Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 -  Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 -  Neither the name of WATERFORD INSTITUTE OF TECHNOLOGY nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package eu.aniketos.wp3.components.spdm.console;


import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Set;

import eu.aniketos.wp3.components.spdm.ds.api.ISPSRepository;
import eu.aniketos.wp3.components.spdm.ds.api.IWebService;

import eu.aniketos.data.ISecurityDescriptor;
import eu.aniketos.data.ISecurityProperty;

/**
 * CommandLineService provides the implementation of the service
 * commands registered with an OSGi container. 
 * The following commands can be used to persist Agreement Templates
 * and its relevant Security Properties.
 * lssp - list Security Properties
 * lsag - list Agreement Templates
 * delsp id - delete Security Property by its id
 * addag value - add Agreement Template by its value
 * addsp value freshness agreementtemplate_id - add Security Property by its attributes
 * commands - list of commands
 * 
 * @author Bernard Butler and M. Arif Fareed (TSSG)
 *
 */
public class CommandLineService {
	
    private BundleContext bc;

	private ISecurityDescriptor security_descriptor;
	private ISecurityProperty security_property;
	private ISPSRepository sps_repository;	
	private IWebService web_service;
	
    /**
     * Obtain BundleContext Handler
     * @param bc
     */
    public CommandLineService(BundleContext bc) {
        this.bc = bc;
    
		System.out.println("=== Default Activator ====");

		// SecurityDescriptor
		ServiceReference security_descriptor_serviceReference = this.bc
				.getServiceReference(ISecurityDescriptor.class.getName());
		this.security_descriptor = (ISecurityDescriptor) this.bc
				.getService(security_descriptor_serviceReference);
		System.out.println("Calling Declarative service using Bundle: "
				+ this.security_descriptor);

		// SecurityProperty
		ServiceReference security_property_serviceReference = this.bc
				.getServiceReference(ISecurityProperty.class.getName());
		this.security_property = (ISecurityProperty) this.bc
				.getService(security_property_serviceReference);
		System.out.println("Calling Declarative service using Bundle: "
				+ this.security_property);

		// SPS Repository
		ServiceReference sps_repository_serviceReference = this.bc
				.getServiceReference(ISPSRepository.class.getName());
		this.sps_repository = (ISPSRepository) this.bc
				.getService(sps_repository_serviceReference);
		System.out.println("Calling Declarative service using Bundle: "
				+ this.sps_repository);

		// WebService
		ServiceReference webservice_serviceReference = this.bc
				.getServiceReference(IWebService.class.getName());
		this.web_service = (IWebService) this.bc
				.getService(webservice_serviceReference);
		System.out.println("Calling Declarative service using Bundle: "
				+ this.web_service);

		
    }
    
    /**
     * Help command which list the commands descriptions
     * @param out
     * @param args
     */
    public void commands(PrintWriter out, String... args) {
    	int index = 1;
    	out.println("=========================================================");
    		out.println(" 	  SPDM Console Client Service                ");
    		out.println("=========================================================");
    		out.println( index++ + "." + " lssp,lssp - List registered Security Properties.");
    		out.println( index++ + "." + " lsws,lsws - List registered services.");
    		out.println( index++ + "." + " delsp,delsp <id> - delete Security Property.");
    				out.println( index++ + "." + " unregister,unregister - unregister services.");
    								out.println( index++ + "." + " cache,cache - Nr. of Entries in SPDM Repository.");
    								out.println( index++ + "." + " register,register - registering Service & Security Descriptor.");
    												out.println( index++ + "." + " commands,commands - List supported commands.");
    	    out.println("---------------------------------------------------------");
    }
    
    /**
     * Help command which list the commands descriptions
     * @param out
     * @param args
     */
    public void cache(PrintWriter out, String... args) {
    	try{
    		System.out.println("Repository Size : "+ this.sps_repository.repository_size());
    		System.out.println(this.sps_repository);
         } catch (Exception e) {
    			e.printStackTrace(out);
        }
    }

    /**
     * List SecurityProperty Command which returns all existing SecurityProperty
     * @param out
     * @param args
     */
    public void lssp(PrintWriter out, String... args) {
        try {
    		System.out.println(this.sps_repository.lookUpSecurityProperty(this.web_service));
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }


    /**
     * List AgreementTemplate Command which return all existing AgreementTemplate(s)
     * @param out
     * @param args
     */
    public void lsws(PrintWriter out, String... args) {
        try {
        	Set<ISecurityProperty> securityProperties = this.sps_repository.lookUpSecurityProperty(this.web_service);
        	for(ISecurityProperty p: securityProperties) {
        		System.out.println(this.sps_repository.lookupService(p));
        		break;
        	}
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }



    /**
     * Delete an exisitng AgreementTemplate Command
     * @param out
     * @param args
     */
    public void unregister(PrintWriter out, String... args) {
//        if (args == null || args.length != 1) {
 //           out.println("ID param is missed");
  //          return;
    //    }
        try {
        	System.out.println("Unregistering Default Security Properties");

        	this.sps_repository.clear_repository();
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }

    /**
     * Delete an exisiting SecurityProperty command.
     * @param out
     * @param args
     */
    public void delsp(PrintWriter out, String... args) {
        
    	if (args == null || args.length != 1) {
            out.println("Security Property ID param is missed");
            return;
        }
        try {
            ISecurityProperty del_p;
        	Set<ISecurityProperty> securityProperties = this.sps_repository.lookUpSecurityProperty(this.web_service);
        	for(ISecurityProperty p: securityProperties) {
        	//	System.out.println("Property ID: " + args[0]);
        		if(p.getPropertyID().equals(args[0])){
        			del_p = p;
        			out.println("Removing Security Property:" + del_p );
        			this.sps_repository.removeSecurityProperty(del_p);
        			break;
        		}
        		
        	}

        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }


    /**
     * Add a new SecurityProperty command
     * @param out
     * @param args
     */
    public void register(PrintWriter out, String... args) {
//        if (args == null || args.length != 3) {
//            out.println("Wrong params");
//            return;
 //       }
        try {
        	System.out.println("Registering Default Security Properties");
    		this.sps_repository.registerService(this.web_service, this.security_descriptor);

  
        } catch (Exception e) {
            e.printStackTrace(out);
        }
    }

}
