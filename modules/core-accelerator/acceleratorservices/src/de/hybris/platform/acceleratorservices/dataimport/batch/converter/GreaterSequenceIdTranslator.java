/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.acceleratorservices.dataimport.batch.converter;

/**
 * SequenceId Translator which tolerates equal sequence ids e.g. for localized attributes for which a sequence id
 * attribute cannot be added.
 */
public class GreaterSequenceIdTranslator extends SequenceIdTranslator
{

	@Override
	protected boolean isInValidSequenceId(final Long sequenceId, final Long curSeqId)
	{
		return curSeqId.compareTo(sequenceId) > 0;
	}
}
