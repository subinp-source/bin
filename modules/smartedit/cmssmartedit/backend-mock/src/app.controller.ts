/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Controller, Get } from '@nestjs/common';

@Controller()
export class AppController {
    @Get('/')
    ok() {
        return 'HEALTH:OK';
    }
}
