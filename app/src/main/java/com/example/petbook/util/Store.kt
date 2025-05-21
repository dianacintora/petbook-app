package com.example.petbook.util

import com.google.firebase.firestore.FirebaseFirestore

fun storeDocument(
    db: FirebaseFirestore,
    path: String,
    document: String,
    data: Map<String, Any>,
    onSuccess: () -> Unit,
    onFail: (String?) -> Unit
) {
    db.collection(path).document(document).set(data).addOnSuccessListener {
        onSuccess()
    }.addOnFailureListener { exception ->
        onFail(exception.message)
    }
}

fun updateDocument(
    db: FirebaseFirestore,
    path: String,
    document: String,
    data: Map<String, Any>,
    onSuccess: () -> Unit,
    onFail: (String?) -> Unit
) {
    db.collection(path).document(document).update(data).addOnSuccessListener {
        onSuccess()
    }.addOnFailureListener { exception ->
        onFail(exception.message)
    }
}

fun getDocument(
    db: FirebaseFirestore,
    path: String,
    id: String,
    onFail: (String?) -> Unit,
    onSuccess: (MutableMap<String, Any>) -> Unit
) {
    db.collection(path).document(id).get().addOnSuccessListener { result ->
        if (result.exists()) {
            onSuccess(result.data!!)
        } else {
            onFail("The document does not exist")
        }
    }.addOnFailureListener { exception ->
        onFail(exception.message)
    }
}

fun getMultipleDocuments(
    db: FirebaseFirestore,
    path: String,
    onFail: (String?) -> Unit,
    onSuccess: (MutableMap<String, Any>) -> Unit
) {
    db.collection(path).get().addOnSuccessListener { result ->
        for (document in result) {
            if (document.exists()) {
                onSuccess(document.data)
            } else {
                onFail("Failed to retrieve document")
            }
        }
    }.addOnFailureListener { exception ->
        onFail(exception.message)
    }
}