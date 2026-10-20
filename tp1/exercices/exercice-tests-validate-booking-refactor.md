# Évaluer et réusiner les tests unitaires de Booking.validate.

Des tests pour `Booking.validate` sont implémentés dans [BookingTest](../../src/test/java/uspace/domain/cruise/booking/BookingTest.java).
Ceux-ci n'appliquent pas toutes les bonnes pratiques vues en classes.

1. Faites la revue de codes des tests présents. Écrivez votre revue dans [le fichier de questions](../questions/question-tests-validate-booking-refactor.md)

2. Modifier les tests de `Booking.validate` afin de respecter les bonnes pratiques.
   1. Vous pouvez ajouter/supprimer des tests.
   2. Vous pouvez ajouter des classes de tests.
   3. Assurez-vous que tous les comportements sont bien testés.
   4. Assurez-vous que tous vos tests s'exécutent et passent avec `mvn test`.
      **Une note de 0 sera donnée au TP si les tests ne s'exécutent pas ou ne passent pas.**