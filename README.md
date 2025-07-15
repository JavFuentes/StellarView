# StellarView 🌟

<div align="center">
  <img src="https://github.com/JavFuentes/StellarView/assets/122236197/bd1fbe45-0ca0-40be-a13c-e42d182290ff" alt="StellarView Banner" width="100%">

  **Una app educativa para Android que trae las maravillas de la astronomía a todos**

  [![Google Play](https://img.shields.io/badge/Google_Play-Descargar-green?style=for-the-badge&logo=google-play)](https://play.google.com/store/apps/details?id=com.astronomy.stellar_view)
  [![MIT License](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)](LICENSE)
  [![Android](https://img.shields.io/badge/Platform-Android-brightgreen.svg?style=for-the-badge&logo=android)](https://android.com)
</div>

## 📱 Acerca de

StellarView es una aplicación educativa para Android diseñada para inspirar curiosidad sobre la astronomía y las ciencias del espacio. Ya seas estudiante, entusiasta de la astronomía o desarrollador Android, esta app es tanto una herramienta educativa atractiva como un ejemplo completo de código.

### 🎯 Objetivos Educativos

- **Para Entusiastas de la Astronomía**: Explora las imágenes astronómicas diarias de la NASA, pon a prueba tus conocimientos con trivia interactiva y descubre el cosmos.
- **Para Desarrolladores Android**: Aprende patrones modernos de desarrollo Android, arquitectura y buenas prácticas mediante una implementación real.

## ✨ Características

🖼️ **Integración con NASA APOD** - Imágenes astronómicas diarias con descripciones profesionales\
🧠 **Trivia Interactiva de Astronomía** - Pon a prueba y expande tu conocimiento cósmico\
⭐ **Sistema de Favoritos** - Guarda y organiza tus descubrimientos astronómicos favoritos\
🌙 **Tema Claro/Oscuro** - Visualización cómoda para el día y la noche\
🌍 **Soporte Multilenguaje** - Disponible en inglés, español e italiano\
🎵 **Efectos de Sonido** - Experiencia mejorada con retroalimentación auditiva

<div align="center">
  <img src="https://github.com/JavFuentes/StellarView/assets/122236197/d3935dd6-ce8b-4e68-a83c-067cdd6ddc6c" alt="Capturas de pantalla" width="80%">
</div>

## 🏗️ Arquitectura y Tecnologías

Este proyecto demuestra prácticas modernas de desarrollo Android:

### 📐 Arquitectura

- **Patrón MVVM** - Separación clara de responsabilidades
- **Patrón Repository** - Abstracción limpia de la capa de datos
- **Navigation Component** - Arquitectura de actividad única
- **View Binding** - Referencias de vistas seguras y con tipado

### 🛠️ Tecnologías Utilizadas

- **Kotlin** - Lenguaje moderno para desarrollo Android
- **Layouts XML + View Binding** - Desarrollo de UI tradicional (antes de Compose)
- **Room Database** - Persistencia de datos local
- **Retrofit** - Integración con API de la NASA
- **Dagger Hilt** - Inyección de dependencias
- **Coroutines** - Programación asíncrona
- **Material Design Components** - Componentes modernos de UI
- **Lottie Animations** - Interacciones visuales atractivas
- **Coil** - Carga y almacenamiento de imágenes
- **Firebase** - Servicios de backend y analíticas

### 🌐 APIs y Datos

- **NASA APOD API** - Imagen Astronómica del Día
- **SQLite local** - Almacenamiento de preguntas de trivia y favoritos
- **Firebase Analytics** - Métricas de uso (solo en producción)

## 🚀 Comenzar

### Requisitos Previos

- Android Studio Arctic Fox o superior
- SDK de Android 30+
- Java 17

### Instalación

1. Clona el repositorio

```bash
git clone https://github.com/JavFuentes/StellarView.git
```

2. Ábrelo en Android Studio

3. Sincroniza el proyecto con los archivos Gradle

4. Ejecuta en un dispositivo o emulador

### 🔑 Configuración de la API de la NASA

1. Obtén tu clave gratuita desde el [Portal de Datos Abiertos de la NASA](https://api.nasa.gov/)
2. Agrégala a tu `local.properties`:

```properties
NASA_API_KEY="tu_clave_aqui"
```


## 🛠️ Configuración del Proyecto

### Configuración de Firebase

Este proyecto usa Firebase para autenticación y almacenamiento. 
Sigue estos pasos si quieres implementar correctamente esta función:

#### Configuración Completa (Para producción)
1. Ve a [Firebase Console](https://console.firebase.google.com)
2. Crea un nuevo proyecto o selecciona uno existente
3. Agrega una app Android:
   - **Nombre del paquete**: `com.astronomy.stellar_view`
   - **Nombre de la app**: StellarView
4. Descarga `google-services.json`
5. Colócalo en la carpeta `app/`

### ⚠️ Importante
- El archivo `google-services-example.json` contiene datos ficticios
- Funciona para compilar, pero Firebase no funcionará completamente
- Para funcionalidad completa de Firebase, usa tu archivo real

## 📚 Recursos de Aprendizaje

### Para Desarrolladores Android

- **Implementación de Arquitectura Limpia** - MVVM, Repository y DI funcionando en conjunto
- **Diseño con XML + View Binding** - Aprende desarrollo de interfaces antes de Jetpack Compose
- **Navigation Component** - Arquitectura de actividad única con fragments
- **Gestión de Base de Datos** - Migraciones de Room, DAOs y relaciones de datos
- **Integración con API** - Configuración de Retrofit, manejo de errores y transformación de datos
- **Carga de Imágenes** - Uso de Coil para caché y optimización
- **Inyección de Dependencias** - Configuración y mejores prácticas con Hilt

### Para Entusiastas de la Astronomía

- **Imágenes Diarias de la NASA** - Fotografía astronómica con explicaciones profesionales
- **Aprendizaje Interactivo** - Trivia sobre sistema solar, galaxias y fenómenos cósmicos
- **Dificultad Progresiva** - Contenido adecuado para todos los niveles

## 🤝 Contribuciones

¡Las contribuciones son bienvenidas! Este proyecto es ideal para:

- **Desarrolladores principiantes** que deseen aprender Android
- **Educadores en astronomía** que quieran sumar contenido
- **Entusiastas del código abierto** interesados en educación científica

### Áreas para Contribuir

- 🌟 Nuevas preguntas de trivia astronómica
- 🌍 Traducciones a más idiomas
- 🎨 Mejoras de UI/UX
- 🧪 Expansión de cobertura de pruebas
- 📚 Enriquecimiento del contenido educativo

## 📱 Descargar

<div align="center">
  <a href="https://play.google.com/store/apps/details?id=com.astronomy.stellar_view">
    <img src="https://i.ibb.co/PtGSG29/github-playstore.png" alt="Descargar en Google Play" width="80%">
  </a>
</div>

## 📄 Licencia

```
Licencia MIT

Copyright (c) 2023 Javier Fuentes

Se otorga permiso, sin costo, a cualquier persona que obtenga una copia
de este software y archivos de documentación asociados (el "Software"), para tratar
con el Software sin restricción, incluidos sin limitación los derechos a usar,
copiar, modificar, fusionar, publicar, distribuir, sublicenciar y/o vender
copias del Software, y permitir a las personas a quienes se les proporcione el
Software que lo hagan, sujeto a las siguientes condiciones:

El aviso de derechos de autor anterior y este aviso de permiso se incluirán en todas
las copias o partes sustanciales del Software.

EL SOFTWARE SE PROPORCIONA "TAL CUAL", SIN GARANTÍA DE NINGÚN TIPO, EXPRESA O
IMPLÍCITA, INCLUYENDO PERO NO LIMITADA A LAS GARANTÍAS DE COMERCIABILIDAD,
IDONEIDAD PARA UN PROPÓSITO PARTICULAR Y NO INFRACCIÓN. EN NINGÚN CASO LOS AUTORES
O TITULARES DEL COPYRIGHT SERÁN RESPONSABLES POR NINGUNA RECLAMACIÓN, DAÑO U OTRA
RESPONSABILIDAD, YA SEA EN UNA ACCIÓN CONTRACTUAL, AGRAVIO O DE OTRA MANERA,
DERIVADA DE, O EN CONEXIÓN CON EL SOFTWARE O EL USO U OTRO TIPO DE ACCIONES EN
EL SOFTWARE.

Para más información sobre el autor y sus proyectos, visita http://javierfuentes.dev
```

## 🌟 Apoyo

Si este proyecto te ayudó a aprender sobre desarrollo Android o astronomía, considera:

<a href='https://ko-fi.com/I2I2OPHE0' target='_blank'>
  <img height='36' src='https://storage.ko-fi.com/cdn/kofi6.png?v=6' alt='Cómprame un café en ko-fi.com' />
</a>

## 👨‍💻 Autor

**Javier Fuentes** - *Desarrollador Android y Entusiasta de la Astronomía*

- Sitio web: [javierfuentes.dev](http://javierfuentes.dev)
- GitHub: [@JavFuentes](https://github.com/JavFuentes)

---

<div align="center">
  <i>"Vivimos en una sociedad profundamente dependiente de la ciencia y la tecnología y en la que casi nadie sabe nada de estos temas."</i>
  <br>
  <b>- Carl Sagan</b>
</div>

