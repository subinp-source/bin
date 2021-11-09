/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Cloneable, CloneableEventHandler } from '@smart/utils';
import { SeDowngradeService } from '../di';
import { IGatewayPostMessageData, MessageGateway } from './gateway';
import { GatewayFactory } from './gateway/GatewayFactory';

@SeDowngradeService()
export class SmarteditBootstrapGateway {
    private instance: MessageGateway;

    constructor(gatewayFactory: GatewayFactory) {
        this.instance = this.instance || gatewayFactory.createGateway('smartEditBootstrap');
    }

    getInstance(): MessageGateway {
        return this.instance;
    }

    subscribe<T extends Cloneable>(
        eventId: string,
        callback: CloneableEventHandler<T>
    ): () => void {
        return this.getInstance().subscribe(eventId, callback);
    }

    publish<Tin extends Cloneable, Tout extends Cloneable>(
        eventId: string,
        _data: Tin,
        retries: number = 0,
        pk?: string
    ): Promise<void | Tout> {
        return this.getInstance().publish(eventId, _data, retries, pk);
    }

    processEvent(event: IGatewayPostMessageData): Promise<any> {
        return this.getInstance().processEvent(event);
    }
}
