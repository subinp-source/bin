/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import * as angular from 'angular';
import { Injector } from '@angular/core';
import { UpgradeModule } from '@angular/upgrade/static';
import { LegacyDecoratorToCustomElementConverter } from 'smartedit/services/sakExecutor/LegacyDecoratorToCustomElementConverter';
import { promiseUtils, stringUtils, NodeUtils, TypedMap } from 'smarteditcommons';

/*
 * Semi integration test:
 * pure black box testing of real custom elements
 * but injected with mocks of AngularJS $compile and $rootscope
 */
describe('LegacyDecoratorToCustomElementConverter test', () => {
    let $compile: jasmine.Spy;
    let $rootScope: jasmine.SpyObj<angular.IRootScopeService>;
    let upgrade: jasmine.SpyObj<UpgradeModule>;
    let injector: jasmine.SpyObj<Injector>;
    let nodeUtils: NodeUtils;
    let converter: LegacyDecoratorToCustomElementConverter;

    let newScope1: jasmine.SpyObj<angular.IScope>;
    let newScope2: jasmine.SpyObj<angular.IScope>;
    let originalCmsComponent: HTMLElement;
    const jQuery = window.smarteditJQuery;

    beforeEach(() => {
        upgrade = jasmine.createSpyObj<UpgradeModule>('UpgradeModule', ['bootstrap']);
        injector = jasmine.createSpyObj<Injector>('Injector', ['get']);
        (upgrade as any).injector = injector;

        newScope1 = jasmine.createSpyObj<angular.IScope>('newScope1', ['$destroy']);
        newScope2 = jasmine.createSpyObj<angular.IScope>('newScope2', ['$destroy']);
        $rootScope = jasmine.createSpyObj<angular.IRootScopeService>('$rootScope', ['$new']);
        $rootScope.$new.and.returnValues(newScope2, newScope1);

        $compile = jasmine.createSpy('$compile');
        $compile.and.callFake((element: HTMLElement) => {
            if (element.tagName === 'DECO-1' || element.tagName === 'DECO-2') {
                return ($scope: angular.IScope) => {
                    if ($scope === newScope2 && element.tagName === 'DECO-2') {
                        element.append(document.createElement('someTemplateContent2'));
                        return [element];
                    } else if ($scope === newScope1 && element.tagName === 'DECO-1') {
                        element.append(document.createElement('someTemplateContent1'));
                        return [element];
                    } else {
                        throw new Error(`unexpected $scope passed to $compile: ${$scope}`);
                    }
                };
            } else {
                throw new Error(`unexpected node passed to $compile: ${element.tagName}`);
            }
        });

        const mocksMap = {
            $compile,
            $rootScope
        } as TypedMap<any>;

        injector.get.and.callFake((name: string) => {
            return mocksMap[name];
        });

        nodeUtils = new NodeUtils();

        converter = new LegacyDecoratorToCustomElementConverter(upgrade, nodeUtils);

        // START custom elements declarations
        converter.convert('deco1');
        converter.convert('deco2');
        // custom element that "projects" content into a <content> tag
        customElements.define(
            'other-custom-element',
            class extends HTMLElement {
                private content: HTMLElement;

                connectedCallback(): void {
                    if (this.isConnected) {
                        this.content = document.createElement('content');
                        Array.prototype.slice
                            .call(this.childNodes)
                            .forEach((childNode: ChildNode) => {
                                this.removeChild(childNode);
                                this.content.append(childNode);
                            });
                        this.append(this.content);
                    }
                }

                disconnectedCallback(): void {
                    if (!this.isConnected && this.content) {
                        Array.prototype.slice
                            .call(this.content.childNodes)
                            .forEach((childNode: ChildNode) => {
                                this.content.removeChild(childNode);
                                this.append(childNode);
                            });
                        this.removeChild(this.content);
                        this.content.remove();
                        this.content = null;
                    }
                }
            }
        );

        // END
        originalCmsComponent = jQuery(`
        <div class='smartEditComponent'
            data-smartedit-element-uuid='thesmarteditElementUuid'
            data-smartedit-component-id='thesmarteditComponentId'
            data-smartedit-component-type='thesmarteditComponentType'
            data-smartedit-custom1='thecustom1'
            data-smartedit-custom2='thecustom2'
            >
        </div>`)[0];

        document.body.append(originalCmsComponent);
    });

    afterEach(() => {
        document.body.removeChild(originalCmsComponent);
        document.body.removeChild(document.querySelector('deco-2'));
    });

    it(`both decorators are compiled sequentially 
		and do not interfere with another custom element
		in between them and not managed by LegacyDecoratorToCustomElementConverter
		and their scope is passed the map of default and extended attributes`, async () => {
        const node = jQuery(`
        <deco-2 class='deco2 se-decorator-wrap'
            active='false'
            data-smartedit-element-uuid='thesmarteditElementUuid'
            data-smartedit-component-id='thesmarteditComponentId'
            data-smartedit-component-type='thesmarteditComponentType'
            component-attributes='componentAttributes'>
                <other-custom-element>
                    <deco-1 class='deco1 se-decorator-wrap'
                        active='false'
                        data-smartedit-element-uuid='thesmarteditElementUuid'
                        data-smartedit-component-id='thesmarteditComponentId'
                        data-smartedit-component-type='thesmarteditComponentType'
                        component-attributes='componentAttributes'>
                        <div id="someProjectedContent">someProjectedContent</div>
                    </deco-1>
                </other-custom-element>
        </deco-2>`)[0];

        document.body.append(node);

        const outerHTML = await promiseUtils.resolveToCallbackWhenCondition(
            () =>
                !!document.querySelector('sometemplatecontent2') &&
                !!document.querySelector('sometemplatecontent1') &&
                !!document.querySelector('#someProjectedContent'),
            () => document.querySelector('deco-2').outerHTML
        );

        expect(stringUtils.formatHTML(outerHTML)).toEqual(
            stringUtils.formatHTML(
                `<deco-2 class="deco2 se-decorator-wrap"
                active="active"
                data-smartedit-element-uuid="thesmarteditElementUuid"
                data-smartedit-component-id="thesmarteditComponentId"
                data-smartedit-component-type="thesmarteditComponentType"
				component-attributes="componentAttributes"
				processed="true">
                <other-custom-element>
                    <content>
                        <deco-1 class="deco1 se-decorator-wrap"
                            active="active"
                            data-smartedit-element-uuid="thesmarteditElementUuid"
                            data-smartedit-component-id="thesmarteditComponentId"
                            data-smartedit-component-type="thesmarteditComponentType"
							component-attributes="componentAttributes"
							processed="true">
                            <div id="someProjectedContent">someProjectedContent</div>
                            <sometemplatecontent1></sometemplatecontent1>
                        </deco-1>
                    </content>
                </other-custom-element>
                <sometemplatecontent2></sometemplatecontent2>
			</deco-2>`
            )
        );

        expect((newScope1 as any).componentAttributes).toEqual({
            smarteditElementUuid: 'thesmarteditElementUuid',
            smarteditComponentId: 'thesmarteditComponentId',
            smarteditComponentType: 'thesmarteditComponentType',
            smarteditCustom1: 'thecustom1',
            smarteditCustom2: 'thecustom2'
        } as any);

        expect((newScope1 as any).active).toBe(false);

        expect((newScope2 as any).componentAttributes).toEqual({
            smarteditElementUuid: 'thesmarteditElementUuid',
            smarteditComponentId: 'thesmarteditComponentId',
            smarteditComponentType: 'thesmarteditComponentType',
            smarteditCustom1: 'thecustom1',
            smarteditCustom2: 'thecustom2'
        } as any);

        expect((newScope2 as any).active).toBe(false);

        expect(converter.getScopes()).toEqual([
            'thesmarteditComponentId_DECO-2',
            'thesmarteditComponentId_DECO-1'
        ]);
    });
});
