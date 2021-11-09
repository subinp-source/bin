/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Controller, Get, Param } from '@nestjs/common';

@Controller()
export class UsersController {
    @Get('cmswebservices/v1/users/:userId')
    getUserById(@Param('userId') uid: string) {
        return {
            uid,
            readableLanguages: ['en', 'it', 'fr', 'pl', 'hi', 'de'],
            writeableLanguages: ['en', 'it', 'fr', 'pl', 'hi', 'de']
        };
    }
}
