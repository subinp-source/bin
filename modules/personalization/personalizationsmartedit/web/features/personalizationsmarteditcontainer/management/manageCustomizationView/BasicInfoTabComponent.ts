import {SeComponent} from 'smarteditcommons';

@SeComponent({
	templateUrl: 'basicInfoTabTemplate.html',
	inputs: [
		'customization: =?'
	]
})
export class BasicInfoTabComponent {

	public datetimeConfigurationEnabled: boolean = false;
	private _customization: any;

	constructor(
		public PERSONALIZATION_MODEL_STATUS_CODES: any,
		public DATE_CONSTANTS: any
	) {}

	get customization(): any {
		return this._customization;
	}
	set customization(value: any) {
		this._customization = value;
		this.datetimeConfigurationEnabled = (this.customization.enabledStartDate || this.customization.enabledEndDate);
	}

	resetDateTimeConfiguration(): void {
		this.customization.enabledStartDate = undefined;
		this.customization.enabledEndDate = undefined;
	}

	customizationStatusChange(): void {
		this.customization.status = this.customization.statusBoolean ? this.PERSONALIZATION_MODEL_STATUS_CODES.ENABLED : this.PERSONALIZATION_MODEL_STATUS_CODES.DISABLED;
	}

}