
# Book TP FINAL

## Description du Projet

Le projet "Book TP FINAL" est une application JavaFX qui permet de gérer une bibliothèque de livres. L'application offre la possibilité d'ajouter des livres manuellement ou par ISBN, et d'effectuer des modifications ou suppressions via une interface utilisateur intuitive.

## Prérequis

- Java JDK (version recommandée : 11 ou plus)
- Maven pour la gestion des dépendances

## Installation et démarrage

1. **Cloner le dépôt :**
   ```bash
   git clone git@github.com:adam-labbane/gestionnaire-de-livres.git
   cd gestionnaire-de-livres
   ```

2. **Compiler le projet :**
   ```bash
   mvn clean install
   ```

3. **Lancer l'application :**
   ```bash
   mvn javafx:run
   ```

## Utilisation

### Ajouter un livre

- **Manuellement :** Accédez à l'interface d'ajout et entrez les détails du livre manuellement.
- **Par ISBN :** Utilisez la barre de recherche située en bas à gauche de l'écran pour entrer l'ISBN. Si l'ISBN est valide, les détails du livre seront récupérés via l'API [OpenLibrary](https://openlibrary.org/) et le livre sera ajouté automatiquement à votre collection.

### ISBN de test

Voici quelques ISBN que vous pouvez utiliser pour tester :

- 1524715824
- 9782075134040
- 2070541274
- OL25312237M
- 2246702615

Si vous souhaitez tester davantage, vous pouvez rechercher des livres sur le site [AbeBooks](https://www.abebooks.fr/) et tester avec l'ISBN 10.

### Éditer ou supprimer un livre

- Sélectionnez un livre dans l'interface. Des options pour modifier ou supprimer le livre seront disponibles.

### Temps de chargement

Le temps de chargement peut varier, notamment lors de la récupération des images des livres par l'API OpenLibrary. Veuillez patienter durant ces opérations.

## Notifications par email

Pour chaque action (ajout, édition, suppression), un email de notification est envoyé au destinataire configuré dans le fichier `config.properties`.

## Configuration

Assurez-vous de configurer le fichier `config.properties` selon vos besoins pour les notifications par email et autres paramètres spécifiques.

## Contribution

Les contributions au projet sont les bienvenues. Veuillez suivre les procédures habituelles pour contribuer via des pull requests.

## Licence

[Inclure ici des informations sur la licence, si applicable]

## Contact

Pour plus d'informations ou assistance, veuillez contacter [Nom du Développeur] à [Email].

