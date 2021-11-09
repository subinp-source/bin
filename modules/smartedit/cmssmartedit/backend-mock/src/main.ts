/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { initializeSwagger } from './swagger';
import { INestApplication } from '@nestjs/common';

const contracts_basepath = `${process.cwd()}/fixtures/contracts`;
const fs = require('fs');
const path = require('path');
const util = require('util');
const readdir = util.promisify(fs.readdir);

async function bootstrap() {
    const app = await NestFactory.create(AppModule);
    app.enableCors();

    const executeContractTesting = false;
    if (executeContractTesting) {
        await loadContractsAndInitSwagger(app);
    }

    const port = process.env.port || 3333;
    await app.listen(port, () => {
        console.log('Listening at http://localhost:' + port);
    });
}

async function getContractsList() {
    const contracts = await readdir(contracts_basepath);
    return contracts
        .filter((file: string) => file.endsWith('.yaml'))
        .map((file: string) => path.join(contracts_basepath, file));
}

async function loadContractsAndInitSwagger(app: INestApplication) {
    const contracts = await getContractsList();

    for await (const contract of contracts) {
        console.log(`Loading Swagger contract: ${contract}...`);
        const yml = require('js-yaml').safeLoad(fs.readFileSync(contract), 'utf8');
        await initializeSwagger(app, yml);
    }
}

bootstrap();
