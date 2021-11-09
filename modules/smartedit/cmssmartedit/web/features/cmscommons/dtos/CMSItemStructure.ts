/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @description
 * Interface to describe a CMS Item field.
 */
export interface CMSItemStructureField {
    cmsStructureType: string;
    i18nKey: string;
    qualifier: string;
    editable: boolean;
    collection?: boolean;
    localized?: boolean;
    paged?: boolean;
    required?: boolean;
}

/**
 * @description
 * Interface to describe the structure of a CMS Item.
 */
export interface CMSItemStructure {
    attributes: CMSItemStructureField[];
    category: string;
    code: string;
    i18nKey: string;
    name: string;
    type: string;
}
