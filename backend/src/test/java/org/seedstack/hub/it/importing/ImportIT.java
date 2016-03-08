package org.seedstack.hub.it.importing;

import org.junit.Test;
import org.seedstack.hub.domain.model.component.Component;
import org.seedstack.hub.domain.model.component.ComponentId;
import org.seedstack.hub.domain.model.component.Version;
import org.seedstack.hub.domain.model.component.VersionId;
import org.seedstack.hub.domain.model.document.DocumentId;
import org.seedstack.hub.domain.model.user.UserId;
import org.seedstack.hub.domain.services.importer.ImportService;
import org.seedstack.seed.it.AbstractSeedIT;

import javax.inject.Inject;
import java.io.File;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class ImportIT extends AbstractSeedIT {
    @Inject
    ImportService importService;

    @Test
    public void import_is_working() throws Exception {
        Component component = importService.importComponent(new File("src/test/resources/components/component1"));
        assertThat(component.getComponentId().getName()).isEqualTo("component1");

        assertThat(component.getVersions()).containsExactly(new Version(new VersionId(1, 0, 0, null)));
        assertThat(component.getVersions().get(0).getPublicationDate()).isEqualTo(LocalDate.of(2016, 3, 8));
        assertThat(component.getVersions().get(0).getUrl()).isEqualTo("http://fake.url.org/component-1");

        assertThat(component.getOwner()).isEqualTo(new UserId("adrien.lauer@mpsa.com"));
        assertThat(component.getDescription().getName()).isEqualTo("component1");
//        assertThat(component.getDescription().getIcon()).isEqualTo(buildDocumentId("component1", "icon.png"));
//        assertThat(component.getDescription().getImages()).containsExactly(buildDocumentId("component1", "screenshots/1.png"), buildDocumentId("component1", "screenshots/2.png"), buildDocumentId("component1", "screenshots/3.png"));
//        assertThat(component.get).isEqualTo("");
//        assertThat(component.).isEqualTo("");
//        assertThat(component.).isEqualTo("");
//        assertThat(component.).isEqualTo("");
//        assertThat(component.).isEqualTo("");
//        assertThat(component.).isEqualTo("");
//        assertThat(component.).isEqualTo("");
//        assertThat(component.).isEqualTo("");
//        assertThat(component.).isEqualTo("");
//        assertThat(component.).isEqualTo("");
//        assertThat(component.).isEqualTo("");
//        assertThat(component.).isEqualTo("");

    }

    private DocumentId buildDocumentId(String componentId, String path) {
        return new DocumentId(new ComponentId(componentId), path);
    }
}
