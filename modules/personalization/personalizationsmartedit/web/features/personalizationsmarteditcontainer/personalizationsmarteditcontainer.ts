/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {UpgradeModule} from '@angular/upgrade/static';
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {diBridgeUtils, ISharedDataService, SeEntryModule} from 'smarteditcommons';
import {BaseSiteHeaderInterceptor} from "personalizationcommons";
import {PersonalizationsmarteditCommonsComponentsModule} from "../personalizationcommons/PersonalizationsmarteditCommonsComponentsModule";
import {PersonalizationsmarteditContextMenuModule} from "../personalizationsmarteditcontainer/contextMenu/PersonalizationsmarteditContextMenuModule";
import {PersonalizationsmarteditRestService} from "./service/PersonalizationsmarteditRestService";
import {PersonalizationsmarteditMessageHandler} from "../personalizationcommons/PersonalizationsmarteditMessageHandler";
import {PersonalizationsmarteditContextService} from "./service/PersonalizationsmarteditContextServiceOuter";
import {PersonalizationsmarteditSharedComponentsModule} from "./commonComponents/PersonalizationsmarteditSharedComponentsModule";

@SeEntryModule('personalizationsmarteditcontainermodule')
@NgModule({
	imports: [
		BrowserModule,
		UpgradeModule,
		PersonalizationsmarteditCommonsComponentsModule,
		PersonalizationsmarteditContextMenuModule,
		PersonalizationsmarteditSharedComponentsModule
	],
	providers: [
		{
			provide: HTTP_INTERCEPTORS,
			useClass: BaseSiteHeaderInterceptor,
			multi: true,
			deps: [ISharedDataService]
		},
		diBridgeUtils.upgradeProvider('personalizationsmarteditRestService', PersonalizationsmarteditRestService),
		diBridgeUtils.upgradeProvider('personalizationsmarteditMessageHandler', PersonalizationsmarteditMessageHandler),
		diBridgeUtils.upgradeProvider('personalizationsmarteditContextService', PersonalizationsmarteditContextService),
		diBridgeUtils.upgradeProvider('PERSONALIZATION_COMBINED_VIEW_CSS_MAPPING'),
		diBridgeUtils.upgradeProvider('MODAL_BUTTON_ACTIONS'),
		diBridgeUtils.upgradeProvider('MODAL_BUTTON_STYLES'),
		diBridgeUtils.upgradeProvider('slotRestrictionsService'),
		diBridgeUtils.upgradeProvider('editorModalService'),
		diBridgeUtils.upgradeProvider('componentMenuService'),
		diBridgeUtils.upgradeProvider('languageService')
	]
})
export class PersonalizationsmarteditContainer {}
