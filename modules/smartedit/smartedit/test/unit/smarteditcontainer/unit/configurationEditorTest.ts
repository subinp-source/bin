/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgForm } from '@angular/forms';
import * as lo from 'lodash';
import { IRestService, LogService, RestServiceFactory } from 'smarteditcommons';
import {
    Configuration,
    ConfigurationItem
} from 'smarteditcontainer/services/bootstrap/Configuration';
import { ConfigurationService, LoadConfigManagerService } from 'smarteditcontainer/services';

function mockConfigurationForm(conf?: {
    dirty?: boolean;
    valid?: boolean;
    invalid?: boolean;
}): NgForm {
    const form = jasmine.createSpyObj('form', ['markAsPristine', 'markAsDirty']);

    return {
        form,
        ...(conf || {})
    } as NgForm;
}

describe('ConfigurationEditor', () => {
    const dataGet = [
        {
            key: '1',
            value: '{"location":"uri"}',
            id: '1'
        },
        {
            key: 'otherkey',
            value: '{malformed}',
            id: '3'
        }
    ];
    const dataUpdate = {
        key: '1',
        value: '2',
        id: '2'
    };

    let loadConfigManagerService: jasmine.SpyObj<LoadConfigManagerService>;
    let restServiceFactory: jasmine.SpyObj<RestServiceFactory>;
    let logService: jasmine.SpyObj<LogService>;
    let service: ConfigurationService;
    let editorCRUDService: jasmine.SpyObj<IRestService<Configuration>>;

    beforeEach(() => {
        (lo as any).uniqueId = jasmine.createSpy('uniqueId').and.returnValue('uniqueId');

        logService = jasmine.createSpyObj('logService', ['log']);

        loadConfigManagerService = jasmine.createSpyObj('loadConfigManagerService', [
            'loadAsArray'
        ]);
        loadConfigManagerService.loadAsArray.and.callFake(() => Promise.resolve(dataGet));

        editorCRUDService = jasmine.createSpyObj('editorCRUDService', [
            'query',
            'update',
            'save',
            'remove'
        ]);
        editorCRUDService.query.and.callFake(() => {
            return Promise.resolve(dataGet);
        });
        editorCRUDService.update.and.callFake(() => {
            return Promise.resolve(dataUpdate);
        });
        editorCRUDService.save.and.callFake(() => {
            return Promise.resolve(dataUpdate);
        });
        editorCRUDService.remove.and.callFake(() => {
            return Promise.resolve();
        });

        restServiceFactory = jasmine.createSpyObj('restServiceFactory', ['get']);

        restServiceFactory.get.and.callFake((uri: string, identifier: string) => {
            if (uri === '/smartedit/configuration' && identifier === 'key') {
                return editorCRUDService;
            } else {
                throw new Error(
                    'unexpected uri ' +
                        uri +
                        ' and identifier ' +
                        identifier +
                        ' passed to restServiceFactory'
                );
            }
        });

        service = new ConfigurationService(
            logService,
            loadConfigManagerService,
            restServiceFactory
        );
    });

    it('calling reset() set component to prior pristine sate and call $setPristine on the component form', () => {
        const pristine = [
            {
                key: '1',
                value: '2'
            }
        ] as Configuration;
        const configurationForm = mockConfigurationForm();

        (service as any).pristine = pristine;

        (service as any).reset(configurationForm);

        expect((service as any).configuration).not.toBe(pristine);
        expect((service as any).configuration).toEqualData(pristine);
        expect(configurationForm.form.markAsPristine).toHaveBeenCalled();
    });

    it('successful loadAndPresent will set pristine state and model to the return a prettified value of the REST call and set errors for non JSON parsable data', async () => {
        await (service as any).loadAndPresent();

        expect((service as any).pristine).toEqualData([
            {
                key: '1',
                value: '{\n  "location": "uri"\n}',
                id: '1',
                uuid: 'uniqueId'
            },
            {
                key: 'otherkey',
                value: '{malformed}',
                id: '3',
                uuid: 'uniqueId',
                errors: {
                    values: [
                        {
                            message: 'se.configurationform.json.parse.error'
                        }
                    ]
                }
            }
        ]);
        expect((service as any).configuration).toEqualData([
            {
                key: '1',
                value: '{\n  "location": "uri"\n}',
                id: '1',
                uuid: 'uniqueId'
            },
            {
                key: 'otherkey',
                value: '{malformed}',
                id: '3',
                uuid: 'uniqueId',
                errors: {
                    values: [
                        {
                            message: 'se.configurationform.json.parse.error'
                        }
                    ]
                }
            }
        ]);

        expect(loadConfigManagerService.loadAsArray).toHaveBeenCalled();
    });

    it("delete will tag the entity 'toDelete' and set the form to dirty", () => {
        const configuration = [
            {
                key: 'a',
                value: '3'
            },
            {
                key: 'b',
                value: '4'
            }
        ] as Configuration;

        const configurationForm = mockConfigurationForm({ dirty: true, valid: true });

        service.removeEntry(configuration[0], configurationForm);

        expect(configuration[0].toDelete).toBe(true);
        expect(configurationForm.form.markAsDirty).toHaveBeenCalled();
    });

    it('delete will filter out the new entry from the configuration list ', () => {
        const configuration = [
            {
                key: 'a',
                value: '3',
                isNew: true
            },
            {
                key: 'b',
                value: '4'
            }
        ];

        (service as any).configuration = configuration;
        const configurationForm = mockConfigurationForm({ dirty: true, valid: true });

        service.removeEntry(configuration[0], configurationForm);

        expect((service as any).configuration).toEqual([
            {
                key: 'b',
                value: '4'
            }
        ]);
    });

    it('submit will do nothing if configurationForm is not dirty', () => {
        const configurationForm = mockConfigurationForm({ dirty: true, valid: false });

        (service as any).configuration = [];

        service.submit(configurationForm);

        expect(editorCRUDService.update).not.toHaveBeenCalled();
        expect(configurationForm.form.markAsPristine).not.toHaveBeenCalled();
    });

    it('submit will do nothing if configurationForm is not valid', () => {
        const configurationForm = mockConfigurationForm({ dirty: true, valid: false });

        (service as any).configuration = [];

        service.submit(configurationForm);

        expect(editorCRUDService.update).not.toHaveBeenCalled();
        expect(configurationForm.form.markAsPristine).not.toHaveBeenCalled();
    });

    it('submit will do nothing if duplicate keys are found and errors will be appended', () => {
        const configuration = [
            {
                key: 'a',
                value: '3'
            },
            {
                key: 'b',
                value: '4'
            },
            {
                key: 'a',
                value: '5'
            }
        ];
        (service as any).configuration = configuration;
        const configurationForm = mockConfigurationForm({ dirty: true, valid: true });

        service.submit(configurationForm);

        expect((service as any).configuration).toEqualData([
            {
                key: 'a',
                value: '3'
            },
            {
                key: 'b',
                value: '4'
            },
            {
                key: 'a',
                value: '5',
                errors: {
                    keys: [
                        {
                            message: 'se.configurationform.duplicate.entry.error'
                        }
                    ]
                }
            }
        ]);

        expect(editorCRUDService.save).not.toHaveBeenCalled();
        expect(editorCRUDService.update).not.toHaveBeenCalled();
        expect(configurationForm.form.markAsPristine).not.toHaveBeenCalled();
    });

    it("submit will call remove if 'toDelete', update if isNew not present and save if isNew is present, and send error if value is not JSON parsable", function(done) {
        const configuration = [
            {
                key: 'a',
                value: '3'
            },
            {
                key: 'b',
                value: '4',
                isNew: true
            },
            {
                key: 'c',
                value: '5',
                toDelete: true
            },
            {
                key: 'otherkey',
                value: '{malformed}'
            }
        ];
        (service as any).configuration = configuration;

        spyOn(service as any, 'loadAndPresent').and.returnValue(Promise.resolve());

        const configurationForm = mockConfigurationForm({ dirty: true, valid: true });

        service.submit(configurationForm).then(
            () => {
                fail('should have rejected');
            },
            () => {
                expect(editorCRUDService.update).toHaveBeenCalledWith({
                    key: 'a',
                    value: '3',
                    secured: false
                });
                expect(editorCRUDService.save).toHaveBeenCalledWith({
                    key: 'b',
                    value: '4',
                    secured: false
                });
                expect(editorCRUDService.remove).toHaveBeenCalledWith({
                    key: 'c',
                    value: '5',
                    secured: false
                });

                expect((service as any).configuration).toEqualData([
                    {
                        key: 'a',
                        value: '3',
                        hasErrors: false
                    },
                    {
                        key: 'b',
                        value: '4',
                        hasErrors: false
                    },
                    {
                        key: 'otherkey',
                        value: '{malformed}',
                        errors: {
                            values: [
                                {
                                    message: 'se.configurationform.json.parse.error'
                                }
                            ]
                        },
                        hasErrors: true
                    }
                ]);
                done();
            }
        );
    });

    it('WHEN submit is called with empty key and empty value in the configuration field THEN the service will respond with error message for both key and value and expect save and update of the editorCRUDService not to be called', function(done) {
        const configuration = [
            {
                key: '',
                value: ''
            },
            {
                key: 'b',
                value: '4',
                isNew: true
            },
            {
                key: 'c',
                value: '5'
            }
        ];
        (service as any).configuration = configuration;
        spyOn(service as any, 'loadAndPresent').and.returnValue(Promise.resolve());
        const configurationForm = mockConfigurationForm({
            dirty: true,
            valid: false,
            invalid: true
        });

        service.submit(configurationForm).then(
            () => {
                fail('should have rejected');
            },
            () => {
                expect((service as any).configuration).toEqualData([
                    {
                        key: '',
                        value: '',
                        errors: {
                            keys: [
                                {
                                    message: 'se.configurationform.required.entry.error'
                                }
                            ],
                            values: [
                                {
                                    message: 'se.configurationform.required.entry.error'
                                }
                            ]
                        },
                        hasErrors: true
                    },
                    {
                        key: 'b',
                        value: '4',
                        isNew: true
                    },
                    {
                        key: 'c',
                        value: '5'
                    }
                ]);

                expect(editorCRUDService.save).not.toHaveBeenCalled();
                expect(editorCRUDService.update).not.toHaveBeenCalled();
                expect(configurationForm.form.markAsPristine).not.toHaveBeenCalled();

                done();
            }
        );
    });

    it('WHEN validateUserInput is called with a valid entry value THEN it will set the entry.requiresUserCheck only if the user entered an absolute URL', () => {
        // Arrange
        const entryAbsoluteURL1 = {
            value: '"http://url.js"'
        } as ConfigurationItem;
        const entryAbsoluteURL2 = {
            value: '"https://url.js"'
        } as ConfigurationItem;
        const entryAbsoluteURL3 = {
            value:
                '{' +
                '"smartEditLocation": "http://cmssmartedit/cmssmartedit/js/cmssmartedit.js"' +
                '}'
        } as ConfigurationItem;
        const entryRelativeURL1 = {
            value: '"something else"'
        } as ConfigurationItem;
        const entryRelativeURL2 = {
            value: '"/path/to/file.js"'
        } as ConfigurationItem;
        const entryRelativeURL3 = {
            value:
                '{' + '"smartEditLocation": "/cmssmartedit/cmssmartedit/js/cmssmartedit.js"' + '}'
        } as ConfigurationItem;

        // Act/Assert

        service.validateUserInput(entryAbsoluteURL1);
        expect(entryAbsoluteURL1.requiresUserCheck).toBe(true);

        service.validateUserInput(entryAbsoluteURL2);
        expect(entryAbsoluteURL2.requiresUserCheck).toBe(true);

        service.validateUserInput(entryAbsoluteURL3);
        expect(entryAbsoluteURL3.requiresUserCheck).toBe(true);

        service.validateUserInput(entryRelativeURL1);
        expect(entryRelativeURL1.requiresUserCheck).toBe(false);

        service.validateUserInput(entryRelativeURL2);
        expect(entryRelativeURL2.requiresUserCheck).toBe(false);

        service.validateUserInput(entryRelativeURL3);
        expect(entryRelativeURL3.requiresUserCheck).toBe(false);
    });

    it('WHEN validate is called THEN it will throw an exception if the entry had to be checked by the user but it wasn not', () => {
        // Arrange
        const validEntry1 = {
            requiresUserCheck: false,
            value: '"some json"'
        } as ConfigurationItem;
        const validEntry2 = {
            requiresUserCheck: true,
            isCheckedByUser: true,
            value: '"some json"'
        } as ConfigurationItem;
        const invalidEntry1 = {
            requiresUserCheck: true,
            isCheckedByUser: false,
            value: '"some json"'
        } as ConfigurationItem;

        // Act/Assert

        expect(() => {
            (service as any).validate(validEntry1);
        }).not.toThrow();
        expect(() => {
            (service as any).validate(validEntry2);
        }).not.toThrow();
        expect(() => {
            (service as any).validate(invalidEntry1);
        }).toThrow();
    });
});
