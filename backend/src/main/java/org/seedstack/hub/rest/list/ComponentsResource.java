/**
 * Copyright (c) 2015-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.hub.rest.list;

import io.swagger.annotations.Api;
import org.hibernate.validator.constraints.NotBlank;
import org.seedstack.business.assembler.FluentAssembler;
import org.seedstack.business.finder.Result;
import org.seedstack.business.view.PaginatedView;
import org.seedstack.hub.application.ImportService;
import org.seedstack.hub.domain.model.component.Component;
import org.seedstack.hub.domain.model.component.State;
import org.seedstack.hub.domain.services.fetch.VCSType;
import org.seedstack.hub.rest.shared.*;
import org.seedstack.seed.Logging;
import org.seedstack.seed.rest.Rel;
import org.seedstack.seed.rest.RelRegistry;
import org.seedstack.seed.rest.hal.HalRepresentation;
import org.seedstack.seed.rest.hal.Link;
import org.seedstack.seed.security.AuthenticationException;
import org.seedstack.seed.security.AuthorizationException;
import org.seedstack.seed.security.RequiresRoles;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static org.seedstack.hub.rest.Rels.*;

@Api
@Path("/")
@Produces({"application/json", "application/hal+json"})
public class ComponentsResource {
    @Inject
    private ComponentFinder componentFinder;
    @Inject
    private ServletContext servletContext;
    @Inject
    private RelRegistry relRegistry;
    @Inject
    private ImportService importService;
    @Inject
    private FluentAssembler fluentAssembler;
    @Logging
    private Logger logger;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("/components")
    public Response post(@FormParam("vcs") @NotBlank String vcs, @FormParam("url") @NotBlank String sourceUrl) throws URISyntaxException {
        VCSType vcsType;
        try {
            vcsType = VCSType.valueOf(vcs.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Unsupported VCS type " + vcs);
        }

        Component component;
        try {
            component = importService.importComponent(vcsType, new URL(sourceUrl));
        } catch (MalformedURLException e) {
            throw new BadRequestException("Malformed URL " + sourceUrl);
        } catch (AuthenticationException | AuthorizationException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Error during component import", e);
            throw new WebApplicationException(e.getMessage(), 400);
        }

        ComponentCard componentCard = fluentAssembler.assemble(component).to(ComponentCard.class);
        updateUrls(componentCard);
        return Response.created(new URI(relRegistry.uri(COMPONENT).set("componentId", componentCard.getId()).expand())).entity(componentCard).build();
    }

    @GET
    @Path("/components")
    @Rel(value = COMPONENTS, home = true)
    public PaginatedHal<ComponentCard> list(@QueryParam("search") String searchName, @BeanParam PageInfo pageInfo,
                                  @QueryParam("sort") String sort) {
        PaginatedView<ComponentCard> paginatedView = componentFinder.findCards(pageInfo.page(), searchName, sort);
        updateUrls(paginatedView);
        Link self = relRegistry.uri(COMPONENTS);
        if (searchName != null && !searchName.equals("")) {
            self.set("search", searchName);
        }
        return new PaginatedHal<>(COMPONENTS, paginatedView, self);
    }

    @RequiresRoles("admin")
    @GET
    @Path("/pending")
    @Rel(value = PENDING, home = true)
    public HalRepresentation list(@BeanParam PageInfo pageInfo) {
        PaginatedView<ComponentCard> paginatedView = componentFinder.findCardsByState(pageInfo.page(), State.PENDING);
        updateUrls(paginatedView);
        return new PaginatedHal<>(COMPONENTS, paginatedView, relRegistry.uri(PENDING));
    }

    @GET
    @Path("/popular")
    @Rel(value = POPULAR, home = true)
    public ResultHal<ComponentCard> popularCards(@BeanParam RangeInfo rangeInfo) {
        Result<ComponentCard> popularCards = componentFinder.findPopularCards(rangeInfo.range());
        updateUrls(popularCards);
        return new ResultHal<>(COMPONENTS, popularCards, relRegistry.uri(POPULAR));
    }

    @GET
    @Path("/recent")
    @Rel(value = RECENT, home = true)
    public HalRepresentation recentCards(@BeanParam RangeInfo rangeInfo) {
        Result<ComponentCard> recentCards = componentFinder.findRecentCards(rangeInfo.range());
        updateUrls(recentCards);
        return new ResultHal<>(COMPONENTS, recentCards, relRegistry.uri(RECENT));
    }

    private void updateUrls(ComponentCard componentCard) {
        componentCard.setIcon(UriBuilder.uri(servletContext.getContextPath(), componentCard.getIcon()));
    }

    private void updateUrls(PaginatedView<ComponentCard> paginatedView) {
        paginatedView.getView().stream().forEach(this::updateUrls);
    }

    private void updateUrls(Result<ComponentCard> result) {
        result.getResult().stream().forEach(this::updateUrls);
    }
}
