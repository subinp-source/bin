/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Payload } from '@smart/utils';

export interface IConfiguration extends Payload {
    defaultToolingLanguage: string;
    domain: string;
    previewTicketURI: string;
    smarteditroot: string;
    storefrontPreviewRoute: string;
    heartBeatTimeoutThreshold?: number;
}
