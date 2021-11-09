import {SeModule} from 'smarteditcommons';
import {PersonalizationsmarteditShowComponentInfoListComponent} from "./ShowComponentInfoList/PersonalizationsmarteditShowComponentInfoList";
import {PersonalizationsmarteditCommonsModule} from "personalizationcommons";

@SeModule({
	imports: [
		PersonalizationsmarteditCommonsModule
	],

	declarations: [PersonalizationsmarteditShowComponentInfoListComponent]

})
export class PersonalizationsmarteditShowComponentInfoListModule {}
