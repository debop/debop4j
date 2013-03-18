package com.kt.vital.domain.model;

/**
 * com.kt.vital.domain.model.ActionType
 * User: sunghyouk.bae@gmail.com
 * Date: 13. 3. 18 오후 4:58
 */
public enum ActionType {

    Nothing("Nothing"),
    Login("Login"),
    Logout("Logout"),
    Search("Search"),
    Export("Export");

    private final String action;

    ActionType(String action) {
        this.action = action;
    }
}
