/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ModalService, SeComponent } from 'smarteditcommons';
import { CMSRestriction } from 'cmscommons';

@SeComponent({
    inputs: ['restrictions'],
    templateUrl: 'restrictionsViewerTemplate.html'
})
export class RestrictionsViewerComponent {
    public restrictions: CMSRestriction[];

    constructor(private modalService: ModalService) {}

    public showRestrictions(restrictions: CMSRestriction[]) {
        return this.modalService.open({
            title: 'se.cms.restrictionsviewer.title',
            size: 'md',
            templateInline: `
            <div class="se-restrictions-list fd-menu__list fd-menu__list--separated">
                <div class="se-restriction__item fd-menu__item" data-ng-repeat="restriction in modalController.restrictions">
                    <div class="se-restrictions-list--content">
                        <div class="se-restriction__item-name">{{restriction.name}}</div>
                        <div class="se-restriction__item-type-and-id">{{restriction.type | l10n}}</div>
                        <div class="se-restriction__item-description">{{restriction.description}}</div>
                    </div>
                </div>
            </div>
            `,
            controller: function ctrl() {
                'ngInject';
                this.restrictions = restrictions;
            }
        });
    }
}
