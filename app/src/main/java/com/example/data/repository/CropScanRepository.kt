package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.model.CropScanEntity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.UUID

class CropScanRepository(
    private val firestore: FirebaseFirestore
) {
    // Secondary constructor resolving custom firestore_database_id from string resource
    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth = Firebase.auth

    fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in with Google before accessing Firestore.")
    }

    /**
     * Realtime observation of crop scans for the authenticated user
     */
    fun observeScans(): Flow<List<CropScanEntity>> = callbackFlow {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            close(IllegalStateException("User must be signed in with Google before accessing Firestore."))
            return@callbackFlow
        }

        val registration = firestore.collection("users")
            .document(uid)
            .collection("scans")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val items = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(CropScanEntity::class.java)?.let { entity ->
                            if (entity.id.isBlank()) entity.copy(id = doc.id) else entity
                        }
                    }
                    trySend(items)
                }
            }

        awaitClose { registration.remove() }
    }

    /**
     * Save a new scan record to the user's Firestore collection
     */
    suspend fun saveScan(
        cropType: String,
        diseaseName: String,
        diseaseNameUrdu: String,
        confidence: Double,
        severity: String,
        notes: String? = null
    ): Result<String> {
        return try {
            val uid = requireUserId()
            val scanId = "scan_${UUID.randomUUID()}"
            val docRef = firestore.collection("users")
                .document(uid)
                .collection("scans")
                .document(scanId)

            val payload = mutableMapOf<String, Any>(
                "id" to scanId,
                "userId" to uid,
                "cropType" to cropType,
                "diseaseName" to diseaseName,
                "diseaseNameUrdu" to diseaseNameUrdu,
                "confidence" to confidence,
                "severity" to severity,
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )
            if (!notes.isNullOrBlank()) {
                payload["notes"] = notes
            }

            docRef.set(payload).await()
            Result.success(scanId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Delete a scan record
     */
    suspend fun deleteScan(scanId: String): Result<Unit> {
        return try {
            val uid = requireUserId()
            firestore.collection("users")
                .document(uid)
                .collection("scans")
                .document(scanId)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
