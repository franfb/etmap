

# Introducción #

El IDE elegido para este proyecto ha sido NetBeans 6.8. En esta página se describen los pasos necesarios para que el código fuente del proyecto pueda ser compilado adecuadamente por NetBeans 6.8, utilizando JDK 1.6 y HDF Object Package 2.6.1.

# Componentes necesarios #

Para compilar adecuadamente los códigos fuente del proyecto necesitamos descargar e instalar los siguientes componentes:
  * **JDK 1.6**
  * **NetBeans 6.8**
  * **HDF Object Package 2.6.1**
  * **Cliente subversion** para línea de comandos (se explica más adelante)

Si estás utilizando Windows x64, tienes que utilizar JDK 1.6 de 32 bits, ya que de lo contrario la librería HDF Object Package no funcionará adecuadamente. Para obtener más información sobre la instalación de HDF y sobre este problema visita [Instalación de HDF Object Package](HdfJavaInstallation.md).

Nota: puede que el código fuente compile correctamente con versiones anteriores de los componentes utilizados, pero no lo hemos comprobado.

## Checkout del proyecto ##

Una vez descargado e instalado todo lo anterior, bastará con hacer un _**checkout**_ del proyecto en el repositorio subversion de este sitio mediante NetBeans. Pero primero, antes de poder utilizar subversion desde NetBeans necesitamos tener instalado un cliente subversion para línea de comandos. Los desarrolladores de NetBeans recomiendan el de [CollabNet en su versión 1.5.x](http://www.open.collab.net/downloads/netbeans/). Éste es el que estamos usando nosotros, pero como hay que registrarse para descargarlo (aunque sea gratis) se puede utilizar cualquier otro, no debería haber ningún problema.

Tras instalar el cliente subversion, procedemos a hacer un checkout del proyecto. Para ello, vamos al menú _Team_ de NetBeans y seleccionamos la opción _Checkout..._ dentro del submenú _Subversion_. Una vez ahí, ponemos la dirección del repositorio:

> https://etmap.googlecode.com/svn/trunk/etmap

También debemos poner nuestro nombre de usuario y contraseña de google code (si estamos autorizados para hacer commits). Le damos a _Next_ y configuramos la ruta local de la copia de trabajo. Si hay alguna duda haciendo esto, podemos consultar [este tutorial](https://docs.google.com/fileview?id=0B-1TFEifNggmY2RjNzM0NTUtNzYzMi00ZjYyLTk1MGQtMWVjNWY0ODRmZTcy&hl=es) en la sección _**Acceder a un repositorio existente**_.

Ahora ya tendremos un proyecto NetBeans creado con el código fuente de _etmap_.