# LinePerm — console SQLite

Depuis PowerShell, à la racine du projet (JDK 17 requis) :

```powershell
.\run.ps1
```

Le script recompile les sources avant de lancer la console. Les anciens JAR/classes
présents dans `target/` et `classes/` ne sont pas utilisés.

- `audit.db` contient les comptes, les métadonnées des fichiers, les permissions et les logs.
- Le contenu des fichiers reste dans `data/`.
- Chaque modification est enregistrée immédiatement ; `exit` n'est pas nécessaire pour sauvegarder.
- `stats` lit les logs SQLite, y compris les refus et les fichiers supprimés.
- Au premier accès, les tables sont créées si nécessaire. L'ancien schéma SQLite des logs
  est migré dans une transaction en conservant les identifiants et l'historique.
- Les anciens fichiers texte `data/users.db`, `data/fichiers.db` et
  `src/main/resources/acces.log` ne sont plus utilisés et ne sont pas importés automatiquement.
  Pour tester un nouveau compte, utiliser `signup`, puis `login`.

Exemple : `touch test.txt`, `write test.txt bonjour`, `cat test.txt`,
`chmod test.txt other r true`, puis `stats`. Quitter et se reconnecter permet
 de vérifier la persistance. `rm test.txt` supprime le fichier tout en conservant ses logs.

Tests (bases et fichiers temporaires, sans modifier votre `audit.db`) :

```powershell
.\run.ps1 -Test
```

Les propriétés Java `lineperm.db` et `lineperm.data` permettent de choisir d'autres
emplacements pour la base et le contenu des fichiers.
