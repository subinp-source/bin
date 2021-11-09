/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { GatewayProxied, SeInjectable } from 'smarteditcommons';
import { IEditorModalService } from 'cmscommons/services/IEditorModalService';

@GatewayProxied('open', 'openAndRerenderSlot')
@SeInjectable()
export class EditorModalService extends IEditorModalService {
    constructor() {
        super();
    }
}
