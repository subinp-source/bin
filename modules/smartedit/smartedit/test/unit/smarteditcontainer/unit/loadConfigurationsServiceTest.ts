/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    promiseUtils,
    CONFIGURATION_URI,
    IRestService,
    ISharedDataService,
    LogService,
    RestServiceFactory,
    SMARTEDIT_RESOURCE_URI_REGEXP,
    SMARTEDIT_ROOT
} from 'smarteditcommons';
import { LoadConfigManagerService } from 'smarteditcontainer/services';
import { ConfigurationItem } from 'smarteditcontainer/services/bootstrap/Configuration';

describe('loadConfigurationService', () => {
    let restServiceFactory: jasmine.SpyObj<RestServiceFactory>;
    let restService: jasmine.SpyObj<IRestService<ConfigurationItem>>;
    let sharedDataService: jasmine.SpyObj<ISharedDataService>;
    let logService: jasmine.SpyObj<LogService>;
    let dataGet: ConfigurationItem[];
    let service: LoadConfigManagerService;

    beforeEach(() => {
        logService = jasmine.createSpyObj<LogService>('logService', ['log', 'error']);

        restServiceFactory = jasmine.createSpyObj<RestServiceFactory>('restServiceFactory', [
            'get'
        ]);
        restServiceFactory.get.and.callFake((url: string) => {
            if (url === CONFIGURATION_URI) {
                return restService;
            }
            throw new Error(`unexpected url ${url} passed to restServiceFactory`);
        });

        sharedDataService = jasmine.createSpyObj('sharedDataService', ['set']);

        dataGet = [
            {
                key: '1',
                value: '{"location":"uri"}'
            },
            {
                key: 'otherkey',
                value: '{malformed"key":"value"}'
            }
        ];

        restService = jasmine.createSpyObj<IRestService<ConfigurationItem>>('restService', [
            'query'
        ]);
        restService.query.and.returnValue(Promise.resolve(dataGet));

        service = new LoadConfigManagerService(
            restServiceFactory,
            sharedDataService,
            logService,
            promiseUtils,
            SMARTEDIT_RESOURCE_URI_REGEXP,
            SMARTEDIT_ROOT
        );
    });

    it('successful loadAsArray will load and only parse stringified JSON content from REST call response', (done) => {
        service.loadAsArray().then(
            function(response) {
                expect(response).toEqualData([
                    {
                        key: '1',
                        value: '{"location":"uri"}'
                    },
                    {
                        key: 'otherkey',
                        value: '{malformed"key":"value"}'
                    }
                ]);
                done();
            },
            () => {
                fail();
            }
        );
        // for promises to actually resolve :

        expect(restService.query).toHaveBeenCalled();
    });

    it('successful loadAsObject will only load and parse stringified JSON content and calculate domain and smarteditroot', async () => {
        sharedDataService.set.and.returnValue(Promise.resolve());
        spyOn(service as any, '_getLocation').and.returnValue('somedomain/smartedit');
        const response = await service.loadAsObject();

        expect(response).toEqualData({
            1: {
                location: 'uri'
            },
            domain: 'somedomain',
            smarteditroot: 'somedomain/smartedit'
        });

        // for promises to actually resolve :

        expect(restService.query).toHaveBeenCalled();
        expect(sharedDataService.set).toHaveBeenCalledWith('configuration', {
            1: {
                location: 'uri'
            },
            domain: 'somedomain',
            smarteditroot: 'somedomain/smartedit'
        });
        expect((service as any)._getLocation).toHaveBeenCalled();
    });
    it('successful loadAsObject will only load and parse stringified JSON content and convert it to object and share it through sharedDataService', async () => {
        dataGet = [
            {
                key: '1',
                value: '{"location":"uri"}'
            },
            {
                key: 'domain',
                value: '"somedomain"'
            }
        ];

        sharedDataService.set.and.returnValue(Promise.resolve());
        spyOn(service as any, '_getLocation').and.returnValue('http://domain/smartedit');
        const response = await service.loadAsObject();
        expect(response).toEqualData({
            1: {
                location: 'uri'
            },
            domain: 'http://domain',
            smarteditroot: 'http://domain/smartedit'
        });
        // for promises to actually resolve :

        expect(restService.query).toHaveBeenCalled();
        expect(sharedDataService.set).toHaveBeenCalledWith('configuration', {
            1: {
                location: 'uri'
            },
            domain: 'http://domain',
            smarteditroot: 'http://domain/smartedit'
        });
    });
});
