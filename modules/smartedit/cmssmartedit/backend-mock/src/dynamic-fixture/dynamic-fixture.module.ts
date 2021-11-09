/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { DynamicModule, Module } from '@nestjs/common';
import { ConfigService } from './services/config.service';
import { StorageService } from './services/storage.service';
import { DYNAMIC_FIXTURE_CONFIG } from './constants';
import { StorageController } from './storage.controller';
import { FixtureAdjustmentInterceptor } from './interceptors/fixture-adjustment.interceptor';
import { APP_INTERCEPTOR } from '@nestjs/core';
import { DynamicFixtureOptions } from './interfaces';

@Module({})
export class DynamicFixtureModule {
    static forRoot(config: DynamicFixtureOptions): DynamicModule {
        return {
            module: DynamicFixtureModule,
            providers: [
                {
                    provide: DYNAMIC_FIXTURE_CONFIG,
                    useValue: config
                },
                ConfigService,
                StorageService,
                {
                    provide: APP_INTERCEPTOR,
                    useClass: FixtureAdjustmentInterceptor
                }
            ],
            controllers: [StorageController]
        };
    }
}
