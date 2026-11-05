package uspace.infra.persistence.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uspace.domain.account.Account;
import uspace.domain.account.AccountRepository;
import uspace.domain.account.Username;

public class HibernateAccountRepository implements AccountRepository {

    private static final Logger logger = LoggerFactory.getLogger(HibernateAccountRepository.class);

    @Override
    public Account findByUsername(Username username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Account.class, username);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du compte avec l'username {}", username, e);
            throw e;
        }
    }

    @Override
    public void save(Account account) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.merge(account);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Erreur lors de la sauvegarde du compte avec l'username {}", account.getUsername(), e);
            throw e;
        }
    }

    @Override
    public boolean existsByUsername(Username username) {
        return findByUsername(username) != null;
    }
}
