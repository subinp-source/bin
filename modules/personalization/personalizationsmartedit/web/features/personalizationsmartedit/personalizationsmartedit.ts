/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
import {BrowserModule} from '@angular/platform-browser';
import {UpgradeModule} from '@angular/upgrade/static';
import {NgModule} from '@angular/core';
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {ISharedDataService, SeEntryModule} from 'smarteditcommons';
import {BaseSiteHeaderInterceptor} from "personalizationcommons";
import {PersonalizationsmarteditCommonsComponentsModule} from "../personalizationcommons/PersonalizationsmarteditCommonsComponentsModule";

@SeEntryModule('personalizationsmarteditmodule')
@NgModule({
	imports: [
		BrowserModule,
		UpgradeModule,
		PersonalizationsmarteditCommonsComponentsModule
	],
	providers: [
		{
			provide: HTTP_INTERCEPTORS,
			useClass: BaseSiteHeaderInterceptor,
			multi: true,
			deps: [ISharedDataService]
		}
	]
})
export class Personalizationsmartedit {}