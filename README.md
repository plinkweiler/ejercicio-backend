# ejercicio-backend
Ejercicio backend api rest

# Para ejecutar en Tomcat
Utilizar el war ejercicio-backend.war, utilice tomcat 9

https://tomcat.apache.org/download-90.cgi

ingresando en cualquier navegar la url http://localhost:8080/ejercicio-backend/globalWarming/getUserInfo/{username} se obtiene la info pedida, por ejemplo http://localhost:8080/ejercicio-backend/globalWarming/getUserInfo/carlos.

Y con la siguiente url los datos que estan en memoria http://localhost:8080/ejercicio-backend/globalWarming/getSaveInfo

# Pasos para la instalación y ejecución de la API

1 - Descargar el proyecto

2 - Tener instalado Maven, se puede descargar del siguiente link https://maven.apache.org/download.cgi
    Y las instrucciones para instalarlo https://maven.apache.org/install.html
    
3 - Luego de instalar maven hay que ingresar a la consola, ubicarnos en la carpeta donde esta el proyecto y 
    ejecutar el comando: mvn spring-boot:run 
    
4 - ingresando en cualquier navegar la url http://localhost:8080/globalWarming/getUserInfo/{username} se obtiene la info pedida.
    No tengo una base de datos local, pero si voy guardando en memoria los datos que se van obteniendo, para ver los datos se ingresa a la url http://localhost:8080/globalWarming/getSaveInfo
    
# decisiones tomadas para el desarrollo

Decidi hacerlo con Spring Boot ya que tiene embebido un servidor tomcat y es mas facil de ejecutar la app.
Por otro lado Spring tiene las herramientas necesarias para realizar una api rest y hay mucha documentacion al respecto.
Separe la aplicacion con la capa de controller, servicio y otra para los errores. Aunque no tengo una base de datos utilice la capa de servicios para realizar la logica de obtencion de datos, sacando esta responsabilidad del controller.
Hice una clase para el manejo de errores que se encarga de capturar las excepciones del controller y retorna un mensaje.
