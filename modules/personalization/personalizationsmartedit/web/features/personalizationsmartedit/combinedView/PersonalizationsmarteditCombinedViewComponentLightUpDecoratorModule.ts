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

/* @internal  */
export interface PersonalizationsmarteditCombinedViewComponentLightUpDecoratorControllerScope extends angular.IScope {
	letterForElement: string;
	classForElement: string;
}

@SeInjectable()
export class PersonalizationsmarteditCombinedViewComponentLightUpDecoratorController implements angular.IController {

	private allBorderClassess: string;
	private unRegister: () => void;

	constructor(
		public $scope: PersonalizationsmarteditCombinedViewComponentLightUpDecoratorControllerScope,
		private personalizationsmarteditContextService: PersonalizationsmarteditContextService,
		private crossFrameEventService: CrossFrameEventService,
		private PERSONALIZATION_COMBINED_VIEW_CSS_MAPPING: any,
		private $element: JQuery<HTMLElement>
	) {
		const allBorderClassessArr: string[] = [];
		Object.keys(this.PERSONALIZATION_COMBINED_VIEW_CSS_MAPPING).forEach((elem, index) => {
			allBorderClassessArr.push(this.PERSONALIZATION_COMBINED_VIEW_CSS_MAPPING[index].borderClass);
		});
		this.allBorderClassess = allBorderClassessArr.join(' ');

		this.$scope.letterForElement = "";
		this.$scope.classForElement = "";
	}

	calculate() {
		const combinedView = this.personalizationsmarteditContextService.getCombinedView();
		if (combinedView.enabled) {
			const container = this.$element.parent().closest('[class~="smartEditComponentX"][data-smartedit-container-source-id][data-smartedit-container-type="CxCmsComponentContainer"][data-smartedit-personalization-customization-id][data-smartedit-personalization-variation-id]');
			if (container.length > 0) {
				container.removeClass(this.allBorderClassess);
				(combinedView.selectedItems || []).forEach((element: any, index: number) => {
					let state = container.data().smarteditPersonalizationCustomizationId === element.customization.code;
					state = state && container.data().smarteditPersonalizationVariationId === element.variation.code;
					const wrappedIndex = index % Object.keys(this.PERSONALIZATION_COMBINED_VIEW_CSS_MAPPING).length;
					if (state) {
						container.addClass(this.PERSONALIZATION_COMBINED_VIEW_CSS_MAPPING[wrappedIndex].borderClass);
						this.$scope.letterForElement = String.fromCharCode('a'.charCodeAt(0) + wrappedIndex).toUpperCase();
						this.$scope.classForElement = this.PERSONALIZATION_COMBINED_VIEW_CSS_MAPPING[wrappedIndex].listClass;
					}
				});
			}
		}
	}

	$onInit() {
		this.unRegister = this.crossFrameEventService.subscribe('PERSONALIZATION_COMBINEDVIEW_CONTEXT_SYNCHRONIZED', () => {
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
		'personalizationsmarteditTemplates',
		'personalizationsmarteditServicesModule',
		'personalizationsmarteditCommonsModule',
		'smarteditServicesModule'
	]
})
export class PersonalizationsmarteditCombinedViewComponentLightUpDecoratorModule {}

angular.module((PersonalizationsmarteditCombinedViewComponentLightUpDecoratorModule as SeModuleConstructor).moduleName)
	.directive('personalizationsmarteditCombinedViewComponentLightUp', () => {
		return {
			templateUrl: 'personalizationsmarteditCombinedViewComponentLightUpDecoratorTemplate.html',
			restrict: 'C',
			transclude: true,
			replace: false,
			controller: PersonalizationsmarteditCombinedViewComponentLightUpDecoratorController,
			controllerAs: 'ctrl',
			scope: {}
		};
	});
