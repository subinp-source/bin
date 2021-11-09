/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Inject, Injectable } from '@nestjs/common';
import { DYNAMIC_FIXTURE_CONFIG } from '../constants';
import { DynamicFixtureOptions } from '../interfaces';

@Injectable()
export class ConfigService {
    private readonly baseURL: string;

    constructor(@Inject(DYNAMIC_FIXTURE_CONFIG) config: DynamicFixtureOptions) {
        this.baseURL = config.baseURL;
    }

    getBaseURL(): string {
        return this.baseURL;
    }
}
