/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Body, Controller, Delete, Param, Post, UseGuards } from '@nestjs/common';
import { StorageService } from './services/storage.service';
import { BaseURLGuard } from './guards/baseURL.gruard';
import { AdjustmentPayload } from './interfaces/index';

/** Handle information stored in StorageService */
@Controller()
@UseGuards(BaseURLGuard)
export class StorageController {
    constructor(private readonly storageService: StorageService) {}

    @Post('*/modify')
    storeModificationFixture(@Body() body: AdjustmentPayload) {
        return this.storageService.storeModificationFixture(body);
    }

    @Post('*/replace')
    storeReplacementFixture(@Body() body: AdjustmentPayload) {
        return this.storageService.storeReplacementFixture(body);
    }

    @Delete('*/fixture/:fixtureID')
    removeFixture(@Param('fixtureID') fixtureID: string) {
        this.storageService.removeFixture(fixtureID);
    }

    @Delete('*/fixture')
    removeAllFixtures() {
        this.storageService.removeAllFixtures();
    }
}
