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
import {PersonalizationsmarteditContextService} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditContextServiceOuter';
import {PersonalizationsmarteditContextUtils} from 'personalizationcommons/PersonalizationsmarteditContextUtils';
import {PersonalizationsmarteditPreviewService} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditPreviewService';

@SeComponent({
	templateUrl: 'personalizationsmarteditToolbarContextTemplate.html'
})
export class PersonalizationsmarteditCustomizeToolbarContextComponent {

	public visible: boolean;
	public title: string;
	public subtitle: string;

	private selectedCustomization: any;
	private selectedVariations: any;

	constructor(
		protected $scope: angular.IScope,
		protected personalizationsmarteditContextService: PersonalizationsmarteditContextService,
		protected personalizationsmarteditContextUtils: PersonalizationsmarteditContextUtils,
		protected personalizationsmarteditPreviewService: PersonalizationsmarteditPreviewService,
		protected crossFrameEventService: any,
		protected SHOW_TOOLBAR_ITEM_CONTEXT: any,
		protected HIDE_TOOLBAR_ITEM_CONTEXT: any,
		protected CUSTOMIZE_VIEW_TOOLBAR_ITEM_KEY: any
	) {
	}

	$onInit(): void {
		this.selectedCustomization = angular.copy(this.personalizationsmarteditContextService.getCustomize().selectedCustomization);
		this.selectedVariations = angular.copy(this.personalizationsmarteditContextService.getCustomize().selectedVariations);

		this.visible = false;

		if (this.selectedCustomization) {
			this.title = this.personalizationsmarteditContextService.getCustomize().selectedCustomization.name;
			this.visible = true;
			if (!angular.isArray(this.selectedVariations)) {
				this.subtitle = this.selectedVariations.name;
			}
		}

		this.$scope.$watch(() => {
			return this.personalizationsmarteditContextService.getCustomize().selectedCustomization;
		}, (newVal, oldVal) => {
			if (newVal && newVal !== oldVal) {
				this.title = newVal.name;
				this.visible = true;
				this.crossFrameEventService.publish(this.SHOW_TOOLBAR_ITEM_CONTEXT, this.CUSTOMIZE_VIEW_TOOLBAR_ITEM_KEY);
			} else if (!newVal) {
				this.visible = false;
				this.crossFrameEventService.publish(this.HIDE_TOOLBAR_ITEM_CONTEXT, this.CUSTOMIZE_VIEW_TOOLBAR_ITEM_KEY);
			}
		});

		this.$scope.$watch(() => {
			return this.personalizationsmarteditContextService.getCustomize().selectedVariations;
		}, (newVal, oldVal) => {
			if (newVal && newVal !== oldVal) {
				this.subtitle = newVal.name;
			}
		});
	}

	clear() {
		this.personalizationsmarteditContextUtils.clearCustomizeContextAndReloadPreview(this.personalizationsmarteditPreviewService, this.personalizationsmarteditContextService);
		this.crossFrameEventService.publish(this.HIDE_TOOLBAR_ITEM_CONTEXT, this.CUSTOMIZE_VIEW_TOOLBAR_ITEM_KEY);
	}
}
