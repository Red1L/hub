/**
 * Copyright (c) 2015-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.hub.rest;

import org.seedstack.seed.rest.Rel;
import org.seedstack.seed.rest.RelRegistry;
import org.seedstack.seed.rest.hal.HalRepresentation;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by kavi87 on 07/03/2016.
 */

@Path("/recent")
public class RecentResource {

    public static final String RECENT = "recent";
    public static final short QUANTITY = 6;

    @Inject
    private ComponentFinder componentFinder;
    @Inject
    private RelRegistry relRegistry;

    @Rel(value = RECENT, home = true)
    @GET
    @Produces({"application/json", "application/hal+json"})
    public HalRepresentation getRecentCards() {
        HalRepresentation halRepresentation = new HalRepresentation();
        halRepresentation.embedded(RECENT, componentFinder.findRecentCards(QUANTITY));
        halRepresentation.link("self", relRegistry.uri(RECENT).expand());

        return halRepresentation;
    }
}
