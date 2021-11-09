/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IFundamentalModalConfig, IModalService, SeDowngradeService } from 'smarteditcommons';
import { ConfigurationModalComponent } from '../components/generalConfiguration/ConfigurationModalComponent';

@SeDowngradeService()
export class ConfigurationModalService {
    constructor(private modalService: IModalService) {}
    /*
     *The edit configuration method opens the modal for the configurations.
     */

    public editConfiguration(): void {
        this.modalService.open({
            templateConfig: {
                title: 'se.modal.administration.configuration.edit.title'
            },
            component: ConfigurationModalComponent,
            config: {
                focusTrapped: false,
                backdropClickCloseable: false
            }
        } as IFundamentalModalConfig<any>);
    }
}
