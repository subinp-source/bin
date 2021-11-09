/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Payload } from '@smart/utils';

export interface IDragAndDropEvents {
    TRACK_MOUSE_POSITION: string;
    DROP_ELEMENT: string;
    DRAG_DROP_START: string;
    DRAG_DROP_END: string;
    DRAG_DROP_CROSS_ORIGIN_START: string;
}
/**
 * @ngdoc object
 * @name smarteditServicesModule.object:IMousePosition
 * @description
 * Pointer coordinates
 */
export interface IMousePosition extends Payload {
    /**
     * @ngdoc property
     * @name x
     * @propertyOf smarteditServicesModule.object:IMousePosition
     * @description
     * abscissa of the pointer position
     */
    x: number;
    /**
     * @ngdoc property
     * @name y
     * @propertyOf smarteditServicesModule.object:IMousePosition
     * @description
     * ordinate of the pointer position
     */
    y: number;
}

export type IDragEventType = 'drop' | 'dragenter' | 'dragover' | 'dragleave';
export const IDragEventType = {
    DROP: 'drop' as IDragEventType,
    DRAG_ENTER: 'dragenter' as IDragEventType,
    DRAG_OVER: 'dragover' as IDragEventType,
    DRAG_LEAVE: 'dragleave' as IDragEventType
};

export * from './InViewElementObserver';
export * from './DragAndDropServiceModule';
export * from './DragAndDropScrollingService';
export * from './DragAndDropService';
