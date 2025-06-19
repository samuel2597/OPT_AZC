package services;

import jakarta.persistence.*;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AzcServiceTest {

    @Mock
    private EntityManager em;
    @Mock
    private EntityTransaction tx;
    @Mock
    private EntityManagerFactory emf;
    @Mock
    private TypedQuery<Azc> azcQuery;
    @Mock
    private Gemeente gemeente;

    private AzcService azcService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(emf.createEntityManager()).thenReturn(em);
        when(em.getTransaction()).thenReturn(tx);

        azcService = new AzcService();
        azcService.emf = emf; // injecteer mock
    }

    @Test
    void testSaveAzc_Success() {
        when(em.find(Gemeente.class, 1L)).thenReturn(gemeente);

        azcService.saveAzc("Teststraat", 12, "1234AB", 1L);

        verify(tx).begin();
        verify(em).persist(any(Azc.class));
        verify(tx).commit();
        verify(em).close();
    }

    @Test
    void testSaveAzc_GemeenteNietGevonden() {
        when(em.find(Gemeente.class, 999L)).thenReturn(null);

        azcService.saveAzc("Onbekend", 0, "0000ZZ", 999L);

        // Geen persist of commit
        verify(em, never()).persist(any(Azc.class));
    }

    @Test
    void testUpdateAzc_Bestaand() {
        Azc mockAzc = new Azc("Oudstraat", 1, "1111AA", gemeente);
        when(em.find(Azc.class, 1)).thenReturn(mockAzc);

        azcService.updateAzc(1, "Nieuwstraat", 99, "9999ZZ");

        assertEquals("Nieuwstraat", mockAzc.getStraat());
        assertEquals(99, mockAzc.getNummer());
        assertEquals("9999ZZ", mockAzc.getPostcode());
        verify(tx).commit();
    }

    @Test
    void testUpdateAzc_NietGevonden() {
        when(em.find(Azc.class, 123)).thenReturn(null);

        azcService.updateAzc(123, "Xstraat", 1, "0000AA");

        verify(tx, never()).commit(); // commit nooit aangeroepen
    }

    @Test
    void testDeleteAzc_Success() {
        Azc azc = mock(Azc.class);
        when(azc.getAsielzoekers()).thenReturn(Collections.emptyList());
        when(em.find(Azc.class, 1L)).thenReturn(azc);

        azcService.deleteAzcById(1L);

        verify(tx).begin();
        verify(em).remove(azc);
        verify(tx).commit();
    }


    @Test
    void testDeleteAzc_MetAsielzoekers() {
        Asielzoeker a = new Asielzoeker("test@asiel.nl", "wachtwoord123");
        Azc azc = mock(Azc.class);
        when(azc.getAsielzoekers()).thenReturn(List.of(a));
        when(em.find(Azc.class, 2L)).thenReturn(azc);

        azcService.deleteAzcById(2L);

        verify(em, never()).remove(azc);
    }

    @Test
    void testDeleteAzc_NietGevonden() {
        when(em.find(Azc.class, 999L)).thenReturn(null);

        azcService.deleteAzcById(999L);

        verify(em, never()).remove(any());
    }

    @Test
    void testGetAllAzcs_LeegResultaat() {
        when(em.createQuery(anyString(), eq(Azc.class))).thenReturn(azcQuery);
        when(azcQuery.getResultList()).thenReturn(Collections.emptyList());

        azcService.getAllAzcs();

        verify(em).createQuery("SELECT a FROM Azc a", Azc.class);
    }




}