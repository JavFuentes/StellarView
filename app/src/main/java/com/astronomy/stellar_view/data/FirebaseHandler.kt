package com.astronomy.stellar_view.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseHandler {

    companion object {
        // Constante utilizada para identificar registros de esta clase en el log
        private const val TAG = "FirebaseHandler"
    }

    // Verifica si el usuario ya existe en Firestore
    fun checkIfUserExistsInFirestore(user: FirebaseUser, onSuccess: (Boolean) -> Unit, onFailure: (Exception) -> Unit) {
        // Referencia al documento del usuario en Firestore
        val userRef = FirebaseFirestore.getInstance().document("users/${user.uid}")

        userRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                Log.d(TAG, "El usuario ya existe en Firestore")
                onSuccess(true)
            } else {
                Log.w(TAG, "El usuario no existe en Firestore. Esto no debería suceder.")
                onSuccess(false)
            }
        }.addOnFailureListener { exception ->
            onFailure(exception)
        }
    }

    // Autentica a un usuario de forma anónima
    fun authenticateAnonymously(onSuccess: (FirebaseUser) -> Unit, onFailure: (Exception) -> Unit) {
        FirebaseAuth.getInstance().signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.let {
                        saveUserToFirestore(it)
                        onSuccess(it)
                    }
                    Log.w(TAG, "Autenticación anónima exitosa")
                } else {
                    task.exception?.let {
                        onFailure(it)
                    }
                    Log.w(TAG, "Autenticación anónima fallida", task.exception)
                }
            }
    }

    // Guarda un nuevo usuario en Firestore
    private fun saveUserToFirestore(user: FirebaseUser) {
        // Referencia al documento del usuario en Firestore
        val userRef = FirebaseFirestore.getInstance().document("users/${user.uid}")

        userRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                Log.d(TAG, "El usuario ya existe en Firestore")
            } else {
                // Creando una estructura de datos para el nuevo usuario
                val newUser = hashMapOf(
                    "id" to user.uid,
                    "score" to "000000"
                )
                // Guardando el usuario en Firestore
                userRef.set(newUser).addOnSuccessListener {
                    Log.d(TAG, "Usuario agregado a Firestore")
                }.addOnFailureListener { e ->
                    Log.w(TAG, "Error agregando al usuario", e)
                }
            }
        }
    }

    // Actualiza el puntaje del usuario en Firestore
    fun updateScoreInFirestore(uid: String, newScore: String) {
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("users").document(uid)

        // Actualizando el campo "score" del usuario
        userRef.update("score", newScore)
            .addOnSuccessListener { Log.d("Firestore", "Puntaje del jugador actualizado.") }
            .addOnFailureListener { e -> Log.w("Firestore", "Error actualizando el puntaje.", e) }
    }
}
