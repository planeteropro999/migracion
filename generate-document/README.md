# **Proyecto MS generate-document**

Servicio de gestión de archivo. Este servicio interactúa con el proyecto de micro gateway por medio de plantillas XSLT.

## **Empezando**

_Instrucciones y comandos para ejecutar una copia del proyecto en un ambiente local o desarrollo_

### **Descarga por primera vez**

`git clone -b development http://10.225.13.14:7071/m2m-autogestion/generate-document.git`

### **Proyecto ya existente**

`git pull -b development http://10.225.13.14:7071/m2m-autogestion/document-manager.git`

### **Configuración Inicial**

#### **Requisitos**

* **docker** ya instalado
* **docker-compose** ya instalado
* Imagen de java "**openjdk:8-jre-slim**" ya instalada o acceso a internet a docker hub para la descarga.
* El usuario de sistema operativo que ejecute la tarea debe tener el acceso para poder crear directorios en caso de necesitarlo.
* Se instalarán los artefactos en las rutas estándares de claro para artefactos docker.
* En caso de OpenShift, conocimientos de despliegue y disponibilidad de infraestructura y administración.

### **Dependencias**

* Debe estar disponible el puerto **8400** o cambiarse en la debida configuración.
* En caso de **OpenShift** debe estar creado el proyecto donde estará el artefacto.
* En caso de **OpenShift** el host debe estar definido.

### **Estructura de despliegue**

Para un correcto despliegue de los artefactos se debe tener en cuenta los siguientes lineamientos:

* Toda la información de despliegue debe encontrarse dentro del directorio '**deploy**' en la raíz de este repositorio.
* Dentro de la carpeta deploy deben de existir dos directorios '**docker**' y '**k8s**'.
* Para OpenShift es necesario que las imágenes de docker generadas existan en un docker registry que tenga acceso OpenShift.
* Para OpenShift es necesario que exista una propiedad en el pom llamada **openshiftProjectName** que contendrá el nombre del proyecto de OpenShift.

#### OpenShift

Dentro del directorio de OpenShift, el cual se denomina '**k8s**', deben estar los siguientes archivos:

* deploymentConfig.yaml
  * Contiene información sobre el despliegue, tales como réplicas, memoria RAM, CPU, ConfigMap. Hace conjunción con el nombre de **persistentVolumeClaim.yaml**.
* persistentVolumeClaim.yaml
  * Contiene información de volumen, está relacionado con los logs. Este volumen debe ser lectura y escritura; tener en cuenta que muchos pods pueden escribir en este mismo volumen.
* route.yaml
  * Contiene información de la URL donde se desplegará el artefacto. En OpenShift, esta URL debe existir y tener el permiso necesario para usar hosts personalizados. Hace conjunción con el nombre de **service.yaml**.
* service.yaml
  * Contiene información del servicio desplegado. Hace conjunción con el nombre de **deploymentConfig.yaml**.

#### **Docker**

Dentro del directorio de Docker, el cual se denomina '**docker**', deben estar los siguientes archivos:

* Dockerfile
  * Contiene información sobre la generación de la imagen de Docker.
* docker-compose.yml
  * Contiene información sobre el despliegue de los artefactos usando **docker-compose**.

### **Build**

_Si el proyecto necesita algunos pasos adicionales para que el desarrollador compile después de algunos cambios de código se deben indicar aquí_

## **Características**

* Existen tareas que se detallarán a futuro.

## **Construido con**

* [Docker](https://www.docker.com/) - Capa adicional de abstracción y automatización de virtualización de aplicaciones en múltiples sistemas operativos
* [Spring Boot](https://spring.io/projects/spring-boot) - Framework para el desarrollo de aplicaciones y contenedor de inversión de control, de código abierto para la plataforma Java
* [Java](https://www.java.com) - La plataforma Java es el nombre de un entorno o plataforma de computación originaria de Sun Microsystems, capaz de ejecutar aplicaciones desarrolladas usando el lenguaje de programación Java u otros lenguajes que compilen a bytecode y un conjunto de herramientas de desarrollo
* [OpenShift](https://www.openshift.com/) - OpenShift, formalmente llamado OpenShift Container Platform (OCP), es un producto de computación en la nube de plataforma como servicio de Red Hat.

## **Documentación de la API**

La documentación de la API está disponible en [Swagger](http://enterprise-m2m-generate-document.router-default.apps.aro-dev.conecel.com/enterprise/m2m-autogestion/v2/swagger-ui/).

## **Autores**

_Listar a las personas que han trabajado en este proyecto junto con sus datos de contacto_

* **Robert Macías** - *Desarrollador* - [robert.macias@gizlocorp.com]
* **Wilson Quinto** - *Desarrollador* - [wilson.quinto@gizlocorp.com]
* **Luis Vargas** - *Desarrollador* - [luis.vargas@gizlocorp.com]
* **Luis Lascano** - *Desarrollador* - [luis.lascano@gizlocorp.com]
