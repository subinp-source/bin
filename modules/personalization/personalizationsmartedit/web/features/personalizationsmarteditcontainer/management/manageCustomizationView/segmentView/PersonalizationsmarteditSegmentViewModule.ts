/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

import {SeModule} from 'smarteditcommons';
import {PersonalizationsmarteditCommonsModule} from 'personalizationcommons';
import {PersonalizationsmarteditServicesModule} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditServicesModule';
import {PersonalizationsmarteditManagementModule} from 'personalizationsmarteditcontainer/management/PersonalizationsmarteditManagementModule';
import {PersonalizationsmarteditSegmentExpressionAsHtmlComponent} from 'personalizationsmarteditcontainer/management/manageCustomizationView/segmentView/segmentExpressionAsHtml/PersonalizationsmarteditSegmentExpressionAsHtmlComponent';
import {PersonalizationsmarteditSegmentViewComponent} from 'personalizationsmarteditcontainer/management/manageCustomizationView/segmentView/PersonalizationsmarteditSegmentViewComponent';
import {PersonalizationsmarteditCommerceCustomizationModule} from 'personalizationsmarteditcontainer/management/commerceCustomizationView/PersonalizationsmarteditCommerceCustomizationModule';
import {PersonalizationsmarteditManageCustomizationViewModule} from 'personalizationsmarteditcontainer/management/manageCustomizationView/PersonalizationsmarteditManageCustomizationViewModule';

@SeModule({
	imports: [
		'modalServiceModule',
		'confirmationModalServiceModule',
		'ui.tree',
		PersonalizationsmarteditCommonsModule,
		PersonalizationsmarteditServicesModule,
		PersonalizationsmarteditManagementModule,
		PersonalizationsmarteditCommerceCustomizationModule,
		PersonalizationsmarteditManageCustomizationViewModule
	],
	declarations: [
		PersonalizationsmarteditSegmentViewComponent,
		PersonalizationsmarteditSegmentExpressionAsHtmlComponent
	]
})
export class PersonalizationsmarteditSegmentViewModule {}