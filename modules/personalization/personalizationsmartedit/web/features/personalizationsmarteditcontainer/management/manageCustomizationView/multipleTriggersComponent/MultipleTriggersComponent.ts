import {SeComponent} from 'smarteditcommons';
import {ITriggerTab} from "./ITriggerTab";
import {TriggerTabService} from "./TriggerTabService";

@SeComponent({
	templateUrl: 'multipleTriggersComponentTemplate.html'
})
export class MultipleTriggersComponent {

	public tabsList: ITriggerTab[];

	constructor(
		private triggerTabService: TriggerTabService
	) {}

	$onInit(): void {
		this.tabsList = this.triggerTabService.getTriggersTabs();
	}

}
