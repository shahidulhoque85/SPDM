/*
Copyright (c) 2012, Bernard Butler and Arif Fareed (Waterford Institute of Technology, Ireland), Project: FP7-ICT-257930 Aniketos
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

 -  Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 -  Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 -  Neither the name of WATERFORD INSTITUTE OF TECHNOLOGY nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package eu.aniketos.wp3.components.spdm.ds.impl;

import java.util.Map;

import java.util.TreeMap;

import eu.aniketos.data.ISecurityDescriptor;
import eu.aniketos.data.ISecurityProperty;
import eu.aniketos.wp3.components.spdm.ds.api.ISPSRepository;
import eu.aniketos.wp3.components.spdm.ds.api.IWebService;
import eu.aniketos.wp3.components.spdm.ds.impl.BidiMultiHashMap.EntrySet;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;


import java.util.Set;

/*
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.jpa.EntityManagerFactoryBuilder;
*/

import javax.persistence.*;

//import org.osgi.service.log.LogService;

/**
 * A local class for manuplating AgreementTemplate & SecurityProperty.
 * 
 * @author Bernard Butler and M. Arif Fareed (TSSG)
 */

@Component(name="sps-repository-service")@Service
public class SPSRepository implements ISPSRepository {
	
	private static final long serialVersionUID = 1L;
	
	@Property(value="Security Centre Service")
	static final String CONSTANT_NAME = "service.description";
	@Property(value="Aniketos SPDM")
	static final String CONSTANT_NAME_1 = "service.vendor";

	@Property(value="*")
	static final String CONSTANT_NAME_2 = "service.exported.interfaces";
	@Property(value="org.apache.cxf.ws")
	static final String CONSTANT_NAME_3 = "service.exported.configs";
	@Property(value="http://localhost:9091/spdm_spsrepository")
	static final String CONSTANT_NAME_4 = "org.apache.cxf.ws.address";
	//HowTo Reference this classes in client application
	// @Reference(name = "service_centre",
	// cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE,
	// referenceInterface = ISPSRepository.class,
	// strategy = ReferenceStrategy.EVENT,
	// policy = ReferencePolicy.DYNAMIC,
	// bind = "bindSPSRepository",
	// unbind = "unbindSPSRepository")
	// private ISPSRepository sps_repository;
	
//	@Reference(name = "log",
//	cardinality = ReferenceCardinality.MANDATORY_UNARY,
//	referenceInterface = org.osgi.service.log.LogService.class,
//	policy = ReferencePolicy.STATIC,
//	bind = "bindLog",
//	unbind = "unbindLog")
//    private LogService log;
	// static Logger log = Logger.getLogger(HelloWorldServiceImp.class);
	
	//HowTo Reference this classes in client application
	// @Reference(name = "bidimap",
	// cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE,
	// referenceInterface = BidiMultiMap.class,
	// strategy = ReferenceStrategy.EVENT,
	// policy = ReferencePolicy.DYNAMIC,
	// bind = "bindSPSRepository",
	// unbind = "unbindBidiMap")
	// private  BidiMultiMap sps_repository;	
	private BidiMultiMap<IWebService, ISecurityProperty> bidi_map;

	//There will be a certificate object here in the future.
	
	/**
     * Register ServiceCentre service with OSGi container
	 * @param context
	 */
	@Activate
	protected void activateServiceCentre(ComponentContext context) {
		// log.debug("Activate ReferenceManager");
		System.out.println("Activate SPS Repository Service Component");
//		log.log(LogService.LOG_INFO, "Activate Service Centre Component!");
		this.bidi_map =  new BidiMultiHashMap<IWebService, ISecurityProperty>();
		// @TODO
	}

	/**
	 * UnRegister ServiceCentre Service with OSCi container 
	 * @param context
	 */
	@Deactivate
	protected void deactivateServiceCentre(ComponentContext context) {
		// log.debug("Deactivate ReferenceManager");
		System.out.println("Deactivate SPS Repository Service Component");
//	    log.log(LogService.LOG_INFO, "Deactivate Service Centre Component!");
	    
	    //@TODO
	}
	 
	
//	protected void bindLog(LogService log)
//	{
//	    this.log = log;
//	}
//
//	protected void unbindLog(LogService log)
//	{
//	    this.log = null;
//	}
	
	public void registerService(IWebService service_key, ISecurityDescriptor security_descriptor){
		
		for(ISecurityProperty p: security_descriptor.getProperties()) {
			this.bidi_map.put(service_key, p);
		}
	}

	public void registerService(IWebService service_key, ISecurityProperty property) {
		this.bidi_map.put(service_key, property);
	}
	
	public  void registerSecurityProperty(ISecurityProperty property, IWebService service_key) {
		
	}

	public void registerSecurityProperty(ISecurityDescriptor security_descriptor, IWebService service_key) {
		
	}

	public Set<ISecurityProperty> lookUpSecurityProperty(IWebService service_key) {
		return this.bidi_map.valueSet(service_key);
	}

	public Set<IWebService> lookupService(ISecurityProperty property_value) {
		return this.bidi_map.keySet(property_value);
	}


	public void  printRegisteredServices() {
		
	}
	
	public void printRegisteredSecurityProperties() {
		
	}


	public int repository_size() {
		return this.bidi_map.size();
	}

	public void clear_repository() {
		this.bidi_map.clear();
	}
	
	public void removeSecurityProperty(ISecurityProperty property_value) {
		this.bidi_map.removeValue(property_value);
	}
	
	public void removeService(IWebService service_key) {
		this.bidi_map.removeKey(service_key);
	}

	public Set<Map.Entry<IWebService, ISecurityProperty>>  getEntriest() {
		return this.bidi_map.entrySet();
	}
	
	public void persistEntrySet(Map.Entry<IWebService, ISecurityProperty> entry) {
        
	}
	
/*	
	protected void persistService(IWebService service){
		
		
	}
	
	protected void persistSecurityProperty(ISecurityProperty sp) {
		

	}
	
*/    
	/**
	 * 
	 */
	public String toString(){
		
		StringBuffer bf = new StringBuffer();
		
		bf.append("SPS Repository Entries: \n");
		Set<Map.Entry<IWebService,ISecurityProperty>> sps_set = this.bidi_map.entrySet(); 	 

		for(Map.Entry<IWebService,ISecurityProperty> entry: sps_set) {
			 bf.append(entry);	 
		}
		
		return bf.toString();
	}
	
	

}
