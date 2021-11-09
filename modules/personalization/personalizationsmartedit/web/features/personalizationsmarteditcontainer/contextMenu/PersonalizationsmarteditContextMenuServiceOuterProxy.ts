import * as angular from "angular";
import {GatewayProxied, IFundamentalModalConfig, SeInjectable} from 'smarteditcommons';
import {LoDashStatic} from 'lodash';
import {PersonalizationsmarteditContextService} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditContextServiceOuter';
import {PersonalizationsmarteditRestService} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditRestService';
import {PersonalizationsmarteditMessageHandler} from 'personalizationcommons/PersonalizationsmarteditMessageHandler';
import {ContextMenuDeleteActionControllerFactory} from 'personalizationsmarteditcontainer/contextMenu/PersonalizationsmarteditContextMenuDeleteActionControllerFactory';
import {PersonalizationsmarteditContextMenuAddEditActionComponent} from "./PersonalizationsmarteditContextMenuAddEditActionComponent";
import {
	ModalRef as FundamentalModalRef
} from '@fundamental-ngx/core';

@GatewayProxied('openDeleteAction', 'openAddAction', 'openEditAction', 'openEditComponentAction')
@SeInjectable()
export class PersonalizationsmarteditContextMenuServiceProxy {

	private confirmModalButtons = [{
		id: 'confirmCancel',
		label: 'personalization.modal.deleteaction.button.cancel',
		style: this.MODAL_BUTTON_STYLES.SECONDARY,
		action: this.MODAL_BUTTON_ACTIONS.DISMISS
	}, {
		id: 'confirmOk',
		label: 'personalization.modal.deleteaction.button.ok',
		action: this.MODAL_BUTTON_ACTIONS.CLOSE
	}];

	constructor(
		private modalService: any,
		private renderService: any,
		private editorModalService: any,
		private personalizationsmarteditContextService: PersonalizationsmarteditContextService,
		private personalizationsmarteditRestService: PersonalizationsmarteditRestService,
		protected personalizationsmarteditMessageHandler: PersonalizationsmarteditMessageHandler,
		private lodash: LoDashStatic,
		protected $translate: angular.translate.ITranslateService,
		private MODAL_BUTTON_ACTIONS: any,
		private MODAL_BUTTON_STYLES: any
	) {
	}

	openDeleteAction(config: any) {
		this.modalService.open({
			size: 'md',
			title: 'personalization.modal.deleteaction.title',
			templateInline: '<div id="confirmationModalDescription">{{ "' + "personalization.modal.deleteaction.content" + '" | translate }}</div>',
			controller: ContextMenuDeleteActionControllerFactory(config),
			cssClasses: 'yFrontModal',
			buttons: this.confirmModalButtons
		}).then(() => {
			if (this.personalizationsmarteditContextService.getCombinedView().enabled) {
				this.personalizationsmarteditRestService.getActions(config.selectedCustomizationCode, config.selectedVariationCode, config)
					.then((response: any) => { // success callback
						const combinedView = this.personalizationsmarteditContextService.getCombinedView();
						if (combinedView.customize.selectedComponents) {
							combinedView.customize.selectedComponents.splice(combinedView.customize.selectedComponents.indexOf(config.containerSourceId), 1);
						}
						this.lodash.forEach(combinedView.selectedItems, (value: any) => {
							if (value.customization.code === config.selectedCustomizationCode && value.variation.code === config.selectedVariationCode) {
								value.variation.actions = response.actions;
							}
						});
						this.personalizationsmarteditContextService.setCombinedView(combinedView);
					}, () => { // error callback
						this.personalizationsmarteditMessageHandler.sendError(this.$translate.instant('personalization.error.gettingactions'));
					});
			} else {
				const customize = this.personalizationsmarteditContextService.getCustomize();
				customize.selectedComponents.splice(customize.selectedComponents.indexOf(config.containerSourceId), 1);
				this.personalizationsmarteditContextService.setCustomize(customize);
			}
			this.renderService.renderSlots(config.slotsToRefresh);
		});
	}

	openAddAction(config: any) {
		(this.modalService.open({
			component: PersonalizationsmarteditContextMenuAddEditActionComponent,
			data: config,
			templateConfig: {
				// buttons: this.modalButtons,
				title: 'personalization.modal.addaction.title',
				isDismissButtonVisible: true
			},
			config: {
				focusTrapped: false,
				modalPanelClass: 'yPersonalizationContextModal'
			}
		} as IFundamentalModalConfig<any>) as FundamentalModalRef).afterClosed.subscribe(
			(resultContainer: string) => {
				if (this.personalizationsmarteditContextService.getCombinedView().enabled) {
					const combinedView = this.personalizationsmarteditContextService.getCombinedView();
					combinedView.customize.selectedComponents.push(resultContainer);
					this.personalizationsmarteditContextService.setCombinedView(combinedView);
				} else {
					const customize = this.personalizationsmarteditContextService.getCustomize();
					customize.selectedComponents.push(resultContainer);
					this.personalizationsmarteditContextService.setCustomize(customize);
				}
				this.renderService.renderSlots(config.slotsToRefresh);
			});
	}

	openEditAction(config: any) {
		config.editEnabled = true;
		(this.modalService.open({
			component: PersonalizationsmarteditContextMenuAddEditActionComponent,
			data: config,
			templateConfig: {
				title: 'personalization.modal.editaction.title',
				isDismissButtonVisible: true
			},
			config: {
				focusTrapped: false,
				modalPanelClass: 'yPersonalizationContextModal'
			}
		} as IFundamentalModalConfig<any>) as FundamentalModalRef).afterClosed.subscribe(
			(resultContainer: string) => {
				this.renderService.renderSlots(config.slotsToRefresh);
			});
	}

	openEditComponentAction(config: any) {
		this.editorModalService.open(config);
	}
}