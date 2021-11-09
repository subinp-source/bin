import {SeModule} from 'smarteditcommons';
import {CustomizationDataFactory} from "personalizationsmarteditcontainer/dataFactory/CustomizationDataFactory";
import {PersonalizationsmarteditServicesModule} from 'personalizationsmarteditcontainer/service/PersonalizationsmarteditServicesModule';

@SeModule({
	imports: [
		PersonalizationsmarteditServicesModule
	],
	providers: [
		CustomizationDataFactory
	]
})
export class PersonalizationsmarteditDataFactory {}
