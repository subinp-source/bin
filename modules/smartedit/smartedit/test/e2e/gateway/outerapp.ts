/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import {
    GatewayFactory,
    MessageGateway,
    SeDowngradeComponent,
    SeEntryModule,
    WindowUtils
} from 'smarteditcommons';
import { FormsModule } from '@angular/forms';

// tslint:disable:max-classes-per-file

@SeDowngradeComponent()
@Component({
    selector: 'outer-app-root',
    template: `
        <div
            style="position:absolute; width: 100vh; height: 100px; z-index: 1000; top: 300px; left: 0"
        >
            <label>Smart edit container</label> <br />
            <input type="text" id="field1" [(ngModel)]="myField" />
            <button id="sendMessage1" class="btg btg-default" (click)="notifyIframeOnGateway1()">
                send message on gateway 1
            </button>
            <button id="sendMessage2" class="btg btg-default" (click)="notifyIframeOnGateway2()">
                send message on gateway 2
            </button>
            <label id="acknowledged">{{ acknowledged }}</label> <br />
            <label>message:</label> <label id="message">{{ message }}</label>
            <hr />
        </div>
    `
})
export class OuterAppRootComponent {
    public acknowledged: string;
    public message: string;
    public myField: string;

    private gateway1: MessageGateway;
    private gateway2: MessageGateway;

    constructor(private windowUtils: WindowUtils, gatewayFactory: GatewayFactory) {
        this.gateway1 = gatewayFactory.createGateway('Gateway1');
        this.gateway2 = gatewayFactory.createGateway('Gateway2');
    }

    ngOnInit() {
        this.windowUtils.setTrustedIframeDomain(document.location.href);

        this.gateway1.subscribe('display2', (eventId, data) => {
            this.message = (data as any).message;
            this.windowUtils.runTimeoutOutsideAngular(() => {
                this.message = null;
            }, 3000);

            return Promise.resolve('hello to you iframe');
        });
    }

    notifyIframeOnGateway1() {
        this.notifyIframe(this.gateway1);
    }

    notifyIframeOnGateway2() {
        this.notifyIframe(this.gateway2);
    }

    notifyIframe(gateway: MessageGateway) {
        gateway
            .publish('display1', {
                message: 'hello Iframe ! (from parent)'
            })
            .then(
                (returnValue) => {
                    this.acknowledged =
                        '(iframe acknowledged my message and sent back:' + returnValue + ')';
                    this.windowUtils.runTimeoutOutsideAngular(() => {
                        this.acknowledged = null;
                    }, 3000);
                },
                () => {
                    this.acknowledged = '(iframe did not acknowledge my message)';
                    this.windowUtils.runTimeoutOutsideAngular(() => {
                        this.acknowledged = null;
                    }, 3000);
                }
            );
    }
}
@SeEntryModule('Outerapp')
@NgModule({
    imports: [CommonModule, FormsModule],
    declarations: [OuterAppRootComponent],
    entryComponents: [OuterAppRootComponent]
})
export class Outerapp {}
