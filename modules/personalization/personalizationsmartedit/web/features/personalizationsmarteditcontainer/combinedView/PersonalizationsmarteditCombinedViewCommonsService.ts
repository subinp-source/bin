import {IModalService, SeInjectable} from 'smarteditcommons';
import * as angular from 'angular';
import {PersonalizationsmarteditRestService} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditRestService';
import {PersonalizationsmarteditContextUtils} from 'personalizationcommons/PersonalizationsmarteditContextUtils';
import {PersonalizationsmarteditContextService} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditContextServiceOuter';
import {PersonalizationsmarteditPreviewService} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditPreviewService';
import {PersonalizationsmarteditUtils} from 'personalizationcommons/PersonalizationsmarteditUtils';
import {PersonalizationsmarteditCombinedViewConfigureController} from 'personalizationsmarteditcontainer/combinedView/PersonalizationsmarteditCombinedViewConfigureController';

@SeInjectable()
export class PersonalizationsmarteditCombinedViewCommonsService {

	constructor(
		private $q: angular.IQService,
		protected personalizationsmarteditContextUtils: PersonalizationsmarteditContextUtils,
		protected personalizationsmarteditContextService: PersonalizationsmarteditContextService,
		protected personalizationsmarteditPreviewService: PersonalizationsmarteditPreviewService,
		protected personalizationsmarteditUtils: PersonalizationsmarteditUtils,
		protected personalizationsmarteditRestService: PersonalizationsmarteditRestService,
		private modalService: IModalService,
		private MODAL_BUTTON_ACTIONS: any,
		private MODAL_BUTTON_STYLES: any) {
	}

	openManagerAction = () => {
		this.modalService.open({
			title: "personalization.modal.combinedview.title",
			templateUrl: 'personalizationsmarteditCombinedViewConfigureTemplate.html',
			controller: PersonalizationsmarteditCombinedViewConfigureController,
			buttons: [{
				id: 'confirmCancel',
				label: 'personalization.modal.combinedview.button.cancel',
				style: this.MODAL_BUTTON_STYLES.SECONDARY,
				action: this.MODAL_BUTTON_ACTIONS.DISMISS
			}, {
				id: 'confirmOk',
				label: 'personalization.modal.combinedview.button.ok',
				action: this.MODAL_BUTTON_ACTIONS.CLOSE
			}]
		}).then(() => {
			this.personalizationsmarteditContextUtils.clearCombinedViewCustomizeContext(this.personalizationsmarteditContextService);
			const combinedView = this.personalizationsmarteditContextService.getCombinedView();
			combinedView.enabled = (combinedView.selectedItems && combinedView.selectedItems.length > 0);
			this.personalizationsmarteditContextService.setCombinedView(combinedView);
			this.updatePreview(this.getVariationsForPreviewTicket());
		}, () => {
			// error
		});
	}

	updatePreview(previewTicketVariations: any) {
		this.personalizationsmarteditPreviewService.updatePreviewTicketWithVariations(previewTicketVariations);
		this.updateActionsOnSelectedVariations();
	}

	getVariationsForPreviewTicket(): any[] {
		const previewTicketVariations: any[] = [];
		const combinedView = this.personalizationsmarteditContextService.getCombinedView();
		(combinedView.selectedItems || []).forEach((item: any) => {
			previewTicketVariations.push({
				customizationCode: item.customization.code,
				variationCode: item.variation.code,
				catalog: item.variation.catalog,
				catalogVersion: item.variation.catalogVersion
			});
		});
		return previewTicketVariations;
	}

	combinedViewEnabledEvent(isEnabled: boolean) {
		const combinedView = this.personalizationsmarteditContextService.getCombinedView();
		combinedView.enabled = isEnabled;
		this.personalizationsmarteditContextService.setCombinedView(combinedView);
		const customize = this.personalizationsmarteditContextService.getCustomize();
		customize.selectedCustomization = null;
		customize.selectedVariations = null;
		customize.selectedComponents = null;
		this.personalizationsmarteditContextService.setCustomize(customize);
		if (isEnabled) {
			this.updatePreview(this.getVariationsForPreviewTicket());
		} else {
			this.updatePreview([]);
		}
	}

	isItemFromCurrentCatalog(item: any) {
		return this.personalizationsmarteditUtils.isItemFromCurrentCatalog(item, this.personalizationsmarteditContextService.getSeData());
	}

	private updateActionsOnSelectedVariations() {
		const combinedView = this.personalizationsmarteditContextService.getCombinedView();
		const promissesArray: any[] = [];
		(combinedView.selectedItems || []).forEach((item: any) => {
			promissesArray.push(this.personalizationsmarteditRestService.getActions(item.customization.code, item.variation.code, item.variation)
				.then((response: any) => {
					item.variation.actions = response.actions;
				}));
		});
		this.$q.all(promissesArray).then(() => {
			this.personalizationsmarteditContextService.setCombinedView(combinedView);
		});
	}
}