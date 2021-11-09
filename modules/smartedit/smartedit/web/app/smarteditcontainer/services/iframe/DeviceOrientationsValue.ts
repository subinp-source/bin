/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/** @internal */
export interface DeviceOrientation {
    orientation: 'vertical' | 'horizontal';
    key: string;
    default?: boolean;
}

/** @internal */
export const DEVICE_ORIENTATIONS: DeviceOrientation[] = [
    {
        orientation: 'vertical',
        key: 'se.deviceorientation.vertical.label',
        default: true
    },
    {
        orientation: 'horizontal',
        key: 'se.deviceorientation.horizontal.label'
    }
];
