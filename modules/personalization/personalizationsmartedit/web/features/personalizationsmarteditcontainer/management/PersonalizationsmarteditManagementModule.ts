import {SeModule} from 'smarteditcommons';
import {MultipleTriggersComponent} from './manageCustomizationView/multipleTriggersComponent/MultipleTriggersComponent';
import {TriggerTabService} from './manageCustomizationView/multipleTriggersComponent/TriggerTabService';

@SeModule({
	imports: [
		'smarteditServicesModule'
	],
	providers: [
		TriggerTabService
	],
	declarations: [
		MultipleTriggersComponent
	]
})
export class PersonalizationsmarteditManagementModule {}
