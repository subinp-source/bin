import {SeModule} from 'smarteditcommons';
import {VersionCheckerService} from "personalizationsmarteditcontainer/versioning/VersionCheckerService";

@SeModule({
	imports: [
		'smarteditServicesModule'
	],
	providers: [
		VersionCheckerService
	]
})
export class VersioningModule {}
