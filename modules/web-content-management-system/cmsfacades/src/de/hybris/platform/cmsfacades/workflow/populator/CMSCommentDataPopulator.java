/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.workflow.populator;

import de.hybris.platform.cms2.model.CMSWorkflowCommentModel;
import de.hybris.platform.cmsfacades.common.service.TimeDiffService;
import de.hybris.platform.cmsfacades.data.CMSCommentData;
import de.hybris.platform.comments.model.CommentModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populates a {@link CMSCommentData} instance from the {@link CommentModel} source data model.
 */
public class CMSCommentDataPopulator implements Populator<CommentModel, CMSCommentData>
{
	private Predicate<CommentModel> cmsWorkflowCommentPredicate;

	private TimeDiffService timeDiffService;

	@Override
	public void populate(final CommentModel commentModel, final CMSCommentData cmsCommentData) throws ConversionException
	{
		cmsCommentData.setCode(commentModel.getCode());
		cmsCommentData.setCreationtime(commentModel.getCreationtime());
		cmsCommentData.setText(commentModel.getText());
		cmsCommentData.setAuthorName(commentModel.getAuthor().getDisplayName());
		cmsCommentData.setCreatedAgoInMillis(getTimeDiffService().difference(commentModel.getCreationtime()));

		if (getCmsWorkflowCommentPredicate().test(commentModel))
		{
			final CMSWorkflowCommentModel cmsWorkflowCommentModel = (CMSWorkflowCommentModel) commentModel;
			if (cmsWorkflowCommentModel.getDecision() != null)
			{
				cmsCommentData.setDecisionCode(cmsWorkflowCommentModel.getDecision().getCode());
				cmsCommentData.setDecisionName(cmsWorkflowCommentModel.getDecision().getName());
				cmsCommentData.setOriginalActionCode(cmsWorkflowCommentModel.getDecision().getAction().getCode());
			}
		}
	}

	protected Predicate<CommentModel> getCmsWorkflowCommentPredicate()
	{
		return cmsWorkflowCommentPredicate;
	}

	@Required
	public void setCmsWorkflowCommentPredicate(final Predicate<CommentModel> cmsWorkflowCommentPredicate)
	{
		this.cmsWorkflowCommentPredicate = cmsWorkflowCommentPredicate;
	}

	protected TimeDiffService getTimeDiffService()
	{
		return timeDiffService;
	}

	@Required
	public void setTimeDiffService(final TimeDiffService timeDiffService)
	{
		this.timeDiffService = timeDiffService;
	}
}
