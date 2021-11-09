import * as angular from "angular";
import {SeInjectable} from 'smarteditcommons';
import {PersonalizationsmarteditRestService} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditRestService';
import {PersonalizationsmarteditMessageHandler} from 'personalizationcommons/PersonalizationsmarteditMessageHandler';
import {PersonalizationsmarteditCommerceCustomizationService} from 'personalizationcommons/PersonalizationsmarteditCommerceCustomizationService';

@SeInjectable()
export class PersonalizationsmarteditManagerViewUtilsService {

	constructor(
		private personalizationsmarteditRestService: PersonalizationsmarteditRestService,
		private personalizationsmarteditMessageHandler: PersonalizationsmarteditMessageHandler,
		private personalizationsmarteditCommerceCustomizationService: PersonalizationsmarteditCommerceCustomizationService,
		private PERSONALIZATION_MODEL_STATUS_CODES: any,
		private waitDialogService: any,
		private confirmationModalService: any,
		private $translate: angular.translate.ITranslateService) {
	}

	public deleteCustomizationAction = (customization: any, customizations: any[]) => {
		this.confirmationModalService.confirm({
			description: 'personalization.modal.manager.deletecustomization.content'
		}).then(() => {
			this.personalizationsmarteditRestService.getCustomization(customization)
				.then((responseCustomization: any) => {
					responseCustomization.status = "DELETED";
					this.personalizationsmarteditRestService.updateCustomization(responseCustomization)
						.then(() => {
							customizations.splice(customizations.indexOf(customization), 1);
						}, () => {
							this.personalizationsmarteditMessageHandler.sendError(this.$translate.instant('personalization.error.deletingcustomization'));
						});
				}, () => {
					this.personalizationsmarteditMessageHandler.sendError(this.$translate.instant('personalization.error.deletingcustomization'));
				});
		});
	}

	public deleteVariationAction = (customization: any, variation: any) => {
		this.confirmationModalService.confirm({
			description: 'personalization.modal.manager.deletevariation.content'
		}).then(() => {
			this.personalizationsmarteditRestService.getVariation(customization.code, variation.code)
				.then((responseVariation: any) => {
					responseVariation.status = "DELETED";
					this.personalizationsmarteditRestService.editVariation(customization.code, responseVariation)
						.then((response: any) => {
							variation.status = response.status;
						}, () => {
							this.personalizationsmarteditMessageHandler.sendError(this.$translate.instant('personalization.error.deletingvariation'));
						});
				}, () => {
					this.personalizationsmarteditMessageHandler.sendError(this.$translate.instant('personalization.error.deletingvariation'));
				});
		});
	}

	public toogleVariationActive = (customization: any, variation: any) => {
		this.personalizationsmarteditRestService.getVariation(customization.code, variation.code)
			.then((responseVariation: any) => {
				responseVariation.enabled = !responseVariation.enabled;
				responseVariation.status = responseVariation.enabled ? this.PERSONALIZATION_MODEL_STATUS_CODES.ENABLED : this.PERSONALIZATION_MODEL_STATUS_CODES.DISABLED;
				this.personalizationsmarteditRestService.editVariation(customization.code, responseVariation)
					.then((response: any) => {
						variation.enabled = response.enabled;
						variation.status = response.status;
					}, () => {
						this.personalizationsmarteditMessageHandler.sendError(this.$translate.instant('personalization.error.editingvariation'));
					});
			}, () => {
				this.personalizationsmarteditMessageHandler.sendError(this.$translate.instant('personalization.error.gettingvariation'));
			});
	}

	public customizationClickAction = (customization: any) => {
		return this.personalizationsmarteditRestService.getVariationsForCustomization(customization.code, customization).then(
			(response: any) => {
				customization.variations.forEach((variation: any) => {
					variation.actions = this.getActionsForVariation(variation.code, response.variations);
					variation.numberOfComponents = this.personalizationsmarteditCommerceCustomizationService.getNonCommerceActionsCount(variation);
					variation.commerceCustomizations = this.personalizationsmarteditCommerceCustomizationService.getCommerceActionsCountMap(variation);
					variation.numberOfCommerceActions = this.personalizationsmarteditCommerceCustomizationService.getCommerceActionsCount(variation);
				});
			},
			() => {
				this.personalizationsmarteditMessageHandler.sendError(this.$translate.instant('personalization.error.gettingcustomization'));
			});
	}

	public getCustomizations = (filter: any) => {
		return this.personalizationsmarteditRestService.getCustomizations(filter);
	}

	public updateCustomizationRank = (customizationCode: string, increaseValue: number) => {
		return this.personalizationsmarteditRestService.updateCustomizationRank(customizationCode, increaseValue)
			.then(() => {
				// 
			}, () => {
				this.personalizationsmarteditMessageHandler.sendError(this.$translate.instant('personalization.error.updatingcustomization'));
			});
	}

	public updateVariationRank = (customization: any, variationCode: string, increaseValue: number) => {
		return this.personalizationsmarteditRestService.getVariation(customization.code, variationCode)
			.then((responseVariation: any) => {
				responseVariation.rank = responseVariation.rank + increaseValue;
				return this.personalizationsmarteditRestService.editVariation(customization.code, responseVariation)
					.then(() => {
						// 
					}, () => {
						this.personalizationsmarteditMessageHandler.sendError(this.$translate.instant('personalization.error.editingvariation'));
					});
			}, () => {
				this.personalizationsmarteditMessageHandler.sendError(this.$translate.instant('personalization.error.gettingvariation'));
			});
	}

	public setCustomizationRank = (customization: any, increaseValue: number, customizations: any[]) => {
		const nextItem = customizations[customizations.indexOf(customization) + increaseValue];
		this.waitDialogService.showWaitModal();
		this.updateCustomizationRank(customization.code, nextItem.rank - customization.rank)
			.then(() => {
				customization.rank += increaseValue;
				nextItem.rank -= increaseValue;
				const index = customizations.indexOf(customization);
				const tempItem = customizations.splice(index, 1);
				customizations.splice(index + increaseValue, 0, tempItem[0]);
				this.waitDialogService.hideWaitModal();
			}).catch(() => {
				this.waitDialogService.hideWaitModal();
			});
	}

	public setVariationRank = (customization: any, variation: any, increaseValue: number) => {
		const nextItem = customization.variations[customization.variations.indexOf(variation) + increaseValue];
		this.waitDialogService.showWaitModal();
		this.updateVariationRank(customization, variation.code, increaseValue)
			.then(() => {
				variation.rank += increaseValue;
				nextItem.rank -= increaseValue;
				const index = customization.variations.indexOf(variation);
				const tempItem = customization.variations.splice(index, 1);
				customization.variations.splice(index + increaseValue, 0, tempItem[0]);
			}).finally(() => {
				this.waitDialogService.hideWaitModal();
			});
	}

	private getActionsForVariation = (variationCode: string, variationsArray: any[]) => {
		variationsArray = variationsArray || [];
		const variation = variationsArray.filter((elem: any) => {
			return variationCode === elem.code;
		})[0];
		return variation ? variation.actions : [];
	}

}