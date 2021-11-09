/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {PersonalizationsmarteditInfiniteScrollingComponent} from './PersonalizationsmarteditInfiniteScrollingComponent';
import {PersonalizationInfiniteScrollDirective} from './PersonalizationInfiniteScrollDirective';
import {PersonalizationPreventParentScrollDirective} from "./PersonalizationPreventParentScrollDirective";
import {PersonalizationPreventParentScrollComponent} from "./PersonalizationPreventParentScrollComponent";

/**
 * @ngdoc overview
 * @name PersonalizationsmarteditCommonsComponentsModule
 */

@NgModule({
	imports: [
		CommonModule
	],
	declarations: [
		PersonalizationInfiniteScrollDirective,
		PersonalizationPreventParentScrollDirective,
		PersonalizationsmarteditInfiniteScrollingComponent,
		PersonalizationPreventParentScrollComponent
	],
	entryComponents: [
		PersonalizationsmarteditInfiniteScrollingComponent,
		PersonalizationPreventParentScrollComponent
	],
	exports: [
		PersonalizationInfiniteScrollDirective,
		PersonalizationPreventParentScrollDirective,
		PersonalizationsmarteditInfiniteScrollingComponent,
		PersonalizationPreventParentScrollComponent
	]
})
export class PersonalizationsmarteditCommonsComponentsModule {}
