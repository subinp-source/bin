/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { INestApplication } from '@nestjs/common';
import { initializeMiddleware } from 'swagger-tools';

/**
 * Initializes a server middleware with Swagger for contract testing.
 *
 * @param app An INestApplication server application.
 * @param config The swagger configuration object.
 */
export const initializeSwagger = async (app: INestApplication, config: any) => {
    return new Promise((resolve) => {
        initializeMiddleware(config, ({ swaggerMetadata, swaggerValidator }) => {
            app.use(swaggerMetadata());
            app.use(
                swaggerValidator({
                    validateResponse: true
                })
            );
            resolve({ success: true });
        });
    });
};
