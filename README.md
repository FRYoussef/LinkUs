# LinkUs
LinkUs es una especie de red social entre profesores y alumnos.
Originalmente iba a disponer de los siguientes apartados:
- Una lista de difusión
- Un foro
- Un calendario con eventos
- Un apartado en el que consultar las notas disponibles

Al final solo he implementado la lista de difusión y el foro.

Para que funcione hay que levantar un servidor y su base de datos. Puede usarse Xampp por ejemlo 
y añadir la base de datos importando el script necesario (está en documentos/scriptDB.sql).
Lo que sigue es ejecutar el .jar adjuntado, o compilar el proyecto (servidorProyecto).
Con la aplicación hay que editar el archivo "ManejadorConexion.java" y cambiar la constante "DOMINIO" 
por la ip de donde esté arrancado el servidor, si es en local la ip de la maquina en la que esté, si disponemos
de un dominio o un servidor remoto, podemos poner el nombre de este, por ejemplo: "www.prueba.com".

#### License: GPL v3
#### Author:
- Youssef El Faqir El Rhazoui
