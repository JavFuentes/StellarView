
# <div align="center">StellarView</div>

![github-banner](https://github.com/JavFuentes/StellarView/assets/122236197/d33bd31d-b49d-49aa-884d-3dcdaebdd973)

"Stellar View" es una aplicación Android enfocada en promover el interés en la educación astronómica. 

## Story Telling: Vista Estelar 🔭
<p>“Vivimos en una sociedad profundamente dependiente de la ciencia y la tecnología, y en la que nadie sabe nada de estos temas”. 
<br>
Esta frase del gran astrónomo, Carl Sagan, habla de un problema que solo se ha agudizado en los últimos años, y ahora de forma exponencial, debido a la revolución de las inteligencias artificiales, que son “cajas negras” que nos dan información, pero no sabemos cómo lo hacen; en este contexo, es fundamental fomentar el interés en la educación científica y permitir que personas de todas las edades y en cualquier parte del mundo puedan explorar y maravillarse con los nuevos descubrimientos y avances tecnológicos, y ¿qué mejor punto de entrada que en un campo tan asombroso como la astronomía?</p>
<p>Imagina un niño pequeño que mira hacia arriba en una noche despejada, asombrado por la infinidad de estrellas que pueblan el cielo. En ese momento, se despierta en él la curiosidad y el deseo de explorar más allá de lo que sus ojos pueden ver. Sin embargo, no sabe por dónde empezar ni cómo obtener información confiable y accesible sobre los cuerpos celestes.</p>
<p>Aquí es donde nuestra aplicación entra en juego. Con una interfaz intuitiva y amigable, "Vista Estelar" lleva al usuario a un viaje cósmico de descubrimiento y aprendizaje. Al abrir la aplicación, se le presenta una amplia variedad de fotografías impactantes de estrellas, planetas, nebulosas y galaxias, cada una de ellas acompañada de una breve descripción que explora sus características únicas y su relevancia en el vasto cosmos.</p>
<p>No importa si eres un estudiante de astronomía, un entusiasta del espacio o simplemente alguien que busca ampliar su conocimiento, nuestra aplicación tiene algo para todos. </p>
<p>"Vista Estelar" es más que una simple aplicación, es una puerta hacia el vasto y asombroso cosmos que nos rodea. Está diseñada para inspirar, educar y fomentar la pasión por la astronomía en personas de todas las edades y orígenes. Juntos, podemos desvelar los misterios del universo y permitir que todos se maravillen con la belleza y la grandeza del cosmos que nos rodea.</p>
<br>

![github-screens](https://github.com/JavFuentes/StellarView/assets/122236197/d68e24fd-117e-4e8a-b3e1-c8fa873537eb)
<br>
## Problemáticas identificadas 🚩

1. Falta de acceso a recursos educativos sobre astronomía en comunidades desfavorecidas.
2. Escasa presencia de la astronomía en los programas educativos formales.
3. Desconocimiento generalizado de los avances científicos y descubrimientos en el campo de la astronomía.
4. Dificultad para acceder a información actualizada y precisa sobre descubrimientos y avances científicos en astronomía.
<br>

## Detalles Técnicos 💻

Desde su concepción, el proyecto ha incorporado una trivia astronómica que no sólo desafía los conocimientos del usuario, sino que también educa a través de la interacción lúdica, haciéndola apropiada para todas las edades.

La app además, permite a usuarios explorar fotografías impactantes de cuerpos celestes. Esta característica es posible gracias al consumo de una API proporcionada por la NASA, conocida como "APOD" (Astronomy Picture of the Day) para obtener imágenes astronómicas fascinantes con interesantes descripciones escritas por astrónomos profesionales. 

Durante el desarrollo de Stellar View, apliqué los siguientes recursos y técnicas:

- Arquitectura MVVM (Modelo-Vista-Vista Modelo): Adopté este patrón para asegurar un código organizado, escalable y fácil de mantener.
- Biblioteca Room: Implementé dos bases de datos SQLite usando Room, que gestionan tanto las imágenes favoritas de los usuarios como el conjunto de preguntas y respuestas de la trivia.
- Firebase: Integré esta plataforma para recolectar y analizar estadísticas telemétricas, ofreciéndome valiosos insights sobre la interacción de los usuarios con la trivia. (El presente repositorio no incluye esta característica para mantener la seguridad de la aplicación publicada)
- Dagger Hilt: Opté por esta herramienta para la inyección de dependencias, facilitando la gestión y optimización de recursos a lo largo de la aplicación.

La creación de Stellar View ha sido un reto enriquecedor que me ha permitido fortalecer y expandir mis competencias en el ámbito del desarrollo de aplicaciones móviles.
<br><br>
[Descarga la App desde la Play Store](https://play.google.com/store/apps/details?id=com.astronomy.stellar_view)





