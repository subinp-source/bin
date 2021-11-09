import {SeInjectable} from 'smarteditcommons';
import {PersonalizationsmarteditContextServiceProxy} from "personalizationsmarteditcontainer/service/PersonalizationsmarteditContextServiceOuterProxy";
import {PersonalizationsmarteditContextUtils} from "personalizationcommons";
import {Personalization} from "personalizationcommons/dtos/Personalization";
import {Customize} from "personalizationcommons/dtos/Customize";
import {CombinedView} from "personalizationcommons/dtos/CombinedView";
import {SeData} from "personalizationcommons/dtos/SeData";

@SeInjectable()
export class PersonalizationsmarteditContextService {

	protected personalization: Personalization;
	protected customize: Customize;
	protected combinedView: CombinedView;
	protected seData: SeData;
	protected customizeFiltersState: any;

	constructor(
		protected $q: any,
		protected sharedDataService: any,
		protected loadConfigManagerService: any,
		protected personalizationsmarteditContextServiceProxy: PersonalizationsmarteditContextServiceProxy,
		protected personalizationsmarteditContextUtils: PersonalizationsmarteditContextUtils) {
		const context: any = personalizationsmarteditContextUtils.getContextObject();
		this.setPersonalization(context.personalization);
		this.setCustomize(context.customize);
		this.setCombinedView(context.combinedView);
		this.setSeData(context.seData);
		this.customizeFiltersState = {};
	}

	getPersonalization(): Personalization {
		return this.personalization;
	}

	setPersonalization(personalization: Personalization): void {
		this.personalization = personalization;
		this.personalizationsmarteditContextServiceProxy.setPersonalization(personalization);
	}

	getCustomize(): Customize {
		return this.customize;
	}

	setCustomize(customize: Customize): void {
		this.customize = customize;
		this.personalizationsmarteditContextServiceProxy.setCustomize(customize);
	}

	getCombinedView(): CombinedView {
		return this.combinedView;
	}

	setCombinedView(combinedView: CombinedView): void {
		this.combinedView = combinedView;
		this.personalizationsmarteditContextServiceProxy.setCombinedView(combinedView);
	}

	getSeData(): SeData {
		return this.seData;
	}

	setSeData(seData: SeData): void {
		this.seData = seData;
		this.personalizationsmarteditContextServiceProxy.setSeData(seData);
	}

	refreshExperienceData(): any {
		return this.sharedDataService.get('experience').then((data: any) => {
			const seData = this.getSeData();
			seData.seExperienceData = data;
			seData.pageId = data.pageId;
			this.setSeData(seData);
			return this.$q.when();
		});
	}

	refreshConfigurationData(): any {
		this.loadConfigManagerService.loadAsObject().then((configurations: any) => {
			const seData = this.getSeData();
			seData.seConfigurationData = configurations;
			this.setSeData(seData);
		});
	}

	applySynchronization(): void {
		this.personalizationsmarteditContextServiceProxy.setPersonalization(this.personalization);
		this.personalizationsmarteditContextServiceProxy.setCustomize(this.customize);
		this.personalizationsmarteditContextServiceProxy.setCombinedView(this.combinedView);
		this.personalizationsmarteditContextServiceProxy.setSeData(this.seData);

		this.refreshExperienceData();
		this.refreshConfigurationData();
	}

	getContexServiceProxy(): PersonalizationsmarteditContextServiceProxy {
		return this.personalizationsmarteditContextServiceProxy;
	}


	getCustomizeFiltersState(): any {
		return this.customizeFiltersState;
	}

	setCustomizeFiltersState(filters: any): any {
		this.customizeFiltersState = filters;
	}
}
