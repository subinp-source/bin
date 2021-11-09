import {SeComponent} from 'smarteditcommons';
import {PersonalizationsmarteditContextService} from 'personalizationsmartedit/service/PersonalizationsmarteditContextServiceInner';
import {PersonalizationsmarteditComponentHandlerService} from 'personalizationsmartedit/service/PersonalizationsmarteditComponentHandlerService';

@SeComponent({
	templateUrl: 'personalizationsmarteditShowActionListTemplate.html',
	inputs: [
		'containerId'
	]
})
export class PersonalizationsmarteditShowActionListComponent {

	public selectedItems: any;
	public containerSourceId: string;
	public containerId: any;

	constructor(
		protected personalizationsmarteditContextService: PersonalizationsmarteditContextService,
		protected personalizationsmarteditUtils: any,
		protected personalizationsmarteditComponentHandlerService: PersonalizationsmarteditComponentHandlerService) {
	}

	$onInit(): void {
		this.selectedItems = this.personalizationsmarteditContextService.getCombinedView().selectedItems;
		this.containerSourceId = this.personalizationsmarteditComponentHandlerService.getContainerSourceIdForContainerId(this.containerId);
	}

	getLetterForElement(index: number): string {
		return this.personalizationsmarteditUtils.getLetterForElement(index);
	}

	getClassForElement(index: number): string {
		return this.personalizationsmarteditUtils.getClassForElement(index);
	}

	initItem(item: any): void {
		item.visible = false;
		(item.variation.actions || []).forEach((elem: any) => {
			if (elem.containerId && elem.containerId === this.containerSourceId) {
				item.visible = true;
			}
		});
		this.personalizationsmarteditUtils.getAndSetCatalogVersionNameL10N(item.variation);
	}

	isCustomizationFromCurrentCatalog(customization: string): boolean {
		return this.personalizationsmarteditUtils.isItemFromCurrentCatalog(customization, this.personalizationsmarteditContextService.getSeData());
	}

}
