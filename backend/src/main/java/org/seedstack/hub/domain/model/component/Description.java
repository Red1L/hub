/**
 * Copyright (c) 2015-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.hub.domain.model.component;

import org.hibernate.validator.constraints.NotBlank;
import org.mongodb.morphia.annotations.Embedded;
import org.seedstack.business.domain.BaseValueObject;
import org.seedstack.hub.domain.model.document.DocumentId;
import org.seedstack.hub.domain.model.user.UserId;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embedded
public class Description extends BaseValueObject {
    public static final DocumentId DEFAULT_ICON = new DocumentId(ComponentId.DEFAULT_COMPONENT, "default:icon");
    public static final DocumentId DEFAULT_README = new DocumentId(ComponentId.DEFAULT_COMPONENT, "default:readme");

    @NotBlank
    private String name;
    private String summary;
    @NotNull
    private DocumentId icon;
    @NotNull
    private DocumentId readme;
    private List<DocumentId> images = new ArrayList<>();
    private List<UserId> maintainers = new ArrayList<>();

    public Description(String name, String summary, DocumentId icon, DocumentId readme) {
        this.name = name;
        this.summary = summary;
        this.icon = icon != null ? icon : DEFAULT_ICON;
        this.readme = readme != null ? readme : DEFAULT_README;
    }

    private Description() {
        // required by morphia
    }

    private Description(Description description) {
        this.name = description.name;
        this.summary = description.summary;
        this.icon = description.icon;
        this.readme = description.readme;
        this.images = new ArrayList<>(description.images);
        this.maintainers = new ArrayList<>(description.maintainers);
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public DocumentId getIcon() {
        return icon;
    }

    public DocumentId getReadme() {
        return readme;
    }

    public List<DocumentId> getImages() {
        return Collections.unmodifiableList(images);
    }

    public List<UserId> getMaintainers() {
        return Collections.unmodifiableList(maintainers);
    }

    public Description setReadme(DocumentId documentId) {
        Description description = new Description(this);
        description.readme = documentId;
        return description;
    }

    public Description addImage(DocumentId image) {
        Description description = new Description(this);
        description.images.add(image);
        return description;
    }

    public Description removeImage(DocumentId image) {
        Description description = new Description(this);
        description.images.remove(image);
        return description;
    }

    public Description setImages(List<DocumentId> images) {
        Description description = new Description(this);
        description.images = new ArrayList<>(images);
        return description;
    }

    public Description addMaintainer(UserId maintainer) {
        Description description = new Description(this);
        description.maintainers.add(maintainer);
        return description;
    }

    public Description removeMaintainer(UserId maintainer) {
        Description description = new Description(this);
        description.maintainers.remove(maintainer);
        return description;
    }

    public Description setMaintainers(List<UserId> maintainers) {
        Description description = new Description(this);
        description.maintainers = new ArrayList<>(maintainers);
        return description;
    }
}
