/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {Component, EventEmitter, Inject, Input, OnInit, Output, Type} from '@angular/core';
import {
	setupL10nFilter,
	CrossFrameEventService,
	EVENT_SERVICE,
	LanguageService,
	SeDowngradeComponent,
	TypedMap
} from "smarteditcommons";
import {PersonalizationsmarteditContextService} from "personalizationsmarteditcontainer/service/PersonalizationsmarteditContextServiceOuter";
import {CatalogVersionFilterItemPrinterComponent} from './CatalogVersionFilterItemPrinterComponent';

@SeDowngradeComponent()
@Component({
	selector: 'catalog-version-filter-dropdown',
	template: `        
        <se-select class="perso-filter"
                   (click)="$event.stopPropagation()"
                   [(model)]="selectedId"
                   [onChange]="onChange"
                   [fetchStrategy]="fetchStrategy"
                   [itemComponent]="itemComponent"
                   [searchEnabled]="false"
        ></se-select>
    `
})
export class CatalogVersionFilterDropdownComponent implements OnInit {
	@Input() public initialValue: string;
	@Output() public onSelectCallback: EventEmitter<string> = new EventEmitter<string>();
	public selectedId: string;
	public items: any[];
	public itemComponent: Type<any> = CatalogVersionFilterItemPrinterComponent;
	public fetchStrategy: {
		fetchAll: any;
	};
	private l10nFilter: (localizedMap: TypedMap<string> | string) => TypedMap<string> | string;

	constructor(
		@Inject(EVENT_SERVICE) private crossFrameEventService: CrossFrameEventService,
		@Inject('languageService') private languageService: LanguageService,
		@Inject('componentMenuService') protected componentMenuService: any,
		@Inject(PersonalizationsmarteditContextService) protected personalizationsmarteditContextService: PersonalizationsmarteditContextService
	) {
		this.l10nFilter = setupL10nFilter(this.languageService, this.crossFrameEventService);
		this.onChange = this.onChange.bind(this);
		this.fetchStrategy = {
			fetchAll: () => {
				return Promise.resolve(this.items);
			}
		};
	}

	ngOnInit() {
		this.componentMenuService.getValidContentCatalogVersions().then((catalogVersions: any) => {
			this.items = catalogVersions;
			const experience = this.personalizationsmarteditContextService.getSeData().seExperienceData;
			this.items.forEach((item) => {
				item.isCurrentCatalog = item.id === experience.catalogDescriptor.catalogVersionUuid;
				item.catalogName = this.l10nFilter(item.catalogName);
			});
			this.componentMenuService.getInitialCatalogVersion(this.items).then((selectedCatalogVersion: any) => {
				this.selectedId = this.initialValue || selectedCatalogVersion.id;
			});
		});
	}

	onChange(changes: any): void {
		this.onSelectCallback.emit(this.selectedId);
	}

}
