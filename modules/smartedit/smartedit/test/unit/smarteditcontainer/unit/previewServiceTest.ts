/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lodash from 'lodash';
import {
    annotationService,
    functionsUtils,
    GatewayProxied,
    IPreviewData,
    IRestService,
    LogService,
    RestServiceFactory
} from 'smarteditcommons';
import { LoadConfigManagerService, PreviewService } from 'smarteditcontainer/services';

describe('previewService', () => {
    const mockSaveResult: IPreviewData = {
        ticketId: 'xyz',
        catalogVersions: [
            {
                catalog: 'catalog',
                catalogVersion: 'catalogVersion'
            }
        ],
        language: 'language',
        resourcePath: 'abc',
        pageId: 'pageId'
    };
    const mockUpdateResult: IPreviewData = {
        ticketId: 'xyz',
        catalogVersions: [
            {
                catalog: 'catalog',
                catalogVersion: 'catalogVersion'
            }
        ],
        language: 'language',
        resourcePath: 'abc'
    };
    const updatedUrl = 'someUpdatedUrl';
    const storefrontUrl = '/storefront';
    const mockPreviewData: IPreviewData = {
        catalogVersions: [
            {
                catalog: 'catalog',
                catalogVersion: 'catalogVersion'
            }
        ],
        language: 'language',
        resourcePath: 'resourcePath',
        pageId: 'pageId'
    };

    const mockConfiguration = {
        domain: 'https://somedomain:999/',
        previewTicketURI: 'somePreviewURI'
    };

    // Injected mocks
    let logService: jasmine.SpyObj<LogService>;
    let loadConfigManagerService: jasmine.SpyObj<LoadConfigManagerService>;
    let restServiceFactory: jasmine.SpyObj<RestServiceFactory>;
    let mockUrlUtils: jasmine.SpyObj<any>;

    // Common mocks
    let rsf: jasmine.SpyObj<IRestService<any>>;
    let createPreviewSpy: jasmine.Spy;

    // Service being tested
    let previewService: PreviewService;

    beforeEach(() => {
        logService = jasmine.createSpyObj('logService', ['debug', 'error']);
        loadConfigManagerService = jasmine.createSpyObj('loadConfigManagerService', [
            'loadAsObject'
        ]);

        restServiceFactory = jasmine.createSpyObj('restServiceFactory', ['get']);
        rsf = jasmine.createSpyObj('rsf', ['save', 'update']);
        rsf.save.and.returnValue(Promise.resolve(mockSaveResult));
        rsf.update.and.returnValue(Promise.resolve(mockUpdateResult));
        restServiceFactory.get.and.returnValue(rsf);

        mockUrlUtils = jasmine.createSpyObj('mockUrlUtils', ['updateUrlParameter']);
        mockUrlUtils.updateUrlParameter.and.returnValue(updatedUrl);

        spyOn(functionsUtils, 'isUnitTestMode').and.returnValue(false);

        loadConfigManagerService.loadAsObject.and.returnValue(Promise.resolve(mockConfiguration));

        previewService = new PreviewService(
            logService,
            loadConfigManagerService,
            restServiceFactory,
            mockUrlUtils
        );
        createPreviewSpy = spyOn(previewService, 'createPreview').and.callThrough();
    });

    describe('initialization', () => {
        it('invokes the gateway proxy', () => {
            expect(annotationService.getClassAnnotation(PreviewService, GatewayProxied)).toEqual(
                []
            );
        });

        describe('prepares restServices', () => {
            it('WHEN prepareRestService is called AND configuration cannot be loaded THEN it will handle a rejected promise', async (done) => {
                const rejectReason = 'reject reason';
                loadConfigManagerService.loadAsObject.and.returnValue(Promise.reject(rejectReason));

                await ((previewService as any).prepareRestService() as Promise<void>).catch(
                    (err) => {
                        expect(err).toBe(rejectReason);
                        done();
                    }
                );
            });

            it('WHEN prepareRestService is called THEN it will set rest services', async () => {
                await (previewService as any).prepareRestService();

                expect((previewService as any).previewRestService).toBeDefined();
                expect((previewService as any).previewByticketRestService).toBeDefined();
            });
        });
    });

    describe('createPreview', () => {
        it('WHEN createPreview is called and save operation has failed THEN it will return a rejected promise', async (done) => {
            const rejectReason = 'reject reason';
            rsf.save.and.returnValue(Promise.reject(rejectReason));

            await previewService.createPreview(mockPreviewData).catch((err) => {
                expect(err).toBe(rejectReason);
                done();
            });
        });

        it('GIVEN required attributes for createPreview are missing WHEN createPreview is called THEN it will throw', async (done) => {
            const invalidPreviewData = lodash.cloneDeep(mockPreviewData);
            delete invalidPreviewData.catalogVersions;

            await previewService.createPreview(invalidPreviewData).catch((err) => {
                expect(err).toEqual(
                    new Error('ValidatePreviewDataAttributes - catalogVersions is empty')
                );
                expect(rsf.save.calls.count()).toBe(0);
                done();
            });
        });

        it('WHEN createPreview is called THEN it will save the Preview Data', async () => {
            const result = await previewService.createPreview(mockPreviewData);

            expect(rsf.save).toHaveBeenCalledWith(mockPreviewData);
            expect(result).toEqual(mockSaveResult);
        });

        it('WHEN createPreview is called THEN previewRestService will be set', async () => {
            // GIVEN
            restServiceFactory.get.calls.reset();
            loadConfigManagerService.loadAsObject.calls.reset();

            // WHEN
            await previewService.createPreview(mockPreviewData);

            // THEN
            expect(loadConfigManagerService.loadAsObject).toHaveBeenCalled();
            expect(restServiceFactory.get.calls.count()).toBe(2);
            expect(restServiceFactory.get).toHaveBeenCalledWith(mockConfiguration.previewTicketURI); // sets previewRestService
            expect(restServiceFactory.get).toHaveBeenCalledWith(
                mockConfiguration.previewTicketURI,
                'ticketId'
            );
        });
    });

    describe('updateUrlWithNewPreviewTicketId', () => {
        it('WHEN updateUrlWithNewPreviewTicketId is called THEN it will return the proper updated url', async () => {
            // WHEN
            const updatedUrlActual = await previewService.updateUrlWithNewPreviewTicketId(
                storefrontUrl,
                mockPreviewData
            );

            // THEN
            expect(createPreviewSpy).toHaveBeenCalledWith(mockPreviewData);
            expect(mockUrlUtils.updateUrlParameter).toHaveBeenCalledWith(
                storefrontUrl,
                'cmsTicketId',
                mockSaveResult.ticketId
            );
            expect(updatedUrlActual).toBe(updatedUrl);
        });
    });

    describe('updatePreview', () => {
        it('GIVEN required attributes for updatePreview are missing WHEN createPreview is called THEN it will throw', async (done) => {
            const invalidPreviewData = lodash.cloneDeep(mockPreviewData);

            await previewService.updatePreview(invalidPreviewData).catch((err) => {
                expect(err).toEqual(new Error('ValidatePreviewDataAttributes - ticketId is empty'));
                expect(rsf.update.calls.count()).toBe(0);
                done();
            });
        });

        it('WHEN updatePreview is called AND update operation has failed THEN it will return a rejected promise', async (done) => {
            const rejectReason = 'reject reason';
            rsf.update.and.returnValue(Promise.reject(rejectReason));
            const data = lodash.cloneDeep(mockPreviewData);
            data.ticketId = 'xyz';

            await previewService.updatePreview(data).catch((err) => {
                expect(err).toBe(rejectReason);
                done();
            });
        });

        it('WHEN updatePreview is called THEN it will return a proper response', async () => {
            const data = lodash.cloneDeep(mockPreviewData);
            data.ticketId = 'xyz';
            delete data.pageId;

            const result = await previewService.updatePreview(data);

            expect(rsf.update).toHaveBeenCalledWith(data);
            expect(result).toBe(mockUpdateResult);
        });

        it('WHEN updatePreview is called THEN previewByticketRestService will be set', async () => {
            // GIVEN
            const data = lodash.cloneDeep(mockPreviewData);
            data.ticketId = 'xyz';
            restServiceFactory.get.calls.reset();
            loadConfigManagerService.loadAsObject.calls.reset();

            // WHEN
            await previewService.updatePreview(data);

            // THEN
            expect(loadConfigManagerService.loadAsObject).toHaveBeenCalled();
            expect(restServiceFactory.get.calls.count()).toBe(2);
            expect(restServiceFactory.get).toHaveBeenCalledWith(mockConfiguration.previewTicketURI);
            expect(restServiceFactory.get).toHaveBeenCalledWith(
                mockConfiguration.previewTicketURI,
                'ticketId'
            );
        });
    });
});
