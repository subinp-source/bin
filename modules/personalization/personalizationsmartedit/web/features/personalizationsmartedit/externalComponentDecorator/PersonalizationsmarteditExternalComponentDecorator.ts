/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* tslint:disable:max-classes-per-file */
import * as angular from 'angular';
import {
	SeModule,
	SeModuleConstructor
} from 'smarteditcommons';

@SeModule({
	imports: [
		'personalizationsmarteditTemplates'
	]
})
export class PersonalizationsmarteditExternalComponentDecoratorModule {}

angular.module((PersonalizationsmarteditExternalComponentDecoratorModule as SeModuleConstructor).moduleName)
	.directive('personalizationsmarteditExternalComponentDecorator', () => {
		return {
			templateUrl: 'personalizationsmarteditExternalComponentDecoratorTemplate.html',
			restrict: 'C',
			transclude: true,
			replace: false,
			controller: 'externalComponentDecoratorController',
			controllerAs: 'ctrl',
			scope: {},
			bindToController: {
				active: '=',
				componentAttributes: '<'
			}
		};
	});