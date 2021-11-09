/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 06-Oct-2021, 11:13:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.punchout.constants;

/**
 * @deprecated since ages - use constants in Model classes instead
 */
@Deprecated(since = "ages", forRemoval = false)
@SuppressWarnings({"unused","cast"})
public class GeneratedB2bpunchoutConstants
{
	public static final String EXTENSIONNAME = "b2bpunchout";
	public static class TC
	{
		public static final String B2BCUSTOMERPUNCHOUTCREDENTIALMAPPING = "B2BCustomerPunchOutCredentialMapping".intern();
		public static final String ITEMINITEMTYPE = "ItemInItemType".intern();
		public static final String PUNCHOUTCLASSIFICATIONDOMAIN = "PunchOutClassificationDomain".intern();
		public static final String PUNCHOUTCREDENTIAL = "PunchOutCredential".intern();
		public static final String PUNCHOUTORDEROPERATIONALLOWED = "PunchOutOrderOperationAllowed".intern();
		public static final String STOREDPUNCHOUTSESSION = "StoredPunchOutSession".intern();
	}
	public static class Attributes
	{
		public static class AbstractOrder
		{
			public static final String PUNCHOUTORDER = "punchOutOrder".intern();
		}
		public static class Product
		{
			public static final String UNITOFMEASURE = "unitOfMeasure".intern();
			public static final String UNSPCS = "unspcs".intern();
		}
	}
	public static class Enumerations
	{
		public static class ItemInItemType
		{
			public static final String COMPOSITE = "COMPOSITE".intern();
			public static final String ITEM = "ITEM".intern();
		}
		public static class PunchOutClassificationDomain
		{
			public static final String UNSPSC = "UNSPSC".intern();
		}
		public static class PunchOutOrderOperationAllowed
		{
			public static final String CREATE = "create".intern();
			public static final String INSPECT = "inspect".intern();
			public static final String EDIT = "edit".intern();
		}
	}
	public static class Relations
	{
		public static final String PUNCHOUTCREDENTIAL2B2BCUSTOMERPUNCHOUTCREDENTIALMAPPING = "PunchOutCredential2B2BCustomerPunchOutCredentialMapping".intern();
	}
	
	protected GeneratedB2bpunchoutConstants()
	{
		// private constructor
	}
	
	
}
