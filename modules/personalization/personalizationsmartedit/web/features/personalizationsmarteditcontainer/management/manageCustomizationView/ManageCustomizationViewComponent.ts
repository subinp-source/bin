import * as angular from "angular";
import {SeComponent} from 'smarteditcommons';
import {PersonalizationsmarteditDateUtils} from "personalizationcommons/PersonalizationsmarteditDateUtils";
import {PersonalizationsmarteditRestService} from "personalizationsmarteditcontainer/service/PersonalizationsmarteditRestService";

@SeComponent({
	templateUrl: 'manageCustomizationViewTemplate.html',
	inputs: [
		'modalManager: =',
		'customizationCode',
		'variationCode'
	]
})
export class ManageCustomizationViewComponent {

	private static DOCHECK_COUNTER_INTERVAL = 30;

	public customizationCode: string;
	public variationCode: string;
	public modalManager: any;

	public initialCustomization: any = {
		name: '',
		enabledStartDate: '',
		enabledEndDate: '',
		status: 'ENABLED',
		statusBoolean: true,
		variations: []
	};

	public customization: any = {};
	public edit: any;
	public activeTabNumber: any = 0;
	public editMode: boolean = false;
	public tabsArr: any;

	private doCheckCounter = 0;

	constructor(
		private DATE_CONSTANTS: any,
		private CUSTOMIZATION_VARIATION_MANAGEMENT_TABS_CONSTANTS: any,
		private PERSONALIZATION_MODEL_STATUS_CODES: any,
		private CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS: any,
		private PERSONALIZATION_DATE_FORMATS: any,
		private MODAL_BUTTON_ACTIONS: any,
		private personalizationsmarteditRestService: PersonalizationsmarteditRestService,
		private personalizationsmarteditDateUtils: PersonalizationsmarteditDateUtils,
		private personalizationsmarteditMessageHandler: any,
		private personalizationsmarteditCommerceCustomizationService: any,
		private personalizationsmarteditUtils: any,
		private confirmationModalService: any,
		private systemEventService: any,
		private $translate: angular.translate.ITranslateService,
		private $log: angular.ILogService
	) {}

	$onInit(): void {

		if (this.customizationCode) {
			this.getCustomization();
			this.editMode = true;
		} else {
			this.customization = angular.copy(this.initialCustomization);
		}

		this.initTabs();

		this.modalManager.setDismissCallback(() => {
			return this.onCancel();
		});

		this.modalManager.setButtonHandler((buttonId: any) => {
			switch (buttonId) {
				case this.CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_OK:
					return this.edit.selectedTab.onConfirm();
				case this.CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_NEXT:
					return this.edit.selectedTab.onConfirm();
				case this.CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_CANCEL:
					return this.edit.selectedTab.onCancel();
				default:
					this.$log.error(this.$translate.instant('personalization.modal.customizationvariationmanagement.targetgrouptab.invalidbuttonid'), buttonId);
					break;
			}
		});

		this.edit = {
			code: '',
			name: '',
			selectedTab: this.tabsArr[0]
		};

	}

	$doCheck(): void {
		this.doCheckCounter += 1;
		if (this.doCheckCounter === ManageCustomizationViewComponent.DOCHECK_COUNTER_INTERVAL) {
			this.doCheckCounter = 0;
			const isSelectedTabDirty = this.edit.selectedTab.isDirty();
			const isSelectedTabValid = this.edit.selectedTab.isValid();

			if (isSelectedTabDirty) {
				if (isSelectedTabValid) {
					this.edit.selectedTab.setEnabled(true);
				} else {
					this.edit.selectedTab.setEnabled(false);
				}
			} else if (this.editMode) {
				if (isSelectedTabValid) {
					this.edit.selectedTab.setEnabled(true);
				} else {
					this.edit.selectedTab.setEnabled(false);
				}
			} else {
				this.edit.selectedTab.setEnabled(false);
			}
		}
	}

	getVariationsForCustomization(customizationCode: any): Promise<any> {
		const filter = {
			includeFullFields: true
		};

		return this.personalizationsmarteditRestService.getVariationsForCustomization(customizationCode, filter);
	}

	createCommerceCustomizationData(variations: any): any {
		variations.forEach((variation: any) => {
			variation.commerceCustomizations = this.personalizationsmarteditCommerceCustomizationService.getCommerceActionsCountMap(variation);
			variation.numberOfCommerceActions = this.personalizationsmarteditCommerceCustomizationService.getCommerceActionsCount(variation);
			delete variation.actions; // no more use for this property and it existence may be harmful
		});
	}

	getCustomization(): void {
		const filter = {
			code: this.customizationCode
		};
		this.personalizationsmarteditRestService.getCustomization(filter).then((responseCustomization: any) => {
			this.customization = responseCustomization;

			this.customization.enabledStartDate = this.personalizationsmarteditDateUtils.formatDate(this.customization.enabledStartDate, undefined);
			this.customization.enabledEndDate = this.personalizationsmarteditDateUtils.formatDate(this.customization.enabledEndDate, undefined);
			this.customization.statusBoolean = (this.customization.status === this.PERSONALIZATION_MODEL_STATUS_CODES.ENABLED);

			this.getVariationsForCustomization(this.customizationCode).then((response: any) => {
				this.createCommerceCustomizationData(response.variations);
				this.customization.variations = response.variations;

				if (angular.isDefined(this.variationCode)) {
					const filteredCollection = this.customization.variations.filter((elem: any) => {
						return elem.code === this.variationCode;
					});

					if (filteredCollection.length > 0) {

						this.activeTabNumber = 1;
						this.edit.selectedTab = this.tabsArr[1];

						const selVariation = filteredCollection[0];
						this.edit.selectedVariation = selVariation;
					}
					this.initialCustomization = angular.copy(this.customization);

				} else {
					this.edit.selectedTab = this.tabsArr[0];
					this.initialCustomization = angular.copy(this.customization);
				}

			}, () => {
				this.personalizationsmarteditMessageHandler.sendError(this.$translate.instant('personalization.error.gettingsegments'));
			});

		}, () => {
			this.personalizationsmarteditMessageHandler.sendError(this.$translate.instant('personalization.error.gettingcomponents'));
		});

	}

	initTabs(): void {
		this.tabsArr = [{
			name: this.CUSTOMIZATION_VARIATION_MANAGEMENT_TABS_CONSTANTS.BASIC_INFO_TAB_NAME,
			active: true,
			disabled: false,
			heading: this.$translate.instant("personalization.modal.customizationvariationmanagement.basicinformationtab"),
			template: 'basicInfoTabWrapperTemplate.html',
			formName: this.CUSTOMIZATION_VARIATION_MANAGEMENT_TABS_CONSTANTS.BASIC_INFO_TAB_FORM_NAME,
			isDirty: () => {
				return this.isModalDirty();
			},
			isValid: () => {
				return this.isBasicInfoTabValid(this.customization).length === 0;
			},
			setEnabled: (enabled: boolean) => {
				if (enabled) {
					this.tabsArr[1].disabled = false;
					this.modalManager.enableButton(this.CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_NEXT);
				} else {
					this.tabsArr[1].disabled = true;
					this.modalManager.disableButton(this.CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_NEXT);
				}
			},
			onConfirm: () => {
				this.activeTabNumber = 1;
			},
			onCancel: () => {
				this.onCancel();
			}
		}, {
			name: this.CUSTOMIZATION_VARIATION_MANAGEMENT_TABS_CONSTANTS.TARGET_GROUP_TAB_NAME,
			active: false,
			disabled: true,
			heading: this.$translate.instant("personalization.modal.customizationvariationmanagement.targetgrouptab"),
			template: 'targetGroupTabWrapperTemplate.html',
			formName: this.CUSTOMIZATION_VARIATION_MANAGEMENT_TABS_CONSTANTS.TARGET_GROUP_TAB_FORM_NAME,
			isDirty: () => {
				return this.isModalDirty();
			},
			isValid: () => {
				return this.isTargetGroupTabValid(this.customization).length === 0;
			},
			setEnabled: (enabled: boolean) => {
				if (enabled) {
					this.modalManager.enableButton(this.CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_OK);
				} else {
					this.modalManager.disableButton(this.CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_OK);
				}
			},
			onConfirm: () => {
				this.onSave();
			},
			onCancel: () => {
				this.onCancel();
			}
		}];

	}


	selectTab(tab: any): void {
		this.edit.selectedTab = tab;
		this.activeTabNumber = this.tabsArr.indexOf(tab);
		switch (tab.name) {
			case this.CUSTOMIZATION_VARIATION_MANAGEMENT_TABS_CONSTANTS.BASIC_INFO_TAB_NAME:
				this.modalManager.removeButton(this.CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_OK);
				if (!this.modalManager.getButton(this.CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_NEXT)) {
					this.modalManager.addButton({
						id: this.CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_NEXT,
						label: 'personalization.modal.customizationvariationmanagement.basicinformationtab.button.next'
					});
				}
				break;
			case this.CUSTOMIZATION_VARIATION_MANAGEMENT_TABS_CONSTANTS.TARGET_GROUP_TAB_NAME:
				this.modalManager.removeButton(this.CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_NEXT);
				if (!this.modalManager.getButton(this.CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_OK)) {
					this.modalManager.addButton({
						id: this.CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_OK,
						label: 'personalization.modal.customizationvariationmanagement.targetgrouptab.button.submit',
						action: this.MODAL_BUTTON_ACTIONS.CLOSE
					});
				}

				if (this.customization.enabledStartDate) {
					this.customization.enabledStartDate = this.personalizationsmarteditDateUtils.formatDate(this.customization.enabledStartDate, this.DATE_CONSTANTS.MOMENT_FORMAT);
				}
				if (this.customization.enabledEndDate) {
					this.customization.enabledEndDate = this.personalizationsmarteditDateUtils.formatDate(this.customization.enabledEndDate, this.DATE_CONSTANTS.MOMENT_FORMAT);
				}

				break;
			default:
				break;
		}
	}

	onSave(): void {
		if (this.customization.enabledStartDate) {
			this.customization.enabledStartDate = this.personalizationsmarteditDateUtils.formatDate(this.customization.enabledStartDate, this.PERSONALIZATION_DATE_FORMATS.MODEL_DATE_FORMAT);
		} else {
			this.customization.enabledStartDate = undefined;
		}

		if (this.customization.enabledEndDate) {
			this.customization.enabledEndDate = this.personalizationsmarteditDateUtils.formatDate(this.customization.enabledEndDate, this.PERSONALIZATION_DATE_FORMATS.MODEL_DATE_FORMAT);
		} else {
			this.customization.enabledEndDate = undefined;
		}

		this.customization.status = this.customization.statusBoolean ? this.PERSONALIZATION_MODEL_STATUS_CODES.ENABLED : this.PERSONALIZATION_MODEL_STATUS_CODES.DISABLED;

		if (this.editMode) {
			this.personalizationsmarteditRestService.updateCustomizationPackage(this.customization).then(() => {
				this.systemEventService.publishAsync('CUSTOMIZATIONS_MODIFIED', {});
				this.personalizationsmarteditMessageHandler.sendSuccess(this.$translate.instant('personalization.info.updatingcustomization'));
			}, () => {
				this.personalizationsmarteditMessageHandler.sendError(this.$translate.instant('personalization.error.updatingcustomization'));
			});
		} else {
			this.personalizationsmarteditRestService.createCustomization(this.customization).then(() => {
				this.systemEventService.publishAsync('CUSTOMIZATIONS_MODIFIED', {});
				this.personalizationsmarteditMessageHandler.sendSuccess(this.$translate.instant('personalization.info.creatingcustomization'));
			}, () => {
				this.personalizationsmarteditMessageHandler.sendError(this.$translate.instant('personalization.error.creatingcustomization'));
			});
		}

	}

	onCancel(): Promise<any> {
		if (this.isModalDirty()) {
			return this.confirmationModalService.confirm({
				description: this.$translate.instant('personalization.modal.customizationvariationmanagement.targetgrouptab.cancelconfirmation')
			}).then(() => {
				this.modalManager.dismiss();
				return Promise.resolve();
			}, () => {
				return Promise.reject();
			});
		} else {
			this.modalManager.dismiss();
			return Promise.resolve();
		}
	}

	isBasicInfoTabValid(customizationForm: any): any {
		const errorArray = [];
		if (angular.isUndefined(customizationForm.name) || customizationForm.name === '') {
			errorArray.push("name cant be empty");
		}
		return errorArray;
	}

	isTargetGroupTabValid(customizationForm: any): any {
		const errorArray = [];
		if (!(angular.isArray(customizationForm.variations) && this.personalizationsmarteditUtils.getVisibleItems(customizationForm.variations).length > 0)) {
			errorArray.push("variations cant be empty");
		}
		return errorArray;
	}

	isCustomizationValid(customizationForm: any): any {
		const errorArray = [];
		errorArray.push(this.isBasicInfoTabValid(customizationForm));
		errorArray.push(this.isTargetGroupTabValid(customizationForm));
		return errorArray;
	}

	isModalDirty(): boolean {
		return !angular.equals(this.initialCustomization, this.customization);
	}
}