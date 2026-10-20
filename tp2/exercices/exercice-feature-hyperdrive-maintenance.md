# Nouvelle fonctionnalité : Planifier une maintenance de module d’hyperpropulsion

Vous devez implémenter cette nouvelle fonctionnalité dans l'application USpace.

1. Implémentez tous les comportements de la fonctionnalité.
2. Assurez-vous de respecter les Principes SOLID+T et les bonnes pratiques de programmation.
3. Implémentez les tests unitaires pour cette fonctionnalité. Nous vous recommandons le TDD.

## Questions à répondre (Seulement pour IFT-6006)

Répondez aux questions dans l'espace prévu dans le [fichier de questions](../questions/questions-feature-hyperdrive-maintenance.md).

## Détails de la fonctionnalité

Le gestionnaire de croisières souhaite pouvoir **planifier des maintenances** pour les modules d’hyperpropulsion d’une croisière.

Une maintenance permet de garantir la sécurité et la performance des modules durant la croisière.

Une maintenance est composée de :

* l’identifiant du module d’hyperpropulsion concerné
* une durée (en heures)
* une liste d’identifiants des employés qui effectueront la maintenance

### Conditions

* Le module d’hyperpropulsion doit exister dans la croisière.
* La durée de la maintenance doit être supérieure à 0.
* Au moins un employé est requis.
* Tous les identifiants des employés fournis doivent faire partis de la croisière.
* Il faut au minimum 1 heure pour chaque niveau de puissance d'un module.
  * Si un module à un niveau de puissance de 10, il faut que la maintenance qu'on y ajoute ait une durée d'au moins 10h.

## Route API à ajouter

POST /cruises/{cruiseId}/hyperdrive-modules/{moduleId}/maintenances

```
{
    "duration": 3::int, // en heures
    "employeeIds": [
        "ABC123"::string,
        "ZYW321"::string,
        ...
    ]
}

```

## Réponses

### Succès

HTTP 201 CREATED

### Si un champ est manquant ou vide

HTTP 400 BAD REQUEST

```
{
    "error": "MISSING_PARAMETER",
    "description": "Missing parameter: <parameterName>"
}
```

(`<parameterName>` est le nom du champ manquant : `duration` ou `employeeIds`)
_NOTE: un int est considéré absent s'il est égal à 0 dans la requête._

### Si la durée est inférieure à 0

HTTP 400 BAD REQUEST

```
{
    "error": "INVALID_PARAMETER",
    "description": "Maintenance duration must be greater than 0"
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

### Si un ou plusieurs employés ne font pas partie de la croisière

HTTP 404 NOT FOUND

```
{
    "error": "EMPLOYEE_NOT_FOUND",
    "description": "One or more employees are not part of the cruise"
}
```

### Si le module d’hyperpropulsion n’existe pas dans la croisière

HTTP 404 NOT FOUND

```
{
    "error": "HYPERDRIVE_MODULE_NOT_FOUND",
    "description": "Hyperdrive module not found in cruise"
}
```

### Si une maintenance a une durée plus petite que le niveau de puissance du module

HTTP 400 BAD REQUEST

```
{
    "error": "INVALID_MAINTENANCE_DURATION",
    "description": "Maintenance must have a duration greater or equal to the module power level"
}
```

## Route API à modifier

Ajouter `"maintenances"` dans la réponse de la route `GET /cruises/{cruiseId}`.
Plus de détails sur la route _originale_ dans le fichier [README](../../README.md).
### GET /cruises/{cruiseId}

```
{
    "id": "JUPITER_MOONS_EXPLORATION_2085"::string,
    "departureDateTime": "2085-01-25T12:00"::string(dateTime, format ISO),
    "endDateTime": "2085-02-01T12:00"::string(dateTime, format ISO),
    "hyperdriveModules": [
        {
            "id": "HY-77-V"::string,
            "powerLevel": 85::number,
            "activationDateTime": "2085-01-26T01:00"::string(dateTime, format ISO),
            "deactivationDateTime": "2085-01-28T01:00"::string(dateTime, format ISO),
            "maintenances": [
                {
                    "duration": 3::int,
                    "employeeIds": [
                        "ABB328",
                        "ABC128",
                        ...
                    ]
                },
                ...
            ]
        },
        ...
    ],
    "crewMembersIds": [
        "ABB328"::string,
        "ABC128"::string,
        ...
    ]
}
```

### Contraintes importantes

* Vous **ne devez pas modifier la forme de la réponse** de la route `GET /cruises/{cruiseId}` en dehors de l’ajout du champ `"maintenances"`.

