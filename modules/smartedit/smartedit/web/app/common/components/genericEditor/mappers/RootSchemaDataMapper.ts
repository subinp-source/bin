/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { AbstractFormSchemas, FormGroupSchema, FormSchema } from '@smart/utils';
import { GenericEditorFieldsMap, GenericEditorTab } from '../types';
import { GenericEditorRootTabsComponent } from '../components/rootTabs/GenericEditorRootTabsComponent';

export interface SchemaDataMapper {
    id: string;
    toValue(): any;
    toSchema(): FormSchema;
}

/**
 * A schema and data mapper for the GenericEditorRootTabsComponent.
 */
export class RootSchemaDataMapper {
    constructor(
        private mappers: SchemaDataMapper[],
        private tabs: GenericEditorTab[],
        private fieldsMap: GenericEditorFieldsMap
    ) {}

    toValue(): any {
        return this.mappers.reduce(
            (acc, mapper) => {
                acc[mapper.id] = mapper.toValue();
                return acc;
            },
            {} as any
        );
    }

    toSchema(): FormGroupSchema {
        return {
            type: 'group',
            component: 'tabs',
            schemas: this.mappers.reduce(
                (acc, mapper) => {
                    acc[mapper.id] = mapper.toSchema();
                    return acc;
                },
                {} as AbstractFormSchemas
            ),
            inputs: {
                tabs: this.tabs,
                fieldsMap: this.fieldsMap
            } as Partial<GenericEditorRootTabsComponent>
        };
    }
}
