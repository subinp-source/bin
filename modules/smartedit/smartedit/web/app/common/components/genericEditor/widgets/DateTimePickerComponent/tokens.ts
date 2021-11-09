/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { InjectionToken } from '@angular/core';

/**
 * Contains a map of all inconsistent locales ISOs between SmartEdit and MomentJS
 */
export const RESOLVED_LOCALE_TO_MOMENT_LOCALE_MAP = new InjectionToken(
    'resolvedLocaleToMomentLocaleMap'
);

/**
 * Contains a map of all tooltips to be localized in the date time picker
 */

export const TOOLTIPS_MAP = new InjectionToken('tooltipsMap');
