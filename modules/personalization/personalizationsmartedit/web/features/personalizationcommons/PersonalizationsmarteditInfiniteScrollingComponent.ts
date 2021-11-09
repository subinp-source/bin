/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {Component, Input, OnChanges, OnInit} from '@angular/core';

import {
	SeDowngradeComponent
} from "smarteditcommons";

@SeDowngradeComponent()
@Component({
	selector: 'personalization-infinite-scrolling',
	templateUrl: './PersonalizationsmarteditInfiniteScrollingComponent.html'
})
export class PersonalizationsmarteditInfiniteScrollingComponent implements OnInit, OnChanges {

	@Input() dropDownContainerClass: string;
	@Input() dropDownClass: string;
	@Input() distance: number = 80;
	@Input() context: {};
	@Input() fetchPage: () => Promise<any>;

	/** @internal */
	public initiated = false;

	ngOnInit() {
		this.context = this.context || this;
		this.init();
	}

	ngOnChanges() {
		this.init();
	}

	public nextPage(): void {
		this.fetchPage();
	}

	private init(): void {
		const wasInitiated = this.initiated;
		this.initiated = true;
		if (wasInitiated) {
			this.nextPage();
		}
	}

}
