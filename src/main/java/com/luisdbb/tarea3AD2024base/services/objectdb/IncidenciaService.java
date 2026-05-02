package com.luisdbb.tarea3AD2024base.services.objectdb;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

import org.springframework.stereotype.Service;

import com.luisdbb.objectdb.modelo.Incidencia;
import com.luisdbb.objectdb.modelo.ResolucionIncidencia;
import com.luisdbb.objectdb.modelo.TipoIncidencia;

@Service
public class IncidenciaService {

    private static final String UNIDAD = "objectdb-unit";

    private EntityManagerFactory emf =
            Persistence.createEntityManagerFactory(UNIDAD);

    public void registrarIncidencia(TipoIncidencia tipo,
                                    String descripcion,
                                    Long idPersona,
                                    Long idEspectaculo,
                                    Long idNumero) {

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Incidencia incidencia = new Incidencia();

            incidencia.setTipo(tipo);
            incidencia.setDescripcion(descripcion);
            incidencia.setIdPersonaReporta(idPersona);

            incidencia.setIdEspectaculo(idEspectaculo);
            incidencia.setIdNumero(idNumero);

            em.persist(incidencia);

            em.getTransaction().commit();

        } catch (Exception e) {
            em.getTransaction().rollback();
            throw new RuntimeException("Error al registrar incidencia");
        } finally {
            em.close();
        }
    }
    
    public void resolverIncidencia(Long idIncidencia,
            String acciones,
            Long idPersonaResuelve) {

    	EntityManager em = emf.createEntityManager();

    	try {
    		em.getTransaction().begin();

    		Incidencia incidencia = em.find(Incidencia.class, idIncidencia);

    		if (incidencia == null) {
    			throw new RuntimeException("Incidencia no encontrada");
    			}

    		if (incidencia.isResuelta()) {
    			throw new RuntimeException("La incidencia ya está resuelta");
    			}


    		incidencia.setResuelta(true);

    		ResolucionIncidencia resolucion = new ResolucionIncidencia();
    		resolucion.setAccionesRealizadas(acciones);
    		resolucion.setIdPersonaResuelve(idPersonaResuelve);
    		resolucion.setIncidencia(incidencia);

    		em.persist(resolucion);

    		em.getTransaction().commit();

    			} catch (Exception e) {
    				em.getTransaction().rollback();
    				throw new RuntimeException("Error al resolver incidencia");
    				} finally {
    						em.close();
    	}
    }
    
    public List<Incidencia> consultarIncidencias(
            TipoIncidencia tipo,
            Boolean resuelta,
            Long idEspectaculo,
            Long idNumero,
            LocalDateTime desde,
            LocalDateTime hasta) {

        EntityManager em = emf.createEntityManager();

        try {

            StringBuilder jpql = new StringBuilder("SELECT i FROM Incidencia i WHERE 1=1");

            if (tipo != null) {
                jpql.append(" AND i.tipo = :tipo");
            }

            if (resuelta != null) {
                jpql.append(" AND i.resuelta = :resuelta");
            }

            if (idEspectaculo != null) {
                jpql.append(" AND i.idEspectaculo = :idEspectaculo");
            }

            if (idNumero != null) {
                jpql.append(" AND i.idNumero = :idNumero");
            }

            if (desde != null) {
                jpql.append(" AND i.fechaHora >= :desde");
            }

            if (hasta != null) {
                jpql.append(" AND i.fechaHora <= :hasta");
            }

            TypedQuery<Incidencia> query =
                    em.createQuery(jpql.toString(), Incidencia.class);

            if (tipo != null) {
                query.setParameter("tipo", tipo);
            }

            if (resuelta != null) {
                query.setParameter("resuelta", resuelta);
            }

            if (idEspectaculo != null) {
                query.setParameter("idEspectaculo", idEspectaculo);
            }

            if (idNumero != null) {
                query.setParameter("idNumero", idNumero);
            }

            if (desde != null) {
                query.setParameter("desde", desde);
            }

            if (hasta != null) {
                query.setParameter("hasta", hasta);
            }

            return query.getResultList();

        } finally {
            em.close();
        }
    }
    
}