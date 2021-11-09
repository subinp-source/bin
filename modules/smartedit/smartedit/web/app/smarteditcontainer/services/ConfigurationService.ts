/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as lo from 'lodash';
import { NgForm } from '@angular/forms';

import {
    objectUtils,
    stringUtils,
    CONFIGURATION_URI,
    Errors,
    IRestService,
    LogService,
    RestServiceFactory,
    SeDowngradeService
} from 'smarteditcommons';
import {
    Configuration,
    ConfigurationItem
} from 'smarteditcontainer/services/bootstrap/Configuration';
import { LoadConfigManagerService } from './bootstrap/LoadConfigManagerService';

/** @internal */
@SeDowngradeService()
export class ConfigurationService {
    // Constants
    private readonly ABSOLUTE_URI_NOT_APPROVED = 'URI_EXCEPTION';
    private readonly ABSOLUTE_URI_REGEX = /(\"[A-Za-z]+:\/|\/\/)/;

    private editorCRUDService: IRestService<Configuration>;
    private configuration: Configuration = [];
    private pristine: Configuration;
    private loadCallback: () => void;

    constructor(
        private logService: LogService,
        private loadConfigManagerService: LoadConfigManagerService,
        private restServiceFactory: RestServiceFactory
    ) {
        this.editorCRUDService = this.restServiceFactory.get<Configuration>(
            CONFIGURATION_URI,
            'key'
        );
    }

    /*
     * The Add Entry method adds an entry to the list of configurations.
     *
     */
    public addEntry(): void {
        const item: ConfigurationItem = { key: '', value: '', isNew: true, uuid: lo.uniqueId() };

        this.configuration = [item, ...(this.configuration || [])];
    }
    /*
     * The Remove Entry method deletes the specified entry from the list of configurations. The method does not delete the actual configuration, but just removes it from the array of configurations.
     * The entry will be deleted when a user clicks the Submit button but if the entry is new we can are removing it from the configuration
     *
     * @param {Object} entry The object to be deleted
     * @param {Object} configurationForm The form object which is an instance of {@link https://docs.angularjs.org/api/ng/type/form.FormController FormController}
     * that provides methods to monitor and control the state of the form.
     */
    public removeEntry(entry: ConfigurationItem, configurationForm: NgForm) {
        if (entry.isNew) {
            this.configuration = this.configuration.filter(
                (confEntry: ConfigurationItem) => !confEntry.isNew || confEntry.key !== entry.key
            );
        } else {
            configurationForm.form.markAsDirty();
            entry.toDelete = true;
        }
    }
    /*
     * Method that returns a list of configurations by filtering out only those configurations whose 'toDelete' parameter is set to false.
     *
     * @returns {Object} A list of filtered configurations.
     */
    public filterConfiguration() {
        return (this.configuration || []).filter(
            (instance: ConfigurationItem) => !instance.toDelete
        );
    }

    public validateUserInput(entry: ConfigurationItem): void {
        if (!entry.value) {
            return;
        }

        entry.requiresUserCheck = !!entry.value.match(this.ABSOLUTE_URI_REGEX);
    }
    /*
     * The Submit method saves the list of available configurations by making a REST call to a web service.
     * The method is called when a user clicks the Submit button in the configuration editor.
     *
     * @param {Object} configurationForm The form object that is an instance of {@link https://docs.angularjs.org/api/ng/type/form.FormController FormController}.
     * It provides methods to monitor and control the state of the form.
     */
    public submit(configurationForm: NgForm): Promise<any[]> {
        if (!configurationForm.dirty || !this.isValid(configurationForm)) {
            return Promise.reject<any[]>([]);
        }

        configurationForm.form.markAsPristine();

        return Promise.all(
            this.configuration.map((entry: ConfigurationItem, i: number) => {
                try {
                    const payload = objectUtils.copy(entry);
                    delete payload.toDelete;
                    delete payload.errors;
                    delete payload.uuid;
                    const method =
                        entry.toDelete === true
                            ? 'remove'
                            : payload.isNew === true
                            ? 'save'
                            : 'update';
                    payload.secured = false; // needed for yaas configuration service
                    delete payload.isNew;
                    switch (method) {
                        case 'save':
                            payload.value = this.validate(payload);
                            break;
                        case 'update':
                            payload.value = this.validate(payload);
                            break;
                        case 'remove':
                            break;
                    }
                    entry.hasErrors = false;
                    return (this.editorCRUDService as any)[method](payload).then(
                        function(entity: ConfigurationItem, index: number, meth: string) {
                            switch (meth) {
                                case 'save':
                                    delete entity.isNew;
                                    break;
                                case 'remove':
                                    this.configuration.splice(index, 1);
                                    break;
                            }
                        }.bind(this, entry, i, method),
                        () => {
                            this.addValueError(entry, 'configurationform.save.error');
                            return Promise.reject<void>({});
                        }
                    );
                } catch (error) {
                    entry.hasErrors = true;
                    if (error instanceof Errors.ParseError) {
                        this.addValueError(entry, 'se.configurationform.json.parse.error');
                        return Promise.reject<void>({});
                    }
                }
            })
        );
    }
    /*
     * The init method initializes the configuration editor and loads all the configurations so they can be edited.
     *
     * @param {Function} loadCallback The callback to be executed after loading the configurations.
     */
    public init(_loadCallback?: () => void): Promise<any> {
        this.loadCallback = _loadCallback || lo.noop;
        return this.loadAndPresent();
    }

    private reset(configurationForm?: NgForm) {
        this.configuration = objectUtils.copy(this.pristine);

        if (configurationForm) {
            configurationForm.form.markAsPristine();
        }
        if (this.loadCallback) {
            this.loadCallback();
        }
    }
    private addError(entry: ConfigurationItem, type: string, message: string) {
        entry.errors = entry.errors || {};
        entry.errors[type] = entry.errors[type] || [];
        entry.errors[type].push({
            message
        });
    }
    private addKeyError(entry: ConfigurationItem, message: string) {
        this.addError(entry, 'keys', message);
    }
    private addValueError(entry: ConfigurationItem, message: string) {
        this.addError(entry, 'values', message);
    }
    private prettify(array: ConfigurationItem[]) {
        const configuration = objectUtils.copy(array);
        configuration.forEach((entry: ConfigurationItem) => {
            try {
                entry.value = JSON.stringify(JSON.parse(entry.value), null, 2);
            } catch (parseError) {
                this.addValueError(entry, 'se.configurationform.json.parse.error');
            }
        });
        return configuration;
    }

    /**
     * for editing purposes
     */
    private loadAndPresent(): Promise<any> {
        return new Promise((resolve, reject) =>
            this.loadConfigManagerService.loadAsArray().then(
                (response: ConfigurationItem[]) => {
                    this.pristine = this.prettify(
                        response.map((item) => ({ ...item, uuid: lo.uniqueId() }))
                    );
                    this.reset();
                    resolve();
                },
                () => {
                    this.logService.log('load failed');
                    reject();
                }
            )
        );
    }

    private isValid(configurationForm: NgForm): boolean {
        this.configuration.forEach((entry: ConfigurationItem) => {
            delete entry.errors;
        });
        if (configurationForm.invalid) {
            this.configuration.forEach((entry: ConfigurationItem) => {
                if (stringUtils.isBlank(entry.key)) {
                    this.addKeyError(entry, 'se.configurationform.required.entry.error');
                    entry.hasErrors = true;
                }
                if (stringUtils.isBlank(entry.value)) {
                    this.addValueError(entry, 'se.configurationform.required.entry.error');
                    entry.hasErrors = true;
                }
            });
        }
        return (
            configurationForm.valid &&
            !this.configuration.reduce(
                (confHolder: any, nextConfiguration: ConfigurationItem) => {
                    if (confHolder.keys.indexOf(nextConfiguration.key) > -1) {
                        this.addKeyError(
                            nextConfiguration,
                            'se.configurationform.duplicate.entry.error'
                        );
                        confHolder.errors = true;
                    } else {
                        confHolder.keys.push(nextConfiguration.key);
                    }
                    return confHolder;
                },
                {
                    keys: [],
                    errors: false
                }
            ).errors
        );
    }

    private validate(entry: ConfigurationItem): string {
        try {
            if (entry.requiresUserCheck && !entry.isCheckedByUser) {
                throw new Error(this.ABSOLUTE_URI_NOT_APPROVED);
            }
            return JSON.stringify(JSON.parse(entry.value));
        } catch (parseError) {
            throw new Errors.ParseError(entry.value);
        }
    }
}
