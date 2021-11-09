import {SeModule} from 'smarteditcommons';
import {PersonalizationsmarteditShowActionListComponent} from "personalizationsmartedit/contextMenu/ShowActionList/PersonalizationsmarteditShowActionListComponent";
import {PersonalizationsmarteditCommonsModule} from "personalizationcommons";

@SeModule({
	imports: [
		PersonalizationsmarteditCommonsModule
	],
	declarations: [PersonalizationsmarteditShowActionListComponent]
})
export class PersonalizationsmarteditShowActionListModule {}
