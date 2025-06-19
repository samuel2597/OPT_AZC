package services;

import jakarta.persistence.*;
import model.Asielzoeker;
import model.Dossier;
import model.Land;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DossierServicesTest {

    @Mock private EntityManager em;
    @Mock private EntityTransaction tx;
    @Mock private EntityManagerFactory emf;

    private DossierServices dossierServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(emf.createEntityManager()).thenReturn(em);
        when(em.getTransaction()).thenReturn(tx);

        dossierServices = new DossierServices();
        dossierServices.emf = emf;
    }

    @Test
    void testUpdateDossier_bestaandDossier() {
        Dossier dossier = new Dossier();
        Asielzoeker asielzoeker = mock(Asielzoeker.class);
        when(asielzoeker.getDossier()).thenReturn(dossier);
        when(em.find(Asielzoeker.class, 1L)).thenReturn(asielzoeker);

        dossierServices.updateDossier(1L, true, true, true, true, "Toegewezen", false, "gestart");

        assertTrue(dossier.isPaspoortGetoond());
        assertTrue(dossier.isAanvraagCompleet());
        assertEquals("Toegewezen", dossier.getUitspraak());
        assertEquals("gestart", dossier.getPlaatsingStatus());
        verify(em).merge(dossier);
        verify(tx).commit();
    }

    @Test
    void testUpdateDossier_geenDossier() {
        Asielzoeker asielzoeker = mock(Asielzoeker.class);
        when(asielzoeker.getDossier()).thenReturn(null);
        when(em.find(Asielzoeker.class, 2L)).thenReturn(asielzoeker);

        dossierServices.updateDossier(2L, true, false, false, false, null, false, "");

        verify(em, never()).merge(any(Dossier.class));
    }

    @Test
    void testUpdateDossier_asielzoekerNietGevonden() {
        when(em.find(Asielzoeker.class, 999L)).thenReturn(null);

        dossierServices.updateDossier(999L, true, true, true, true, "Afgewezen", true, "nee");

        verify(em, never()).merge(any(Dossier.class));
    }

    @Test
    void testUpdateDossier_partialUpdate() {
        Dossier dossier = new Dossier();
        Asielzoeker asielzoeker = mock(Asielzoeker.class);
        when(asielzoeker.getDossier()).thenReturn(dossier);
        when(em.find(Asielzoeker.class, 5L)).thenReturn(asielzoeker);

        dossierServices.updateDossier(5L, false, false, true, false, null, false, "afgerond");

        assertFalse(dossier.isPaspoortGetoond());
        assertTrue(dossier.isRechterToegewezen());
        assertEquals("afgerond", dossier.getPlaatsingStatus());
        verify(em).merge(dossier);
    }

    @Test
    void testUpdateDossier_legeVelden() {
        Dossier dossier = new Dossier();
        Asielzoeker asielzoeker = mock(Asielzoeker.class);
        when(asielzoeker.getDossier()).thenReturn(dossier);
        when(em.find(Asielzoeker.class, 10L)).thenReturn(asielzoeker);

        dossierServices.updateDossier(10L, false, false, false, false, "", false, "");

        assertEquals("", dossier.getUitspraak());
        assertEquals("", dossier.getPlaatsingStatus());
        verify(em).merge(dossier);
    }
}
