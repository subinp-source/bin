/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cmsfacades.common.function;

import java.util.function.BiConsumer;

import org.springframework.validation.Errors;


/**
 * ValidationConsumer is an interface that works along with other Validators to help with the validation work.
 * It extends the Consumer interface and it should perform the validation work.
 * @param <T> the type of the object to be validated
 */
@FunctionalInterface
public interface ValidationConsumer<T> extends BiConsumer<T, Errors>
{
	// Intentionally left empty.
}
