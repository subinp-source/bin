/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 08-Nov-2021, 4:51:25 PM                     ---
 * ----------------------------------------------------------------
 *  
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.comments.model.CommentTypeModel;
import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;

/**
 * Generated model class for type CMSWorkflowComment first defined at extension cms2.
 */
@SuppressWarnings("all")
public class CMSWorkflowCommentModel extends CommentModel
{
	/**<i>Generated model type code constant.</i>*/
	public static final String _TYPECODE = "CMSWorkflowComment";
	
	/** <i>Generated constant</i> - Attribute key of <code>CMSWorkflowComment.decision</code> attribute defined at extension <code>cms2</code>. */
	public static final String DECISION = "decision";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CMSWorkflowCommentModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CMSWorkflowCommentModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _author initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 * @param _commentType initial attribute declared by type <code>Comment</code> at extension <code>comments</code>
	 * @param _component initial attribute declared by type <code>Comment</code> at extension <code>comments</code>
	 * @param _text initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public CMSWorkflowCommentModel(final UserModel _author, final CommentTypeModel _commentType, final ComponentModel _component, final String _text)
	{
		super();
		setAuthor(_author);
		setCommentType(_commentType);
		setComponent(_component);
		setText(_text);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated since 4.1.1 Please use the default constructor without parameters
	 * @param _author initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 * @param _commentType initial attribute declared by type <code>Comment</code> at extension <code>comments</code>
	 * @param _component initial attribute declared by type <code>Comment</code> at extension <code>comments</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _text initial attribute declared by type <code>AbstractComment</code> at extension <code>comments</code>
	 */
	@Deprecated(since = "4.1.1", forRemoval = true)
	public CMSWorkflowCommentModel(final UserModel _author, final CommentTypeModel _commentType, final ComponentModel _component, final ItemModel _owner, final String _text)
	{
		super();
		setAuthor(_author);
		setCommentType(_commentType);
		setComponent(_component);
		setOwner(_owner);
		setText(_text);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSWorkflowComment.decision</code> attribute defined at extension <code>cms2</code>. 
	 * @return the decision - Decision that generated a comment
	 */
	@Accessor(qualifier = "decision", type = Accessor.Type.GETTER)
	public WorkflowDecisionModel getDecision()
	{
		return getPersistenceContext().getPropertyValue(DECISION);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CMSWorkflowComment.decision</code> attribute defined at extension <code>cms2</code>. 
	 *  
	 * @param value the decision - Decision that generated a comment
	 */
	@Accessor(qualifier = "decision", type = Accessor.Type.SETTER)
	public void setDecision(final WorkflowDecisionModel value)
	{
		getPersistenceContext().setPropertyValue(DECISION, value);
	}
	
}
