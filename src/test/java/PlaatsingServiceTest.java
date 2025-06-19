package services;

import jakarta.persistence.*;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlaatsingServiceTest {

    @Mock private EntityManager em;
    @Mock private EntityTransaction tx;
    @Mock private EntityManagerFactory emf;

    @InjectMocks private PlaatsingService plaatsingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(emf.createEntityManager()).thenReturn(em);
        when(em.getTransaction()).thenReturn(tx);
        plaatsingService = new PlaatsingService();
    }

    @Test
    void testVerplaatsAsielzoeker_metBestaandeGegevens() {
        Asielzoeker asielzoeker = mock(Asielzoeker.class);
        Gemeente oudeGemeente = new Gemeente("Oudstad", 1000, 10);
        Gemeente nieuweGemeente = new Gemeente("Nieuwstad", 1500, 5);

        Azc huidigAzc = new Azc("OudeStraat", 1, "1111AA", oudeGemeente);
        Azc nieuwAzc = new Azc("NieuweStraat", 2, "2222BB", nieuweGemeente);

        when(asielzoeker.getHuidigeAzc()).thenReturn(huidigAzc);
        when(em.find(Asielzoeker.class, 1L)).thenReturn(asielzoeker);
        when(em.find(Gemeente.class, 2L)).thenReturn(nieuweGemeente);
        when(em.find(Azc.class, 3L)).thenReturn(nieuwAzc);

        plaatsingService.verplaatsAsielzoeker(1L, 2L, 3L);

        verify(tx).begin();
        verify(em).merge(asielzoeker);
        verify(tx).commit();
    }

    @Test
    void testVerplaatsAsielzoeker_asielzoekerNietGevonden() {
        when(em.find(Asielzoeker.class, 99L)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            plaatsingService.verplaatsAsielzoeker(99L, 1L, 1L);
        });

        assertTrue(ex.getMessage().contains("niet gevonden"));
    }

    @Test
    void testVerplaatsAsielzoeker_azcNietGevonden() {
        Asielzoeker asielzoeker = mock(Asielzoeker.class);
        Gemeente gemeente = new Gemeente("Stad", 1000, 10);

        when(em.find(Asielzoeker.class, 1L)).thenReturn(asielzoeker);
        when(em.find(Gemeente.class, 2L)).thenReturn(gemeente);
        when(em.find(Azc.class, 3L)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            plaatsingService.verplaatsAsielzoeker(1L, 2L, 3L);
        });

        assertTrue(ex.getMessage().contains("AZC met ID 3 niet gevonden"));
    }

    @Test
    void testVerplaatsAsielzoeker_gemeenteNietGevonden() {
        Asielzoeker asielzoeker = mock(Asielzoeker.class);
        when(em.find(Asielzoeker.class, 1L)).thenReturn(asielzoeker);
        when(em.find(Gemeente.class, 5L)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            plaatsingService.verplaatsAsielzoeker(1L, 5L, 6L);
        });

        assertTrue(ex.getMessage().contains("Gemeente met ID 5 niet gevonden"));
    }

    @Test
    void testVerplaatsAsielzoeker_zonderHuidigAzc() {
        Asielzoeker asielzoeker = mock(Asielzoeker.class);
        Gemeente gemeente = new Gemeente("Vrijstad", 1200, 7);
        Azc nieuwAzc = new Azc("Vrijstraat", 4, "4444VV", gemeente);

        when(asielzoeker.getHuidigeAzc()).thenReturn(null);
        when(em.find(Asielzoeker.class, 10L)).thenReturn(asielzoeker);
        when(em.find(Gemeente.class, 11L)).thenReturn(gemeente);
        when(em.find(Azc.class, 12L)).thenReturn(nieuwAzc);

        plaatsingService.verplaatsAsielzoeker(10L, 11L, 12L);

        verify(em).merge(nieuwAzc);
        verify(em).merge(gemeente);
    }

    // Nieuw: Test voor plaatsAsielzoekerMetKeuze

    @Test
    void testPlaatsAsielzoekerMetKeuze_geldigScenario() {
        Asielzoeker asielzoeker = mock(Asielzoeker.class);
        Gemeente gemeente = mock(Gemeente.class);
        Azc azc = mock(Azc.class);

        when(em.find(Asielzoeker.class, 1L)).thenReturn(asielzoeker);
        when(em.find(Azc.class, 2L)).thenReturn(azc);
        when(azc.getGemeente()).thenReturn(gemeente);
        when(gemeente.getId()).thenReturn(100L);

        // Mocking helper methode
        PlaatsingService spyService = spy(plaatsingService);
        doReturn(gemeente).when(spyService).zoekGemeenteMetMeesteBeschikbarePlekken(em);

        spyService.plaatsAsielzoekerMetKeuze(1L, 2L);

        verify(tx).begin();
        verify(em).merge(asielzoeker);
        verify(tx).commit();
    }

    @Test
    void testPlaatsAsielzoekerMetKeuze_asielzoekerNietGevonden() {
        when(em.find(Asielzoeker.class, 1L)).thenReturn(null);

        plaatsingService.plaatsAsielzoekerMetKeuze(1L, 2L);

        verify(tx, never()).begin();
    }

    @Test
    void testPlaatsAsielzoekerMetKeuze_azcNietGevonden() {
        Asielzoeker asielzoeker = mock(Asielzoeker.class);
        when(em.find(Asielzoeker.class, 1L)).thenReturn(asielzoeker);
        when(em.find(Azc.class, 2L)).thenReturn(null);

        plaatsingService.plaatsAsielzoekerMetKeuze(1L, 2L);

        verify(tx, never()).begin();
    }

    @Test
    void testPlaatsAsielzoekerMetKeuze_azcNietInGemeente() {
        Asielzoeker asielzoeker = mock(Asielzoeker.class);
        Gemeente gekozenGemeente = mock(Gemeente.class);
        Gemeente andereGemeente = mock(Gemeente.class);
        Azc azc = mock(Azc.class);

        when(em.find(Asielzoeker.class, 1L)).thenReturn(asielzoeker);
        when(em.find(Azc.class, 2L)).thenReturn(azc);
        when(azc.getGemeente()).thenReturn(andereGemeente);
        when(andereGemeente.getId()).thenReturn(200L);
        when(gekozenGemeente.getId()).thenReturn(100L);

        PlaatsingService spyService = spy(plaatsingService);
        doReturn(gekozenGemeente).when(spyService).zoekGemeenteMetMeesteBeschikbarePlekken(em);

        spyService.plaatsAsielzoekerMetKeuze(1L, 2L);

        verify(tx, never()).begin();
    }
}
