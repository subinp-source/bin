/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {Component, Inject, OnInit} from '@angular/core';
import {
	SeDowngradeComponent
} from "smarteditcommons";
import {PersonalizationsmarteditContextService} from "personalizationsmarteditcontainer/service/PersonalizationsmarteditContextServiceOuter";

@SeDowngradeComponent()
@Component({
	selector: 'has-multicatalog',
	template: `
        <div *ngIf="hasMulticatalog">
            <ng-content></ng-content>
        </div>
    `
})
export class HasMulticatalogComponent implements OnInit {

	public hasMulticatalog: boolean;
	constructor(
		@Inject(PersonalizationsmarteditContextService) protected personalizationsmarteditContextService: PersonalizationsmarteditContextService
	) {}

	ngOnInit() {
		this.hasMulticatalog = this.getSeExperienceData().siteDescriptor.contentCatalogs.length > 1;
	}

	getSeExperienceData() {
		return this.personalizationsmarteditContextService.getSeData().seExperienceData;
	}
}
