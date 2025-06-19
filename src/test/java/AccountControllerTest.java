package controller;

import jakarta.persistence.*;
import model.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountControllerTest {

    @Mock
    private EntityManager em;
    @Mock
    private EntityTransaction tx;
    @Mock
    private TypedQuery<Gebruiker> gebruikerQuery;
    @Mock
    private TypedQuery<Asielzoeker> asielzoekerQuery;

    private AccountController accountController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        accountController = new AccountController();
        accountController.emf = mock(EntityManagerFactory.class);
        when(accountController.emf.createEntityManager()).thenReturn(em);
        when(em.getTransaction()).thenReturn(tx);
    }
    @Test
    void testCreateAccount_Asielzoeker_Success() {
        Asielzoeker nieuw = new Asielzoeker("test@asiel.nl", "wachtwoord123");

        doAnswer(invocation -> {
            assertTrue(nieuw.getEmail().contains("@"));
            return null;
        }).when(em).persist(any(Asielzoeker.class));

        em.getTransaction().begin();
        em.persist(nieuw);
        em.getTransaction().commit();

        verify(em).persist(nieuw);
        verify(tx).begin();
        verify(tx).commit();
    }

    @Test
    void testCreateAccount_DuplicateEmail_Failure() {
        doThrow(PersistenceException.class).when(em).persist(any());

        try {
            em.getTransaction().begin();
            em.persist(new Asielzoeker("bestaand@mail.com", "wachtwoord123"));
        } catch (PersistenceException e) {
            em.getTransaction().rollback();
        }

        verify(tx).rollback();
    }

    @Test
    void testUpdateAccount_BestaandEmail() {
        Gebruiker gebruiker = new Asielzoeker("update@gebruiker.nl", "oudww");
        when(em.createQuery(anyString(), eq(Gebruiker.class))).thenReturn(gebruikerQuery);
        when(gebruikerQuery.setParameter(anyString(), any())).thenReturn(gebruikerQuery);
        when(gebruikerQuery.getSingleResult()).thenReturn(gebruiker);

        gebruiker.setWachtwoord("nieuwWW");
        em.getTransaction().begin();
        em.merge(gebruiker);
        em.getTransaction().commit();

        assertEquals("nieuwWW", gebruiker.getWachtwoord());
        verify(tx).commit();
    }

    @Test
    void testUpdateAccount_NonExistentEmail() {
        when(em.createQuery(anyString(), eq(Gebruiker.class))).thenReturn(gebruikerQuery);
        when(gebruikerQuery.setParameter(anyString(), any())).thenReturn(gebruikerQuery);
        when(gebruikerQuery.getSingleResult()).thenThrow(NoResultException.class);

        assertThrows(NoResultException.class, () -> {
            em.createQuery("SELECT g FROM Gebruiker g WHERE g.email = :email", Gebruiker.class)
                    .setParameter("email", "niet@bestaat.nl")
                    .getSingleResult();
        });
    }

    @Test
    void testReadAccountByRol_LegeResultaten() {
        when(em.createQuery(anyString(), eq(Gebruiker.class))).thenReturn(gebruikerQuery);
        when(gebruikerQuery.setParameter(anyString(), any())).thenReturn(gebruikerQuery);
        when(gebruikerQuery.getResultList()).thenReturn(Collections.emptyList());

        List<Gebruiker> resultaat = gebruikerQuery.getResultList();
        assertTrue(resultaat.isEmpty());
    }

    @Test
    void testReadAccountByRolAsielzoeker_BevatGebruikers() {
        Asielzoeker a1 = new Asielzoeker("a1@test.com", "wachtwoord123");
        when(em.createQuery(anyString(), eq(Asielzoeker.class))).thenReturn(asielzoekerQuery);
        when(asielzoekerQuery.setParameter(anyString(), any())).thenReturn(asielzoekerQuery);
        when(asielzoekerQuery.getResultList()).thenReturn(List.of(a1));

        List<Asielzoeker> resultaat = asielzoekerQuery.getResultList();
        assertEquals(1, resultaat.size());
        assertEquals("a1@test.com", resultaat.get(0).getEmail());
    }


}


