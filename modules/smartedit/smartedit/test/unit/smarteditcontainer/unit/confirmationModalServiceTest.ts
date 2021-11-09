/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

import { ConfirmationModalService } from 'smarteditcontainer/services';
import {
    ConfirmDialogComponent,
    FundamentalModalButtonStyle,
    IModalService,
    ModalButtonActions,
    ModalButtonStyles
} from 'smarteditcommons';

describe('ConfirmationModalService', () => {
    // Service Under Test
    let confirmationModalService: ConfirmationModalService;
    let modalService: jasmine.SpyObj<IModalService>;

    // Set-up Service Under Test

    beforeEach(() => {
        modalService = jasmine.createSpyObj('modalService', ['open']);
        modalService.open.and.returnValue(Promise.resolve());

        confirmationModalService = new ConfirmationModalService(modalService);
    });

    it('GIVEN showOkButtonOnly WHEN modalService is called THEN it will display the modal with only the OK button', () => {
        // Arrange

        // Act
        confirmationModalService.confirm({
            title: 'my.confirmation.title',
            description: 'my.confirmation.message',
            showOkButtonOnly: true
        });

        // Assert
        expect(modalService.open).toHaveBeenCalledWith({
            component: ConfirmDialogComponent,
            data: {
                description: 'my.confirmation.message',
                descriptionPlaceholders: undefined
            },
            config: {
                modalPanelClass: 'se-confirmation-dialog',
                focusTrapped: false,
                container: 'body'
            },
            templateConfig: {
                isDismissButtonVisible: true,
                title: 'my.confirmation.title',
                buttons: [
                    {
                        id: 'confirmOk',
                        label: 'se.confirmation.modal.ok',
                        action: ModalButtonActions.Close,
                        style: FundamentalModalButtonStyle.Primary
                    }
                ]
            }
        });
    });

    describe('with description', () => {
        it('confirm will call open on the modalService with the given description and title when provided with a description and title', () => {
            // Arrange

            // Act
            confirmationModalService.confirm({
                title: 'my.confirmation.title',
                description: 'my.confirmation.message'
            });

            // Assert
            expect(modalService.open).toHaveBeenCalledWith({
                component: ConfirmDialogComponent,
                data: {
                    description: 'my.confirmation.message',
                    descriptionPlaceholders: undefined
                },
                config: {
                    modalPanelClass: 'se-confirmation-dialog',
                    container: 'body',
                    focusTrapped: false
                },
                templateConfig: {
                    title: 'my.confirmation.title',
                    isDismissButtonVisible: true,
                    buttons: [
                        {
                            id: 'confirmCancel',
                            label: 'se.confirmation.modal.cancel',
                            style: FundamentalModalButtonStyle.Default,
                            action: ModalButtonActions.Dismiss
                        },
                        {
                            id: 'confirmOk',
                            label: 'se.confirmation.modal.ok',
                            style: FundamentalModalButtonStyle.Primary,
                            action: ModalButtonActions.Close
                        }
                    ]
                }
            });
        });
    });

    describe('with template', () => {
        it('confirm will call open on the modalService with the given template and title when provided with a template and title', () => {
            // Arrange

            // Act
            confirmationModalService.confirm({
                title: 'my.confirmation.title',
                template: '<div>my template</div>'
            });

            // Assert
            expect(modalService.open).toHaveBeenCalledWith({
                size: 'md',
                title: 'my.confirmation.title',
                templateInline: '<div>my template</div>',
                templateUrl: undefined,
                controller: undefined,
                cssClasses: 'yFrontModal',
                buttons: [
                    {
                        id: 'confirmCancel',
                        label: 'se.confirmation.modal.cancel',
                        style: ModalButtonStyles.Default,
                        action: ModalButtonActions.Dismiss
                    },
                    {
                        id: 'confirmOk',
                        label: 'se.confirmation.modal.ok',
                        action: ModalButtonActions.Close
                    }
                ]
            });
        });
    });

    describe('with templateUrl', () => {
        it('confirm will call open on the modalService with the given templateUrl and title when provided with a templateUrl and title', () => {
            // Arrange

            // Act
            confirmationModalService.confirm({
                title: 'my.confirmation.title',
                templateUrl: 'myTemplate.html'
            });

            // Assert
            expect(modalService.open).toHaveBeenCalledWith({
                size: 'md',
                title: 'my.confirmation.title',
                templateInline: undefined,
                templateUrl: 'myTemplate.html',
                controller: undefined,
                cssClasses: 'yFrontModal',
                buttons: [
                    {
                        id: 'confirmCancel',
                        label: 'se.confirmation.modal.cancel',
                        style: ModalButtonStyles.Default,
                        action: ModalButtonActions.Dismiss
                    },
                    {
                        id: 'confirmOk',
                        label: 'se.confirmation.modal.ok',
                        action: ModalButtonActions.Close
                    }
                ]
            });
        });
    });

    describe('confirm validation', () => {
        it('Confirm will return rejected promise when all configuration properties description, template, templateUrl are undefined', (done) => {
            // Arrange

            const promise = confirmationModalService.confirm({
                title: 'my.confirmation.title'
            }) as Promise<any>;

            promise.catch((err) => {
                expect(err).toBe(
                    'You must have one of the following configuration properties configured: description, template, or templateUrl'
                );
                done();
            });
        });

        it('Confirm will return rejected promise when more than on configuration property description, template, templateUrl is set', (done) => {
            // Arrange
            const promise = confirmationModalService.confirm({
                title: 'my.confirmation.title',
                templateUrl: 'myTemplate.html',
                description: 'myTemplate.html'
            }) as Promise<any>;

            // Act

            promise.catch((err) => {
                expect(err).toBe(
                    'You have more than one of the following configuration properties configured: description, template, or templateUrl'
                );
                done();
            });
        });
    });
});
