import * as angular from 'angular';
import {PersonalizationsmarteditRestService} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditRestService';

/* @internal */
export const ContextMenuDeleteActionControllerFactory = (config: any): angular.IControllerConstructor => {

	/* @ngInject */
	class PersonalizationsmarteditContextMenuDeleteActionController {

		public catalog: string = config.catalog;
		public catalogVersion: string = config.catalogVersion;
		public selectedCustomizationCode: string = config.selectedCustomizationCode;
		public selectedVariationCode: string = config.selectedVariationCode;
		public actionId: string = config.actionId;

		constructor(
			private $q: angular.IQService,
			private modalManager: any,
			private personalizationsmarteditRestService: PersonalizationsmarteditRestService
		) {
			this.modalManager.setButtonHandler(this.buttonHandlerFn);
		}

		buttonHandlerFn = (buttonId: any) => {
			const deferred = this.$q.defer();
			if (buttonId === 'confirmOk') {
				const filter = {
					catalog: this.catalog,
					catalogVersion: this.catalogVersion
				};
				return this.personalizationsmarteditRestService.deleteAction(this.selectedCustomizationCode, this.selectedVariationCode, this.actionId, filter);
			}
			return deferred.reject();
		}
	}

	return PersonalizationsmarteditContextMenuDeleteActionController;

};