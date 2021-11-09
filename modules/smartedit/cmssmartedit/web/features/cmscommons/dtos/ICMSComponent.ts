/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { CMSItem } from 'cmscommons';

/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @description
 * Interface for cms-component information
 */
export interface ICMSComponent extends CMSItem {
    visible: boolean;
    cloneable: boolean;
    // Contains the uuid of the slots where this component is used.
    slots: string[];
}
