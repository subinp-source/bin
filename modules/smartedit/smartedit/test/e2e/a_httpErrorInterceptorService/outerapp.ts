/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, Inject, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse, HttpRequest } from '@angular/common/http';

import {
    HttpErrorInterceptorService,
    IAlertService,
    OperationContextService,
    RestServiceFactory,
    RetryInterceptor,
    SeDowngradeComponent,
    SeEntryModule,
    YJQUERY_TOKEN
} from 'smarteditcommons';

// tslint:disable:max-classes-per-file

@SeDowngradeComponent()
@Component({
    selector: 'app-root',
    template: `
        <div
            style="position: fixed; z-index: 1000; top: 150px; left: 0; width: 100vw; height: 100vh; background-color: white"
        >
            <h2 id="customView">API Error handling</h2>
            <button (click)="triggerError404Json()" id="trigger-error-404-json">
                Trigger Error 404 (Content-type: text/json)
            </button>
            <button (click)="triggerError400Json()" id="trigger-error-400-json">
                Trigger Error 400 (Content-type: text/json)
            </button>
            <button (click)="triggerError404Html()" id="trigger-error-404-html">
                Trigger Error 404 (Content-type: text/html)
            </button>
            <button (click)="triggerError501Json()" id="trigger-error-501-json">
                Trigger Error 501 (Content-type: text/json)
            </button>

            <h2>Graceful Degradation</h2>
            <button (click)="triggerError503()" id="trigger-error-503">
                Trigger Error 503 - Service Unavailable (Retry FAIL)
            </button>
            <button (click)="triggerError502()" id="trigger-error-502">
                Trigger Error 502 - Bad Gateway (Retry SUCCESS)
            </button>
            <!-- graceful degradation status -->
            <div id="gd-status">{{ message }}</div>
        </div>
    `
})
export class AppRootComponent {
    public message: string;

    constructor(
        private restServiceFactory: RestServiceFactory,
        private httpErrorInterceptorService: HttpErrorInterceptorService,
        private alertService: IAlertService,
        private operationContextService: OperationContextService,
        private retryInterceptor: RetryInterceptor,
        @Inject(YJQUERY_TOKEN) private yjQuery: JQueryStatic
    ) {}

    ngOnInit() {
        this.httpErrorInterceptorService.addInterceptor({
            predicate: (request, response) => {
                return response.status === 501;
            },
            responseError: (request, response) => {
                this.alertService.showDanger({
                    message: response.error.errors[0].message
                });

                return Promise.reject(response);
            }
        });

        this.setupCustomGracefulDegradation();
    }
    triggerError404Json() {
        this.restServiceFactory.get('/error404_json').get();
    }
    triggerError400Json() {
        this.restServiceFactory.get('/error400_json').get();
    }
    triggerError404Html() {
        this.restServiceFactory.get('/error404_html').get();
    }
    triggerError501Json() {
        this.restServiceFactory.get('/error501_json').get();
    }
    triggerError503() {
        this.restServiceFactory.get('/error503/a123/v1/foobar/').get();
    }
    triggerError502() {
        this.restServiceFactory
            .get('/error502/retry')
            .get()
            .then(() => {
                this.message = 'PASSED';
            });
    }

    /**
     * Graceful degradation - Retry Interceptor
     *
     * Register a custom retry strategy with a custom operation context.
     * The e2e test assert that the user see a message when the custom retry strategy reached it's maximum number of retry.
     */
    setupCustomGracefulDegradation() {
        this.operationContextService.register(
            '/error503/:var1/v1/:var2',
            'CUSTOM_OPERATION_CONTEXT'
        );
        const yJQuery = this.yjQuery;

        const predicate503 = (
            request: HttpRequest<any>,
            response: HttpErrorResponse,
            operationContext: string
        ) => {
            return response.status === 503 && operationContext === 'CUSTOM_OPERATION_CONTEXT';
        };

        const predicate502 = (
            request: HttpRequest<any>,
            response: HttpErrorResponse,
            operationContext: string
        ) => {
            return response.status === 502;
        };

        class StrategyFactory {
            firstFastRetry: boolean;
            attemptCount: number;

            canRetry() {
                if (this.attemptCount > 1) {
                    yJQuery('#gd-status').html('FAILED');
                }
                return this.attemptCount <= 1;
            }

            calculateNextDelay() {
                return 0;
            }
        }

        this.retryInterceptor.register(predicate503, StrategyFactory);

        // Strategy Factory for errors 502

        class StrategyFactory502 {
            firstFastRetry: boolean;
            attemptCount: number;

            canRetry() {
                return this.attemptCount <= 1;
            }

            calculateNextDelay() {
                return 0;
            }
        }

        this.retryInterceptor.register(predicate502, StrategyFactory502);
    }
}

@SeEntryModule('Outerapp')
@NgModule({
    imports: [CommonModule],
    declarations: [AppRootComponent],
    entryComponents: [AppRootComponent]
})
export class Outerapp {}

window.pushModules(Outerapp);
