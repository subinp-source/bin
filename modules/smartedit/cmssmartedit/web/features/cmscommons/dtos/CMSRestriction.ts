/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @description
 * Interface for cms-restriction information
 */
export interface CMSRestriction {
    name: string;
    type: {
        [language: string]: string;
    };
    typeCode: string;
    description: string;
}
