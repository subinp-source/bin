/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.accountsummaryaddon.jalo;

import de.hybris.platform.accountsummaryaddon.constants.AccountsummaryaddonConstants;
import de.hybris.platform.accountsummaryaddon.cronjob.DeleteDocumentFileCronJob;
import de.hybris.platform.accountsummaryaddon.jalo.AccountSummaryAccountStatusComponent;
import de.hybris.platform.accountsummaryaddon.jalo.AccountSummaryUnitTreeComponent;
import de.hybris.platform.accountsummaryaddon.jalo.B2BDocument;
import de.hybris.platform.accountsummaryaddon.jalo.B2BDocumentPaymentInfo;
import de.hybris.platform.accountsummaryaddon.jalo.B2BDocumentType;
import de.hybris.platform.accountsummaryaddon.jalo.DocumentMedia;
import de.hybris.platform.b2b.jalo.B2BUnit;
import de.hybris.platform.commerceservices.jalo.OrgUnit;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>AccountsummaryaddonManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast"})
public abstract class GeneratedAccountsummaryaddonManager extends Extension
{
	/**
	* {@link OneToManyHandler} for handling 1:n DOCUMENT's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<B2BDocument> B2BUNIT2B2BDOCUMENTDOCUMENTHANDLER = new OneToManyHandler<B2BDocument>(
	AccountsummaryaddonConstants.TC.B2BDOCUMENT,
	false,
	"unit",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	/**
	* {@link OneToManyHandler} for handling 1:n DOCUMENT's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<B2BDocument> B2BDOCUMENT2ABSTRACTORDERDOCUMENTHANDLER = new OneToManyHandler<B2BDocument>(
	AccountsummaryaddonConstants.TC.B2BDOCUMENT,
	false,
	"order",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		DEFAULT_INITIAL_ATTRIBUTES = ttmp;
	}
	@Override
	public Map<String, AttributeMode> getDefaultAttributeModes(final Class<? extends Item> itemClass)
	{
		Map<String, AttributeMode> ret = new HashMap<>();
		final Map<String, AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
		if (attr != null)
		{
			ret.putAll(attr);
		}
		return ret;
	}
	
	public AccountSummaryAccountStatusComponent createAccountSummaryAccountStatusComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( AccountsummaryaddonConstants.TC.ACCOUNTSUMMARYACCOUNTSTATUSCOMPONENT );
			return (AccountSummaryAccountStatusComponent)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating AccountSummaryAccountStatusComponent : "+e.getMessage(), 0 );
		}
	}
	
	public AccountSummaryAccountStatusComponent createAccountSummaryAccountStatusComponent(final Map attributeValues)
	{
		return createAccountSummaryAccountStatusComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public AccountSummaryUnitTreeComponent createAccountSummaryUnitTreeComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( AccountsummaryaddonConstants.TC.ACCOUNTSUMMARYUNITTREECOMPONENT );
			return (AccountSummaryUnitTreeComponent)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating AccountSummaryUnitTreeComponent : "+e.getMessage(), 0 );
		}
	}
	
	public AccountSummaryUnitTreeComponent createAccountSummaryUnitTreeComponent(final Map attributeValues)
	{
		return createAccountSummaryUnitTreeComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public B2BDocument createB2BDocument(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( AccountsummaryaddonConstants.TC.B2BDOCUMENT );
			return (B2BDocument)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating B2BDocument : "+e.getMessage(), 0 );
		}
	}
	
	public B2BDocument createB2BDocument(final Map attributeValues)
	{
		return createB2BDocument( getSession().getSessionContext(), attributeValues );
	}
	
	public B2BDocumentPaymentInfo createB2BDocumentPaymentInfo(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( AccountsummaryaddonConstants.TC.B2BDOCUMENTPAYMENTINFO );
			return (B2BDocumentPaymentInfo)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating B2BDocumentPaymentInfo : "+e.getMessage(), 0 );
		}
	}
	
	public B2BDocumentPaymentInfo createB2BDocumentPaymentInfo(final Map attributeValues)
	{
		return createB2BDocumentPaymentInfo( getSession().getSessionContext(), attributeValues );
	}
	
	public B2BDocumentType createB2BDocumentType(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( AccountsummaryaddonConstants.TC.B2BDOCUMENTTYPE );
			return (B2BDocumentType)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating B2BDocumentType : "+e.getMessage(), 0 );
		}
	}
	
	public B2BDocumentType createB2BDocumentType(final Map attributeValues)
	{
		return createB2BDocumentType( getSession().getSessionContext(), attributeValues );
	}
	
	public DeleteDocumentFileCronJob createDeleteDocumentFileCronJob(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( AccountsummaryaddonConstants.TC.DELETEDOCUMENTFILECRONJOB );
			return (DeleteDocumentFileCronJob)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating DeleteDocumentFileCronJob : "+e.getMessage(), 0 );
		}
	}
	
	public DeleteDocumentFileCronJob createDeleteDocumentFileCronJob(final Map attributeValues)
	{
		return createDeleteDocumentFileCronJob( getSession().getSessionContext(), attributeValues );
	}
	
	public DocumentMedia createDocumentMedia(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( AccountsummaryaddonConstants.TC.DOCUMENTMEDIA );
			return (DocumentMedia)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating DocumentMedia : "+e.getMessage(), 0 );
		}
	}
	
	public DocumentMedia createDocumentMedia(final Map attributeValues)
	{
		return createDocumentMedia( getSession().getSessionContext(), attributeValues );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.document</code> attribute.
	 * @return the document
	 */
	public Collection<B2BDocument> getDocument(final SessionContext ctx, final B2BUnit item)
	{
		return B2BUNIT2B2BDOCUMENTDOCUMENTHANDLER.getValues( ctx, item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>B2BUnit.document</code> attribute.
	 * @return the document
	 */
	public Collection<B2BDocument> getDocument(final B2BUnit item)
	{
		return getDocument( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.document</code> attribute. 
	 * @param value the document
	 */
	public void setDocument(final SessionContext ctx, final B2BUnit item, final Collection<B2BDocument> value)
	{
		B2BUNIT2B2BDOCUMENTDOCUMENTHANDLER.setValues( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>B2BUnit.document</code> attribute. 
	 * @param value the document
	 */
	public void setDocument(final B2BUnit item, final Collection<B2BDocument> value)
	{
		setDocument( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to document. 
	 * @param value the item to add to document
	 */
	public void addToDocument(final SessionContext ctx, final B2BUnit item, final B2BDocument value)
	{
		B2BUNIT2B2BDOCUMENTDOCUMENTHANDLER.addValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to document. 
	 * @param value the item to add to document
	 */
	public void addToDocument(final B2BUnit item, final B2BDocument value)
	{
		addToDocument( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from document. 
	 * @param value the item to remove from document
	 */
	public void removeFromDocument(final SessionContext ctx, final B2BUnit item, final B2BDocument value)
	{
		B2BUNIT2B2BDOCUMENTDOCUMENTHANDLER.removeValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from document. 
	 * @param value the item to remove from document
	 */
	public void removeFromDocument(final B2BUnit item, final B2BDocument value)
	{
		removeFromDocument( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.document</code> attribute.
	 * @return the document
	 */
	public Collection<B2BDocument> getDocument(final SessionContext ctx, final AbstractOrder item)
	{
		return B2BDOCUMENT2ABSTRACTORDERDOCUMENTHANDLER.getValues( ctx, item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrder.document</code> attribute.
	 * @return the document
	 */
	public Collection<B2BDocument> getDocument(final AbstractOrder item)
	{
		return getDocument( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.document</code> attribute. 
	 * @param value the document
	 */
	public void setDocument(final SessionContext ctx, final AbstractOrder item, final Collection<B2BDocument> value)
	{
		B2BDOCUMENT2ABSTRACTORDERDOCUMENTHANDLER.setValues( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrder.document</code> attribute. 
	 * @param value the document
	 */
	public void setDocument(final AbstractOrder item, final Collection<B2BDocument> value)
	{
		setDocument( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to document. 
	 * @param value the item to add to document
	 */
	public void addToDocument(final SessionContext ctx, final AbstractOrder item, final B2BDocument value)
	{
		B2BDOCUMENT2ABSTRACTORDERDOCUMENTHANDLER.addValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to document. 
	 * @param value the item to add to document
	 */
	public void addToDocument(final AbstractOrder item, final B2BDocument value)
	{
		addToDocument( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from document. 
	 * @param value the item to remove from document
	 */
	public void removeFromDocument(final SessionContext ctx, final AbstractOrder item, final B2BDocument value)
	{
		B2BDOCUMENT2ABSTRACTORDERDOCUMENTHANDLER.removeValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from document. 
	 * @param value the item to remove from document
	 */
	public void removeFromDocument(final AbstractOrder item, final B2BDocument value)
	{
		removeFromDocument( getSession().getSessionContext(), item, value );
	}
	
	@Override
	public String getName()
	{
		return AccountsummaryaddonConstants.EXTENSIONNAME;
	}
	
}
