/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Inject, Injectable } from '@angular/core';
import { DOCUMENT } from '@angular/common';

import { GatewayProxied, IIframeClickDetectionService, SeDowngradeService } from 'smarteditcommons';

/**
 * @ngdoc service
 * @name smarteditServicesModule.service:IframeClickDetectionService
 * @extends {smarteditServicesModule.interface:IIframeClickDetectionService}
 * @description
 */
@SeDowngradeService(IIframeClickDetectionService)
@GatewayProxied('onIframeClick')
@Injectable()
export class IframeClickDetectionService extends IIframeClickDetectionService {
    constructor(@Inject(DOCUMENT) document: Document) {
        super();
        document.addEventListener('mousedown', () => this.onIframeClick());
    }
}
