package co.touchlab.library

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import platform.Foundation.NSThread
import kotlin.native.concurrent.AtomicReference
import kotlin.native.concurrent.TransferMode
import kotlin.native.concurrent.Worker
import kotlin.native.concurrent.freeze
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MemoryTest {
    @Test
    fun oldMemoryTest() {
        fun <T> doInBackground(action: () -> T): T {
            val worker = Worker.start()
            val future = worker.execute(TransferMode.SAFE, { action.freeze() }, { it() })
            return future.result
        }

        val didRunLambda = AtomicReference(false)
        assertTrue(NSThread.isMainThread)
        doInBackground {
            didRunLambda.value = true
            assertFalse(NSThread.isMainThread)
        }
        assertTrue(didRunLambda.value)
    }

    @Test
    fun newMemoryTest() {
        fun <T> doInBackground(action: () -> T): T {
            val worker = Worker.start()
            val future = worker.execute(TransferMode.SAFE, { action }, { it() })
            return future.result
        }

        var didRunLambda = false
        assertTrue(NSThread.isMainThread)
        doInBackground {
            didRunLambda = true
            assertFalse(NSThread.isMainThread)
        }
        assertTrue(didRunLambda)
    }

    @Test
    fun oldMemoryCoroutinesTest() = runBlocking {
        var didRunLambda = AtomicReference(false)
        assertTrue(NSThread.isMainThread)
        withContext(Dispatchers.Default) {
            didRunLambda.value = true
            assertFalse(NSThread.isMainThread)
        }
        assertTrue(didRunLambda.value)
    }

    @Test
    fun newMemoryCoroutinesTest() = runBlocking {
        var didRunLambda = false
        assertTrue(NSThread.isMainThread)
        withContext(Dispatchers.Default) {
            didRunLambda = true
            assertFalse(NSThread.isMainThread)
        }
        assertTrue(didRunLambda)
    }
}


