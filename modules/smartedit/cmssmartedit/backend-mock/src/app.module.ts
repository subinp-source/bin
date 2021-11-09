/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Module } from '@nestjs/common';
import { DynamicFixtureModule } from './dynamic-fixture/dynamic-fixture.module';

import { AppController } from './app.controller';

import {
    InboxController,
    LanguagesController,
    MediaController,
    MiscellaneousController,
    PagesAndNavigationsController,
    PagesController,
    PermissionsController,
    ProductsController,
    RestrictionsController,
    TypesController,
    UsersController,
    UserGroupsController,
    VersionsController
} from './controllers';

import { MediaService, VersionsService, WorkflowsService } from './services';

@Module({
    imports: [DynamicFixtureModule.forRoot({ baseURL: '$$$' })],
    controllers: [
        AppController,
        PagesAndNavigationsController,
        TypesController,
        LanguagesController,
        PagesController,
        MiscellaneousController,
        RestrictionsController,
        MediaController,
        VersionsController,
        UserGroupsController,
        UsersController,
        ProductsController,
        InboxController,
        PermissionsController
    ],
    providers: [WorkflowsService, MediaService, VersionsService]
})
export class AppModule {}
