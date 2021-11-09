/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { browser, by, element } from 'protractor';

describe('Test gateway', () => {
    beforeEach(async () => {
        await browser.get('test/e2e/gateway/index.html');
        await browser.waitForWholeAppToBeReady();
    });

    it('iframe sending message is returned a success acknowledgement and parent frame gets message', async () => {
        await browser.switchToIFrame();
        await browser.click(by.id('sendMessage'));
        expect(await element(by.id('acknowledged')).getText()).toBe(
            '(parent acknowledged my message and sent back:hello to you iframe)'
        );
        await browser.switchToParent();
        expect(await element(by.id('message')).getText()).toBe('hello parent ! (from iframe)');
    });

    it('parent frame sending message is returned a failure acknowledgement and listeners 1 and 2 get a failure', async () => {
        await browser.click(by.id('sendMessage1'));
        expect(await element(by.id('acknowledged')).getText()).toBe(
            '(iframe did not acknowledge my message)'
        );
        await browser.switchToIFrame();
        expect(await element(by.id('message1')).getText()).toBe('failure');
        expect(await element(by.id('message2')).getText()).toBe('failure');
    });

    it('parent frame sending message is returned a failure acknowledgement and listener 1 gets the message while listener 2 gets a failure', async () => {
        await browser.switchToIFrame();
        await browser.click(by.id('check1'));

        await browser.switchToParent();
        await browser.click(by.id('sendMessage1'));
        expect(await element(by.id('acknowledged')).getText()).toBe(
            '(iframe did not acknowledge my message)'
        );

        await browser.switchToIFrame();

        expect(await element(by.id('message1')).getText()).toBe('hello Iframe ! (from parent)');
        expect(await element(by.id('message2')).getText()).toBe('failure');
    });

    it('parent frame sending message is returned a success acknowledgement and both listeners on gateway 1 get message; listener on gateway 2 is not triggered', async () => {
        await browser.switchToIFrame();
        await browser.click(by.id('check1'));
        await browser.click(by.id('check2'));

        await browser.switchToParent();
        await browser.click(by.id('sendMessage1'));
        expect(await element(by.id('acknowledged')).getText()).toBe(
            '(iframe acknowledged my message and sent back:hello to you parent from second listener on gateway1)'
        );

        await browser.switchToIFrame();

        expect(await element(by.id('message1')).getText()).toBe('hello Iframe ! (from parent)');
        expect(await element(by.id('message2')).getText()).toBe('hello Iframe ! (from parent)');
        expect(await element(by.id('message3')).getText()).toBe('');
    });

    it(
        'parent frame sending message on gateway 2 is returned a failure acknowledgement ' +
            'and listener 1 on gateway 2 gets a failure while listeners 1 and 2 on gateway 1 are not invoked',
        async () => {
            await browser.click(by.id('sendMessage2'));
            expect(await element(by.id('acknowledged')).getText()).toBe(
                '(iframe did not acknowledge my message)'
            );
            await browser.switchToIFrame();
            expect(await element(by.id('message3')).getText()).toBe('failure');
            expect(await element(by.id('message1')).getText()).toBe('');
            expect(await element(by.id('message2')).getText()).toBe('');
        }
    );

    it(
        'parent frame sending message on gateway 2 is returned a success acknowledgement' +
            'and listener 1 on gateway 2 gets a message while listeners 1 and 2 on gateway 1 are not invoked',
        async () => {
            await browser.switchToIFrame();
            await browser.click(by.id('check3'));

            await browser.switchToParent();
            await browser.click(by.id('sendMessage2'));
            expect(await element(by.id('acknowledged')).getText()).toBe(
                '(iframe acknowledged my message and sent back:hello to you parent from unique listener on gateway2)'
            );

            await browser.switchToIFrame();

            expect(await element(by.id('message3')).getText()).toBe('hello Iframe ! (from parent)');
            expect(await element(by.id('message1')).getText()).toBe('');
            expect(await element(by.id('message2')).getText()).toBe('');
        }
    );
});
