/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ModalManager, ModalService, SeInjectable, TypedMap } from 'smarteditcommons';

@SeInjectable()
export class SelectComponentTypeModalService {
    constructor(private modalService: ModalService) {}

    open(types: TypedMap<string>): angular.IPromise<string> {
        return this.modalService.open({
            title: 'se.cms.nestedcomponenteditor.select.type',
            cssClasses: 'modal-dialog-window--normalized',
            templateInline: `<sub-type-selector class="sub-type-selector" data-sub-types="modalController.subTypes"
                data-on-sub-type-selected="modalController.onSubTypeSelected"></sub-type-selector>`,
            controller: function ctrl(modalManager: ModalManager) {
                'ngInject';

                this.subTypes = Object.keys(types).map((id: string) => {
                    return {
                        id,
                        label: types[id]
                    };
                });

                this.onSubTypeSelected = function(subType: { id: string; label: string }) {
                    modalManager.close(subType.id);
                };
            }
        });
    }
}
