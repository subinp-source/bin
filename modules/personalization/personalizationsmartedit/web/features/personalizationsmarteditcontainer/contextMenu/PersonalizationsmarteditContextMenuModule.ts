/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {TranslateModule} from '@ngx-translate/core';
import {InlineHelpModule} from '@fundamental-ngx/core';
import {
	SelectModule,
	SharedComponentsModule,
	TooltipModule
} from 'smarteditcommons';
import {
	PersonalizationsmarteditContextMenuAddEditActionComponent
} from "./PersonalizationsmarteditContextMenuAddEditActionComponent";
import {PersonalizationsmarteditSharedComponentsModule} from "../commonComponents/PersonalizationsmarteditSharedComponentsModule";
import {ComponentDropdownItemPrinterComponent} from "./ComponentDropdownItemPrinterComponent";

/**
 * @ngdoc overview
 * @name PersonalizationsmarteditContextMenuModule
 */

@NgModule({
	imports: [
		CommonModule,
		TranslateModule,
		SharedComponentsModule,
		InlineHelpModule,
		SelectModule,
		TooltipModule,
		PersonalizationsmarteditSharedComponentsModule
	],
	declarations: [
		PersonalizationsmarteditContextMenuAddEditActionComponent,
		ComponentDropdownItemPrinterComponent
	],
	entryComponents: [
		PersonalizationsmarteditContextMenuAddEditActionComponent,
		ComponentDropdownItemPrinterComponent
	],
	exports: [
		PersonalizationsmarteditContextMenuAddEditActionComponent,
		ComponentDropdownItemPrinterComponent
	]
})
export class PersonalizationsmarteditContextMenuModule {}
