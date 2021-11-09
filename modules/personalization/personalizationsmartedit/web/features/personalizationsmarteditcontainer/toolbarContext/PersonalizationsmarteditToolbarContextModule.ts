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
import {PersonalizationsmarteditCustomizeToolbarContextComponent} from 'personalizationsmarteditcontainer/toolbarContext/PersonalizationsmarteditCustomizeToolbarContextComponent';
import {PersonalizationsmarteditCombinedViewToolbarContextComponent} from 'personalizationsmarteditcontainer/toolbarContext/PersonalizationsmarteditCombinedViewToolbarContextComponent';
import {PersonalizationsmarteditManageCustomizationViewMenuComponent} from 'personalizationsmarteditcontainer/toolbarContext/PersonalizationsmarteditManageCustomizationViewMenuComponent';

@SeModule({
	imports: [
		PersonalizationsmarteditCommonsModule
	],
	declarations: [
		PersonalizationsmarteditCustomizeToolbarContextComponent,
		PersonalizationsmarteditCombinedViewToolbarContextComponent,
		PersonalizationsmarteditManageCustomizationViewMenuComponent
	]
})
export class PersonalizationsmarteditToolbarContextModule {}