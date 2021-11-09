/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {TranslateModule} from '@ngx-translate/core';
import {
	SelectModule
} from 'smarteditcommons';
import {HasMulticatalogComponent} from "./HasMulticatalogComponent";
import {CatalogVersionFilterDropdownComponent} from "./CatalogVersionFilterDropdownComponent";
import {CatalogVersionFilterItemPrinterComponent} from "./CatalogVersionFilterItemPrinterComponent";

/**
 * @ngdoc overview
 * @name PersonalizationsmarteditSharedComponentsModule
 */

@NgModule({
	imports: [
		CommonModule,
		TranslateModule,
		SelectModule
	],
	declarations: [
		HasMulticatalogComponent,
		CatalogVersionFilterDropdownComponent,
		CatalogVersionFilterItemPrinterComponent
	],
	entryComponents: [
		HasMulticatalogComponent,
		CatalogVersionFilterDropdownComponent,
		CatalogVersionFilterItemPrinterComponent
	],
	exports: [
		HasMulticatalogComponent,
		CatalogVersionFilterDropdownComponent,
		CatalogVersionFilterItemPrinterComponent
	]
})
export class PersonalizationsmarteditSharedComponentsModule {}