/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Controller, Get } from '@nestjs/common';

@Controller()
export class InboxController {
    @Get('cmssmarteditwebservices/v1/inbox/workflowtasks*')
    getWorkflowTasks() {
        return {
            pagination: {
                count: 0,
                page: 0,
                totalCount: 0,
                totalPages: 0
            },
            tasks: []
        };
    }
}
