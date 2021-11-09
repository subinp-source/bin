/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import {
    GatewayFactory,
    MessageGateway,
    SeCustomComponent,
    SeEntryModule,
    WindowUtils
} from 'smarteditcommons';

// tslint:disable:max-classes-per-file

@SeCustomComponent()
@Component({
    selector: 'inner-app-root',
    template: `
        <label>Smart edit (this is an iFrame !)</label> <br />
        <input type="text" id="field2" [(ngModel)]="myField" />
        <button id="sendMessage" class="btg btg-default" (click)="notifyParent()">
            send message
        </button>
        <label id="acknowledged">{{ acknowledged }}</label> <br />
        <label
            >(first listener; gateway 1) (set to succeed :
            <input type="checkbox" id="check1" [(ngModel)]="listener1WillSucceed" />) message:
        </label>
        <label id="message1">{{ message }}</label> <br />
        <label
            >(second listener; gateway 1) (set to succeed :
            <input type="checkbox" id="check2" [(ngModel)]="listener2WillSucceed" />) message:
        </label>
        <label id="message2">{{ message2 }}</label> <br />
        <label
            >(third listener; gateway 2) (set to succeed :
            <input type="checkbox" id="check3" [(ngModel)]="listener3WillSucceed" />) message:
        </label>
        <label id="message3">{{ message3 }}</label>
    `
})
export class InnerAppRootComponent {
    public acknowledged: string;
    public message: string;
    public message1: string;
    public message2: string;
    public message3: string;

    public listener3WillSucceed: boolean;
    public listener2WillSucceed: boolean;
    public listener1WillSucceed: boolean;

    public myField: string;

    private gateway1: MessageGateway;
    private gateway2: MessageGateway;

    constructor(private windowUtils: WindowUtils, gatewayFactory: GatewayFactory) {
        this.gateway1 = gatewayFactory.createGateway('Gateway1');
        this.gateway2 = gatewayFactory.createGateway('Gateway2');
    }

    notifyParent() {
        this.acknowledged = '';
        this.gateway1
            .publish('display2', {
                message: 'hello parent ! (from iframe)'
            })
            .then(
                (returnValue) => {
                    this.acknowledged =
                        '(parent acknowledged my message and sent back:' + returnValue + ')';
                    this.windowUtils.runTimeoutOutsideAngular(() => {
                        delete this.acknowledged;
                    }, 2000);
                },
                () => {
                    this.acknowledged = '(parent did not acknowledge my message)';
                    this.windowUtils.runTimeoutOutsideAngular(() => {
                        delete this.acknowledged;
                    }, 2000);
                }
            );
    }

    ngOnInit() {
        if (this.gateway1 && this.gateway2) {
            this.gateway1.subscribe('display1', (eventId, data) => {
                if (this.listener1WillSucceed) {
                    this.message = (data as any).message;
                    this.windowUtils.runTimeoutOutsideAngular(() => {
                        delete this.message;
                    }, 2000);

                    return Promise.resolve('hello to you parent from first listener on gateway1');
                } else {
                    this.message = 'failure';
                    this.windowUtils.runTimeoutOutsideAngular(() => {
                        delete this.message;
                    }, 3000);

                    return Promise.reject();
                }
            });

            // second listener
            this.gateway1.subscribe('display1', (eventId, data) => {
                if (this.listener2WillSucceed) {
                    this.message2 = (data as any).message;
                    this.windowUtils.runTimeoutOutsideAngular(() => {
                        delete this.message2;
                    }, 2000);

                    return Promise.resolve('hello to you parent from second listener on gateway1');
                } else {
                    this.message2 = 'failure';
                    this.windowUtils.runTimeoutOutsideAngular(() => {
                        delete this.message2;
                    }, 3000);

                    return Promise.reject();
                }
            });

            // third listener, on second gateway
            this.gateway2.subscribe('display1', (eventId, data) => {
                if (this.listener3WillSucceed) {
                    this.message3 = (data as any).message;
                    this.windowUtils.runTimeoutOutsideAngular(() => {
                        delete this.message3;
                    }, 2000);
                    return Promise.resolve('hello to you parent from unique listener on gateway2');
                } else {
                    this.message3 = 'failure';
                    this.windowUtils.runTimeoutOutsideAngular(() => {
                        delete this.message3;
                    }, 3000);

                    return Promise.reject();
                }
            });
        }
    }
}
@SeEntryModule('Innerapp')
@NgModule({
    imports: [CommonModule, FormsModule],
    declarations: [InnerAppRootComponent],
    entryComponents: [InnerAppRootComponent]
})
export class Innerapp {}
