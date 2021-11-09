import * as angular from "angular";
import {TypedMap} from 'smarteditcommons';
import {PersonalizationsmarteditContextService} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditContextServiceOuter';
import {PersonalizationsmarteditMessageHandler} from 'personalizationcommons/PersonalizationsmarteditMessageHandler';
import {PersonalizationsmarteditUtils} from 'personalizationcommons/PersonalizationsmarteditUtils';
import {PersonalizationsmarteditRestService} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditRestService';
import {PersonalizationsmarteditCommerceCustomizationService} from 'personalizationcommons/PersonalizationsmarteditCommerceCustomizationService';
import {ActionsDataFactory} from 'personalizationsmarteditcontainer/management/commerceCustomizationView/ActionsDataFactory';

/* @internal  */
export interface PersonalizationsmarteditCommerceCustomizationViewControllerScope extends angular.IScope {
	actions: any[];
	isItemInSelectedActions: any;
	removeSelectedAction: any;
	addAction: any;
	customization: any;
	variation: any;
}

/* @internal */
export const PersonalizationsmarteditCommerceCustomizationViewControllerFactory = (customization: any, variation: any): angular.IControllerConstructor => {

	/* @ngInject */
	class PersonalizationsmarteditCommerceCustomizationViewController {

		public init: () => void;

		public availableTypes: any[] = [];
		public select: {} = {};
		public customizationStatusText: any;
		public variationStatusText: any;
		public customizationStatus: any;
		public variationStatus: any;

		private removedActions: any;

		constructor(
			private $scope: PersonalizationsmarteditCommerceCustomizationViewControllerScope,
			private $translate: angular.translate.ITranslateService,
			private $q: angular.IQService,
			private actionsDataFactory: ActionsDataFactory,
			private personalizationsmarteditRestService: PersonalizationsmarteditRestService,
			private personalizationsmarteditMessageHandler: PersonalizationsmarteditMessageHandler,
			private systemEventService: any,
			private personalizationsmarteditCommerceCustomizationService: PersonalizationsmarteditCommerceCustomizationService,
			private personalizationsmarteditContextService: PersonalizationsmarteditContextService,
			private personalizationsmarteditUtils: PersonalizationsmarteditUtils,
			private confirmationModalService: any,
			private PERSONALIZATION_COMMERCE_CUSTOMIZATION_ACTION_STATUSES: TypedMap<string>,
			private modalManager: any,
			private $log: angular.ILogService
		) {

			this.availableTypes = this.personalizationsmarteditCommerceCustomizationService.getAvailableTypes(this.personalizationsmarteditContextService.getSeData().seConfigurationData);
			this.select = {
				type: this.availableTypes[0]
			};
			this.$scope.customization = customization;
			this.$scope.variation = variation;

			this.actionsDataFactory.resetActions();
			this.actionsDataFactory.resetRemovedActions();
			this.$scope.actions = this.actionsDataFactory.getActions();
			this.removedActions = this.actionsDataFactory.getRemovedActions();
			this.customizationStatusText = this.personalizationsmarteditUtils.getEnablementTextForCustomization(this.$scope.customization, 'personalization.modal.commercecustomization');
			this.variationStatusText = this.personalizationsmarteditUtils.getEnablementTextForVariation(this.$scope.variation, 'personalization.modal.commercecustomization');
			this.customizationStatus = this.personalizationsmarteditUtils.getActivityStateForCustomization(this.$scope.customization);
			this.variationStatus = this.personalizationsmarteditUtils.getActivityStateForVariation(this.$scope.customization, this.$scope.variation);

			this.init = () => {

				this.$scope.isItemInSelectedActions = this.isItemInSelectedActions;
				this.$scope.removeSelectedAction = this.removeSelectedAction;
				this.$scope.addAction = this.addAction;

				this.populateActions();

				this.$scope.$watch('actions', () => {
					if (this.isDirty()) {
						this.modalManager.enableButton("confirmSave");
					} else {
						this.modalManager.disableButton("confirmSave");
					}
				}, true);
				this.modalManager.setButtonHandler((buttonId: any) => {
					if (buttonId === 'confirmSave') {
						this.onSave();
					} else if (buttonId === 'confirmCancel') {
						return this.dismissModalCallback();
					}
				});
				this.modalManager.setDismissCallback(() => {
					return this.dismissModalCallback();
				});

			};
		}

		isItemInSelectedActions = (action: any, comparer: any) => {
			return this.actionsDataFactory.isItemInSelectedActions(action, comparer);
		}

		removeSelectedAction = (actionWrapper: any) => {
			const index = this.$scope.actions.indexOf(actionWrapper);
			if (index < 0) {
				return;
			}
			const removed = this.$scope.actions.splice(index, 1);
			// only old item should be added to delete queue
			// new items are just deleted
			if (removed[0].status === this.PERSONALIZATION_COMMERCE_CUSTOMIZATION_ACTION_STATUSES.OLD ||
				removed[0].status === this.PERSONALIZATION_COMMERCE_CUSTOMIZATION_ACTION_STATUSES.UPDATE) {
				removed[0].status = this.PERSONALIZATION_COMMERCE_CUSTOMIZATION_ACTION_STATUSES.DELETE;
				this.removedActions.push(removed[0]);
			}
		}

		// This function requires two parameters
		// action to be added
		// and comparer = function(action,action) for defining if two actions are identical
		// comparer is used
		addAction = (action: any, comparer: any) => {
			this.actionsDataFactory.addAction(action, comparer);
		}

		displayAction = (actionWrapper: any) => {
			const action = actionWrapper.action;
			const type = this.getType(action.type);
			if (type.getName) {
				return type.getName(action);
			} else {
				return action.code;
			}
		}

		getActionsToDisplay = () => {
			return this.actionsDataFactory.getActions();
		}

		private populateActions = () => {
			this.personalizationsmarteditRestService.getActions(this.$scope.customization.code, this.$scope.variation.code, {})
				.then((response: any) => {
					const actions = response.actions.filter((elem: any) => {
						return elem.type !== 'cxCmsActionData';
					}).map((item: any) => {
						return {
							code: item.code,
							action: item,
							status: this.PERSONALIZATION_COMMERCE_CUSTOMIZATION_ACTION_STATUSES.OLD
						};
					});
					this.actionsDataFactory.resetActions();
					this.personalizationsmarteditUtils.uniqueArray(this.actionsDataFactory.actions, actions || []);
				}, () => {
					this.personalizationsmarteditMessageHandler.sendError(this.$translate.instant('personalization.error.gettingactions'));
				});
		}

		private getType = (type: any) => {
			for (const item of this.availableTypes) {
				if (item.type === type) {
					return item;
				}
			}
			return {};
		}

		private sendRefreshEvent = () => {
			this.systemEventService.publishAsync('CUSTOMIZATIONS_MODIFIED', {});
		}

		private dismissModalCallback = () => {
			if (this.isDirty()) {
				return this.confirmationModalService.confirm({
					description: 'personalization.modal.commercecustomization.cancelconfirmation'
				}).then(() => {
					return this.$q.resolve();
				}, () => {
					return this.$q.reject();
				});
			} else {
				return this.$q.resolve();
			}
		}

		private isDirty = () => {
			let dirty = false;
			// dirty if at least one new
			this.$scope.actions.forEach((wrapper: any) => {
				dirty = dirty || wrapper.status === this.PERSONALIZATION_COMMERCE_CUSTOMIZATION_ACTION_STATUSES.NEW ||
					wrapper.status === this.PERSONALIZATION_COMMERCE_CUSTOMIZATION_ACTION_STATUSES.UPDATE;
			});
			// or one deleted
			dirty = dirty || this.removedActions.length > 0;
			return dirty;
		}

		// modal buttons
		private onSave = () => {
			const createData = {
				actions: this.$scope.actions.filter((item: any) => {
					return item.status === this.PERSONALIZATION_COMMERCE_CUSTOMIZATION_ACTION_STATUSES.NEW;
				}).map((item: any) => {
					return item.action;
				})
			};
			const deleteData = this.removedActions.filter((item: any) => {
				return item.status === this.PERSONALIZATION_COMMERCE_CUSTOMIZATION_ACTION_STATUSES.DELETE;
			}).map((item: any) => {
				return item.action.code;
			});
			const updateData = {
				actions: this.$scope.actions.filter((item: any) => {
					return item.status === this.PERSONALIZATION_COMMERCE_CUSTOMIZATION_ACTION_STATUSES.UPDATE;
				}).map((item: any) => {
					return item.action;
				})
			};
			const shouldCreate = createData.actions.length > 0;
			const shouldDelete = deleteData.length > 0;
			const shouldUpdate = updateData.actions.length > 0;

			((): any => {
				if (shouldCreate) {
					return this.createActions(this.$scope.customization.code, this.$scope.variation.code, createData);
				} else {
					return this.$q.resolve();
				}
			})().then((respCreate: any) => {
				((): any => {
					if (shouldDelete) {
						return this.deleteActions(this.$scope.customization.code, this.$scope.variation.code, deleteData);
					} else {
						return this.$q.resolve();
					}
				})().then((respDelete: any) => {
					if (shouldUpdate) {
						this.updateActions(this.$scope.customization.code, this.$scope.variation.code, updateData, respCreate, respDelete);
					}
				});
			});
		}

		// customization and variation status helper functions
		private getActionTypesForActions = (actions: any) => {
			return actions.map((a: any) => {
				return a.type;
			}).filter((item: any, index: any, arr: any) => {
				// removes duplicates from mapped array
				return arr.indexOf(item) === index;
			}).map((typeCode: any) => {
				return this.availableTypes.filter((availableType: any) => {
					return availableType.type === typeCode;
				})[0];
			});
		}

		private createActions = (customizationCode: any, variationCode: any, createData: any) => {
			const deferred = this.$q.defer();
			this.personalizationsmarteditRestService.createActions(customizationCode, variationCode, createData, {})
				.then((response: any) => {
					this.personalizationsmarteditMessageHandler.sendSuccess(this.$translate.instant('personalization.info.creatingaction'));
					this.sendRefreshEvent();
					deferred.resolve(response);
				}, () => {
					this.personalizationsmarteditMessageHandler.sendError(this.$translate.instant('personalization.error.creatingaction'));
					deferred.reject();
				});
			return deferred.promise;
		}

		private deleteActions = (customizationCode: any, variationCode: any, deleteData: any) => {
			const deferred = this.$q.defer();
			this.personalizationsmarteditRestService.deleteActions(customizationCode, variationCode, deleteData, {})
				.then((response: any) => {
					this.personalizationsmarteditMessageHandler.sendSuccess(this.$translate.instant('personalization.info.removingaction'));
					this.sendRefreshEvent();
					deferred.resolve(response);
				}, () => {
					this.personalizationsmarteditMessageHandler.sendError(this.$translate.instant('personalization.error.removingaction'));
					deferred.resolve();
				});
			return deferred.promise;
		}

		private updateActions = (customizationCode: any, variationCode: any, updateData: any, respCreate: any, respDelete: any) => {
			const updateTypes = this.getActionTypesForActions(updateData.actions);

			updateTypes.forEach((type: any) => {
				if (type.updateActions) {
					const actionsForType = updateData.actions.filter((a: any) => {
						return this.getType(a.type) === type;
					});
					type.updateActions(customizationCode, variationCode, actionsForType, respCreate, respDelete)
						.then(() => {
							this.personalizationsmarteditMessageHandler.sendSuccess(this.$translate.instant('personalization.info.updatingactions'));
							this.sendRefreshEvent();
						}, () => {
							this.personalizationsmarteditMessageHandler.sendError(this.$translate.instant('personalization.error.updatingactions'));
						});

				} else {
					this.$log.debug(this.$translate.instant('personalization.error.noupdatingactions'));
				}
			});
		}

	}

	return PersonalizationsmarteditCommerceCustomizationViewController;
};