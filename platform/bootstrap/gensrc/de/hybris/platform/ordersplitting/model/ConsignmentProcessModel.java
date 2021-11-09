/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.ordersplitting.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type ConsignmentProcess first defined at extension basecommerce.
 */
@SuppressWarnings("all")
public class ConsignmentProcessModel extends BusinessProcessModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "ConsignmentProcess";
	
	/**<i>Generated relation code constant for relation <code>Consignment2ConsignmentProcess</code> defining source attribute <code>consignment</code> in extension <code>basecommerce</code>.</i>*/
	public static final String _CONSIGNMENT2CONSIGNMENTPROCESS = "Consignment2ConsignmentProcess";
	
	/**<i>Generated relation code constant for relation <code>OrderProcess2ConsignmentProcess</code> defining source attribute <code>parentProcess</code> in extension <code>basecommerce</code>.</i>*/
	public static final String _ORDERPROCESS2CONSIGNMENTPROCESS = "OrderProcess2ConsignmentProcess";
	
	/** <i>Generated constant</i> - Attribute key of <code>ConsignmentProcess.consignment</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String CONSIGNMENT = "consignment";
	
	/** <i>Generated constant</i> - Attribute key of <code>ConsignmentProcess.parentProcess</code> attribute defined at extension <code>basecommerce</code>. */
	public static final String PARENTPROCESS = "parentProcess";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public ConsignmentProcessModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public ConsignmentProcessModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>BusinessProcess</code> at extension <code>processing</code>
	 * @param _processDefinitionName initial attribute declared by type <code>BusinessProcess</code> at extension <code>processing</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public ConsignmentProcessModel(final String _code, final String _processDefinitionName)
	{
		super();
		setCode(_code);
		setProcessDefinitionName(_processDefinitionName);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _code initial attribute declared by type <code>BusinessProcess</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _processDefinitionName initial attribute declared by type <code>BusinessProcess</code> at extension <code>processing</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public ConsignmentProcessModel(final String _code, final ItemModel _owner, final String _processDefinitionName)
	{
		super();
		setCode(_code);
		setOwner(_owner);
		setProcessDefinitionName(_processDefinitionName);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsignmentProcess.consignment</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the consignment
	 */
	@Accessor(qualifier = "consignment", type = Accessor.Type.GETTER)
	public ConsignmentModel getConsignment()
	{
		return getPersistenceContext().getPropertyValue(CONSIGNMENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ConsignmentProcess.parentProcess</code> attribute defined at extension <code>basecommerce</code>. 
	 * @return the parentProcess
	 */
	@Accessor(qualifier = "parentProcess", type = Accessor.Type.GETTER)
	public OrderProcessModel getParentProcess()
	{
		return getPersistenceContext().getPropertyValue(PARENTPROCESS);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ConsignmentProcess.consignment</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the consignment
	 */
	@Accessor(qualifier = "consignment", type = Accessor.Type.SETTER)
	public void setConsignment(final ConsignmentModel value)
	{
		getPersistenceContext().setPropertyValue(CONSIGNMENT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>ConsignmentProcess.parentProcess</code> attribute defined at extension <code>basecommerce</code>. 
	 *  
	 * @param value the parentProcess
	 */
	@Accessor(qualifier = "parentProcess", type = Accessor.Type.SETTER)
	public void setParentProcess(final OrderProcessModel value)
	{
		getPersistenceContext().setPropertyValue(PARENTPROCESS, value);
	}
	
}
