/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * This class can be used as a mock for a variet of event based smartedit services
 * $rootscope - $on events
 * CrossFrameEventService
 * MessageGateway
 */
export class GenericEventer {
    eventsCbMap: { [index: string]: (payload?: any) => any } = {};

    public $on = (eventName: string, cb: (payload?: any) => any) => {
        this.eventsCbMap[eventName] = cb;
    };

    public subscribe = (eventName: string, cb: (payload?: any) => any) => {
        this.eventsCbMap[eventName] = cb;
    };

    public publish = (eventName: string, payload?: any) => {
        this.eventsCbMap[eventName](payload);
    };
}
