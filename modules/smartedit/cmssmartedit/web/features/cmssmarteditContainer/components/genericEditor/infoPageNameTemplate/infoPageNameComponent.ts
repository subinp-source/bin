/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import {
    GenericEditorField,
    ICatalogService,
    IGenericEditor,
    ISeComponent,
    IUriContext,
    SeComponent,
    TypedMap
} from 'smarteditcommons';
import { ICMSPage } from 'cmscommons';

/**
 * @ngdoc directive
 * @name genericEditorWidgetsModule.directive:PageInfoPageNameComponent
 * @scope
 * @restrict E
 * @element page-info-page-name
 *
 * @description
 * Component responsible for rendering the page name inside the page info menu and a home icon beside the name
 * if the current page is a home page (current or old)
 */
@SeComponent({
    templateUrl: 'infoPageNameTemplate.html',
    inputs: ['field', 'qualifier', 'model', 'editor', 'disabled']
})
export class InfoPageNameComponent implements ISeComponent {
    public disabled: boolean;
    public field: GenericEditorField;
    public qualifier: string;
    public model: TypedMap<any>;
    public editor: IGenericEditor;
    public uriContext: IUriContext;
    public cmsPage: ICMSPage;

    constructor(private catalogService: ICatalogService, private pageService: any) {}

    $onInit() {
        Promise.all([
            this.catalogService.retrieveUriContext(),
            this.pageService.getCurrentPageInfo()
        ]).then(([uriContext, cmsPage]: [IUriContext, ICMSPage]) => {
            this.uriContext = uriContext;
            this.cmsPage = cmsPage;
        });
    }
}
