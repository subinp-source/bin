/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { FunctionsModule, GenericEditorModule, SeModule } from 'smarteditcommons';
import { CmsGenericEditorConfigurationService } from './CmsGenericEditorConfigurationService';

@SeModule({
    imports: [GenericEditorModule, FunctionsModule],
    providers: [CmsGenericEditorConfigurationService]
})
export class CmsGenericEditorConfigurationServiceModule {}
