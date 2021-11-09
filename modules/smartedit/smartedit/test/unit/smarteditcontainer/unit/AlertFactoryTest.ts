/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { DomSanitizer } from '@angular/platform-browser';
import { TranslateService } from '@ngx-translate/core';
import {
    AlertConfig,
    AlertRef,
    AlertService as FundamentalAlertService
} from '@fundamental-ngx/core';
import { Alert, ALERT_CONFIG_DEFAULTS, IAlertServiceType, LogService } from 'smarteditcommons';
import { AlertFactory } from 'smarteditcontainer/services/alerts';
import { of } from 'rxjs';

describe('alertFactory', () => {
    let alertFactory: AlertFactory;
    let logService: LogService;
    let domSanitizer: jasmine.SpyObj<DomSanitizer>;
    const MOCK_ALERT_CONFIG_DEFAULTS: AlertConfig = { ...ALERT_CONFIG_DEFAULTS };

    let fundamentalAlertService: jasmine.SpyObj<FundamentalAlertService>;
    const translateService: jasmine.SpyObj<TranslateService> = jasmine.createSpyObj<
        TranslateService
    >('translateService', ['get']);
    translateService.get.and.callFake((content: any) => of(`${content}_translated`));

    beforeEach(() => {
        logService = new LogService();
        domSanitizer = jasmine.createSpyObj<DomSanitizer>('sanitize', ['sanitize']);
        domSanitizer.sanitize.and.callFake((context: any, str: string) => str);

        fundamentalAlertService = jasmine.createSpyObj<FundamentalAlertService>(
            'fundamentalAlertService',
            ['open']
        );
        fundamentalAlertService.open.and.returnValue(new AlertRef());

        alertFactory = new AlertFactory(
            logService,
            domSanitizer,
            fundamentalAlertService,
            translateService,
            MOCK_ALERT_CONFIG_DEFAULTS
        );
    });

    it('Will convert a string param into an AlertConfig with message', async () => {
        const message = 'my alert message';
        const alert = alertFactory.createAlert(message);
        await alert.show();
        expect(alert.alertConf.message).toBe(`${message}_translated`);
    });

    it('Alert "show()" function delegates call to Base Class', async () => {
        const baseAlertSpyShow = spyOn(Alert.prototype, 'show');
        baseAlertSpyShow.and.callThrough();
        const alert = alertFactory.createAlert({
            message: 'Alert message',
            timeout: 3000
        });
        await alert.show();
        expect(baseAlertSpyShow).toHaveBeenCalled();
    });

    it('Alert "hide()" function delegates call to Base Class', async () => {
        const baseAlertSpyHide = spyOn(Alert.prototype, 'hide');
        baseAlertSpyHide.and.callThrough();
        const alert = alertFactory.createAlert({
            message: 'Alert message',
            timeout: 3000
        });
        await alert.show();
        alert.hide();
        expect(baseAlertSpyHide).toHaveBeenCalled();
    });

    it('Alert is of the Template type that was given', () => {
        const alert1 = alertFactory.createAlert({
            message: 'A string message'
        });
        const alert2 = alertFactory.createAlert({
            template: '<h1>This is a sentence.</h1>'
        });
        const alert3 = alertFactory.createAlert({
            templateUrl: 'somehtmlfile.html'
        });

        expect(alert1.message).toBeDefined();
        expect(alert2.alertConf.data.template).toBeDefined();
        expect(alert3.alertConf.data.templateUrl).toBeDefined();
    });

    it('Alert can be only one type of template', () => {
        const error =
            'alertService._validateAlertConfig - only one template type is allowed for the alert: message, template, or templateUrl';

        expect(() => {
            alertFactory.createAlert({
                message: 'A string message.',
                template: '<h1>This is a sentence.</h1>'
            });
        }).toThrowError(error);

        expect(() => {
            alertFactory.createAlert({
                message: 'A string message.',
                templateUrl: 'somehtmlfile.html'
            });
        }).toThrowError(error);

        expect(() => {
            alertFactory.createAlert({
                template: '<h1>This is a sentence.</h1>',
                templateUrl: 'somehtmlfile.html'
            });
        }).toThrowError(error);
    });

    it('Factory properly assigns the Alert Type', () => {
        const info = alertFactory.createInfo({ template: '<h1>a</h1>' });
        const danger = alertFactory.createDanger({ template: '<h1>a</h1>' });
        const warning = alertFactory.createWarning({ template: '<h1>a</h1>' });
        const success = alertFactory.createSuccess({ template: '<h1>a</h1>' });

        expect(info.type).toBe(IAlertServiceType.INFO);
        expect(danger.type).toBe(IAlertServiceType.DANGER);
        expect(warning.type).toBe(IAlertServiceType.WARNING);
        expect(success.type).toBe(IAlertServiceType.SUCCESS);
    });
});
