/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * @ngdoc interface
 * @name cmsSmarteditServicesModule.interfaces:WorkflowActionComment
 * @description
 * Interface used by {@link cmsSmarteditServicesModule.service:WorkflowService WorkflowService} to query
 * workflow action comments.
 */
export interface WorkflowActionComment {
    code: string;
    text: string;
    creationtime: string;
    authorName: string;
    decisionName?: string;
    decisionCode?: string;
    originalActionCode?: string;
    createdAgoInMillis?: number;
}
