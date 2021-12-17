/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 15-Dec-2021, 3:07:46 PM
 * ----------------------------------------------------------------
 *
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.facades;

import java.io.Serializable;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.sap.productconfig.facades.CsticStatusType;
import de.hybris.platform.sap.productconfig.facades.ProductConfigMessageData;
import de.hybris.platform.sap.productconfig.facades.UiType;
import de.hybris.platform.sap.productconfig.facades.UiValidationType;
import java.util.List;


import java.util.Objects;
public  class CsticData  implements Serializable 
{

 	/** Default serialVersionUID value. */
 
	private static final long serialVersionUID = 1L;

	/** <i>Generated property</i> for <code>CsticData.key</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String key;

	/** <i>Generated property</i> for <code>CsticData.value</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String value;

	/** <i>Generated property</i> for <code>CsticData.formattedValue</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String formattedValue;

	/** <i>Generated property</i> for <code>CsticData.instanceId</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String instanceId;

	/** <i>Generated property</i> for <code>CsticData.additionalValue</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String additionalValue;

	/** <i>Generated property</i> for <code>CsticData.lastValidValue</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String lastValidValue;

	/** <i>Generated property</i> for <code>CsticData.name</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>CsticData.longText</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String longText;

	/** <i>Generated property</i> for <code>CsticData.longTextHTMLFormat</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private boolean longTextHTMLFormat;

	/** <i>Generated property</i> for <code>CsticData.showFullLongText</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private boolean showFullLongText;

	/** <i>Generated property</i> for <code>CsticData.type</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private UiType type;

	/** <i>Generated property</i> for <code>CsticData.validationType</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private UiValidationType validationType;

	/** <i>Generated property</i> for <code>CsticData.visible</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private boolean visible;

	/** <i>Generated property</i> for <code>CsticData.required</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private boolean required;

	/** <i>Generated property</i> for <code>CsticData.intervalInDomain</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private boolean intervalInDomain;

	/** <i>Generated property</i> for <code>CsticData.maxlength</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private int maxlength;

	/** <i>Generated property</i> for <code>CsticData.domainvalues</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private List<CsticValueData> domainvalues;

	/** <i>Generated property</i> for <code>CsticData.langdepname</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String langdepname;

	/** <i>Generated property</i> for <code>CsticData.typeLength</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private int typeLength;

	/** <i>Generated property</i> for <code>CsticData.numberScale</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private int numberScale;

	/** <i>Generated property</i> for <code>CsticData.entryFieldMask</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String entryFieldMask;

	/** <i>Generated property</i> for <code>CsticData.conflicts</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private List<ConflictData> conflicts;

	/** <i>Generated property</i> for <code>CsticData.position</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private int position;

	/** <i>Generated property</i> for <code>CsticData.csticStatus</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private CsticStatusType csticStatus;

	/** <i>Generated property</i> for <code>CsticData.placeholder</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private String placeholder;

	/** <i>Generated property</i> for <code>CsticData.retractTriggered</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private boolean retractTriggered;

	/** <i>Generated property</i> for <code>CsticData.priceRelevant</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private boolean priceRelevant;

	/** <i>Generated property</i> for <code>CsticData.media</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private List<ImageData> media;

	/** <i>Generated property</i> for <code>CsticData.messages</code> property defined at extension <code>sapproductconfigfacades</code>. */
		
	private List<ProductConfigMessageData> messages;
	
	public CsticData()
	{
		// default constructor
	}
	
	public void setKey(final String key)
	{
		this.key = key;
	}

	public String getKey() 
	{
		return key;
	}
	
	public void setValue(final String value)
	{
		this.value = value;
	}

	public String getValue() 
	{
		return value;
	}
	
	public void setFormattedValue(final String formattedValue)
	{
		this.formattedValue = formattedValue;
	}

	public String getFormattedValue() 
	{
		return formattedValue;
	}
	
	public void setInstanceId(final String instanceId)
	{
		this.instanceId = instanceId;
	}

	public String getInstanceId() 
	{
		return instanceId;
	}
	
	public void setAdditionalValue(final String additionalValue)
	{
		this.additionalValue = additionalValue;
	}

	public String getAdditionalValue() 
	{
		return additionalValue;
	}
	
	public void setLastValidValue(final String lastValidValue)
	{
		this.lastValidValue = lastValidValue;
	}

	public String getLastValidValue() 
	{
		return lastValidValue;
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}
	
	public void setLongText(final String longText)
	{
		this.longText = longText;
	}

	public String getLongText() 
	{
		return longText;
	}
	
	public void setLongTextHTMLFormat(final boolean longTextHTMLFormat)
	{
		this.longTextHTMLFormat = longTextHTMLFormat;
	}

	public boolean isLongTextHTMLFormat() 
	{
		return longTextHTMLFormat;
	}
	
	public void setShowFullLongText(final boolean showFullLongText)
	{
		this.showFullLongText = showFullLongText;
	}

	public boolean isShowFullLongText() 
	{
		return showFullLongText;
	}
	
	public void setType(final UiType type)
	{
		this.type = type;
	}

	public UiType getType() 
	{
		return type;
	}
	
	public void setValidationType(final UiValidationType validationType)
	{
		this.validationType = validationType;
	}

	public UiValidationType getValidationType() 
	{
		return validationType;
	}
	
	public void setVisible(final boolean visible)
	{
		this.visible = visible;
	}

	public boolean isVisible() 
	{
		return visible;
	}
	
	public void setRequired(final boolean required)
	{
		this.required = required;
	}

	public boolean isRequired() 
	{
		return required;
	}
	
	public void setIntervalInDomain(final boolean intervalInDomain)
	{
		this.intervalInDomain = intervalInDomain;
	}

	public boolean isIntervalInDomain() 
	{
		return intervalInDomain;
	}
	
	public void setMaxlength(final int maxlength)
	{
		this.maxlength = maxlength;
	}

	public int getMaxlength() 
	{
		return maxlength;
	}
	
	public void setDomainvalues(final List<CsticValueData> domainvalues)
	{
		this.domainvalues = domainvalues;
	}

	public List<CsticValueData> getDomainvalues() 
	{
		return domainvalues;
	}
	
	public void setLangdepname(final String langdepname)
	{
		this.langdepname = langdepname;
	}

	public String getLangdepname() 
	{
		return langdepname;
	}
	
	public void setTypeLength(final int typeLength)
	{
		this.typeLength = typeLength;
	}

	public int getTypeLength() 
	{
		return typeLength;
	}
	
	public void setNumberScale(final int numberScale)
	{
		this.numberScale = numberScale;
	}

	public int getNumberScale() 
	{
		return numberScale;
	}
	
	public void setEntryFieldMask(final String entryFieldMask)
	{
		this.entryFieldMask = entryFieldMask;
	}

	public String getEntryFieldMask() 
	{
		return entryFieldMask;
	}
	
	public void setConflicts(final List<ConflictData> conflicts)
	{
		this.conflicts = conflicts;
	}

	public List<ConflictData> getConflicts() 
	{
		return conflicts;
	}
	
	public void setPosition(final int position)
	{
		this.position = position;
	}

	public int getPosition() 
	{
		return position;
	}
	
	public void setCsticStatus(final CsticStatusType csticStatus)
	{
		this.csticStatus = csticStatus;
	}

	public CsticStatusType getCsticStatus() 
	{
		return csticStatus;
	}
	
	public void setPlaceholder(final String placeholder)
	{
		this.placeholder = placeholder;
	}

	public String getPlaceholder() 
	{
		return placeholder;
	}
	
	public void setRetractTriggered(final boolean retractTriggered)
	{
		this.retractTriggered = retractTriggered;
	}

	public boolean isRetractTriggered() 
	{
		return retractTriggered;
	}
	
	public void setPriceRelevant(final boolean priceRelevant)
	{
		this.priceRelevant = priceRelevant;
	}

	public boolean isPriceRelevant() 
	{
		return priceRelevant;
	}
	
	public void setMedia(final List<ImageData> media)
	{
		this.media = media;
	}

	public List<ImageData> getMedia() 
	{
		return media;
	}
	
	public void setMessages(final List<ProductConfigMessageData> messages)
	{
		this.messages = messages;
	}

	public List<ProductConfigMessageData> getMessages() 
	{
		return messages;
	}
	

}