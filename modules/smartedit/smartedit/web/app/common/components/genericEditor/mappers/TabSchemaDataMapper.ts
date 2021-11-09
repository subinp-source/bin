/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { AbstractFormSchemas, FormGroupSchema } from '@smart/utils';
import { SchemaDataMapper } from './RootSchemaDataMapper';

/**
 * A schema and data mapper for the GenericEditorTabComponent.
 */
export class TabSchemaDataMapper implements SchemaDataMapper {
    constructor(public id: string, private mappers: SchemaDataMapper[]) {}

    toValue(): any {
        return this.mappers.reduce(
            (acc, field) => {
                acc[field.id] = field.toValue();
                return acc;
            },
            {} as any
        );
    }

    toSchema(): FormGroupSchema {
        return {
            type: 'group',
            persist: false,
            schemas: this.mappers.reduce(
                (acc, field) => {
                    acc[field.id] = field.toSchema();
                    return acc;
                },
                {} as AbstractFormSchemas
            )
        };
    }
}
