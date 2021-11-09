When adding grovy tests, mark them as @ManualTests, so they get not executed automatically.
Instead add them to nAllTextFieldConfiguratorSpockTests.groovy. This ensure that the testdata
i prepared/erased once before/after all tests. 