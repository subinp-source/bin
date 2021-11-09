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

import {SeComponent} from 'smarteditcommons';
import {PersonalizationsmarteditContextService} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditContextServiceOuter';
import {PersonalizationsmarteditContextUtils} from 'personalizationcommons/PersonalizationsmarteditContextUtils';
import {PersonalizationsmarteditPreviewService} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditPreviewService';

@SeComponent({
	templateUrl: 'manageCustomizationViewMenuTemplate.html'
})
export class PersonalizationsmarteditManageCustomizationViewMenuComponent {

	constructor(
		protected personalizationsmarteditContextService: PersonalizationsmarteditContextService,
		protected personalizationsmarteditContextUtils: PersonalizationsmarteditContextUtils,
		protected personalizationsmarteditPreviewService: PersonalizationsmarteditPreviewService,
		protected personalizationsmarteditManager: any,
		protected personalizationsmarteditManagerView: any
	) {
	}

	createCustomizationClick() {
		this.personalizationsmarteditManager.openCreateCustomizationModal();
	}

	managerViewClick() {
		this.personalizationsmarteditContextUtils.clearCombinedViewCustomizeContext(this.personalizationsmarteditContextService);
		this.personalizationsmarteditContextUtils.clearCustomizeContextAndReloadPreview(this.personalizationsmarteditPreviewService, this.personalizationsmarteditContextService);
		this.personalizationsmarteditContextUtils.clearCombinedViewContextAndReloadPreview(this.personalizationsmarteditPreviewService, this.personalizationsmarteditContextService);
		this.personalizationsmarteditManagerView.openManagerAction();
	}
}
