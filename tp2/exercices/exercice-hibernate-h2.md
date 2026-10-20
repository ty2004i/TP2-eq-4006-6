# Mettre en place Hibernate avec une base de données H2

## Mise en contexte
### Hibernate: Un ORM

Hibernate est un framework ORM (Object-Relational Mapping) pour Java, conçu pour simplifier l'interaction entre une application Java et une base de données relationnelle. 
En utilisant Hibernate, les développeurs peuvent travailler avec les données en utilisant des objets Java sans avoir à écrire de requêtes SQL complexes. 
Hibernate convertit automatiquement les objets Java en enregistrements de base de données et vice versa, facilitant la persistance des données. 

Plus d'informations sur Hibernate sont disponibles sur le [site officiel](https://hibernate.org/orm/).

### H2: Une base de données en mémoire

H2 est une base de données relationnelle écrite en Java, conçue pour être rapide et facile à utiliser.

### Mettre en place Hibernate avec H2 dans un projet Java

Pour vous aider, consulter [le guide d'utilisation d'Hibernate avec H2](../astuces/astuce-hibernate-h2.md) conçu pour ce TP.

## Déjà en place

- Un fichier `hibernate.cfg.xml` qui contient la configuration de Hibernate.
- Les dépendances Maven nécessaires pour Hibernate et H2.
- Les annotations nécessaires pour les entités déjà présentes dans le projet.
- La classe `HibernateUtil` qui contient une méthode utilitaire permettant de créer une session Hibernate.


## Objectif

Le gestionnaire de USpace veut que vous utilisiez Hibernate pour gérer les données de l'application.

**Accès aux données qui doivent utiliser Hibernate:**
* Avec Cruise (actuellement `InMemoryCruiseRepository`)
* Avec Account (à faire dans l'exercice [Ajouter un compte utilisateur](./exercice-feature-account.md))

1. Implémenter l'accès aux données de l'application en utilisant Hibernate.
2. Les repositories InMemory doivent continuer d'exister, mais **ce sont ceux avec Hibernate qui doivent être utilisés dans l'application.**
3. Ajouter les annotations/configurations nécessaires selon le code ajouté dans ce TP.
4. Respecter les Principes SOLID (Portez une attention particulière au DIP) et les bonnes pratiques de programmation.

### Exclus
Il n'est pas nécessaire de tester votre accès aux données avec des tests unitaires.

Ce comportement est testé dans les tests d'intégration qui seront vus plus tard dans le cours. 

### Erreur possible
Tout dépendant l'ordre dans lequel vous effectuez les exercices, vous pourriez obtenir l'erreur ci-dessous en mettant en place Hibernate:

`[ERROR] u.i.p.h.HibernateUtil : class org.hibernate.mapping.SingleTableSubclass cannot be cast to class org.hibernate.mapping.RootClass`

Cette erreur sera réglée en complétant un autre exercice. C'est à vous de trouver la source de l'erreur.

## Questions à répondre (Seulement pour IFT-6006)

Répondez aux questions dans l'espace prévu dans le [fichier de questions](../questions/questions-hibernate-h2.md).
