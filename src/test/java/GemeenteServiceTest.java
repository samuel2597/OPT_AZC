package services;

import jakarta.persistence.*;
import model.Gemeente;
import model.Azc;
import model.Asielzoeker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GemeenteServiceTest {

    @Mock private EntityManager em;
    @Mock private EntityTransaction tx;
    @Mock private EntityManagerFactory emf;
    @Mock private TypedQuery<Gemeente> gemeenteQuery;
    @Mock private TypedQuery<Azc> azcQuery;

    private GemeenteService gemeenteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(emf.createEntityManager()).thenReturn(em);
        when(em.getTransaction()).thenReturn(tx);

        gemeenteService = new GemeenteService();
        gemeenteService.emf = emf; // Injecteer mock
    }

    @Test
    void testSaveGemeente_validInput() {
        gemeenteService.saveGemeente("Teststad", 10000, 50);

        verify(em).getTransaction();
        verify(tx).begin();
        verify(em).persist(any(Gemeente.class));
        verify(tx).commit();
        verify(em).close();
    }

    @Test
    void testSaveGemeente_invalidInput_nullNaam() {
        assertThrows(NullPointerException.class, () -> {
            gemeenteService.saveGemeente(null, 1000, 10);
        });
    }

    @Test
    void testUpdateGemeente_bestaand() {
        Gemeente gemeente = new Gemeente("Teststad", 10000, 50);
        when(em.find(Gemeente.class, 1)).thenReturn(gemeente);

        gemeenteService.updateGemeente(1, 20000, 100);

        assertEquals(20000, gemeente.getAantalInwoners());
        assertEquals(100, gemeente.getAangebodenPlaatsen());
    }

    @Test
    void testUpdateGemeente_nietBestaand() {
        when(em.find(Gemeente.class, 999)).thenReturn(null);

        gemeenteService.updateGemeente(999, 20000, 100);

        verify(tx, never()).begin();
    }

    @Test
    void testGetAllGemeente_emptyList() {
        when(em.createQuery(anyString(), eq(Gemeente.class))).thenReturn(gemeenteQuery);
        when(gemeenteQuery.getResultList()).thenReturn(List.of());

        gemeenteService.getAllGemeente();

        verify(em).createQuery(anyString(), eq(Gemeente.class));
    }

    @Test
    void testUitkeringsRapportage_geenGemeentes() {
        when(em.createQuery("SELECT g FROM Gemeente g", Gemeente.class)).thenReturn(gemeenteQuery);
        when(gemeenteQuery.getResultList()).thenReturn(List.of());

        gemeenteService.uitkeringsRapportage();

        verify(em).createQuery("SELECT g FROM Gemeente g", Gemeente.class);
    }



    @Test
    void testUitkeringsRapportage_aangebodenPlaatsenNul() {
        Gemeente gemeente = new Gemeente("Nulstad", 10000, 0);

        when(em.createQuery("SELECT g FROM Gemeente g", Gemeente.class)).thenReturn(gemeenteQuery);
        when(gemeenteQuery.getResultList()).thenReturn(List.of(gemeente));
        when(em.createQuery("SELECT a FROM Azc a WHERE a.gemeente = :gemeente", Azc.class)).thenReturn(azcQuery);
        when(azcQuery.setParameter("gemeente", gemeente)).thenReturn(azcQuery);
        when(azcQuery.getResultList()).thenReturn(List.of());

        gemeenteService.uitkeringsRapportage();
    }
}
