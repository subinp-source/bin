/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* tslint:disable:max-classes-per-file */
import * as angular from 'angular';
import {
	CrossFrameEventService,
	SeInjectable,
	SeModule,
	SeModuleConstructor
} from 'smarteditcommons';

import {PersonalizationsmarteditContextService} from 'personalizationsmartedit/service/PersonalizationsmarteditContextServiceInner';
import {PersonalizationsmarteditComponentHandlerService} from 'personalizationsmartedit/service/PersonalizationsmarteditComponentHandlerService';


@SeInjectable()
export class PersonalizationsmarteditComponentLightUpDecoratorController implements angular.IController {

	private CONTAINER_TYPE: string = 'CxCmsComponentContainer';
	private ACTION_ID_ATTR: string = 'data-smartedit-personalization-action-id';
	private PARENT_CONTAINER_SELECTOR: string;
	private PARENT_CONTAINER_WITH_ACTION_SELECTOR: string;
	private COMPONENT_SELECTOR: string;
	private unRegister: () => void;

	constructor(
		private personalizationsmarteditContextService: PersonalizationsmarteditContextService,
		private personalizationsmarteditComponentHandlerService: PersonalizationsmarteditComponentHandlerService,
		private crossFrameEventService: CrossFrameEventService,
		private CONTAINER_SOURCE_ID_ATTR: string,
		private OVERLAY_COMPONENT_CLASS: string,
		private CONTAINER_TYPE_ATTRIBUTE: string,
		private ID_ATTRIBUTE: string,
		private TYPE_ATTRIBUTE: string,
		private CATALOG_VERSION_UUID_ATTRIBUTE: string,
		private $element: JQuery<HTMLElement>,
		private yjQuery: JQueryStatic
	) {
		this.PARENT_CONTAINER_SELECTOR = '[class~="' + this.OVERLAY_COMPONENT_CLASS + '"][' + this.CONTAINER_SOURCE_ID_ATTR + '][' + this.CONTAINER_TYPE_ATTRIBUTE + '="' + this.CONTAINER_TYPE + '"]';
		this.PARENT_CONTAINER_WITH_ACTION_SELECTOR = '[class~="' + this.OVERLAY_COMPONENT_CLASS + '"][' + this.CONTAINER_TYPE_ATTRIBUTE + '="' + this.CONTAINER_TYPE + '"][' + this.ACTION_ID_ATTR + ']';
		this.COMPONENT_SELECTOR = '[' + this.ID_ATTRIBUTE + '][' + this.CATALOG_VERSION_UUID_ATTRIBUTE + '][' + this.TYPE_ATTRIBUTE + ']';
	}


	isComponentSelected(): boolean {
		let elementSelected = false;
		if (angular.isArray(this.personalizationsmarteditContextService.getCustomize().selectedVariations)) {
			const containerId = this.personalizationsmarteditComponentHandlerService.getParentContainerIdForComponent(this.$element);
			elementSelected = this.yjQuery.inArray(containerId, this.personalizationsmarteditContextService.getCustomize().selectedComponents) > -1;
		}
		return elementSelected;
	}

	isVariationComponentSelected(component: JQuery<HTMLElement>): boolean {
		let elementSelected = false;
		const customize = this.personalizationsmarteditContextService.getCustomize();
		if (customize.selectedCustomization && customize.selectedVariations) {
			const container = component.closest(this.PARENT_CONTAINER_WITH_ACTION_SELECTOR);
			elementSelected = container.length > 0;
		}
		return elementSelected;
	}

	calculate(): void {
		const component = this.$element.parent().closest(this.COMPONENT_SELECTOR);
		const container = component.closest(this.PARENT_CONTAINER_SELECTOR);
		container.toggleClass("perso__component-decorator", this.isVariationComponentSelected(component));
		container.toggleClass("hyicon hyicon-checkedlg perso__component-decorator-icon", this.isVariationComponentSelected(component));
		container.toggleClass("personalizationsmarteditComponentSelected", this.isComponentSelected());
	}

	$onInit() {
		this.unRegister = this.crossFrameEventService.subscribe('PERSONALIZATION_CUSTOMIZE_CONTEXT_SYNCHRONIZED', () => {
			this.calculate();
		});
		this.calculate();
	}

	$onDestroy() {
		this.unRegister();
	}

}

@SeModule({
	imports: [
		'yjqueryModule',
		'personalizationsmarteditTemplates',
		'personalizationsmarteditServicesModule',
		'smarteditServicesModule'
	]
})
export class PersonalizationsmarteditComponentLightUpDecoratorModule {}

angular.module((PersonalizationsmarteditComponentLightUpDecoratorModule as SeModuleConstructor).moduleName)
	.directive('personalizationsmarteditComponentLightUp', () => {
		return {
			templateUrl: 'personalizationsmarteditComponentLightUpDecoratorTemplate.html',
			restrict: 'C',
			transclude: true,
			replace: false,
			controller: PersonalizationsmarteditComponentLightUpDecoratorController,
			controllerAs: 'ctrl',
			scope: {}
		};
	});
