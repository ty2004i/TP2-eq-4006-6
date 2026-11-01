package uspace.infra.persistence.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uspace.domain.cruise.Cruise;
import uspace.domain.cruise.CruiseId;
import uspace.domain.cruise.CruiseRepository;

public class HibernateCruiseRepository implements CruiseRepository {

    private static final Logger logger = LoggerFactory.getLogger(HibernateCruiseRepository.class);

    @Override
    public Cruise findById(CruiseId cruiseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Cruise.class, cruiseId);

        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la croisière avec l'id {}", cruiseId, e);
            throw e;
        }
    }

    @Override
    public void save(Cruise cruise) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.merge(cruise);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Erreur lors de la sauvegarde de la croisière avec l'id {}", cruise.getId(), e);
            throw e;
        }
    }
}
