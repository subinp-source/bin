import {SeInjectable} from 'smarteditcommons';
import {ITriggerTab} from "./ITriggerTab";
import {TriggerDataState} from "./TriggerDataState";

@SeInjectable()
export class TriggerTabService {

	private tabsList: ITriggerTab[] = [];
	private triggerDataState: TriggerDataState;

	constructor() {
		this.triggerDataState = new TriggerDataState();
	}

	getTriggersTabs(): ITriggerTab[] {
		return this.tabsList;
	}

	addTriggerTab(trigger: ITriggerTab): void {
		const itemWithSameId = this.tabsList.filter((item: ITriggerTab) => {
			return item.id === trigger.id;
		});
		if (itemWithSameId.length === 0) {
			this.tabsList.push(trigger);
		}
	}

	removeTriggerTab(trigger: ITriggerTab): void {
		const itemWithSameId = this.tabsList.filter((item: ITriggerTab) => {
			return item.id === trigger.id;
		});
		if (itemWithSameId.length > 0) {
			const index = this.tabsList.indexOf(itemWithSameId[0]);
			this.tabsList.splice(index, 1);
		}
	}

	getTriggerDataState(): TriggerDataState {
		return this.triggerDataState.state;
	}

}
