import {SeModule} from 'smarteditcommons';
import {PersonalizationsmarteditCustomizeViewServiceProxy} from "./PersonalizationsmarteditCustomizeViewServiceInnerProxy";
import {PersonalizationsmarteditServicesModule} from "personalizationsmartedit/service/PersonalizationsmarteditServicesModule";

@SeModule({
	imports: [
		PersonalizationsmarteditServicesModule
	],
	providers: [
		PersonalizationsmarteditCustomizeViewServiceProxy
	]
})
export class PersonalizationsmarteditCustomizeViewModule {}