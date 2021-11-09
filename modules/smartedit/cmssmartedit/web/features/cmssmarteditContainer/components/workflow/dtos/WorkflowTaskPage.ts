/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * Interface used by WorkflowTask as a page related attachment.
 */
export interface WorkflowTaskPage {
    pageName: string;
    pageUid: string;
    catalogId: string;
    catalogName: string;
    catalogVersion: string;
}
