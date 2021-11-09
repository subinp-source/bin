/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { browser, by, element, ElementFinder } from 'protractor';
import { ClickThroughOverlayComponentObject } from './ClickThroughOverlayComponentObject';

export namespace SfBuilderComponentObject {
    async function forceClickThroughOverlay(e: ElementFinder): Promise<void> {
        await ClickThroughOverlayComponentObject.Utils.clickThroughOverlay(e);
    }

    export const Elements = {
        addComponent: {
            button(): ElementFinder {
                return element(by.id('sf-builder-add-component-button'));
            },
            queueButton(): ElementFinder {
                return element(by.id('sf-builder-queue-add-component-button'));
            },
            alias(): ElementFinder {
                return element(by.id('sf-builder-add-component-alias'));
            },
            parentId(): ElementFinder {
                return element(by.id('sf-builder-add-component-parent'));
            }
        },
        removeComponent: {
            button(): ElementFinder {
                return element(by.id('sf-builder-remove-component-button'));
            },
            queueButton(): ElementFinder {
                return element(by.id('sf-builder-queue-remove-component-button'));
            },
            id(): ElementFinder {
                return element(by.id('sf-builder-remove-component-id'));
            },
            parentId(): ElementFinder {
                return element(by.id('sf-builder-remove-component-parent'));
            }
        },
        rerenderComponent: {
            button(): ElementFinder {
                return element(by.id('sf-builder-rerender-component-button'));
            },
            id(): ElementFinder {
                return element(by.id('sf-builder-rerender-component-id'));
            },
            parentId(): ElementFinder {
                return element(by.id('sf-builder-rerender-component-parent'));
            }
        },
        pageIdAndCatalogVersion: {
            button(): ElementFinder {
                return element(by.id('sf-builder-update-page-id-button'));
            },
            pageId(): ElementFinder {
                return element(by.id('sf-builder-update-page-id'));
            },
            catalogVersionUuid(): ElementFinder {
                return element(by.id('sf-builder-update-catalog-version'));
            }
        },
        pageBuilderConfig: {
            button(): ElementFinder {
                return element(by.id('sf-builder-change-page-button'));
            },
            layout(): ElementFinder {
                return element(by.id('sf-builder-change-page-layout'));
            },
            delays(): ElementFinder {
                return element(by.id('sf-builder-change-page-delays'));
            },
            render(): ElementFinder {
                return element(by.id('sf-builder-change-page-render'));
            }
        }
    };

    export const Actions = {
        async addComponent(alias: string, parentId?: string): Promise<void> {
            await browser.switchToIFrame();
            await browser.sendKeys(Elements.addComponent.alias(), alias);
            await browser.sendKeys(Elements.addComponent.parentId(), parentId);
            await forceClickThroughOverlay(Elements.addComponent.button());
        },
        async queueAddComponent(alias: string, parentId: string): Promise<void> {
            await browser.switchToIFrame();
            await browser.sendKeys(Elements.addComponent.alias(), alias);
            await browser.sendKeys(Elements.addComponent.parentId(), parentId);
            await forceClickThroughOverlay(Elements.addComponent.queueButton());
        },
        async removeComponent(id: string, parentId?: string): Promise<void> {
            await browser.switchToIFrame();
            await browser.sendKeys(Elements.removeComponent.id(), id);
            await browser.sendKeys(Elements.removeComponent.parentId(), parentId);
            await forceClickThroughOverlay(Elements.removeComponent.button());
        },
        async queueRemoveComponent(id: string, parentId: string): Promise<void> {
            await browser.switchToIFrame();
            await browser.sendKeys(Elements.removeComponent.id(), id);
            await browser.sendKeys(Elements.removeComponent.parentId(), parentId);
            await forceClickThroughOverlay(Elements.removeComponent.queueButton());
        },
        async rerenderComponent(id: string, parentId: string): Promise<void> {
            await browser.switchToIFrame();
            await browser.sendKeys(Elements.rerenderComponent.id(), id);
            await browser.sendKeys(Elements.rerenderComponent.parentId(), parentId);
            await forceClickThroughOverlay(Elements.rerenderComponent.button());
        },

        // =========== COMPONENT ACTIONS =============

        async changePageIdAndCatalogVersion(
            pageId: string,
            catalogVersionUuid: string
        ): Promise<void> {
            await browser.switchToIFrame();
            await browser.sendKeys(Elements.pageIdAndCatalogVersion.pageId(), pageId);
            await browser.sendKeys(
                Elements.pageIdAndCatalogVersion.catalogVersionUuid(),
                catalogVersionUuid
            );
            await forceClickThroughOverlay(Elements.pageIdAndCatalogVersion.button());
        },
        async changePageIdWithoutInteration(id: string): Promise<void> {
            await browser.executeScript('sfBuilder.updatePageId(arguments[0]);', id);
        },
        async changePage(layout: string, delay: string, renderer: string): Promise<void> {
            await browser.switchToIFrame();
            await browser.sendKeys(Elements.pageBuilderConfig.layout(), layout);
            await browser.sendKeys(Elements.pageBuilderConfig.delays(), delay);
            await browser.sendKeys(Elements.pageBuilderConfig.render(), renderer);
            await forceClickThroughOverlay(Elements.pageBuilderConfig.button());
        }
    };
}
