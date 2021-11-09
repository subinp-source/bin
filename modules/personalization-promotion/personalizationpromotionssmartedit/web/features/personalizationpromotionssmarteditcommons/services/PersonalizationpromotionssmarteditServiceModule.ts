
import {SeModule} from 'smarteditcommons';
import {PersonalizationpromotionssmarteditRestService} from './PersonalizationpromotionssmarteditRestService';

@SeModule({
	imports: [
		'smarteditServicesModule',
		'personalizationsmarteditCommonsModule'
	],
	providers: [
		PersonalizationpromotionssmarteditRestService
	]
})
export class PersonalizationpromotionssmarteditServiceModule {}
