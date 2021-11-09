/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IComponentTypeAttribute } from './componentTypeAttribute.entity';
import { IcmsLinkToComponentAttribute } from './cmslinkToComponentAttribute.entity';

export interface IComponentType {
    attributes: (IcmsLinkToComponentAttribute | IComponentTypeAttribute)[];
    category: string;
    code: string;
    i18nKey?: string;
    name: string;
    type?: string;
}
