

# Introducción #

Para poder compilar los códigos fuentes de este proyecto es necesario tener configurada la librería HDF para Java (conocida como HDF Object Package) en el IDE elegido, en concreto, debemos poder usar las clases correspondientes al formato HDF4. En nuestro caso usaremos NetBeans como referencia, pero la forma de hacerlo en Eclipse es muy parecida.

# Configuración de HDF Object Package #

Una vez descargada y descomprimida la librería, lo primero que tenemos que hacer es configurar el CLASSPATH y el Library Path tal y como se explica en el [tutorial de HDF](http://www.hdfgroup.org/hdf-java-html/hdf-object/use.html).

Tras esto, añadimos una librería nueva en NetBeans. Para ello, dentro del menú _**Tools**_ seleccionamos la opción _**Libraries**_. Dentro de la ventana que se abre, hacemos clic en _**New Library...**_ y le ponemos el nombre _HdfJava\_2.6.1_ (o el de la versión correspondiente, aunque esto no es trascendental). Una vez creada la librería, en la pestaña _**Classpath**_, le damos a _**Add JAR/Folder**_, buscamos la ruta donde hemos descomprimido la librería y seleccionamos los siguientes ficheros dentro del subdirectorio _lib_, con el fin de poder utilizar HDF4 en nuestro proyecto:
  * jhdf.jar
  * jhdf4obj.jar
  * jhdfobj.jar

Con esto ya podemos utilizar las clases Java correspondientes a HDF4 en nuestro IDE.

## Incompatibilidad con Windows x64 ##

Si estamos programando en Windows x64, hay que tener en cuenta que la librería HDF sólo funciona con una VM de Java de 32 bits (con Java de 64 bits da errores). Esto es debido a que en Windows la librería utiliza una DLL que está compilada para 32 bits. De momento no hay soporte para Windows x64. En Linux x64 no debería haber problemas, aunque no lo hemos probado. Si alguien está dispuesto y tiene tiempo de recompilar los fuentes de la DLL para Windows x64, que avise :-)