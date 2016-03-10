/**
 * Copyright (c) 2015-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.hub.domain.model.user;

import org.hibernate.validator.constraints.Email;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.seedstack.business.domain.BaseAggregateRoot;

import javax.validation.constraints.NotNull;

@Entity("users")
public class User extends BaseAggregateRoot<UserId> {
    @NotNull
    @Id
    private UserId userId;
    @NotNull
    @Email
    private String email;

    public User(UserId userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    private User() {
        // required by morphia
    }

    @Override
    public UserId getEntityId() {
        return userId;
    }

    public UserId getId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }
}
