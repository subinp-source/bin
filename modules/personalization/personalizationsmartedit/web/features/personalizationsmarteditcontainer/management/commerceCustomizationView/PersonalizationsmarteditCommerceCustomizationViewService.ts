import {IModalService, SeInjectable} from 'smarteditcommons';
import {PersonalizationsmarteditCommerceCustomizationViewControllerFactory} from 'personalizationsmarteditcontainer/management/commerceCustomizationView/PersonalizationsmarteditCommerceCustomizationViewControllerFactory';

@SeInjectable()
export class PersonalizationsmarteditCommerceCustomizationView {

	constructor(
		private modalService: IModalService,
		private MODAL_BUTTON_ACTIONS: any,
		private MODAL_BUTTON_STYLES: any) {
	}

	openCommerceCustomizationAction = (customization: any, variation: any) => {
		this.modalService.open({
			title: "personalization.modal.commercecustomization.title",
			templateUrl: 'personalizationsmarteditCommerceCustomizationViewTemplate.html',
			controller: PersonalizationsmarteditCommerceCustomizationViewControllerFactory(customization, variation),
			buttons: [{
				id: 'confirmCancel',
				label: 'personalization.modal.commercecustomization.button.cancel',
				style: this.MODAL_BUTTON_STYLES.SECONDARY,
				action: this.MODAL_BUTTON_ACTIONS.CLOSE
			}, {
				id: 'confirmSave',
				label: 'personalization.modal.commercecustomization.button.submit',
				action: this.MODAL_BUTTON_ACTIONS.CLOSE
			}]
		}).then(() => {
			// success
		}, () => {
			// error
		});
	}
}
