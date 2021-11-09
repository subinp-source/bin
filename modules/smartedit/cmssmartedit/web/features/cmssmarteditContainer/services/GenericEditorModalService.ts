/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { GenericEditorOnSubmitResponse, IModalService, SeDowngradeService } from 'smarteditcommons';
import { IGenericEditorModalServiceComponent } from 'cmscommons';
import { GenericEditorModalComponent } from './components/GenericEditorModalComponent';

export interface IGenericEditorModalComponentData {
    data: IGenericEditorModalServiceComponent;
    saveCallback?: (item: GenericEditorOnSubmitResponse) => void;
    errorCallback?: (messages: string[], instance: GenericEditorModalComponent) => void;
}

/**
 * The Generic Editor Modal Service is used to open an editor modal window that contains a tabset.
 */

@SeDowngradeService()
export class GenericEditorModalService {
    constructor(private modalService: IModalService) {}

    /**
     * Function that opens an editor modal. For this method, you must specify an object to contain the edited information, and a save
     * callback that will be triggered once the Save button is clicked.
     */
    public open(
        data: IGenericEditorModalServiceComponent,
        saveCallback?: () => void,
        errorCallback?: () => void
    ) {
        const ref = this.modalService.open<IGenericEditorModalComponentData>({
            component: GenericEditorModalComponent,
            data: {
                data,
                saveCallback,
                errorCallback
            },
            config: {
                modalPanelClass: 'modal-lg',
                escKeyCloseable: false
            },
            templateConfig: {
                title: data.title,
                isDismissButtonVisible: true,
                titleSuffix: 'se.cms.editor.title.suffix'
            }
        });

        return new Promise((resolve, reject) => ref.afterClosed.subscribe(resolve, reject));
    }
}
