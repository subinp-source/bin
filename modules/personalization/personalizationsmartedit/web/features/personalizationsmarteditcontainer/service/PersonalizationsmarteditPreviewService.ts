import {
	IExperienceService,
	SeInjectable
} from 'smarteditcommons';

@SeInjectable()
export class PersonalizationsmarteditPreviewService {

	constructor(
		protected experienceService: IExperienceService
	) {}

	removePersonalizationDataFromPreview() {
		return this.updatePreviewTicketWithVariations([]);
	}

	updatePreviewTicketWithVariations(variations: any) {
		return this.experienceService.getCurrentExperience().then((experience: any) => {
			if (!experience) {
				return undefined;
			}
			if (JSON.stringify(experience.variations) === JSON.stringify(variations)) {
				return undefined;
			}
			experience.variations = variations;
			return this.experienceService.setCurrentExperience(experience).then(() => {
				return this.experienceService.updateExperience();
			});
		});
	}
}
