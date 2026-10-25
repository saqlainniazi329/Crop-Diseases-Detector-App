package com.example.data.repository

import com.example.base.FirestoreEmulatorTestBase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CropScanRepositoryRuleTest : FirestoreEmulatorTestBase() {

    @Test
    fun saveScan_authenticatedUser_succeeds() = runBlocking {
        signInTestUser(TEST_USER_A)
        val repository = CropScanRepository(firestore)

        val result = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.saveScan(
                cropType = "Rice",
                diseaseName = "Bacterial Blight",
                diseaseNameUrdu = "بیکٹیریل بلائٹ",
                confidence = 0.96,
                severity = "High",
                notes = "Field 4 test sample"
            )
        }

        assertTrue("Expected saveScan to succeed, got $result", result.isSuccess)
        val scanId = result.getOrThrow()
        assertNotNull(scanId)
    }

    @Test
    fun observeScans_authenticatedUser_emitsSavedScan() = runBlocking {
        signInTestUser(TEST_USER_A)
        val repository = CropScanRepository(firestore)

        val saveResult = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.saveScan(
                cropType = "Rice",
                diseaseName = "Blast",
                diseaseNameUrdu = "رائس بلاسٹ",
                confidence = 0.94,
                severity = "Critical"
            )
        }
        val scanId = saveResult.getOrThrow()

        val scans = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.observeScans().first { list ->
                list.any { it.id == scanId }
            }
        }

        val found = scans.find { it.id == scanId }
        assertNotNull(found)
        assertEquals("Blast", found?.diseaseName)
    }

    @Test
    fun saveScan_unauthenticatedUser_failsFast() = runBlocking {
        auth.signOut()
        val repository = CropScanRepository(firestore)

        val result = repository.saveScan(
            cropType = "Rice",
            diseaseName = "Healthy",
            diseaseNameUrdu = "صحت مند",
            confidence = 0.99,
            severity = "None"
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalStateException)
    }

    private companion object {
        const val TEST_USER_A = "saqlain_farmer@test.com"
        const val DEFAULT_TIMEOUT_MS = 5000L
    }
}
