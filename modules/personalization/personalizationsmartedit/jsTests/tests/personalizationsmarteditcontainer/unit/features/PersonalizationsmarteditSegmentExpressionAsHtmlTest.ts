/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

import 'jasmine';
import {PersonalizationsmarteditTriggerService} from '../../../../../web/features/personalizationsmarteditcontainer/management/manageCustomizationView/PersonalizationsmarteditTriggerService';
import {PersonalizationsmarteditSegmentExpressionAsHtmlComponent} from '../../../../../web/features/personalizationsmarteditcontainer/management/manageCustomizationView/segmentView/segmentExpressionAsHtml/PersonalizationsmarteditSegmentExpressionAsHtmlComponent';

describe('personalizationsmarteditSegmentExpressionAsHtml', () => {

	let triggerService: PersonalizationsmarteditTriggerService;
	let segmentExpressionAsHtmlComponent: PersonalizationsmarteditSegmentExpressionAsHtmlComponent;

	beforeEach(() => {
		triggerService = new PersonalizationsmarteditTriggerService();
		segmentExpressionAsHtmlComponent = new PersonalizationsmarteditSegmentExpressionAsHtmlComponent(triggerService);
	});

	describe('Component API', () => {
		it('should have proper api when initialized', () => {
			expect(segmentExpressionAsHtmlComponent.segmentExpression).toEqual({});
			expect(segmentExpressionAsHtmlComponent.operators).toEqual(['AND', 'OR', 'NOT']);
			expect(segmentExpressionAsHtmlComponent.emptyGroup).toEqual('[]');
			expect(segmentExpressionAsHtmlComponent.emptyGroupAndOperators).toEqual(['AND', 'OR', 'NOT', '[]']);
			expect(segmentExpressionAsHtmlComponent.getExpressionAsArray).toBeDefined();
		});
	});
});
