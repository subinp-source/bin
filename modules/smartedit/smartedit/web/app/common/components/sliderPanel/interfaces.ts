/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { DynamicScope } from '../../directives';

export interface SliderPanelModalConfiguration {
    showDismissButton: boolean;
    title: string;
    save: SliderPanelAction;
    dismiss: SliderPanelAction;
}

export interface SliderPanelConfiguration {
    modal?: SliderPanelModalConfiguration;
    /**
     *  CSS pattern used to select the element covered by the slider panel.
     */
    cssSelector?: string;
    /**
     *   Used to indicate if a greyed-out overlay is to be displayed or not.
     */
    noGreyedOutOverlay?: boolean;
    /**
     *    Indicates the dimension of the container slider panel once it is displayed.
     */
    overlayDimension?: string;
    /**
     *   Specifies from which side of its container the slider panel slides out.
     */
    slideFrom?: string;
    /**
     *    Specifies whether the slider panel is to be shown by default.
     */
    displayedByDefault?: boolean;
    /**
     *     Indicates the z-index value to be applied on the slider panel.
     */
    zIndex?: string;
    /**
     *  A template used to render AngularJS Slider Panel body
     *
     * @deprecated Deprecated since 2005, provide as a content element of <se-slider-panel> component.
     */
    template?: string;
    /**
     * A template URL used to render AngularJS Slider Panel body
     *
     * @deprecated Deprecated since 2005, provide as a content element of <se-slider-panel> component.
     */
    templateUrl?: string;
    /**
     * template or templateUrl data to be consumed by the template
     *
     * @deprecated Deprecated since 2005, provide as a content element of <se-slider-panel> component.
     */
    scope?: DynamicScope;
}

export interface SliderPanelAction {
    onClick: () => void;
    label: string;
    isDisabledFn: () => boolean;
}
