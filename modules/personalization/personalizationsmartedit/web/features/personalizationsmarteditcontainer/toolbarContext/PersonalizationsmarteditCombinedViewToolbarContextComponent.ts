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

import * as angular from 'angular';
import {SeComponent} from 'smarteditcommons';
import {PersonalizationsmarteditCombinedViewCommonsService} from 'personalizationsmarteditcontainer/combinedView/PersonalizationsmarteditCombinedViewCommonsService';
import {PersonalizationsmarteditContextService} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditContextServiceOuter';
import {PersonalizationsmarteditContextUtils} from 'personalizationcommons/PersonalizationsmarteditContextUtils';

@SeComponent({
	templateUrl: 'personalizationsmarteditToolbarContextTemplate.html'
})
export class PersonalizationsmarteditCombinedViewToolbarContextComponent {

	public visible: boolean;
	public title: string;
	public subtitle: string;

	private selectedCustomization: any;

	constructor(
		protected $scope: angular.IScope,
		protected personalizationsmarteditCombinedViewCommonsService: PersonalizationsmarteditCombinedViewCommonsService,
		protected personalizationsmarteditContextService: PersonalizationsmarteditContextService,
		protected personalizationsmarteditContextUtils: PersonalizationsmarteditContextUtils,
		protected crossFrameEventService: any,
		protected SHOW_TOOLBAR_ITEM_CONTEXT: any,
		protected HIDE_TOOLBAR_ITEM_CONTEXT: any,
		protected COMBINED_VIEW_TOOLBAR_ITEM_KEY: any
	) {
	}

	$onInit(): void {
		this.selectedCustomization = angular.copy(this.personalizationsmarteditContextService.getCombinedView().customize.selectedCustomization);

		this.visible = false;

		if (this.selectedCustomization) {
			this.title = this.personalizationsmarteditContextService.getCombinedView().customize.selectedCustomization.name;
			this.subtitle = this.personalizationsmarteditContextService.getCombinedView().customize.selectedVariations.name;
			this.visible = true;
		}

		this.$scope.$watch(() => {
			return this.personalizationsmarteditContextService.getCombinedView().customize.selectedCustomization;
		}, (newVal, oldVal) => {
			if (newVal && newVal !== oldVal) {
				this.title = newVal.name;
				this.subtitle = this.personalizationsmarteditContextService.getCombinedView().customize.selectedVariations.name;
				this.visible = true;
				this.crossFrameEventService.publish(this.SHOW_TOOLBAR_ITEM_CONTEXT, this.COMBINED_VIEW_TOOLBAR_ITEM_KEY);
			} else if (!newVal) {
				this.visible = false;
				this.crossFrameEventService.publish(this.HIDE_TOOLBAR_ITEM_CONTEXT, this.COMBINED_VIEW_TOOLBAR_ITEM_KEY);
			}
		});

		this.$scope.$watch(() => {
			return this.personalizationsmarteditContextService.getCombinedView().enabled;
		}, (newVal, oldVal) => {
			if (newVal === false && newVal !== oldVal) {
				this.personalizationsmarteditContextUtils.clearCombinedViewCustomizeContext(this.personalizationsmarteditContextService);
			}
		});
	}

	clear() {
		this.personalizationsmarteditContextUtils.clearCombinedViewCustomizeContext(this.personalizationsmarteditContextService);
		const combinedView = this.personalizationsmarteditContextService.getCombinedView();
		const variations: any[] = [];
		(combinedView.selectedItems || []).forEach((item: any) => {
			variations.push({
				customizationCode: item.customization.code,
				variationCode: item.variation.code,
				catalog: item.variation.catalog,
				catalogVersion: item.variation.catalogVersion
			});
		});
		this.personalizationsmarteditCombinedViewCommonsService.updatePreview(variations);
		this.crossFrameEventService.publish(this.HIDE_TOOLBAR_ITEM_CONTEXT, this.COMBINED_VIEW_TOOLBAR_ITEM_KEY);
	}
}
