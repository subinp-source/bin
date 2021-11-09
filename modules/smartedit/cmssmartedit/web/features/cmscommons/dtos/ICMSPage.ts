/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { CMSItem, CMSRestriction } from './';

/**
 * @description
 * Interface for cms-page information
 */
export interface ICMSPage extends CMSItem {
    label?: string;
    type?:
        | string
        | {
              [index: string]: string;
          };
    [index: string]: any;
    pageStatus: CMSPageStatus;
    approvalStatus: CmsApprovalStatus;
    displayStatus: string;
    template?: string;
    masterTemplate: string;
    masterTemplateId: string;
    title: {
        [index: string]: string;
    };
    defaultPage: boolean;
    restrictions: CMSRestriction[];
    identifier?: string;
    homepage: boolean;
    isPrimary?: boolean;
    primaryPage?: string | ICMSPage;
}

export enum CMSPageTypes {
    ContentPage = 'ContentPage',
    CategoryPage = 'CategoryPage',
    ProductPage = 'ProductPage',
    EmailPage = 'EmailPage'
}

export enum CMSPageStatus {
    ACTIVE = 'ACTIVE',
    DELETED = 'DELETED'
}

export enum CmsApprovalStatus {
    APPROVED = 'APPROVED',
    CHECK = 'CHECK',
    UNAPPROVED = 'UNAPPROVED'
}
