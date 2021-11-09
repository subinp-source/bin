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
import {ManageCustomizationViewComponent} from "personalizationsmarteditcontainer/management/manageCustomizationView/ManageCustomizationViewComponent";
import {BasicInfoTabComponent} from "personalizationsmarteditcontainer/management/manageCustomizationView/BasicInfoTabComponent";
import {TargetGroupTabComponent} from "personalizationsmarteditcontainer/management/manageCustomizationView/TargetGroupTabComponent";
import {PersonalizationsmarteditTriggerService} from "personalizationsmarteditcontainer/management/manageCustomizationView/PersonalizationsmarteditTriggerService";
import {PersonalizationsmarteditCommonsModule} from 'personalizationcommons';
import {PersonalizationsmarteditDataFactory} from 'personalizationsmarteditcontainer/dataFactory/PersonalizationsmarteditDataFactoryModule';
import {
	CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS_PROVIDER,
	CUSTOMIZATION_VARIATION_MANAGEMENT_SEGMENTTRIGGER_GROUPBY_PROVIDER,
	CUSTOMIZATION_VARIATION_MANAGEMENT_TABS_CONSTANTS_PROVIDER,
	DATE_CONSTANTS_PROVIDER,
	PersonalizationsmarteditManager
} from 'personalizationsmarteditcontainer/management/manageCustomizationView/PersonalizationsmarteditManager';

@SeModule({
	imports: [
		'modalServiceModule',
		'coretemplates',
		'ui.select',
		'confirmationModalServiceModule',
		'functionsModule',
		'sliderPanelModule',
		'seConstantsModule',
		'yjqueryModule',
		PersonalizationsmarteditDataFactory,
		PersonalizationsmarteditCommonsModule
	],
	declarations: [
		ManageCustomizationViewComponent,
		BasicInfoTabComponent,
		TargetGroupTabComponent
	],
	providers: [
		PersonalizationsmarteditTriggerService,
		PersonalizationsmarteditManager,
		CUSTOMIZATION_VARIATION_MANAGEMENT_TABS_CONSTANTS_PROVIDER,
		CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS_PROVIDER,
		CUSTOMIZATION_VARIATION_MANAGEMENT_SEGMENTTRIGGER_GROUPBY_PROVIDER,
		DATE_CONSTANTS_PROVIDER
	]
})
export class PersonalizationsmarteditManageCustomizationViewModule {}