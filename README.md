
Descripción del Proyecto

Cámara Demo es una aplicación de Android que permite a los usuarios capturar imágenes usando la cámara del dispositivo, guardar las imágenes en la galería y compartirlas a través de diferentes aplicaciones, como WhatsApp y correo electrónico. La aplicación gestiona correctamente los permisos necesarios y ofrece una interfaz de usuario sencilla e intuitiva para interactuar con las funcionalidades de la cámara.

Características

- Captura de imágenes usando la cámara del dispositivo.
- Visualización de la imagen capturada en la interfaz.
- Guardado de imágenes en la galería del dispositivo.
- Compartición de imágenes a través de aplicaciones de terceros.
- Manejo de permisos de cámara y almacenamiento.

 Requisitos

- Android Studio
- SDK de Android 33 (o superior)
- Dispositivo físico o emulador con capacidad de cámara

Instrucciones para Ejecutar el Proyecto

1.	descarga el proyecto
2.	Abre el Proyecto en Android Studio:
3.	Lanza Android Studio.
4.	Selecciona File > Open y elige la carpeta del proyecto clonado.
5.	Configura las Dependencias:
6.	Asegúrate de que las dependencias de tu proyecto estén actualizadas. Puedes revisar el archivo build.gradle en el nivel de la aplicación.
7.	Conéctate a un Dispositivo Físico (o configura un emulador):
Si usas un dispositivo físico, asegúrate de habilitar la opción de Depuración USB en el dispositivo.
8.	Ejecuta la Aplicación:
9.	Selecciona el dispositivo en la barra de herramientas.
10.	Haz clic en el botón de Run (o presiona Shift + F10).
Permisos Requeridos
          La aplicación requiere los siguientes permisos:
Cámara: Para acceder a la cámara del dispositivo.
android.permission.CAMERA
Almacenamiento: Para guardar imágenes en la galería y leer imágenes almacenadas.
android.permission.READ_EXTERNAL_STORAGE
android.permission.WRITE_EXTERNAL_STORAGE (solo hasta Android 28)
android.permission.READ_MEDIA_IMAGES (Android 33 y superior)
Manejo de Permisos
La aplicación solicita estos permisos en tiempo de ejecución. Asegúrate de aceptarlos para utilizar todas las funcionalidades de la aplicación. Si los permisos son denegados, la aplicación mostrará un mensaje de advertencia.
