/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
describe('Workflow Inbox - \n', function() {
    var inbox = e2e.componentObjects.inbox;
    var experienceSelector = e2e.componentObjects.ExperienceSelector;

    beforeEach(function(done) {
        inbox.actions.navigateToPage(__dirname, done);
    });

    afterEach(function() {
        inbox.utils.updateInboxTasks(false);
    });

    it('should have 4 workflow tasks for the user', function() {
        // WHEN
        inbox.actions.openInbox();

        // THEN
        inbox.assertions.inboxHasTasks(4);
    });

    it('The inbox task should have a task name, the catalog version details and the time the task was activated', function() {
        // WHEN
        inbox.actions.openInbox();

        // THEN
        inbox.assertions.inboxTaskHasName(0, 'Publish Page');
        inbox.assertions.inboxTaskHasDescription(0, 'Apparel Content Catalog Staged | Homepage');
        inbox.assertions.inboxTaskHasCreatedAgo(0, '<1 hour(s) ago');
    });

    it('Clicking on a task will load the page', function() {
        // WHEN
        inbox.actions.openInbox();

        // THEN
        inbox.actions.selectTask(0);
        experienceSelector.assertions.experienceSelectorButtonHasText(
            'Apparel Content Catalog - Staged'
        );
    });

    it('Opening inbox tasks should update Inbox badge count', function() {
        // GIVEN
        inbox.utils.updateInboxTasks(true);

        // WHEN
        inbox.actions.openInbox();

        // THEN
        inbox.assertions.inboxHasTasks(3);
        inbox.assertions.inboxBadgeHasUpdated('3');
    });
});
