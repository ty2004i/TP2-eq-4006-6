# Nouvelle fonctionnalité : Voir l’oxygène requis pour le reste de la croisière

Vous devez implémenter cette nouvelle fonctionnalité dans l'application USpace.

1. Implémentez tous les comportements de la fonctionnalité.
2. Assurez-vous de respecter les Principes SOLID+T et les bonnes pratiques de programmation.
3. Implémentez les tests unitaires pour toutes vos modifications. Nous vous recommandons le TDD.

## Questions à répondre (Seulement pour IFT-6006)

Répondez aux questions dans l’espace prévu dans le [fichier de questions](../questions/questions-feature-oxygen.md).

## Détails de la fonctionnalité

Le gestionnaire de croisières souhaite pouvoir **voir la quantité d’oxygène requise** dans le vaisseau pour le reste d’une croisière spatiale, à partir d’une date donnée.

Cette quantité est indiquée en **unités d’oxygène (o2)**.

La quantité d’oxygène requise dépend de :

* la date de calcul
* la durée restante de la croisière à partir de cette date
* le ratio de temps restant sur la durée totale de la croisière
* le nombre de passagers
* le nombre d’employés

Une marge de sécurité est ajoutée afin d’éviter toute pénurie d’oxygène durant le voyage.

---

## Calcul de l'oxygène requis

### 1. Date de calcul

* Le calcul est effectué à partir d’une date donnée (`calculationDateTime`).
* Cette date doit être durant la durée de la croisière:
  * postérieure ou égale à la date de départ de la croisière ;
  * antérieure ou égale à la date de fin de la croisière.
* La durée utilisée pour le calcul correspond à la durée restante entre `calculationDateTime` et la fin de la croisière.

### 2. Consommation d’oxygène par minute

La consommation d’oxygène varie selon le type de personne à bord :

#### Passagers

* `CHILD` : 150 o2 / minute
* `ADULT` : 250 o2 / minute
* `SENIOR` : 300 o2 / minute

#### Employés

* Chaque employé consomme 250 o2 / minute


### 3. Calcul de base

#### Standard: S'il reste moins de 75% du temps restant à la croisière:
Le calcul de l’oxygène requis est le suivant :

```
(durée restante en minutes × consommation totale par minute)
```

où :

```
consommation totale par minute = (somme des consommations par minute de tous les passagers) + (somme des consommations par minute de tous les employés)
```

#### Surconsommation: S'il reste 75% ou plus du temps restant à la croisière:
Il faut appliquer une surconsommation préventive:
- La consommation par minute des passagers (`Traveler`) est augmentée de **5 %**.
  - Ne s'applique PAS aux employés.
- On applique une marge de sécurité de **10 %** sur le total.

```
(durée restante en minutes × consommation totale par minute majorée) × (100% + marge de sécurité)
```

---

## Autres conditions

* Le résultat final doit être **arrondi à l’entier supérieur**.
* La marge de sécurité et la majoration des consommations présentés plus haut sont les données de la croisière JUPITER_MOONS_EXPLORATION_2085.
  Ces valeurs pourraient être différentes pour les autres croisières.
* Les taux de consommations d'oxygène selon le type de passager et les employés est le même pour toutes les croisières.
* Il devrait être facile de modifier toutes les constantes **sans modifier l'implémentation**.

## Exemple

Croisière :

* Départ : `2085-01-01T00:00`
* Fin : `2085-01-03T04:00`
* Durée totale: 3120 minutes

Passagers :

* 2 `ADULT`
* 1 `CHILD`
* 1 `SENIOR`

Employés :

* 3 employés

### Calcul Standard: < 75% du temps de croisière
Date de calcul :

* `2085-01-02T00:00` → **28h restantes = 1680 minutes**
* Durée restantes/Durée totale de la croisière = 1680 min /3120 min = **53,8% < 75%**

### Consommation par minute

* Passagers :

  * 2 `ADULT` × 250 = 500
  * 1 `CHILD` × 150 = 150
  * 1 `SENIOR` × 300 = 300
    → **950 o2 / minute**
* Employés :

  * 3 × 250 = 750 o2 / minute

**Consommation totale par minute** = 1700 o2

**Résultat =** `(1680 minutes × 1700 o2/min) = 2 856 000 o2`

### Calcul Surconsommation: >= 75% du temps de croisière
Date de calcul :

* `2085-01-01T04:00` → **48h restantes = 2880 minutes**
* Durée restante/Durée totale de la croisière = 2880 min /3120 min = **92,3% >= 75%**

### Consommation par minute

* Passagers :

  * 2 `ADULT` × 250 **x 1.05** = 525 o2 / minute
  * 1 `CHILD` × 150 **x 1.05** = 157,5 o2 / minute
  * 1 `SENIOR` × 300 **x 1.05**= 315 o2 / minute
  
* Employés :

  * 3 × 250 = 750 o2 / minute

**Consommation totale par minute** = 1747,5 o2

**Résultat =** `(2880 minutes × 1747,5 o2/min) x 110% = 5 536 080 o2`

---

## Route API à ajouter

### GET /cruises/{cruiseId}/oxygen?calculationDateTime=2085-01-31T08:00

#### Query parameter

`calculationDateTime` (obligatoire, format ISO)

## Réponses

### Succès

HTTP 200 OK

```
{
    "requiredOxygen": 3141600::int
}
```

### Si la date de calcul est absente ou vide:

HTTP 400 BAD REQUEST

```
{
    "error": "MISSING_PARAMETER",
    "description": "Missing parameter: calculationDateTime"
}
```

### Si la date de calcul n'est pas dans le bon format:

HTTP 400 BAD REQUEST

```
{
    "error": "INVALID_DATE_FORMAT",
    "description": "Invalid date format"
}
```


### Si la date de calcul est hors de la croisière

HTTP 400 BAD REQUEST

```
{
    "error": "OXYGEN_CALCULATION_DATE_OUTSIDE_CRUISE",
    "description": "Calculation date must be within the cruise timeframe"
}
```


### Si la croisière n’existe pas

HTTP 404 NOT FOUND

```
{
    "error": "CRUISE_NOT_FOUND",
    "description": "Cruise not found"
}
```
