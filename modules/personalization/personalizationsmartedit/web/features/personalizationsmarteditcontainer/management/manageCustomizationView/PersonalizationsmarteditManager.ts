import {IModalService, SeInjectable, SeValueProvider} from 'smarteditcommons';

export const CUSTOMIZATION_VARIATION_MANAGEMENT_TABS_CONSTANTS_PROVIDER: SeValueProvider = {
	provide: "CUSTOMIZATION_VARIATION_MANAGEMENT_TABS_CONSTANTS",
	useValue: {
		BASIC_INFO_TAB_NAME: 'basicinfotab',
		BASIC_INFO_TAB_FORM_NAME: 'form.basicinfotab',
		TARGET_GROUP_TAB_NAME: 'targetgrptab',
		TARGET_GROUP_TAB_FORM_NAME: 'form.targetgrptab'
	}
};

export const CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS_PROVIDER: SeValueProvider = {
	provide: "CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS",
	useValue: {
		CONFIRM_OK: 'confirmOk',
		CONFIRM_CANCEL: 'confirmCancel',
		CONFIRM_NEXT: 'confirmNext'
	}
};

export const CUSTOMIZATION_VARIATION_MANAGEMENT_SEGMENTTRIGGER_GROUPBY_PROVIDER: SeValueProvider = {
	provide: "CUSTOMIZATION_VARIATION_MANAGEMENT_SEGMENTTRIGGER_GROUPBY",
	useValue: {
		CRITERIA_AND: 'AND',
		CRITERIA_OR: 'OR'
	}
};

export const DATE_CONSTANTS_PROVIDER: SeValueProvider = {
	provide: "DATE_CONSTANTS",
	useValue: {
		ANGULAR_FORMAT: 'short',
		MOMENT_FORMAT: 'M/D/YY h:mm A',
		MOMENT_ISO: 'YYYY-MM-DDTHH:mm:00ZZ',
		ISO: 'yyyy-MM-ddTHH:mm:00Z'
	}
};

@SeInjectable()
export class PersonalizationsmarteditManager {

	constructor(
		private modalService: IModalService,
		private MODAL_BUTTON_STYLES: any,
		private CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS: any) {
	}

	openCreateCustomizationModal = () => {
		this.modalService.open({
			title: 'personalization.modal.customizationvariationmanagement.title',
			templateInline: '<manage-customization-view data-modal-manager="modalController.modalManager"></manage-customization-view>',
			controller: function ctrl(modalManager: IModalService) {
				'ngInject';

				this.modalManager = modalManager;
			},
			buttons: [{
				id: this.CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_CANCEL,
				label: 'personalization.modal.customizationvariationmanagement.button.cancel',
				style: this.MODAL_BUTTON_STYLES.SECONDARY
			}],
			size: 'lg sliderPanelParentModal'
		});
	}

	openEditCustomizationModal = (customizationCode: string, variationCode: string) => {
		this.modalService.open({
			title: 'personalization.modal.customizationvariationmanagement.title',
			templateInline: '<manage-customization-view data-modal-manager="modalController.modalManager" data-customization-code="modalController.customizationCode" data-variation-code="modalController.variationCode"/>',
			controller: function ctrl(modalManager: IModalService) {
				'ngInject';

				this.modalManager = modalManager;
				this.customizationCode = customizationCode;
				this.variationCode = variationCode;
			},
			buttons: [{
				id: 'confirmCancel',
				label: 'personalization.modal.customizationvariationmanagement.button.cancel',
				style: this.MODAL_BUTTON_STYLES.SECONDARY
			}],
			size: 'lg sliderPanelParentModal'
		});
	}
}