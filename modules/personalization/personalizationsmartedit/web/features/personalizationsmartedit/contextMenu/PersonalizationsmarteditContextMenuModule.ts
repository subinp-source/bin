import {SeModule} from 'smarteditcommons';
import {PersonalizationsmarteditServicesModule} from 'personalizationsmartedit/service/PersonalizationsmarteditServicesModule';
import {PersonalizationsmarteditContextMenuServiceProxy} from 'personalizationsmartedit/contextMenu/PersonalizationsmarteditContextMenuServiceInnerProxy';

@SeModule({
	imports: [
		PersonalizationsmarteditServicesModule,
	],
	providers: [
		PersonalizationsmarteditContextMenuServiceProxy
	],
})
export class PersonalizationsmarteditContextMenuModule {}