/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IWorkflowTemplate } from '../../entities/workflows/workflowTemplate.entity';

export const workflowTemplates: IWorkflowTemplate[] = [
    {
        code: 'PageApproval',
        name: 'Page Approval'
    },
    {
        code: 'PageTranslation',
        name: 'Page Translation and Approval'
    }
];
