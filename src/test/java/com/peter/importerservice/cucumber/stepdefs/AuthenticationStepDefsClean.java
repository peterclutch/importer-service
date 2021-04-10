package com.peter.importerservice.cucumber.stepdefs;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;

public class AuthenticationStepDefsClean {

    @Given("a brand is logged - clean")
    public void brand_is_logged() {
    }

    @Given("a self-picking inspector is logged - clean")
    public void selfPickingInspectorIsLogged() {
    }

    @Given("a user is logged with username {string} and password {string}")
    public void authenticateUser(String username, String password) {
    }

    @Given("an admin is logged - clean")
    public void admin_is_logged() {
    }

    @Given("an anonymous is logged - clean")
    public void anonymous_is_logged() {
    }

    @Given("^(an|another) inspector is logged - clean$")
    public void inspector_is_logged(String inspector) {
    }

    @Given("^inspector (1|2) is logged - clean$")
    public void inspector_is_logged_by_number(Integer inspectorNumber) {
    }

    @Given("user {string} is logged - clean")
    public void user_is_logged(String userEmail) {
    }

    @Given("The last invited factory is logged - clean")
    public void last_invited_factory_is_logged() {
    }

    @Given("a user with email {string} validates his account with password {string}")
    public void validateAccount(String username, String password) {
    }

    @Given("I {string} switch to brand {string}")
    public void switchBrand(String action, String brandName) {
    }

    @And("I select the factory - clean")
    public void iSelectTheFactoryClean() {
    }
}
