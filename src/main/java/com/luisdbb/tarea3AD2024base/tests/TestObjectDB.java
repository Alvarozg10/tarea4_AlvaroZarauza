package com.luisdbb.tarea3AD2024base.tests;
import javax.persistence.*;

public class TestObjectDB {

    public static void main(String[] args) {

        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("objectdb-unit");

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        System.out.println("Conectado a ObjectDB");

        em.getTransaction().commit();

        em.close();
        emf.close();
    }
}