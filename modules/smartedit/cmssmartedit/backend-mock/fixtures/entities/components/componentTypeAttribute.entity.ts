/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
export interface IComponentTypeAttribute {
    cmsStructureType: string;
    cmsStructureEnumType?: string;
    collection?: boolean;
    editable?: boolean;
    i18nKey: string;
    localized?: boolean;
    paged?: boolean;
    params?: { typeCode: string };
    qualifier: string;
    required?: boolean;
    containedTypes?: string[];
    dependsOn?: string;
    subTypes?: {
        BannerComponent?: string;
        ResponsiveBannerComponent?: string;
        CmsTab?: string;
        CMSLinkComponent?: string;
    };
    postfix?: string;
    uri?: string;
}
