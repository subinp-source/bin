import {SeInjectable} from 'smarteditcommons';
import {Dictionary, LoDashStatic} from 'lodash';
import {PersonalizationsmarteditComponentHandlerService} from "personalizationsmartedit/service/PersonalizationsmarteditComponentHandlerService";

@SeInjectable()
export class PersonalizationsmarteditCustomizeViewHelper {

	constructor(
		private personalizationsmarteditComponentHandlerService: PersonalizationsmarteditComponentHandlerService,
		private lodash: LoDashStatic) {
	}

	getSourceContainersInfo(): Dictionary<number> {
		let slotsSelector: string = this.personalizationsmarteditComponentHandlerService.getAllSlotsSelector();
		slotsSelector += ' [data-smartedit-container-source-id]'; // space at beginning is important
		const slots: any = this.personalizationsmarteditComponentHandlerService.getFromSelector(slotsSelector);
		const slotIds = slots.map((key: number, val: JQuery) => {
			const component: JQuery = this.personalizationsmarteditComponentHandlerService.getFromSelector(val);
			const slot = {
				containerId: this.personalizationsmarteditComponentHandlerService.getParentContainerIdForComponent(component),
				containerSourceId: this.personalizationsmarteditComponentHandlerService.getParentContainerSourceIdForComponent(component)
			};
			return slot;
		});
		return this.lodash.countBy(slotIds, 'containerSourceId');
	}
}