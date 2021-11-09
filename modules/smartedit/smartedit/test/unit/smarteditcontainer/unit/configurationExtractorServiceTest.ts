/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import 'jasmine';
import { LogService } from 'smarteditcommons';
import { ConfigurationExtractorService } from 'smarteditcontainer/services';
import { ConfigurationModules } from 'smarteditcontainer/services/bootstrap/ConfigurationModules';

describe('configurationExtractorService', () => {
    let result: ConfigurationModules;
    let logService: jasmine.SpyObj<LogService>;

    describe('extractSEContainerModules', () => {
        const CONFIGURATION = {
            smarteditroot: 'smarteditroot1',
            domain: 'domain1',
            authenticationMap: {
                someKey: 'someValue'
            },
            'applications.cmssmarteditValidContainer': {
                smartEditContainerLocation: '/cmssmartedit/cmssmartedit/js/cmssmarteditContainer.js'
            },
            'applications.cmssmarteditValidContainerFullPathHttps': {
                smartEditContainerLocation:
                    'https://domain2/cmssmartedit/cmssmartedit/js/cmssmarteditContainer.js',
                extends: 'cmssmarteditValidContainer'
            },
            'applications.cmssmarteditValidContainerFullPathHttp': {
                smartEditContainerLocation:
                    'http://domain2/cmssmartedit/cmssmartedit/js/cmssmarteditContainer.js'
            },
            'applications.cmssmarteditInvalidContainerNumberAsValue': 3,
            'applications.cmssmarteditInvalidContainerStringAsValue': 'Hello',
            'applications.cmssmarteditInvalidContainerArrayAsValue': ['Hello'],
            'applications.cmssmarteditInvalidContainerObjectMissingRequiredKey': {}
        };

        beforeEach(() => {
            logService = jasmine.createSpyObj<LogService>('logService', ['error']);
            result = new ConfigurationExtractorService(logService).extractSEContainerModules(
                CONFIGURATION
            );
        });

        it('should return an object with applications, and an authentication map', () => {
            expect(result.applications).toBeDefined();
            expect(result.authenticationMap).toBeDefined();
        });

        it('should keep the authentication map on the returned object as is', () => {
            expect(result.authenticationMap).toEqual({
                someKey: 'someValue'
            });
        });

        it('should filter out applications that define a number as the value instead of an object specifying the location of the container script', () => {
            expect(
                result.applications.find(
                    (app) => app.name === 'cmssmarteditInvalidContainerNumberAsValue'
                )
            ).not.toBeDefined();
        });

        it('should filter out applications that define a string as the value instead of an object specifying the location of the container script', () => {
            expect(
                result.applications.find(
                    (app) => app.name === 'cmssmarteditInvalidContainerStringAsValue'
                )
            ).not.toBeDefined();
        });

        it('should filter out applications that define a array as the value instead of an object specifying the location of the container script', () => {
            expect(
                result.applications.find(
                    (app) => app.name === 'cmssmarteditInvalidContainerArrayAsValue'
                )
            ).not.toBeDefined();
        });

        it('should filter out applications that define an object as the value but the object is missing the smartedit container location key', () => {
            expect(
                result.applications.find(
                    (app) => app.name === 'cmssmarteditInvalidContainerObjectMissingRequiredKey'
                )
            ).not.toBeDefined();
        });

        it('should accept a smartedit container application location and append the domain onto the location if the location is a subpath', () => {
            expect(result.applications).toContain({
                name: 'cmssmarteditValidContainer',
                location: 'domain1/cmssmartedit/cmssmartedit/js/cmssmarteditContainer.js'
            });
        });

        it('should accept a smartedit container application location as is if it is a full path with the https protocol', () => {
            expect(result.applications).toContain({
                name: 'cmssmarteditValidContainerFullPathHttps',
                location: 'https://domain2/cmssmartedit/cmssmartedit/js/cmssmarteditContainer.js',
                extends: 'cmssmarteditValidContainer'
            });
        });

        it('should accept a smartedit container application location as is if it is a full path with the http protocol', () => {
            expect(result.applications).toContain({
                name: 'cmssmarteditValidContainerFullPathHttp',
                location: 'http://domain2/cmssmartedit/cmssmartedit/js/cmssmarteditContainer.js'
            });
        });
    });

    describe('extractSEModules', () => {
        const CONFIGURATION = {
            smarteditroot: 'smarteditroot1',
            domain: 'domain1',
            authenticationMap: {
                someKey: 'someValue'
            },
            'applications.cmssmarteditValid': {
                smartEditLocation: '/cmssmartedit/cmssmartedit/js/cmssmartedit.js'
            },
            'applications.cmssmarteditValidFullPathHttps': {
                smartEditLocation: 'https://domain2/cmssmartedit/cmssmartedit/js/cmssmartedit.js',
                extends: 'cmssmarteditValid'
            },
            'applications.cmssmarteditValidFullPathHttp': {
                smartEditLocation: 'http://domain2/cmssmartedit/cmssmartedit/js/cmssmartedit.js'
            },
            'applications.cmssmarteditInvalidNumberAsValue': 3,
            'applications.cmssmarteditInvalidStringAsValue': 'Hello',
            'applications.cmssmarteditInvalidArrayAsValue': ['Hello'],
            'applications.cmssmarteditInvalidObjectMissingRequiredKey': {}
        };

        beforeEach(() => {
            result = new ConfigurationExtractorService(logService).extractSEModules(CONFIGURATION);
        });

        it('should return an object with applications, and an authentication map', () => {
            expect(result.applications).toBeDefined();
            expect(result.authenticationMap).toBeDefined();
        });

        it('should keep the authentication map on the returned object as is', () => {
            expect(result.authenticationMap).toEqual({
                someKey: 'someValue'
            });
        });

        it('should filter out applications that define a number as the value instead of an object specifying the location of the  script', () => {
            expect(
                result.applications.find((app) => app.name === 'cmssmarteditInvalidNumberAsValue')
            ).not.toBeDefined();
        });

        it('should filter out applications that define a string as the value instead of an object specifying the location of the  script', () => {
            expect(
                result.applications.find((app) => app.name === 'cmssmarteditInvalidStringAsValue')
            ).not.toBeDefined();
        });

        it('should filter out applications that define a array as the value instead of an object specifying the location of the  script', () => {
            expect(
                result.applications.find((app) => app.name === 'cmssmarteditInvalidArrayAsValue')
            ).not.toBeDefined();
        });

        it('should filter out applications that define an object as the value but the object is missing the smartedit  location key', () => {
            expect(
                result.applications.find(
                    (app) => app.name === 'cmssmarteditInvalidObjectMissingRequiredKey'
                )
            ).not.toBeDefined();
        });

        it('should accept a smartedit  application location and append the domain onto the location if the location is a subpath', () => {
            expect(result.applications).toContain({
                name: 'cmssmarteditValid',
                location: 'domain1/cmssmartedit/cmssmartedit/js/cmssmartedit.js'
            });
        });

        it('should accept a smartedit  application location as is if it is a full path with the https protocol', () => {
            expect(result.applications).toContain({
                name: 'cmssmarteditValidFullPathHttps',
                location: 'https://domain2/cmssmartedit/cmssmartedit/js/cmssmartedit.js',
                extends: 'cmssmarteditValid'
            });
        });

        it('should accept a smartedit  application location as is if it is a full path with the http protocol', () => {
            expect(result.applications).toContain({
                name: 'cmssmarteditValidFullPathHttp',
                location: 'http://domain2/cmssmartedit/cmssmartedit/js/cmssmartedit.js'
            });
        });
    });

    describe('orderApplications', () => {
        it('applications will be sorted by means of extends keyword', () => {
            logService = jasmine.createSpyObj<LogService>('logService', ['error']);
            const service = new ConfigurationExtractorService(logService);

            const orderedResult = service.orderApplications([
                { name: 'AppD', location: 'SEContainerLocationForAppD', extends: 'AppC' },
                { name: 'AppA', location: 'SEContainerLocationForAppA' },
                { name: 'AppE', location: 'SEContainerLocationForAppE', extends: 'unkownApp' },
                { name: 'AppC', location: 'SEContainerLocationForAppC', extends: 'AppB' },
                { name: 'AppB', location: 'SEContainerLocationForAppB' }
            ]);
            expect(orderedResult).toEqual([
                { name: 'AppA', location: 'SEContainerLocationForAppA' },
                { name: 'AppB', location: 'SEContainerLocationForAppB' },
                { name: 'AppC', location: 'SEContainerLocationForAppC', extends: 'AppB' },
                { name: 'AppD', location: 'SEContainerLocationForAppD', extends: 'AppC' }
            ]);

            expect(logService.error).toHaveBeenCalledWith(
                "Application AppE located at SEContainerLocationForAppE is ignored because it extends an unknown application 'unkownApp'; SmartEdit functionality may be compromised."
            );
        });
    });
});
