/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/** @internal */
export interface DeviceSupport {
    type: string;
    width: number | string;
    height?: number | string;
    iconClass?: string;
    icon?: string;
    default?: boolean;
    selectedIcon?: string;
    blueIcon?: string;
}

/** @internal */
export const DEVICE_SUPPORTS: DeviceSupport[] = [
    {
        iconClass: 'sap-icon--iphone',
        type: 'phone',
        width: 480
    },
    {
        iconClass: 'sap-icon--iphone-2',
        type: 'wide-phone',
        width: 600
    },
    {
        iconClass: 'sap-icon--ipad',
        type: 'tablet',
        width: 700
    },
    {
        iconClass: 'sap-icon--ipad-2',
        type: 'wide-tablet',
        width: 1024
    },
    {
        iconClass: 'sap-icon--sys-monitor',
        type: 'desktop',
        width: 1200
    },
    {
        iconClass: 'hyicon hyicon-wide-screen',
        type: 'wide-desktop',
        width: '100%',
        default: true
    }
];
