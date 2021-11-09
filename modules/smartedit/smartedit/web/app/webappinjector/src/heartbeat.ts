/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
export abstract class Heartbeat {
    static startSendingHeartBeatToIframe(webappScript: HTMLScriptElement) {
        const HEART_BEAT_PERIOD = Heartbeat.getHeartBeatInterval(webappScript);
        setInterval(() => {
            const HEART_BEAT_GATEWAY_ID = 'heartBeatGateway';
            const HEART_BEAT_MSG_ID = 'heartBeat';
            parent.postMessage(
                {
                    pk: Math.random(),
                    gatewayId: HEART_BEAT_GATEWAY_ID,
                    eventId: HEART_BEAT_MSG_ID,
                    data: {
                        location: document.location.href
                    }
                },
                '*'
            );
        }, HEART_BEAT_PERIOD);
    }

    private static readonly DEFAULT_HEARTBEAT_INTERVAL = '500';

    private static getHeartBeatInterval(webappScript: HTMLScriptElement) {
        return parseInt(
            webappScript.getAttribute('data-smartedit-heart-beat-interval') ||
                Heartbeat.DEFAULT_HEARTBEAT_INTERVAL,
            10
        );
    }
}
