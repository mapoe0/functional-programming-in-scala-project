
# Project de formation JUNIA HEI - 2022

Description du project

# Introduction
L’objectif est de construire une interface entre un modèle de stockage de
données et votre machine. 

Les données seront fournies sur des fichiers et votre tâche sera d’être
en mesure de lire, ajouter, et supprimer des données en respectant les contraintes de ce modèle.
Ce modèle est divisé en secteur qui contiennent chacun un octet/byte (8 bits). Le modèle cherche
à gagner en rapidité d’accès en mettant bout à bout les différentes données et en ayant une table
d’adressage afin de pouvoir retrouver quelles données correspondent à quels secteurs.
Les données seront présentées dans un fichier qui contiens 50 lignes. Chaque ligne est la succession de 20 valeurs entre 0 et 255 (pour un total de 1000 valeurs en tout), chaque valeur étant
séparée de la suivante par une virgule, y compris en fin de ligne.
Les valeurs se divisent en deux grandes sections : 800 valeurs qui correspondent aux données
et 200 qui correspondent à leur adressage. Leur format de l’adressage est le suivant : Numéro
de la donnée, Taille de la donnée
Exemple sur 30 secteurs (dont 6 d’adressage) :

104,101,108,108,111,102,  
111,110,099,116,105,111,  
110,110,101,108,0,0  
0,0,0,0,0,0  
3,5,14,11,0,0


Dans cet exemple, la ligne d’adressage est : "3,5,14,11,0,0".

Cela signifie que la première information est celle qui porte le nom "3" et qui est de taille 5, puis l’information "14" de taille 11. On
obtiens donc le découpage suivant :

Information 3 (taille 5)
104,101,108,108,111 (’h’,’e’,’l’, ’l’, ’o’ en ascii)
Information 14 (taille 11)
102,111,110,099,116,105,111,110,110,101,108

Il reste également 8 emplacements mémoire libres ainsi qu’une position dans la table d’adressage. On pourrait donc ajouter, par exemple, 
"hei5iti".On nommera cette information "1", et l’encodage ascii donne :

104,101,105,53,105,116,105 (taille 7).

On obtiens donc, après écriture :

104,101,108,108,111,102,  
111,110,99,116,105,111,  
110,110,101,108,104,101,  
105,53,105,116,105,0  
3,5,14,11,1,7  
L’objet de ce devoir maison est de concevoir un programme capable d’interfacer ce système de
données avec un utilisateur.

# Partie 0 : Interface

L’ensemble des interactions avec l’utilisateur se feront au travers de la console. A l’aide de la
fonction scala.io.StdIn.readLine() (ou readInt(), readBoolean() pour d’autres types de valeurs), il
est possible de permettre à l’utilisateur d’entrer des informations depuis la console d’execution.
En utilisant println() pour proposer différents choix, il est alors possible de créé une interface
simple qui ne met en jeu que les outils basiques et fonctionnels du scala.
Au démarrage du programme, voilà les choix proposés à l’utilisateur :
1. Lecture
2. Ajout
3. Suppression
4. A propos
5. Quitter
#
Les options 1, 2 et 3 sont décrites dans le reste du sujet. L’option 4 doit afficher un texte contenant
le numéro de version de votre outil, l’auteur du code, ainsi que la liste des documentations et
sites qui vous ont aidé à compléter votre projet.
L’option 5, de manière logique, quitte le programme.

# Partie 1 : Lecture
Afin de pouvoir lire les données, il faut tout d’abord les lire depuis le fichier fourni par l’énnoncé. La méthode de lecture n’est pas imposée, mais il est recommandé d’utiliser une méthode
telle que :
Exemple

`import scala . io.Source. _`

`val source = fromFile("file.txt")`

`val lines = try source . getLines finally source.close()`

ou équivalent (pensez tout particulièrement à bien refermer l’accès à vos fichier avec file.close()
une fois la lecture effectuée!).

Une fois cette lecture faite, les données doivent ensuite être stoquées dans un objet de type
’cassette’ qui dois contenir au moins une liste de 800 int "donnees" et une liste de 200 int "table".

A partir de cet objet nouvellement créé, afficher l’ensemble des données à l’utilisateur, avec
pour chaque information son numéro, sa taille, sa représentation numérique et sa représentation alphabétique.

# Partie 2 : Ajout
L’option 2 du menu, Ajout, propose à l’utilisateur d’insérer de nouvelles informations au bloc
de donnés.

Tout d’abord, il est demandé à l’utilisateur de renseigner un nom pour l’information à stocker,
puis d’écrire cette information en toutes lettres. Ensuite, le programme doit effectuer les tests
suivants :

* S’assurer que l’information n’existe pas déjà et que son nom ne soit pas ’0’

* S’assurer qu’il reste suffisamment de place dans le bloc de données partie ’donnees’

* S’assurer qu’il reste suffisamment de place dans le bloc de données partie ’table’

Si un de ces points n’est pas valide, le programme doit le signaler à l’utilisateur et revenir au
menu. Si toues les conditions sont validées, une dernière confirmation, rappelant le nom et le
contenu de l’information, est demandée à l’utilisateur avant écriture.

L’écriture de ces données doit être refletée à la fois dans les données stockées dans le programme mais également dans le fichier qui sert de source d’informations.
# Partie 3 : Suppression

La dernière option du menu permet de supprimer un élément du bloc de données.
Contrairement à la procédure d’écriture, l’utilisateur renseigne un nom d’information, mais aucun
autre élément. Le programme vérifie alors que l’information existe effectivement. Si elle n’existe
pas, l’utilisateur est informé et est reconduit au menu principal.
Si l’information existe bien, elle est présentée à l’utilisateur pour vérification (nom et contenu)
qui doit valider la suppression.

La suppression est une opération plus complexe que l’addition : il faut effectuer une défragmentation après retrait d’une information, c’est à dire s’assurer que les informations restantes
sont connexes dans le medium de l’information afin de ne pas perdre de place.
Pour ce faire, il faudra donc décaler les informations restantes de la cassette pour combler la
place nouvellement libérée.
Exemple de suppression de l’info "14" :

avant :

104,101,108,108,111,102,   
111,110,99,116,105,111,  
110,110,101,108,104,101,  
105,53,105,116,105,0  
3,5,14,11,1,7  

après:

104,101,108,108,111,104,  
101,105,53,105,116,105,  
0,0,0,0,0,0,  
0,0,0,0,0,0,  
3,5,1,7,0,0  

Tout comme pour l’écriture, cette opération doit être effectuée dans le programme puis dans le
fichier (il est recommandé d’envisager de réécrire le fichier entier après avoir effectué la procédure dans le programme!)
