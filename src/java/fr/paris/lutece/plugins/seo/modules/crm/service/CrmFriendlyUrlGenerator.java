/*
 * Copyright (c) 2002-2014, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.seo.modules.crm.service;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import fr.paris.lutece.plugins.crm.business.demand.DemandType;
import fr.paris.lutece.plugins.crm.service.demand.DemandTypeService;
import fr.paris.lutece.plugins.seo.business.FriendlyUrl;
import fr.paris.lutece.plugins.seo.service.FriendlyUrlUtils;
import fr.paris.lutece.plugins.seo.service.SEODataKeys;
import fr.paris.lutece.plugins.seo.service.generator.FriendlyUrlGenerator;
import fr.paris.lutece.plugins.seo.service.generator.GeneratorOptions;
import fr.paris.lutece.plugins.seo.service.sitemap.SitemapUtils;
import fr.paris.lutece.portal.service.datastore.DatastoreService;


/**
 * Document Alias Generator
 */
public class CrmFriendlyUrlGenerator implements FriendlyUrlGenerator
{
    private static final String GENERATOR_NAME = "Crm Friendly URL Generator";
    private static final String TECHNICAL_URL = "/jsp/site/plugins/crm/DoOpenDemandType.jsp?id_demand_type={0}";
    private static final String SLASH = "/";
    private static final String PATH_CRM = "/crm/";
    private static final String DEFAULT_CHANGE_FREQ = SitemapUtils.CHANGE_FREQ_VALUES[3];
    private static final String DEFAULT_PRIORITY = SitemapUtils.PRIORITY_VALUES[3];
    private boolean _bCanonical;
    private boolean _bSitemap;
    private String _strChangeFreq;
    private String _strPriority;

    /**
     * {@inheritDoc }
     */
    @Override
    public String generate( List<FriendlyUrl> list, GeneratorOptions options )
    {
      
    	init(  );
        
    	DemandTypeService demandTypeService = DemandTypeService.getService(  );
        
        List<DemandType> listDemandTypes= demandTypeService.findAll();
        
        if(listDemandTypes!=null)
        {
	        for ( DemandType  demandType : listDemandTypes )
	        {
	           
	            FriendlyUrl url = new FriendlyUrl(  );
	
	            if ( options.isAddPath(  ) )
	            {
	            	 String strPath = PATH_CRM;
	                 url.setFriendlyUrl( strPath + FriendlyUrlUtils.convertToFriendlyUrl( demandType.getLabel()) );
	            }
	            else
	            {
	            	
	                url.setFriendlyUrl( SLASH + FriendlyUrlUtils.convertToFriendlyUrl( demandType.getLabel() ) );
	            }
	
	            Object[] args = { demandType.getIdDemandType()};
	            String strTechnicalUrl = MessageFormat.format( TECHNICAL_URL, args );
	            url.setTechnicalUrl( strTechnicalUrl );
	            url.setCanonical( _bCanonical );
	            url.setSitemap( _bSitemap );
	            url.setSitemapChangeFreq( _strChangeFreq );
	            url.setSitemapLastmod( SitemapUtils.formatDate(new Timestamp( new Date().getTime())) );
	            url.setSitemapPriority( _strPriority );
	            list.add( url );
	        }
        }

        return "";
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getName(  )
    {
        return GENERATOR_NAME;
    }

    /**
     * Initialiaze generator with options stored with Datastore
     */
    private void init(  )
    {
        String strKeyPrefix = SEODataKeys.PREFIX_GENERATOR + getClass(  ).getName(  );
        _bCanonical = DatastoreService.getDataValue( strKeyPrefix + SEODataKeys.SUFFIX_CANONICAL,
                DatastoreService.VALUE_TRUE ).equals( DatastoreService.VALUE_TRUE );
        _bSitemap = DatastoreService.getDataValue( strKeyPrefix + SEODataKeys.SUFFIX_SITEMAP,
                DatastoreService.VALUE_TRUE ).equals( DatastoreService.VALUE_TRUE );
        _strChangeFreq = DatastoreService.getDataValue( strKeyPrefix + SEODataKeys.SUFFIX_CHANGE_FREQ,
                DEFAULT_CHANGE_FREQ );
        _strPriority = DatastoreService.getDataValue( strKeyPrefix + SEODataKeys.SUFFIX_PRIORITY, DEFAULT_PRIORITY );
    }
}
