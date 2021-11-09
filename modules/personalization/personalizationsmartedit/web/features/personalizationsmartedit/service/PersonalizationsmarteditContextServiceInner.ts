import {SeInjectable} from 'smarteditcommons';
import {PersonalizationsmarteditContextUtils} from "personalizationcommons";
import {PersonalizationsmarteditContextServiceReverseProxy} from "personalizationsmartedit/service/PersonalizationsmarteditContextServiceInnerReverseProxy";
import {Personalization} from "personalizationcommons/dtos/Personalization";
import {Customize} from "personalizationcommons/dtos/Customize";
import {SeData} from "personalizationcommons/dtos/SeData";
import {CombinedView} from "personalizationcommons/dtos/CombinedView";

@SeInjectable()
export class PersonalizationsmarteditContextService {

	protected personalization: Personalization;
	protected customize: Customize;
	protected combinedView: CombinedView;
	protected seData: SeData;

	constructor(
		protected yjQuery: any,
		protected contextualMenuService: any,
		protected personalizationsmarteditContextServiceReverseProxy: PersonalizationsmarteditContextServiceReverseProxy,
		protected personalizationsmarteditContextUtils: PersonalizationsmarteditContextUtils) {
		const context: any = personalizationsmarteditContextUtils.getContextObject();
		this.setPersonalization(context.personalization);
		this.setCustomize(context.customize);
		this.setCombinedView(context.combinedView);
		this.setSeData(context.seData);
	}

	getPersonalization(): Personalization {
		return this.personalization;
	}

	setPersonalization(personalization: Personalization): void {
		this.personalization = personalization;
		this.contextualMenuService.refreshMenuItems();
	}

	getCustomize(): Customize {
		return this.customize;
	}

	setCustomize(customize: Customize): void {
		this.customize = customize;
		this.contextualMenuService.refreshMenuItems();
	}

	getCombinedView(): CombinedView {
		return this.combinedView;
	}

	setCombinedView(combinedView: CombinedView): void {
		this.combinedView = combinedView;
		this.contextualMenuService.refreshMenuItems();
	}

	getSeData(): SeData {
		return this.seData;
	}

	setSeData(seData: SeData): void {
		this.seData = seData;
	}

	isCurrentPageActiveWorkflowRunning(): Promise<boolean> {
		return this.personalizationsmarteditContextServiceReverseProxy.isCurrentPageActiveWorkflowRunning();
	}
}
