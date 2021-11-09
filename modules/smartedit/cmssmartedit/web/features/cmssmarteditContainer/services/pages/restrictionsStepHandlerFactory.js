/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
angular
    .module('restrictionsStepHandlerFactoryModule', [])
    .factory('restrictionsStepHandlerFactory', function($q) {
        function RestrictionsStepHandler(
            wizardManager,
            restrictionsEditorFunctionBindings,
            stepProperties
        ) {
            var stepDetails = stepProperties;

            var isStepOnWizard = function() {
                return wizardManager.containsStep(this.getStepId());
            }.bind(this);

            this.hideStep = function hideStep() {
                if (isStepOnWizard()) {
                    wizardManager.removeStepById(this.getStepId());
                }
            };

            this.showStep = function showStep() {
                if (!isStepOnWizard()) {
                    wizardManager.addStep(stepDetails, wizardManager.getStepsCount());
                }
            };

            this.isStepValid = function isStepValid() {
                return (
                    restrictionsEditorFunctionBindings.isDirty &&
                    restrictionsEditorFunctionBindings.isDirty()
                );
            };

            this.save = function save() {
                return (
                    (restrictionsEditorFunctionBindings.save &&
                        restrictionsEditorFunctionBindings.save()) ||
                    $q.when()
                );
            };

            this.getStepId = function getStepId() {
                return stepDetails.id;
            };

            this.goToStep = function goToStep() {
                wizardManager.goToStepWithId(this.getStepId());
            };
        }

        return {
            createRestrictionsStepHandler: function(
                wizardManager,
                restrictionsEditorFunctionBindings,
                stepProperties
            ) {
                return new RestrictionsStepHandler(
                    wizardManager,
                    restrictionsEditorFunctionBindings,
                    stepProperties
                );
            }
        };
    });
