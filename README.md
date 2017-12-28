# LinkUs
LinkUs es una especie de red social entre profesores y alumnos.
Originalmente iba a disponer de los siguientes apartados:
- Una lista de difusión
- Un foro
- Un calendario con eventos
- Un apartado en el que consultar las notas disponibles

Al final solo he implementado la lista de difusión y el foro.

Para que funcione hay que levantar un servidor y su base de datos.
Puede usarse Xampp por ejemlo, y añadir la base de datos importando el script necesario,
que está en "documentos/scriptDB.sql".
Lo que sigue es ejecutar el .jar adjuntado, o compilar el proyecto (servidorProyecto).
Para la aplicación hay que editar el archivo "ManejadorConexion.java" y cambiar la constante "DOMINIO" 
por la ip de donde esté arrancado el servidor, si es en local la ip de la maquina en la que esté, o si disponemos
de un dominio o un servidor remoto, podemos poner el nombre de este, por ejemplo: "www.prueba.com".

<img src="https://user-images.githubusercontent.com/25131320/34417671-f9d3bbfc-ebf9-11e7-99e9-9e4416cd552a.png" width="250" height="400"/> <img src="https://user-images.githubusercontent.com/25131320/34417702-1752e43c-ebfa-11e7-8940-dfa84ca3d5d9.png" width="250" height="400"/></br></br>
<img src="https://user-images.githubusercontent.com/25131320/34417676-035f233c-ebfa-11e7-8cfc-684dce41d818.png" width="250" height="400"/> <img src="https://user-images.githubusercontent.com/25131320/34417687-0cb46168-ebfa-11e7-877d-382e4f4ee99d.png" width="250" height="400"/>


#### License: GPL v3
#### Author:
- Youssef El Faqir El Rhazoui
