/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { GenericEditorStructure, Payload } from 'smarteditcommons';

/**
 * Interface used by generic editor modal service.
 */
export interface IGenericEditorModalServiceComponent {
    componentUuid?: string;
    componentType: string;
    title: string;
    content?: Payload;
    structure?: GenericEditorStructure;
    contentApi?: string;
    targetedQualifier?: string;
    editorStackId?: string;
    cancelLabel?: string;
    saveLabel?: string;
    initialDirty?: boolean;
    readOnlyMode?: boolean;
    messages?: {
        type: string;
        message: string;
    }[];
    saveCallback?: (item: Payload) => void;
    errorCallback?: (messages: object[], form: any) => void;
}
