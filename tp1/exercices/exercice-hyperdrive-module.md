# Ajouter un module d'hyperpropulsion à la croisière spatiale

## 1. Implémenter la nouvelle fonctionnalité et les tests unitaires

1. Ajouter les tests unitaires pertinents. Nous recommandons d'utiliser le TDD.
2. Assurez-vous de respecter les bonnes pratiques vues en classe.

Implémenter la fonctionnalité selon les comportements décrits plus bas. Le guide [Créer une fonctionnalité complète](../astuces/astuce-creer-une-fonctionnalite.md) peut vous aider dans votre démarche.

### IMPORTANT: 
Assurez-vous que la route `GET cruises/{cruiseId}` reste conforme à sa description dans [le README du projet](../../README.md).
**Vous ne devez pas modifier la forme de la réponse, car elle sera utilisée ainsi pour la correction.**

### Détails de la fonctionnalité

Le gestionnaire de croisières veut pouvoir ajouter un module d’hyperpropulsion au vaisseau d’une croisière.
Ce module améliore la vitesse de saut hyperspatial. Ajouter un module permettra d'aller plus loin, plus rapidement !

Chaque module possède :

- un identifiant
- un niveau de puissance
- un temps d'activation (en jours)
- une date d’activation prévue 

L'activation se fait toujours à 1h am à la date donnée (01:00).
Le module est désactivé après le temps d'activation prévu à 1h am.

_L'heure d'activation est normalisée entre les croisières et sera donc toujours la même pour toutes les croisières qui existeront dans le futur._

La classe `HyperdriveModule` existe déjà dans l’application.

#### Conditions à respecter:
* L’identifiant du module doit être unique dans la croisière.
* L’identifiant du module doit correspondre au format `HY-{un chiffre entre 1 et 999}-{une lettre}` (Exemple: HY-321-Z)
  * Les lettres doivent être majuscules
* Le niveau de puissance doit être un entier plus grand que 0.
* Le temps d'activation doit être un entier plus grand que 0.
* Le module doit être considéré comme stable (validation externe: Voir le point 2 de l'exercice).
* L'activation doit être durant la durée de la croisière:
  * L'activation (à 1h am) doit être après la date de départ de la croisière.
  * La désactivation (à 1h am) doit être avant la date de fin de la croisière.
* Il ne peut y avoir qu’un seul module actif à la fois :
  * L'activation d'un nouveau module doit être au moment ou après la désactivation du module précédent.
  * La désactivation d'un nouveau module doit être au moment ou avant l'activation du module suivant.

**Exemple:**

1. On ajoute le module HY-200-A avec la date d'activation 2085-01-26 et le temps d'activation 2 jours.
   * Activation: 2085-01-26T01:00
   * Désactivation 2 jours plus tard: 2085-01-28T01:00
2. On veut ajouter le module HY-1-A avec la date d'activation 2085-01-27 et le temps d'activation 3 jours.
    * L'activation serait 2085-01-27T01:00. **On ne peut pas l'ajouter, car HY-200-A est déjà activé à ce moment**
2. On veut ajouter le module HY-601-C avec la date d'activation 2085-01-24 et le temps d'activation 3 jours.
    * La désactivation serait 2085-01-27T01:00. **On ne peut pas l'ajouter, car HY-200-A est déjà activé à ce moment**
3. On veut ajouter le module HY-25-M avec la date d'activation 2085-01-28 et le temps d'activation 1 jour.
    * L'activation serait 2085-01-28T01:00. **On peut l'ajouter, car HY-200-A se désactive au même moment**
    * Désactivation 1 jour plus tard: 2085-01-29T01:00



#### Détails de la route API

POST /cruises/{cruiseId}/hyperdrive-modules

```
{
    "id": "HY-77-V"::string,
    "powerLevel": 85::number,
    "activationDays": 2::number,
    "activationDate": "2085-01-26"::string(date, yyyy-MM-dd)
}
```

Réponses:

HTTP 201 CREATED

Si un des champs est manquant:

HTTP 400 BAD REQUEST

```
{
    "error": "MISSING_PARAMETER",
    "description": "Missing parameter: <parameterName>"
}
```
(`<parameterName>` est le nom du champ manquant: id, powerLevel, activationDays, activationDate)
_NOTE: un int est considéré absent s'il est égal à 0 dans la requête._

Si le niveau de puissance est négatif (< 0):

HTTP 400 BAD REQUEST

```
{
    "error": "INVALID_PARAMETER",
    "description": "Power level must be a positive number"
}
```

Si le temps d'activation est négatif (< 0):

HTTP 400 BAD REQUEST

```
{
    "error": "INVALID_PARAMETER",
    "description": "Activation days must be a positive number"
}
```

Si la croisière n'existe pas:

HTTP 404 NOT FOUND

```
{
"error": "CRUISE_NOT_FOUND",
"description": "Cruise not found"
}
```

Si l'id n'est pas dans le bon format:

HTTP 400 BAD REQUEST

```
{
    "error": "INVALID_HYPERDRIVE_ID_FORMAT",
    "description": "Invalid hyperdrive module id format"
}
```

Si la date d'activation n'est pas dans le bon format (yyyy-MM-dd):

HTTP 400 BAD REQUEST

```
{
    "error": "INVALID_DATE_FORMAT",
    "description": "Invalid date format"
}
```

Si le module existe déjà pour la croisière:

HTTP 400 BAD REQUEST

```
{
    "error": "MODULE_ALREADY_EXISTS",
    "description": "Hyperdrive module already exists in the cruise"
}
```

Si l'activation ou la désactivation n'est pas durant la croisière:

HTTP 400 BAD REQUEST

```
{
    "error": "INVALID_ACTIVATION_DATE",
    "description": "Activation date or deactivation date is outside the cruise timeframe"
}
```

Si un autre module est déjà actif à cette date:

HTTP 400 BAD REQUEST

```
{
    "error": "MODULE_CONFLICT",
    "description": "Another hyperdrive module is already active at this time"
}

```

## 2. Utilisation d'un système externe pour valider la stabilité des modules d'hyperpropulsion

USpace a décidé d'utiliser un système externe pour valider la stabilité des modules d'hyperpropulsion.
`StandardHyperdriveStabilitySystem` est le système de validation actuellement utilisé.

Ce système va valider tous les modules sauf: `HY-222-Z` qui ne respecte pas les normes actuelles d'hyperpropulsion.

1. Intégrer `StandardHyperdriveStabilitySystem` dans la fonctionnalité d'ajout d'une planète à l'itinéraire.
   **Assurez-vous que le système de validation pourra être remplacé facilement par un autre en vous basant sur les concepts vus en classe.**

2. Si le système de validation externe ne valide pas le module, retourner la réponse ci-dessous:

HTTP 400 BAD REQUEST

```
{
    "error": "INVALID_HYPERDRIVE_MODULE",
    "description": "Hyperdrive module invalid"
}
```

3. N'oubliez pas vos tests unitaires pour l'intégration dans la fonctionnalité!
(Il n'est pas nécessaire de tester le service externe. En effet, il est "externe" et donc pas de votre responsabilité).