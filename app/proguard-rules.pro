# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# =====================================
# CONFIGURACION BASICA
# =====================================

# Mantener informacion de lineas para debugging
-keepattributes LineNumberTable,SourceFile

# Mantener anotaciones
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes Exceptions

# =====================================
# REGLAS ESPECIFICAS PARA TU APP
# =====================================

# Mantener todas las clases de data/model para serializacion
-keep class com.astronomy.stellar_view.data.** { *; }
-keep class com.astronomy.stellar_view.model.** { *; }

# Mantener clases de Firebase
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

# Mantener clases de Retrofit y Gson
-keep class retrofit2.** { *; }
-keep class com.google.gson.** { *; }
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

# Mantener clases de Room
-keep class androidx.room.** { *; }
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *

# Mantener clases de Hilt/Dagger
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.lifecycle.HiltViewModel

# Mantener Navigation Component
-keep class androidx.navigation.** { *; }

# Mantener ViewBinding
-keep class * extends androidx.viewbinding.ViewBinding {
    public static *** inflate(android.view.LayoutInflater);
    public static *** inflate(android.view.LayoutInflater, android.view.ViewGroup, boolean);
    public static *** bind(android.view.View);
}

# Mantener clases que se usan con reflexion
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}

# Mantener enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Mantener Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Mantener clases de Lottie
-keep class com.airbnb.lottie.** { *; }

# Mantener clases de Coil
-keep class coil.** { *; }

# Reglas para corrutinas
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

# =====================================
# REGLAS ESPECIFICAS PARA TUS FRAGMENTOS
# =====================================

# Mantener todos los fragmentos para Navigation Component
-keep class com.astronomy.stellar_view.ui.** extends androidx.fragment.app.Fragment

# Mantener ViewModels
-keep class com.astronomy.stellar_view.ui.**.*ViewModel { *; }

# =====================================
# OPTIMIZACIONES ADICIONALES
# =====================================

# Permitir optimizacion agresiva pero mantener funcionalidad
-allowaccessmodification
-mergeinterfacesaggressively
-overloadaggressively

# No advertencias para librerias conocidas
-dontwarn java.lang.invoke.**
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions**