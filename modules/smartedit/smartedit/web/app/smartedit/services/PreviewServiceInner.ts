/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { GatewayProxied, IPreviewService, SeDowngradeService, UrlUtils } from 'smarteditcommons';

/** @internal */
@GatewayProxied()
@SeDowngradeService(IPreviewService)
export class PreviewService extends IPreviewService {
    constructor(urlUtils: UrlUtils) {
        super(urlUtils);
    }
}
