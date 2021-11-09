/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* tslint:disable:max-classes-per-file */
import * as angular from 'angular';
import {
	SeInjectable,
	SeModule,
	SeModuleConstructor
} from 'smarteditcommons';

@SeInjectable()
export class PersonalizationsmarteditSharedSlotDecoratorController implements angular.IController {

	public active: boolean;
	public smarteditComponentId: string;
	public isPopupOpened: boolean;
	public slotSharedFlag: boolean;

	constructor(
		private slotSharedService: any,
		private $element: JQuery<HTMLElement>
	) {
	}

	positionPanel() {
		// Function copied from slotContextualMenuDecoratorModule.ts
		const decorativePanelArea = this.$element.find('.se-decorative-panel-area');
		const decoratorPaddingContainer = this.$element.find('.se-decoratorative-body-area');
		let marginTop;
		const height = decorativePanelArea.height();
		if (this.$element.offset().top <= height) {
			const borderOffset = 6;
			marginTop = decoratorPaddingContainer.height() + borderOffset;
			decoratorPaddingContainer.css('margin-top', -(marginTop + height));
		} else {
			marginTop = -32;
		}
		decorativePanelArea.css('margin-top', marginTop);
	}

	$onChanges(changes: any) {
		if (changes.active && changes.active.currentValue) {
			this.positionPanel();
			this.isPopupOpened = false;
		}
	}

	$onInit() {
		this.slotSharedFlag = false;
		this.slotSharedService.isSlotShared(this.smarteditComponentId).then((result: any) => {
			this.slotSharedFlag = result;
		});
	}

}

@SeModule({
	imports: [
		'slotSharedServiceModule'
	]
})
export class PersonalizationsmarteditSharedSlotDecoratorModule {}

angular.module((PersonalizationsmarteditSharedSlotDecoratorModule as SeModuleConstructor).moduleName)
	.directive('personalizationsmarteditSharedSlot', () => {
		return {
			templateUrl: 'personalizationsmarteditSharedSlotDecoratorTemplate.html',
			restrict: 'C',
			transclude: true,
			replace: false,
			controller: PersonalizationsmarteditSharedSlotDecoratorController,
			controllerAs: 'ctrl',
			scope: {},
			bindToController: {
				smarteditComponentId: '@',
				active: '<'
			}
		};
	});
