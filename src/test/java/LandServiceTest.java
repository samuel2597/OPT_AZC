package services;

import jakarta.persistence.*;
import model.Land;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LandServiceTest {

    @Mock private EntityManager em;
    @Mock private EntityTransaction tx;
    @Mock private EntityManagerFactory emf;

    private LandService landService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(emf.createEntityManager()).thenReturn(em);
        when(em.getTransaction()).thenReturn(tx);

        landService = new LandService();
        landService.emf = emf; // injecteer mock
    }

    @Test
    void testSaveLand() {
        landService.saveLand("Testland", true);

        verify(em).getTransaction();
        verify(tx).begin();
        verify(em).persist(any(Land.class));
        verify(tx).commit();
        verify(em).close();
    }

    @Test
    void testUpdateLand_landBestaat() {
        Land mockLand = new Land("Testland", false);
        when(em.find(Land.class, 1)).thenReturn(mockLand);

        landService.updateLand(1, true);

        assertTrue(mockLand.isVeilig());
        verify(tx).begin();
        verify(tx).commit();
    }

    @Test
    void testUpdateLand_landBestaatNiet() {
        when(em.find(Land.class, 999)).thenReturn(null);

        landService.updateLand(999, true);

        verify(tx, never()).begin();
    }
    @Test
    void testSaveLand_legeNaam() {
        assertDoesNotThrow(() -> {
            landService.saveLand("", true);
        }, "Een lege string mag niet crashen.");
    }
    @Test
    void testUpdateLand_naarOnveilig() {
        Land mockLand = new Land("Testland", true);
        when(em.find(Land.class, 2)).thenReturn(mockLand);

        landService.updateLand(2, false);

        assertFalse(mockLand.isVeilig(), "Land moet nu onveilig zijn.");
        verify(tx).begin();
        verify(tx).commit();
    }

    @Test
    void testGetAllLands_eenResultaat() {
        Land testLand = new Land("Nederland", true);
        TypedQuery<Land> mockQuery = mock(TypedQuery.class);
        when(em.createQuery(anyString(), eq(Land.class))).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(List.of(testLand));

        landService.getAllLands();

        verify(em).createQuery(anyString(), eq(Land.class));
    }

    @Test
    void testUpdateLand_entityManagerNull() {
        when(emf.createEntityManager()).thenReturn(null);

        assertThrows(NullPointerException.class, () -> {
            landService.updateLand(1, true);
        });
    }





    @Test
    void testGetAllLands_leegResultaat() {
        TypedQuery<Land> mockQuery = mock(TypedQuery.class);
        when(em.createQuery(anyString(), eq(Land.class))).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(List.of());

        landService.getAllLands(); // zou niks printen maar geen fout

        verify(em).createQuery(anyString(), eq(Land.class));
    }

    @Test
    void testSaveLand_nullNaam() {
        assertThrows(NullPointerException.class, () -> {
            landService.saveLand(null, true);
        });
    }
}
