/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ISeComponent, SeComponent } from 'smarteditcommons/di';

/**
 * @ngdoc directive
 * @name SmarteditCommonsModule.component:yMoreTextComponent
 * @deprecated since 2005
 * @element more-text
 * @description
 * Deprecated, use {@link SmarteditCommonsModule.component:MoreTextComponent MoreTextComponent}
 * The component for truncating strings and adding an ellipsis.
 * If the limit is less then the string length then the string is truncated and 'more'/'less' buttons
 * are displayed to expand or collapse the string.
 *
 * @param {< String} text the text to be displayed
 * @param {< String =} limit index in text to truncate to. Default value is 100.
 * @param {< String =} moreLabelI18nKey the label property value for a more button. Default value is 'more'.
 * @param {< String =} lessLabelI18nKey the label property value for a less button. Default value is 'less'.
 * @param {< String =} ellipsis the ellipsis for a truncated text. Default value is an empty string.
 */
@SeComponent({
    templateUrl: 'moreTextTemplate.html',
    inputs: ['text', 'limit:?', 'moreLabelI18nKey:?', 'lessLabelI18nKey:?', 'ellipsis:?']
})
export class YMoreTextComponent implements ISeComponent {}
