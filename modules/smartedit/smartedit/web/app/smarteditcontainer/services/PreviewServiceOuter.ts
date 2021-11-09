/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lodash from 'lodash';

import {
    objectUtils,
    GatewayProxied,
    IPreviewData,
    IPreviewService,
    IRestService,
    LogService,
    PREVIEW_RESOURCE_URI,
    RestServiceFactory,
    SeDowngradeService,
    UrlUtils
} from 'smarteditcommons';
import { LoadConfigManagerService } from 'smarteditcontainer/services';

/** @internal */
@GatewayProxied()
@SeDowngradeService(IPreviewService)
export class PreviewService extends IPreviewService {
    // TODO - cache invalidation on configuration changes
    private previewRestService: IRestService<IPreviewData>;
    private previewByticketRestService: IRestService<IPreviewData>;
    private domain: string;
    private ticketIdIdentifier: string = 'ticketId';

    constructor(
        private log: LogService,
        private loadConfigManagerService: LoadConfigManagerService,
        private restServiceFactory: RestServiceFactory,
        urlUtils: UrlUtils
    ) {
        super(urlUtils);
    }

    public async createPreview(previewData: IPreviewData): Promise<IPreviewData> {
        /**
         * We don't know about any fields coming from other extensions, but throw error for any of the fields
         * that we do know about, namely the IPreviewData interface fields
         */
        const requiredFields: Readonly<string[]> = ['catalogVersions', 'resourcePath'];
        this.validatePreviewDataAttributes(previewData, requiredFields);

        await this.prepareRestService();
        try {
            const response: IPreviewData = await this.previewRestService.save(previewData);
            /**
             * The response object being stringified, when using copy method, has a method named toJSON()
             * because it is originally of type angular.resource.IResource<IPreviewData> and
             * that IResource.toJSON() method is responsible to remove $promise, $resolved properties from the response object.
             */
            return objectUtils.copy(response);
        } catch (err) {
            this.log.error('PreviewService.createPreview() - Error creating preview');
            return Promise.reject(err);
        }
    }

    public async updatePreview(previewData: IPreviewData): Promise<IPreviewData> {
        const requiredFields: Readonly<string[]> = ['catalogVersions', 'resourcePath', 'ticketId'];
        this.validatePreviewDataAttributes(previewData, requiredFields);

        await this.prepareRestService();
        try {
            return await this.previewByticketRestService.update(previewData);
        } catch (err) {
            this.log.error('PreviewService.updatePreview() - Error updating preview');
            return Promise.reject(err);
        }
    }

    public getResourcePathFromPreviewUrl(previewUrl: string): Promise<string> {
        return this.prepareRestService().then(() =>
            this.urlUtils.getAbsoluteURL(this.domain, previewUrl)
        );
    }

    private prepareRestService(): Promise<void> {
        if (!this.previewRestService || !this.previewByticketRestService) {
            return this.loadConfigManagerService.loadAsObject().then(
                (configurations) => {
                    const RESOURCE_URI = (configurations.previewTicketURI ||
                        PREVIEW_RESOURCE_URI) as string;

                    this.previewRestService = this.restServiceFactory.get(RESOURCE_URI);
                    this.previewByticketRestService = this.restServiceFactory.get(
                        RESOURCE_URI,
                        this.ticketIdIdentifier
                    );

                    this.domain = configurations.domain as string;
                },
                (err: any) => {
                    this.log.error('PreviewService.getRestService() - Error loading configuration');
                    return Promise.reject(err);
                }
            );
        }
        return Promise.resolve();
    }

    private validatePreviewDataAttributes(
        previewData: IPreviewData,
        requiredFields: Readonly<string[]>
    ): void {
        if (requiredFields) {
            requiredFields.forEach((elem) => {
                if (lodash.isEmpty(previewData[elem])) {
                    throw new Error(`ValidatePreviewDataAttributes - ${elem} is empty`);
                }
            });
        }
    }
}
