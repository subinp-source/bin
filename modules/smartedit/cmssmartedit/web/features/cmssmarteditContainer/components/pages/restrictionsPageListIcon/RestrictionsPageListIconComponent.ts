/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeComponent } from 'smarteditcommons';
import * as angular from 'angular';
import { AssetsService } from 'cmscommons';

@SeComponent({
    inputs: ['numberOfRestrictions'],
    templateUrl: 'restrictionsPageListIconTemplate.html'
})
export class RestrictionsPageListIconComponent {
    public numberOfRestrictions: number;

    constructor(
        private assetsService: AssetsService,
        private $translate: angular.translate.ITranslateService
    ) {}

    get imageSrc() {
        return (
            this.assetsService.getAssetsRoot() +
            (this.numberOfRestrictions > 0
                ? '/images/icon_restriction_small_blue.png'
                : '/images/icon_restriction_small_grey.png')
        );
    }

    get title() {
        return this.$translate.instant('se.icon.tooltip.visibility', {
            numberOfRestrictions: this.numberOfRestrictions
        });
    }
}
